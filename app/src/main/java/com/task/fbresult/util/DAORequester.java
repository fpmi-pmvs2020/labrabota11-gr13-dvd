package com.task.fbresult.util;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.task.fbresult.db.fbdao.ConstraintPair;
import com.task.fbresult.db.fbdao.ConstraintType;
import com.task.fbresult.db.fbdao.FBDutyDao;
import com.task.fbresult.db.fbdao.FBDutyTypesDao;
import com.task.fbresult.db.fbdao.FBMessageDao;
import com.task.fbresult.db.fbdao.FBPeopleOnDutyDao;
import com.task.fbresult.db.fbdao.FBPersonDao;
import com.task.fbresult.model.Duty;
import com.task.fbresult.model.DutyType;
import com.task.fbresult.model.MyMessage;
import com.task.fbresult.model.PeopleOnDuty;
import com.task.fbresult.model.Person;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

import lombok.var;

import static com.task.fbresult.db.DBHelper.DUTY_ID_COLUMN;
import static com.task.fbresult.db.DBHelper.MESSAGES_AUTHOR_COLUMN;
import static com.task.fbresult.db.DBHelper.MESSAGES_RECIPIENT_COLUMN;
import static com.task.fbresult.db.DBHelper.PER_ON_DUTY_DUTY_ID_COLUMN;
import static com.task.fbresult.db.DBHelper.PER_ON_DUTY_FROM_COLUMN;
import static com.task.fbresult.db.DBHelper.PER_ON_DUTY_PERSON_ID_COLUMN;

public class DAORequester {
    public static List<PeopleOnDuty> getPeopleOnDuty(Duty duty) {
        return new FBPeopleOnDutyDao().get(
                PER_ON_DUTY_DUTY_ID_COLUMN,
                new ConstraintPair(duty.getFirebaseId(), ConstraintType.EQUALS)
        );
    }

    public static DutyType getDutyType(Duty duty) {
        return new FBDutyTypesDao().getWithId(duty.getTypeId());
    }

    public static List<Person> getPersonsOnDuty(Duty duty) {
        List<PeopleOnDuty> peopleOnDuties = getPeopleOnDuty(duty);
        return getPersonsInPeopleOnDuties(peopleOnDuties);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static List<Duty> getPersonDuties(Person person) {
        List<PeopleOnDuty> peopleOnDuties = new FBPeopleOnDutyDao().get(
                PER_ON_DUTY_PERSON_ID_COLUMN,
                new ConstraintPair(person.getFirebaseId(), ConstraintType.EQUALS)
        );
        List<Duty> result = new ArrayList<>();
        FBDutyDao dutyDao = new FBDutyDao();
        for (PeopleOnDuty peopleOnDuty : peopleOnDuties) {
            List<Duty> duties = dutyDao.get(
                    DUTY_ID_COLUMN,
                    new ConstraintPair(peopleOnDuty.getDutyId(),ConstraintType.EQUALS)
            );
            result.addAll(duties);
        }
        return result;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static Duty getFirstOfNextDutiesOfPerson(Person person) {
        List<PeopleOnDuty> peopleOnDuties = getFuturePeopleOnDutiesOfPerson(person);

        if (peopleOnDuties.isEmpty())
            return null;
        else {
            peopleOnDuties.sort((o1, o2) -> o1.getFrom().compareTo(o2.getFrom()));
            return getDutyWithPeopleOnDuty(peopleOnDuties.get(0));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static List<PeopleOnDuty> getFuturePeopleOnDutiesOfPerson(Person person){
        String todayDateAsString = LocalDateTimeHelper.getTodayDateAsString();
        var personsOnDuties = getPeopleOnDutiesOfPerson(person);
        return personsOnDuties.stream()
                .filter(peopleOnDuty -> peopleOnDuty.getFrom().compareTo(todayDateAsString)>0)
                .collect(Collectors.toList());
    }

    private static List<PeopleOnDuty>getPeopleOnDutiesOfPerson(Person person){
        return new FBPeopleOnDutyDao().get(
                PER_ON_DUTY_PERSON_ID_COLUMN,
                new ConstraintPair(person.getFirebaseId(),ConstraintType.EQUALS)
        );
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static Duty getDutyWithPeopleOnDuty(PeopleOnDuty peopleOnDuty) {
        return new FBDutyDao().getWithId(peopleOnDuty.getDutyId());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static List<Duty> getFutureDutiesOfPerson(Person person){
        var peopleOnDuties = getFuturePeopleOnDutiesOfPerson(person);
        var result = new ArrayList<Duty>();
        for(var peopleOnDuty:peopleOnDuties)
            result.add(getDutyWithPeopleOnDuty(peopleOnDuty));
        return result;
    }

    public static List<Person> getPersonsInPeopleOnDuties(List<PeopleOnDuty> peopleOnDutyList) {
        List<Person> persons = new ArrayList<>();
        TreeSet<String> userIds = new TreeSet<>();
        for (PeopleOnDuty peopleOnDuty : peopleOnDutyList) {
            Person person = getPersonInPeopleOnDuty(peopleOnDuty);
            if (!userIds.contains(person.getFirebaseId())) {
                persons.add(person);
                userIds.add(person.getFirebaseId());
            }
        }
        return persons;
    }

    public static Person getPersonInPeopleOnDuty(PeopleOnDuty peopleOnDuty) {
        return new FBPersonDao().getWithId(peopleOnDuty.getPersonId());
    }

    public static List<MyMessage> getPersonToOtherMessages(Person currentUser) {
        return new FBMessageDao().get(
                MESSAGES_AUTHOR_COLUMN,
                new ConstraintPair(currentUser.getFirebaseId(),ConstraintType.EQUALS)
        );
    }

    public static List<MyMessage> getPersonIncomingMessages(Person currentUser) {
        return new FBMessageDao().get(
                MESSAGES_RECIPIENT_COLUMN,
                new ConstraintPair(currentUser.getFirebaseId(),ConstraintType.EQUALS)
        );
    }
}
