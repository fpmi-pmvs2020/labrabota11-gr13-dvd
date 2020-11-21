package com.task.fbresult.model;

import android.content.res.Resources;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.task.fbresult.R;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static java.time.temporal.ChronoUnit.DAYS;

public class DutyManager {
    private final Duty duty;
    private final Resources resources;

    public DutyManager(Duty duty, Resources resources){
        this.duty = duty;
        this.resources = resources;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getStartTime(){
        LocalTime localTime = duty.getFrom().toLocalTime();
        return getFormattedTime(localTime);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getFinishTime(){
        LocalTime localTime = duty.getTo().toLocalTime();
        return getFormattedTime(localTime);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String getFormattedTime(LocalTime localTime){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("hh:mm");
        return localTime.format(dateTimeFormatter);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getDaysLeftAsString(){
        LocalDate dutyDate = duty.getFrom().toLocalDate();
        LocalDate todayDate = LocalDate.now();

        long daysBetween = DAYS.between(dutyDate,todayDate);
        if(daysBetween == 0)
            return resources.getString(R.string.today);
        else
            return String.format(resources.getString(R.string.days_left),daysBetween);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getDayOfWeek(){
        LocalDateTime localDateTime = duty.getFrom();
        int dayOfWeek = localDateTime.getDayOfWeek().getValue()-1;
        String[]days = resources.getStringArray(R.array.daysOfWeek);
        return days[dayOfWeek];
    }
}
