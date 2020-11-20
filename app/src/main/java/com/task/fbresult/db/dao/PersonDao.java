package com.task.fbresult.db.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.task.fbresult.db.DBHelper;
import com.task.fbresult.model.Person;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static com.task.fbresult.db.DBHelper.*;

public class PersonDao extends Dao<Person>{
    @Override
    String getTableName() {
        return DBHelper.PERSON_TABLE;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    ContentValues getContentValues(Person person) {
        ContentValues cv = new ContentValues();

        cv.put(PERSON_ID_COLUMN, person.getId());
        cv.put(PERSON_LOGIN_COLUMN, person.getLogin());
        cv.put(PERSON_FIO_COLUMN, person.getFio());
        cv.put(PERSON_TEL_COLUMN, person.getTelephone());
        cv.put(PERSON_ADDRESS_COLUMN, person.getAddress());
        cv.put(PERSON_BIRTH_COLUMN, person.getBirthday().format(DateTimeFormatter.ISO_DATE));
        cv.put(PERSON_ROLE_COLUMN, person.getRole());

        return cv;
    }

    @Override
    long getID(Person person) {
        return person.getId();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    List<Person> parseCursor(Cursor c) {
        List<Person> ans = new ArrayList<>();

        if (c.moveToFirst()) {
            int id = c.getColumnIndex(PERSON_ID_COLUMN);
            int login = c.getColumnIndex(PERSON_LOGIN_COLUMN);
            int fio = c.getColumnIndex(PERSON_FIO_COLUMN);
            int tel = c.getColumnIndex(PERSON_TEL_COLUMN);
            int add = c.getColumnIndex(PERSON_ADDRESS_COLUMN);
            int birth = c.getColumnIndex(PERSON_BIRTH_COLUMN);
            int role = c.getColumnIndex(PERSON_ROLE_COLUMN);

            do {

                Person person = new Person(
                        c.getInt(id),
                        c.getString(login),
                        c.getString(fio),
                        c.getString(tel),
                        c.getString(add),
                        LocalDate.parse(c.getString(birth), DateTimeFormatter.ISO_DATE),
                        c.getString(role)
                );
                ans.add(person);

                Log.d(LOG_TAG, "--- get in table person: "+ person);

            } while (c.moveToNext());
        }
        return ans;
    }

    @Override
    void updateId(Person person, long id) {
        person.setId(id);
    }
}
