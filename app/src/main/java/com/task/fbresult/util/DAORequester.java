package com.task.fbresult.util;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.task.fbresult.db.dao.DutyDao;
import com.task.fbresult.db.dao.DutyTypesDao;
import com.task.fbresult.db.dao.PeopleOnDutyDao;
import com.task.fbresult.db.dao.PersonDao;
import com.task.fbresult.model.Duty;
import com.task.fbresult.model.DutyType;
import com.task.fbresult.model.PeopleOnDuty;
import com.task.fbresult.model.Person;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class DAORequester {
    public static List<PeopleOnDuty> getPeopleOnDuty(Duty duty){
        String query = String.format(PeopleOnDutyDao.GET_PEOPLE_ON_DUTY_WITH_DUTY_ID,duty.getId());
        return new PeopleOnDutyDao().get(query);
    }

    public static DutyType getDutyType(Duty duty){
        DutyTypesDao dutyTypesDao = new DutyTypesDao();
        return dutyTypesDao.get(DutyTypesDao.GET_BY_ID_QUERY + duty.getType()).get(0);
    }

    public static List<Person>getPersonsOnDuty(Duty duty){
        List<PeopleOnDuty>peopleOnDuties = getPeopleOnDuty(duty);
        return getPersonsInPeopleOnDuties(peopleOnDuties);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static List<Duty>getPersonDuties(Person person){
        String dutiesQuery = String.format(DutyDao.GET_ALL_DUTIES_WITH_PERSON_ID,
                person.getId(), LocalDateTimeHelper.getTodayDateAsString());
        return new DutyDao().get(dutiesQuery);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static Duty getFirstOfNextDutiesOfPerson(Person person){
        String todayDateAsString = LocalDateTimeHelper.getTodayDateAsString();
        String peopleOnDutyWithIdQuery = String.format(PeopleOnDutyDao.GET_FIRST_PEOPLE_ON_DUTY_OF_PERSON,
                todayDateAsString, person.getId());
        List<PeopleOnDuty> peopleOnDuties = new PeopleOnDutyDao().get(peopleOnDutyWithIdQuery);
        if(peopleOnDuties.isEmpty())
            return null;
        else
            return getDutyWithPeopleOnDuty(peopleOnDuties.get(0));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static Duty getDutyWithPeopleOnDuty(PeopleOnDuty peopleOnDuty){
        String query = String.format(DutyDao.GET_DUTY_WITH_ID,peopleOnDuty.getDutyId());
        return new DutyDao().get(query).get(0);
    }

    public static List<Person> getPersonsInPeopleOnDuties(List<PeopleOnDuty>peopleOnDutyList){
        List<Person>persons = new ArrayList<>();
        TreeSet<Long> userIds = new TreeSet<>();
        for(PeopleOnDuty peopleOnDuty:peopleOnDutyList) {
            Person person = getPersonInPeopleOnDuty(peopleOnDuty);
            if(!userIds.contains(person.getId())) {
                persons.add(person);
                userIds.add(person.getId());
            }
        }
        return persons;
    }

    private static Person getPersonInPeopleOnDuty(PeopleOnDuty peopleOnDuty){
        String query = String.format(PersonDao.GET_USER_WITH_ID, peopleOnDuty.getPersonId());
        return new PersonDao().get(query).get(0);
    }

}
