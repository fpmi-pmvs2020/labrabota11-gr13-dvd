package com.task.fbresult.dialogs;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.task.fbresult.R;
import com.task.fbresult.db.fbdao.FBPersonDao;
import com.task.fbresult.model.Duty;
import com.task.fbresult.model.MyMessage;
import com.task.fbresult.model.PeopleOnDuty;
import com.task.fbresult.model.Person;
import com.task.fbresult.util.DAORequester;
import com.task.fbresult.util.DutyUtils;
import com.task.fbresult.util.FBUtils;
import com.task.fbresult.util.MessageUtils;

import java.util.List;
import java.util.stream.Collectors;

import lombok.var;

public class ExchangeOnCurrentDialogBuilder extends ExchangeDialogBuilder {

    private Spinner spGoalPerson;
    private Spinner spGoalPersonOnDuty;
    private TextView tvMyDuty;

    private List<Person> persons;
    private List<PeopleOnDuty> peopleOnDuties;

    public ExchangeOnCurrentDialogBuilder(Context context, Duty currentDuty) {
        super(context, currentDuty);
    }

    @Override
    View findMainWindow() {
        LayoutInflater inflater = LayoutInflater.from(context);
        return inflater.inflate(R.layout.exchange_on_my_dialog, null);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    void checkAndSetFields() {
        spGoalPerson = mainWindow.findViewById(R.id.spExchangeGoalPersonMyDuty);
        spGoalPersonOnDuty = mainWindow.findViewById(R.id.spExchangeGoalPersonOnDuty);
        tvMyDuty = mainWindow.findViewById(R.id.tvMyDuty);
        setCurrentDutyInfo();
        fillGoalPersonsSpinner();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setCurrentDutyInfo() {
        var currentPeopleOnDuty = getCurrentPeopleOnDuty();
        tvMyDuty.setText(getDutyDayAndTime(currentPeopleOnDuty));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private PeopleOnDuty getCurrentPeopleOnDuty() {
        var currentPerson = FBUtils.getCurrentUserAsPerson();
        return DAORequester.getPeopleOnDuty(currentDuty).stream()
                .filter(peopleOnDuty -> peopleOnDuty.getPersonId().equals(currentPerson.getFirebaseId()))
                .findFirst()
                .get();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void fillGoalPersonsSpinner() {
        persons = new FBPersonDao().getAll();
        var currentPerson = FBUtils.getCurrentUserAsPerson();
        persons.remove(currentPerson);
        List<String> personsInitials = persons.stream()
                .map(Person::getSurnameWithInitials)
                .collect(Collectors.toList());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, personsInitials);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spGoalPerson.setAdapter(adapter);
        spGoalPerson.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updatePersonsWithDuties(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                spGoalPerson.setSelection(0);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void updatePersonsWithDuties(int position) {
        var person = persons.get(position);

        peopleOnDuties = DAORequester.getFuturePeopleOnDutiesOfPerson(person);

        List<String> peopleOnDutiesWithTime = peopleOnDuties.stream()
                .map(this::getDutyDayAndTime)
                .collect(Collectors.toList());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, peopleOnDutiesWithTime);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spGoalPersonOnDuty.setAdapter(adapter);
    }

    @Override
    String[] getFieldValues() throws Exception {
        return new String[0];
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    void setData(String[] values) {
        var selectedPeopleOnDuty
                = peopleOnDuties.get((int) spGoalPersonOnDuty.getSelectedItemId());
        var currentPeopleOnDuty = getCurrentPeopleOnDuty();
        if (DutyUtils.doWorkOnTheSameTime(currentPeopleOnDuty, selectedPeopleOnDuty)) {
            Toast.makeText(context, context.getText(R.string.already_on_this_duty), Toast.LENGTH_LONG).show();
            return;
        }

        MyMessage message = new MyMessage(currentPeopleOnDuty, selectedPeopleOnDuty);
        tryToSendMessage(message);
    }
}
