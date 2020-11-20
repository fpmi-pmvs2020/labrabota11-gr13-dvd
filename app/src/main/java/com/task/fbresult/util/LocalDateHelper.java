package com.task.fbresult.util;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateHelper {
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String getDateAsString(LocalDate date){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return date.format(dateTimeFormatter);
    }
}
