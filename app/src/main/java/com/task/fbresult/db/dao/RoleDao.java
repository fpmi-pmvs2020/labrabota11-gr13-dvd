package com.task.fbresult.db.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.task.fbresult.model.Role;

import java.util.ArrayList;
import java.util.List;

import static com.task.fbresult.db.DBHelper.DB_LOG;
import static com.task.fbresult.db.DBHelper.ROLE_ID_COLUMN;
import static com.task.fbresult.db.DBHelper.ROLE_NAME_COLUMN;
import static com.task.fbresult.db.DBHelper.ROLE_TABLE;
import static com.task.fbresult.db.DBHelper.TYPES_TABLE;

public class RoleDao extends Dao<Role>{

    public static String GET_ALL_QUERY = "select * from "+ ROLE_TABLE ;

    @Override
    String getTableName() {
        return ROLE_TABLE;
    }

    @Override
    ContentValues getContentValues(Role role) {
        ContentValues cv = new ContentValues();

        cv.put(ROLE_NAME_COLUMN, role.getName());

        return cv;
    }

    @Override
    long getID(Role role) {
        return role.getId();
    }

    @Override
    List<Role> parseCursor(Cursor c) {
        List<Role> ans = new ArrayList<>();

        if (c.moveToFirst()) {
            int id = c.getColumnIndex(ROLE_ID_COLUMN);
            int name = c.getColumnIndex(ROLE_NAME_COLUMN);

            do {

                Role role = new Role(
                        c.getInt(id),
                        c.getString(name)
                );

                ans.add(role);

                Log.d(DB_LOG, "--- get in table duty types "+ role);

            } while (c.moveToNext());
        }
        return ans;
    }

    @Override
    void updateId(Role role, long id) {
        role.setId(id);
    }
}
