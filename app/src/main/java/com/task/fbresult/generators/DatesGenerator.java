package com.task.fbresult.generators;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.task.fbresult.util.LocalDateTimeHelper;

import java.time.LocalDate;

public class DatesGenerator{
    private final static int datesNumber = 40;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String[][] generate() {
        String[]result = new String[datesNumber];
       /* LocalDate date = LocalDate.now();
        date = date.plusDays(50);
        for(int i = 0;i<datesNumber;i++) {
            date = date.plusDays(1);
            result[i] = LocalDateTimeHelper.getDateTimeAsString(date);
        }*/
        return new String[][]{result};
    }
}
