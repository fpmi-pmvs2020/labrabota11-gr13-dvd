package com.task.fbresult.generators;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.task.fbresult.db.fbdao.FBDutyDao;
import com.task.fbresult.db.fbdao.FBPersonDao;
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
        List<PeopleOnDuty> ans = new ArrayList<>();
        FBDutyDao dutyDao = new FBDutyDao();
        FBPersonDao personDao = new FBPersonDao();


        List<Duty>duties = dutyDao.getAll();
        List<Person>persons = personDao.getAll();
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
                        persons.get(index).getFirebaseId(),
                        duty.getFirebaseId(),
                        duty.fromAsLocalDateTime(),
                        duty.toAsLocalDateTime()
                );
                ans.add(peopleOnDuty);
            }
        }
        return ans;
    }
}
