package com.task.fbresult.model;

import android.content.res.Resources;

import com.task.fbresult.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DutyManager {
    private Duty duty;
    private Resources resources;

    public DutyManager(Duty duty, Resources resources){
        this.duty = duty;
        this.resources = resources;
    }

    public String getDaysLeftAsString(){
        Date currentDate = getCurrentDate();
        GregorianCalendar currentCalendar = new GregorianCalendar();
        currentCalendar.setTime(currentDate);

        int daysBetween = (int)(currentDate.getTime()-new Date().getTime())/(24*60*60*1000);
        if(daysBetween == 0)
            return resources.getString(R.string.today);
        else
            return String.format(resources.getString(R.string.days_left),daysBetween);
    }

    public String getDayOfWeek(){
        Date currentDate = getCurrentDate();
        GregorianCalendar currentCalendar = new GregorianCalendar();
        currentCalendar.setTime(currentDate);
        int dayOfWeek = currentCalendar.get(Calendar.DAY_OF_WEEK);
        String[]days = resources.getStringArray(R.array.daysOfWeek);
        return days[dayOfWeek];
    }

    private Date getCurrentDate(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd.mm.yyyy");
        Date currentDate = null;
//        try {
//            currentDate = sdf.parse(duty.getDate());
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
        throw new UnsupportedOperationException("method isn't realised yet");
        //return currentDate;
    }
}
