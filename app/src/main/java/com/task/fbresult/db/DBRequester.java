package com.task.fbresult.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.task.fbresult.model.Duty;
import com.task.fbresult.util.LocalDateTimeHelper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DBRequester {
    SQLiteDatabase db;

    DBRequester(SQLiteDatabase db){
        this.db = db;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Duty getFirstDutyWithName(String name){
        /*String todayDate = LocalDateTimeHelper.getDateTimeAsString(LocalDate.now());
        Cursor c = db.rawQuery("select cur_date from dates" +
                " inner join dates_to_workers_con as con on dates.id = con.date_id" +
                " inner join workers on con.worker_id = workers.id" +
                " where name = '"+name+"' and cur_date > '"+todayDate +
                "' order by cur_date limit 1;",null);
        if(c.getCount() == 0)
            return null;
        String date = c.getString(1);
        c.close();

        c = getNamesWorkersOnDate(date);
        return parseCursorWithUserName(c,name);*/
        return null;
    }

    private Cursor getNamesWorkersOnDate(String date){
        return db.rawQuery("select name from dates" +
                " inner join dates_to_workers_con as con on dates.id = con.date_id" +
                " inner join workers on con.worker_id = workers.id" +
                " where cur_date = '"+date+"';",null);
    }

    private Duty parseCursorWithUserName(Cursor c, String name){
        String partnerName = c.getString(2);
        String date = c.getString(1);
        c.moveToNext();
        if(partnerName.equals(name)) {
            if(c.moveToNext()) {
                partnerName = c.getString(2);
            }else{
                partnerName = null;
            }
        }
        throw new UnsupportedOperationException();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public List<Duty> getDutiesForName(String name){
        /*String todayDate = LocalDateTimeHelper.getDateTimeAsString(LocalDate.now());
        Cursor c = db.rawQuery("select cur_date, name from dates" +
                " inner join dates_to_workers_con as con on dates.id = con.date_id" +
                " inner join workers on con.worker_id = workers.id" +
                " where name = '"+name+"' and cur_date > '"+todayDate+
                "' order by cur_date;",null);
        ArrayList<Duty> duties = new ArrayList<>();
        if(c.getCount()!=0){
            Duty firstDuty = parseCursorWithUserName(c,name);
            duties.add(firstDuty);
        }

        while(c.moveToNext()){
            Duty tempDuty = parseCursorWithUserName(c,name);
            duties.add(tempDuty);
        }

        return duties;*/
        return null;
    }

    public Duty getDutyWithDate(String date){
        Cursor c = getNamesWorkersOnDate(date);
        if(!c.moveToFirst())
            return null;
        String firstName = c.getString(0);
        c.moveToNext();
        String secondName = c.getString(0);
        throw new UnsupportedOperationException();
    }
}
