package com.task.fbresult.util;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class LocalDateTimeInterval {
    LocalDateTime start;
    LocalDateTime end;

    public LocalDateTimeInterval(LocalDateTime start, LocalDateTime end){
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
    public boolean contains(LocalDateTime localTime){
        return (localTime.isBefore(end) && localTime.isAfter(start));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public int getPointPosition(LocalDateTime localDateTime){
        if(localDateTime.isBefore(start))
            return -1;
        else if(localDateTime.isBefore(end))
            return 0;
        else
            return 1;
    }
}
