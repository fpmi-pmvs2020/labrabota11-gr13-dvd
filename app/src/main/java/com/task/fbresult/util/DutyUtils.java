package com.task.fbresult.util;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.task.fbresult.model.PeopleOnDuty;

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
}
