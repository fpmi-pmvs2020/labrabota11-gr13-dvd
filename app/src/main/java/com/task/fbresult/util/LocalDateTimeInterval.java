package com.task.fbresult.util;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.task.fbresult.model.DutyIntervalData;
import com.task.fbresult.model.PeopleOnDuty;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

import lombok.var;

public class LocalDateTimeInterval {
    LocalDateTime start;
    LocalDateTime end;


    public LocalDateTimeInterval(LocalDateTime start, LocalDateTime end) {
        this.start = start;
        this.end = end;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean intersects(LocalDateTimeInterval anotherInterval) {
        return anotherInterval.contains(start)
                || anotherInterval.contains(end)
                || contains(anotherInterval.start.plusMinutes(1))
                || contains(anotherInterval.end.minusMinutes(1));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static LocalDateTimeInterval of(DutyIntervalData dutyIntervalData) {
        var from = LocalDateTimeHelper.parseString(dutyIntervalData.getFrom());
        var to = LocalDateTimeHelper.parseString(dutyIntervalData.getTo());
        return new LocalDateTimeInterval(from, to);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static LocalDateTimeInterval of(PeopleOnDuty peopleOnDuty){
        var from = LocalDateTimeHelper.parseString(peopleOnDuty.getFrom());
        var to = LocalDateTimeHelper.parseString(peopleOnDuty.getTo());
        return new LocalDateTimeInterval(from, to);
    }


    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public int getHoursBetween() {
        var temp = end.getHour() - start.getHour();
        temp = end.getMinute() != 0 ? temp + 1 : temp;
        return temp;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean contains(LocalDateTime localTime) {
        return (localTime.isBefore(end) && localTime.isAfter(start)
                || localTime.isEqual(end) || localTime.isEqual(start));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public int getPointPosition(LocalDateTime localDateTime) {
        if (localDateTime.isBefore(start))
            return -1;
        else if (localDateTime.isBefore(end))
            return 0;
        else
            return 1;
    }
}
