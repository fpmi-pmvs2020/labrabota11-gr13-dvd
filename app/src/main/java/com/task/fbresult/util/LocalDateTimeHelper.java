package com.task.fbresult.util;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

@RequiresApi(api = Build.VERSION_CODES.O)
public class LocalDateTimeHelper {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String getDateTimeAsString(LocalDateTime date){
        return date.format(formatter);
    }

    public static String getFormattedDate(LocalDate date){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return date.format(dateTimeFormatter);
    }

    public static String getFormattedTime(LocalDateTime localDateTime){
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        return localDateTime.format(timeFormatter);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static LocalDateTime parseString(String strDate){
        return LocalDateTime.parse(strDate,formatter);
    }

    public static String getTodayDateAsString(){
        LocalDateTime localDateTime = LocalDateTime.now();
        return getDateTimeAsString(localDateTime);
    }

    public static String getFormattedMonthAndYear(LocalDate localDate, Context context){
        Locale currentLocale = context.getResources().getConfiguration().locale;
        String month = localDate.getMonth().getDisplayName(TextStyle.FULL_STANDALONE,currentLocale);
        String year = String.valueOf(localDate.getYear());
        return month+", "+year;
    }
}
