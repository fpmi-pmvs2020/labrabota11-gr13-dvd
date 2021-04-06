package com.task.fbresult.dialogs;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import androidx.annotation.RequiresApi;

import com.task.fbresult.R;
import com.task.fbresult.db.fbdao.FBMessageDao;
import com.task.fbresult.db.fbdao.FBPersonDao;
import com.task.fbresult.model.Duty;
import com.task.fbresult.model.MyMessage;
import com.task.fbresult.model.PeopleOnDuty;
import com.task.fbresult.model.Person;
import com.task.fbresult.util.DAORequester;
import com.task.fbresult.util.FBUtils;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

import static android.content.ContentValues.TAG;

public class ExchangeDialogBuilder extends DialogBuilder {

    private final Duty currentDuty;
    private Spinner spMyDuty;
    private Spinner spGoalPerson;
    private List<PersonWithDuty> persons;
    private List<PeopleOnDuty> personDuties;


    public ExchangeDialogBuilder(Context context, Duty currentDuty) {
        super(context);
        this.currentDuty = currentDuty;
    }

    @Override
    View findMainWindow() {
        LayoutInflater inflater = LayoutInflater.from(context);
        return inflater.inflate(R.layout.exchange_dialog, null);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    void checkAndSetFields() {
        spGoalPerson = mainWindow.findViewById(R.id.spExchangeGoalPerson);
        spMyDuty = mainWindow.findViewById(R.id.spMyDutyToChange);
        fillGoalPersonSpinner();
        fillMyDuties();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void fillGoalPersonSpinner() {
        List<PeopleOnDuty> peopleOnDuty = DAORequester.getPeopleOnDuty(currentDuty);
        persons = peopleOnDuty.stream()
                .map(PersonWithDuty::new)
                .collect(Collectors.toList());

        List<String> personWithTime = persons.stream()
                .map(this::getNameAndDutyTime)
                .collect(Collectors.toList());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, personWithTime);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spGoalPerson.setAdapter(adapter);
    }

    private String getNameAndDutyTime(PersonWithDuty personWithDuty) {
        String surnameWithInitials = personWithDuty.person.getSurnameWithInitials();
        String from = personWithDuty.peopleOnDuty.getFrom().split("T")[1];
        String to = personWithDuty.peopleOnDuty.getTo().split("T")[1];
        return surnameWithInitials + " " + from + " - " + to;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void fillMyDuties() {
        Person me = FBUtils.getCurrentUserAsPerson();
        personDuties = DAORequester.getFuturePeopleOnDutiesOfPerson(me);
        List<String> dutyDayAndTime = personDuties.stream()
                .map(this::getDutyDayAndTime)
                .collect(Collectors.toList());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, dutyDayAndTime);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMyDuty.setAdapter(adapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String getDutyDayAndTime(PeopleOnDuty duty) {
        String date = duty.fromAsLocalDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE);
        String from = duty.getFrom().split("T")[1];
        String to = duty.getTo().split("T")[1];
        return date + " " + from + " - " + to;
    }

    @Override
    String[] getFieldValues() throws Exception {
        //ignored
        return new String[0];
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    void setData(String[] values) {
        PersonWithDuty personWithDuty = persons.get((int) spGoalPerson.getSelectedItemId());
        PeopleOnDuty myDuty = personDuties.get((int) spMyDuty.getSelectedItemId());

        MyMessage message = writeMessage(personWithDuty, myDuty);

        sendMessage(message);
        Log.i(TAG, "person with duty " + personWithDuty);
        Log.i(TAG, "my duty " + myDuty);

    }

    private MyMessage writeMessage(PersonWithDuty personWithDuty, PeopleOnDuty myDuty){
        return new MyMessage(
                myDuty.getPersonId(),
                personWithDuty.person.getFirebaseId(),
                personWithDuty.peopleOnDuty.getDutyId(),
                personWithDuty.peopleOnDuty.getFrom(),
                personWithDuty.peopleOnDuty.getTo()
        );
    }

    private void sendMessage(MyMessage message){
        FBMessageDao dao = new FBMessageDao();
        dao.save(message);
    }

    @RequiredArgsConstructor
    static class PersonWithDuty {
        public Person person;
        public PeopleOnDuty peopleOnDuty;

        public PersonWithDuty(PeopleOnDuty peopleOnDuty) {
            this.peopleOnDuty = peopleOnDuty;
            person = DAORequester.getPersonInPeopleOnDuty(peopleOnDuty);
        }
    }
}
