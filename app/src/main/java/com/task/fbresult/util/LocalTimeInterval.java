package com.task.fbresult.util;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalTime;

public class LocalTimeInterval {
    LocalTime start;
    LocalTime end;

    public LocalTimeInterval(LocalTime start,LocalTime end){
        this.start = start;
        this.end = end;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean intersects(LocalTimeInterval anotherInterval) {
        return anotherInterval.contains(start)
                || anotherInterval.contains(end)
                || contains(anotherInterval.start.plusMinutes(1))
                || contains(anotherInterval.end.minusMinutes(1));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean contains(LocalTime localTime){
        return (localTime.isBefore(end) && localTime.isAfter(start));
    }

}
