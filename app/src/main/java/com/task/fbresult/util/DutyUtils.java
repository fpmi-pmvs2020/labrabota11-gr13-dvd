package com.task.fbresult.util;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.task.fbresult.db.fbdao.FBPeopleOnDutyDao;
import com.task.fbresult.model.DutyIntervalData;
import com.task.fbresult.model.PeopleOnDuty;

import lombok.var;

public class DutyUtils {
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static boolean doWorkOnTheSameTime(PeopleOnDuty first, PeopleOnDuty second){
        LocalDateTimeInterval firstInterval = new LocalDateTimeInterval(
                first.fromAsLocalDateTime(),
                first.toAsLocalDateTime()
        );
        LocalDateTimeInterval secondInterval = new LocalDateTimeInterval(
                second.fromAsLocalDateTime(),
                second.toAsLocalDateTime()
        );
        return firstInterval.intersects(secondInterval);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static boolean alreadyWorksOnThatTime(
            String personId,
            LocalDateTimeInterval goalDutyInterval,
            PeopleOnDuty goalPeopleOnDuty) {
        if(goalDutyInterval == null)
            return false;
        var goalDuty = DAORequester.getDutyWithPeopleOnDuty(goalPeopleOnDuty);
        var goalPeopleOnDuties = DAORequester.getPeopleOnDuty(goalDuty);
        for(var goalPeople:goalPeopleOnDuties){
            if(goalPeople.getPersonId().equals(personId)){
                if(containsInterval(goalPeople,goalDutyInterval))
                    return true;
            }
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static boolean containsInterval(PeopleOnDuty peopleOnDuty, LocalDateTimeInterval interval){
        var dutyFrom = LocalDateTimeHelper.parseString(peopleOnDuty.getFrom());
        var dutyTo = LocalDateTimeHelper.parseString(peopleOnDuty.getTo());
        var dutyLocalDateTimeInterval = new LocalDateTimeInterval(dutyFrom,dutyTo);
        return dutyLocalDateTimeInterval.intersects(interval);
    }
}
