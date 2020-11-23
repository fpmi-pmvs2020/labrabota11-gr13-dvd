package com.task.fbresult.util;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.task.fbresult.model.Duty;
import com.task.fbresult.model.PeopleOnDuty;
import com.task.fbresult.model.Person;

import java.time.LocalDate;
import java.time.LocalTime;
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
        Person currentUser = FBUtils.getCurrentUserAsPerson();
        List<PeopleOnDuty>peopleOnDuties = DAORequester.getPeopleOnDuty(duty);
        List<PeopleOnDuty>currentUserOnDuty = peopleOnDuties.stream()
                .filter(peopleOnDuty -> peopleOnDuty.getPersonId() == currentUser.getId())
                .collect(Collectors.toList());
        peopleOnDuties.removeIf(peopleOnDuty -> peopleOnDuty.getPersonId() == currentUser.getId());
        List<PeopleOnDuty>partnersOnDuty = getWhoWorksWithUser(peopleOnDuties,currentUserOnDuty);
        return DAORequester.getPersonsInPeopleOnDuties(partnersOnDuty);
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
        LocalDateTimeInterval firstInterval = new LocalDateTimeInterval(first.getFrom(),first.getTo());
        LocalDateTimeInterval secondInterval = new LocalDateTimeInterval(second.getFrom(),second.getTo());
        return firstInterval.intersects(secondInterval);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public long getDaysLeft(){
        LocalDate dutyDate = duty.getFrom().toLocalDate();
        LocalDate todayDate = LocalDate.now();
        return DAYS.between(todayDate,dutyDate);
    }
}
