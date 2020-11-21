package com.task.fbresult.db.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.task.fbresult.model.Duty;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static com.task.fbresult.db.DBHelper.DUTY_FROM_COLUMN;
import static com.task.fbresult.db.DBHelper.DUTY_ID_COLUMN;
import static com.task.fbresult.db.DBHelper.DUTY_MAX_PEOPLE_COLUMN;
import static com.task.fbresult.db.DBHelper.DUTY_TABLE;
import static com.task.fbresult.db.DBHelper.DUTY_TO_COLUMN;
import static com.task.fbresult.db.DBHelper.DUTY_TYPE_FK_COLUMN;
import static com.task.fbresult.db.DBHelper.DB_LOG;

@RequiresApi(api = Build.VERSION_CODES.O)
public class DutyDao extends Dao<Duty> {

    DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    @Override
    String getTableName() {
        return DUTY_TABLE;
    }

    @Override
    ContentValues getContentValues(Duty duty) {
        ContentValues cv = new ContentValues();

        cv.put(DUTY_ID_COLUMN, duty.getId());
        cv.put(DUTY_FROM_COLUMN, duty.getFrom().format(formatter));
        cv.put(DUTY_TO_COLUMN, duty.getTo().format(formatter));
        cv.put(DUTY_TYPE_FK_COLUMN, duty.getType());
        cv.put(DUTY_MAX_PEOPLE_COLUMN, duty.getMaxPeople());

        return cv;
    }

    @Override
    long getID(Duty duty) {
        return duty.getId();
    }

    @Override
    List<Duty> parseCursor(Cursor c) {
        List<Duty> ans = new ArrayList<>();

        if (c.moveToFirst()) {
            int id = c.getColumnIndex(DUTY_ID_COLUMN);
            int from = c.getColumnIndex(DUTY_FROM_COLUMN);
            int to = c.getColumnIndex(DUTY_TO_COLUMN);
            int type_fk = c.getColumnIndex(DUTY_TYPE_FK_COLUMN);
            int max = c.getColumnIndex(DUTY_MAX_PEOPLE_COLUMN);

            do {

                Duty duty = new Duty(
                        c.getInt(id),
                        LocalDateTime.parse(c.getString(from),formatter),
                        LocalDateTime.parse(c.getString(to),formatter),
                        c.getString(type_fk),
                        c.getInt(max)
                );

                ans.add(duty);

                Log.d(DB_LOG, "--- get in table duty: "+ duty);

            } while (c.moveToNext());
        }
        return ans;
    }

    @Override
    void updateId(Duty duty, long id) {
        duty.setId(id);
    }
}
