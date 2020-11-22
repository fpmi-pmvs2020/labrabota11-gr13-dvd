package com.task.fbresult.util;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RequiresApi(api = Build.VERSION_CODES.O)
public class LocalDateTimeHelper {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String getDateTimeAsString(LocalDateTime date){
        return date.format(formatter);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static LocalDateTime parseString(String strDate){
        return LocalDateTime.parse(strDate,formatter);
    }

    public static String getTodayDateAsString(){
        LocalDateTime localDateTime = LocalDateTime.now();
        return getDateTimeAsString(localDateTime);
    }
}
