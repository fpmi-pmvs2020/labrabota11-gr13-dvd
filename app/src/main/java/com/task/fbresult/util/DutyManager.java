package com.task.fbresult.util;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.firebase.auth.FirebaseAuth;
import com.task.fbresult.db.dao.PeopleOnDutyDao;
import com.task.fbresult.db.dao.PersonDao;
import com.task.fbresult.model.Duty;
import com.task.fbresult.model.PeopleOnDuty;
import com.task.fbresult.model.Person;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.DAYS;

public class DutyManager {
    private final Duty duty;

    public DutyManager(Duty duty){
        this.duty = duty;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public List<Person> getPartners(){
        String login = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String userQuery = String.format(PersonDao.GET_USER_WITH_LOGIN_QUERY,login);
        Person currentUser = new PersonDao().get(userQuery).get(0);
        String query = String.format(PeopleOnDutyDao.GET_PEOPLE_ON_DUTY_WITH_DUTY_ID,duty.getId());
        List<PeopleOnDuty>peopleOnDuties = new PeopleOnDutyDao().get(query);
        List<PeopleOnDuty>currentUserOnDuty = peopleOnDuties.stream()
                .filter(peopleOnDuty -> peopleOnDuty.getPersonId() == currentUser.getId())
                .collect(Collectors.toList());
        peopleOnDuties.removeIf(peopleOnDuty -> peopleOnDuty.getPersonId() == currentUser.getId());
        List<PeopleOnDuty>partnersOnDuty = getWhoWorksWithUser(peopleOnDuties,currentUserOnDuty);
        return transformPeopleOnDutyToPersons(partnersOnDuty);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private List<PeopleOnDuty> getWhoWorksWithUser(List<PeopleOnDuty>peopleOnDuties,
                                                   List<PeopleOnDuty>currentUserOnDuty){
        return peopleOnDuties.stream()
                .filter(peopleOnDuty -> workOnTimeWithPersonIntervals(currentUserOnDuty,peopleOnDuty))
                .collect(Collectors.toList());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private boolean workOnTimeWithPersonIntervals(List<PeopleOnDuty>intervals, PeopleOnDuty person){
        List<PeopleOnDuty> currentIntervals = intervals.stream()
                .filter(peopleOnDuty -> doWorkOnTheSameTime(peopleOnDuty,person))
                .collect(Collectors.toList());
        return !currentIntervals.isEmpty();
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private boolean doWorkOnTheSameTime(PeopleOnDuty first, PeopleOnDuty second){
        LocalTime firstFrom = first.getFrom().toLocalTime();
        LocalTime firstTo = first.getTo().toLocalTime();
        LocalTime secondFrom = second.getFrom().toLocalTime();
        LocalTime secondTo = second.getTo().toLocalTime();

        LocalTimeInterval firstInterval = new LocalTimeInterval(firstFrom,firstTo);
        LocalTimeInterval secondInterval = new LocalTimeInterval(secondFrom,secondTo);
        return firstInterval.intersects(secondInterval);
    }

    private List<Person> transformPeopleOnDutyToPersons(List<PeopleOnDuty>peopleOnDutyList){
        PersonDao personDao = new PersonDao();
        List<Person>persons = new ArrayList<>();
        for(PeopleOnDuty personOnDuty:peopleOnDutyList) {
            String query = String.format(PersonDao.GET_USER_WITH_ID, personOnDuty.getPersonId());
            persons.add(personDao.get(query).get(0));
        }
        return persons;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public long getDaysLeft(){
        LocalDate dutyDate = duty.getFrom().toLocalDate();
        LocalDate todayDate = LocalDate.now();
        return DAYS.between(dutyDate,todayDate);
    }
}
