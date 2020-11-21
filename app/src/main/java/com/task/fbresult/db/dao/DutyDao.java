package com.task.fbresult.db.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.task.fbresult.model.Duty;
import com.task.fbresult.util.LocalDateTimeHelper;

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
import static com.task.fbresult.db.DBHelper.TYPES_TABLE;

@RequiresApi(api = Build.VERSION_CODES.O)
public class DutyDao extends Dao<Duty> {

    public static String GET_DUTY_WITH_ID = "select * from "+DUTY_TABLE+" where "+DUTY_ID_COLUMN+"=";
    public static String GET_ALL_QUERY = "select * from "+ DUTY_TABLE ;
    public static String GET_DUTIES_WITH_DAY = "select * from"
            +DUTY_TABLE+"where "+DUTY_FROM_COLUMN+"> %s and "+DUTY_TO_COLUMN+"< %s";

    @Override
    String getTableName() {
        return DUTY_TABLE;
    }

    @Override
    ContentValues getContentValues(Duty duty) {
        ContentValues cv = new ContentValues();

        cv.put(DUTY_FROM_COLUMN, LocalDateTimeHelper.getDateTimeAsString(duty.getFrom()));
        cv.put(DUTY_TO_COLUMN, LocalDateTimeHelper.getDateTimeAsString(duty.getTo()));
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
                        LocalDateTimeHelper.parseString(c.getString(from)),
                        LocalDateTimeHelper.parseString(c.getString(to)),
                        c.getInt(type_fk),
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
