package com.task.fbresult.generators;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.task.fbresult.db.fbdao.FBDutyTypesDao;
import com.task.fbresult.model.Duty;
import com.task.fbresult.model.DutyType;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DutyGenerator {
    public static int DUTY_AMOUNTS = 50;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static List<Duty> generate(){
        List<Duty> ans = new ArrayList<>();
        List<DutyType>dutyTypes = (new FBDutyTypesDao()).getAll();
        for(int i=0; i< DUTY_AMOUNTS; i++){

            Month month = getMonth();
            int day = getDay(month);
            Random random = new Random();
            int typeIndex = random.nextInt(dutyTypes.size());
            Duty duty = new Duty(
                    LocalDateTime.of(LocalDateTime.now().getYear(), month, day, 19, 0,0),
                    LocalDateTime.of(LocalDateTime.now().getYear(), month, day, 23, 59,0),
                    dutyTypes.get(typeIndex).getFirebaseId(),
                    random.nextInt(4)+1
            );

            ans.add(duty);
        }
        return ans;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static Month getMonth(){
        Random random = new Random();
        return LocalDateTime.now().getMonth().plus(random.nextBoolean()?0:1);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static int getDay(Month month){
        Random random= new Random();
        return random.nextInt(month.maxLength())+1;
    }
}
