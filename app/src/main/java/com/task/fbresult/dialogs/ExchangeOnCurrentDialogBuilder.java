package com.task.fbresult.dialogs;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.task.fbresult.R;
import com.task.fbresult.db.fbdao.FBPersonDao;
import com.task.fbresult.model.Duty;
import com.task.fbresult.model.PeopleOnDuty;
import com.task.fbresult.model.Person;
import com.task.fbresult.util.DAORequester;
import com.task.fbresult.util.DutyUtils;
import com.task.fbresult.util.FBUtils;
import com.task.fbresult.util.LocalDateTimeInterval;
import com.task.fbresult.util.MessageSender;
import com.task.fbresult.util.SpinnerUtils;

import java.util.List;
import java.util.stream.Collectors;

import lombok.var;

public class ExchangeOnCurrentDialogBuilder extends ExchangeDialogBuilder {

    private Spinner spGoalPerson;
    private Spinner spGoalPersonOnDuty;
    private TextView tvMyDuty;
    private CheckBox cbInCredit;

    private List<Person> persons;

    private PeopleOnDuty myPeopleOnDuty;

    public ExchangeOnCurrentDialogBuilder(Context context) {
        super(context);
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
        cbInCredit = mainWindow.findViewById(R.id.cbInCredit);
        configureCbInCredit();
        configureSeekBars();
        setCurrentDutyInfo();
        fillGoalPersonsSpinner();
    }

    private void configureCbInCredit() {
        cbInCredit.setOnCheckedChangeListener(
                (buttonView, isChecked) -> setOtherDutyFieldsEnabled(!isChecked)
        );
    }

    @Override
    protected void setOtherDutyFieldsEnabled(boolean enabled) {
        spGoalPersonOnDuty.setEnabled(enabled);
        otherDutySeekBarConfiguration.rangeSeekBar.setEnabled(enabled);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setCurrentDutyInfo() {
        myPeopleOnDuty = getCurrentPeopleOnDuty();
        myDutySeekBarConfiguration.updateWithDiapason(myPeopleOnDuty);
        tvMyDuty.setText(getDutyDayAndTime(myPeopleOnDuty));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private PeopleOnDuty getCurrentPeopleOnDuty() {
        var currentPerson = FBUtils.getCurrentUserAsPerson();
        return DAORequester.getPeopleOnDuty(currentDuty).stream()
                .filter(peopleOnDuty -> peopleOnDuty.getPersonId().equals(currentPerson.getFirebaseId()))
                .findFirst()
                .get();
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
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
        goalPeopleOnDuties = DAORequester.getFuturePeopleOnDutiesOfPerson(person);

        List<String> peopleOnDutiesWithTime = goalPeopleOnDuties.stream()
                .map(this::getDutyDayAndTime)
                .collect(Collectors.toList());
        spGoalPersonOnDuty.setAdapter(SpinnerUtils.getStringAdapterOf(context, peopleOnDutiesWithTime));
        configureSpinner(spGoalPersonOnDuty, goalPeopleOnDuties, otherDutySeekBarConfiguration);
    }

    @Override
    String[] getFieldValues() throws Exception {
        return new String[0];
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    void setData(String[] values) {
        if (!cbInCredit.isChecked()) {
            PeopleOnDuty selectedPeopleOnDuty
                    = goalPeopleOnDuties.get((int) spGoalPersonOnDuty.getSelectedItemId());
            tryToSendMessageWith(myPeopleOnDuty, selectedPeopleOnDuty);
        } else {
            var selectedPerson = persons.get((int) spGoalPerson.getSelectedItemId());
            tryToSendMessageWith(myPeopleOnDuty, selectedPerson);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    protected void tryToSendMessageWith(PeopleOnDuty meOnDuty, Person selectedPerson) {
        try {
            var myDutyInterval = myDutySeekBarConfiguration.getSelectedInterval();
            MessageSender messageSender = new MessageSender(context, meOnDuty, selectedPerson.getFirebaseId());
            messageSender.tryToSendMessageWith(myDutyInterval, null);
        } catch (Exception e) {
            Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
