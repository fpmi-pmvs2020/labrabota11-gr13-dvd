package com.task.fbresult.generators;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.task.fbresult.db.dao.DutyDao;
import com.task.fbresult.db.dao.PersonDao;
import com.task.fbresult.model.Duty;
import com.task.fbresult.model.PeopleOnDuty;

import java.util.ArrayList;
import java.util.List;

public class PeopleOnDutyGenerator {
    static final int DUTIES_AMOUNT = 10;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static List<PeopleOnDuty> generate(){
        PersonDao personDao = new PersonDao();
        List<PeopleOnDuty> ans = new ArrayList<>();
        DutyDao dutyDao = new DutyDao();


        for (int i=0;i< DUTIES_AMOUNT;i++) {
            Duty duty = dutyDao.get(DutyDao.GET_ALL_QUERY).stream().findAny().get();

            for (int j = 0; j < duty.getMaxPeople(); j++) {
                PeopleOnDuty peopleOnDuty = new PeopleOnDuty(
                        personDao.get(PersonDao.GET_ALL_QUERY).stream().findAny().get().getId(),
                        duty.getId(),
                        duty.getFrom(),
                        duty.getTo()
                );
                ans.add(peopleOnDuty);
            }
        }
        return ans;
    }
}
