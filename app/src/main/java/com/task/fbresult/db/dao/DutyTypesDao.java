package com.task.fbresult.db.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.task.fbresult.db.DBHelper;
import com.task.fbresult.model.DutyTypes;

import java.util.ArrayList;
import java.util.List;

import static com.task.fbresult.db.DBHelper.TYPES_TITLE_COLUMN;
import static com.task.fbresult.db.DBHelper.DB_LOG;
import static com.task.fbresult.db.DBHelper.TYPES_ID_COLUMN;
import static com.task.fbresult.db.DBHelper.TYPES_TABLE;

public class DutyTypesDao extends Dao<DutyTypes> {

    public static String GET_ALL_QUERY = "select * from "+ TYPES_TABLE ;
    public static String GET_BY_ID_QUERY = "select * from "+ TYPES_TABLE + " where " + TYPES_ID_COLUMN + " =" ;

    @Override
    String getTableName() {
        return TYPES_TABLE;
    }

    @Override
    ContentValues getContentValues(DutyTypes dutyTypes) {
        ContentValues cv = new ContentValues();

        cv.put(TYPES_TITLE_COLUMN, dutyTypes.getTitle());

        return cv;
    }

    @Override
    long getID(DutyTypes dutyTypes) {
        return dutyTypes.getId();
    }

    @Override
    List<DutyTypes> parseCursor(Cursor c) {
        List<DutyTypes> ans = new ArrayList<>();

        if (c.moveToFirst()) {
            int id = c.getColumnIndex(TYPES_ID_COLUMN);
            int title = c.getColumnIndex(TYPES_TITLE_COLUMN);

            do {

                DutyTypes types = new DutyTypes(
                        c.getInt(id),
                        c.getString(title)
                );

                ans.add(types);

                Log.d(DB_LOG, "--- get in table duty types "+ types);

            } while (c.moveToNext());
        }
        return ans;
    }

    @Override
    void updateId(DutyTypes dutyTypes, long id) {
        dutyTypes.setId(id);
    }
}
