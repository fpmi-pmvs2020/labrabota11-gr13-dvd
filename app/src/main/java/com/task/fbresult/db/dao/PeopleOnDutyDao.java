package com.task.fbresult.db.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.task.fbresult.model.PeopleOnDuty;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static com.task.fbresult.db.DBHelper.*;

public class PeopleOnDutyDao extends Dao<PeopleOnDuty> {
    public static String GET_PEOPLE_ON_DUTY_WITH_DUTY_ID = "select * from "+ PER_ON_DUTY_TABLE
            + "where "+PER_ON_DUTY_DUTY_FK_COLUMN+ "=";

    @Override
    String getTableName() {
        return PER_ON_DUTY_TABLE;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    ContentValues getContentValues(PeopleOnDuty peopleOnDuty) {
        ContentValues cv = new ContentValues();

        cv.put(PER_ON_DUTY_PERSON_FK_COLUMN, peopleOnDuty.getPersonId());
        cv.put(PER_ON_DUTY_DUTY_FK_COLUMN, peopleOnDuty.getDutyId());
        cv.put(PER_ON_DUTY_FROM_COLUMN, peopleOnDuty.getFrom().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        cv.put(PER_ON_DUTY_TO_COLUMN, peopleOnDuty.getTo().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

        return cv;
    }

    @Override
    long getID(PeopleOnDuty peopleOnDuty) {
        return peopleOnDuty.getId();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    List<PeopleOnDuty> parseCursor(Cursor c) {
        List<PeopleOnDuty> ans = new ArrayList<>();

        if (c.moveToFirst()) {
            int id = c.getColumnIndex(PER_ON_DUTY_ID_COLUMN);
            int per_fk = c.getColumnIndex(PER_ON_DUTY_PERSON_FK_COLUMN);
            int duty_fk = c.getColumnIndex(PER_ON_DUTY_DUTY_FK_COLUMN);
            int from = c.getColumnIndex(PER_ON_DUTY_FROM_COLUMN);
            int to = c.getColumnIndex(PER_ON_DUTY_TO_COLUMN);

            do {

                PeopleOnDuty person = new PeopleOnDuty(
                        c.getInt(id),
                        c.getInt(per_fk),
                        c.getInt(duty_fk),
                        LocalDateTime.parse(c.getString(from),DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                        LocalDateTime.parse(c.getString(to),DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                );

                ans.add(person);

                Log.d(DB_LOG, "--- get in table people on duty: "+ person);

            } while (c.moveToNext());
        }
        return ans;
    }

    @Override
    void updateId(PeopleOnDuty peopleOnDuty, long id) {
        peopleOnDuty.setId(id);

    }
}

