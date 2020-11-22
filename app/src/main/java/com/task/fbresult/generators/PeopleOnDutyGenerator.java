package com.task.fbresult.generators;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.task.fbresult.db.dao.DutyDao;
import com.task.fbresult.db.dao.PersonDao;
import com.task.fbresult.model.Duty;
import com.task.fbresult.model.PeopleOnDuty;
import com.task.fbresult.model.Person;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.TreeSet;

public class PeopleOnDutyGenerator {
    static final int DUTIES_AMOUNT = 10;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static List<PeopleOnDuty> generate(){
        PersonDao personDao = new PersonDao();
        List<PeopleOnDuty> ans = new ArrayList<>();
        DutyDao dutyDao = new DutyDao();


        List<Duty>duties = dutyDao.get(DutyDao.GET_ALL_QUERY);
        List<Person>persons = personDao.get(PersonDao.GET_ALL_QUERY);
        Random random = new Random();
        for (Duty duty:duties) {
            TreeSet<Integer> peopleIndexes = new TreeSet<>();
            for (int j = 0; j < duty.getMaxPeople(); j++) {
                int index;
                do {
                    index = random.nextInt(persons.size());
                }while (peopleIndexes.contains(index));
                peopleIndexes.add(index);
                PeopleOnDuty peopleOnDuty = new PeopleOnDuty(
                        persons.get(index).getId(),
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
