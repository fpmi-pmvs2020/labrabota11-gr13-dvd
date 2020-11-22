package com.task.fbresult.db;

import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.task.fbresult.db.dao.DutyDao;
import com.task.fbresult.db.dao.DutyTypesDao;
import com.task.fbresult.db.dao.PeopleOnDutyDao;
import com.task.fbresult.db.dao.PersonDao;
import com.task.fbresult.db.dao.RoleDao;
import com.task.fbresult.generators.DutyGenerator;
import com.task.fbresult.generators.DutyTypesGenerator;
import com.task.fbresult.generators.PeopleOnDutyGenerator;
import com.task.fbresult.generators.PersonGenerator;
import com.task.fbresult.generators.RoleGenerator;

public class DBFillers {

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void cleanTables() {
        SQLiteDatabase db = DBHelper.getInstance().getWritableDatabase();

        db.delete(DBHelper.DUTY_TABLE, null, null);
        db.delete(DBHelper.TYPES_TABLE, null, null);
        db.delete(DBHelper.PERSON_TABLE, null, null);
        db.delete(DBHelper.ROLE_TABLE, null, null);
        db.delete(DBHelper.PER_ON_DUTY_TABLE, null, null);

        Log.d(DBHelper.DB_LOG, "information from table was deleted");

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void fillData() {

        cleanTables();
        DutyDao dutyDao = new DutyDao();
        DutyTypesDao dutyTypesDao = new DutyTypesDao();
        PeopleOnDutyDao peopleOnDutyDao = new PeopleOnDutyDao();
        PersonDao personDao = new PersonDao();
        RoleDao roleDao = new RoleDao();

        RoleGenerator.generate().forEach(roleDao::save);
        DutyTypesGenerator.generate().forEach(dutyTypesDao::save);
        PersonGenerator.generate().forEach(personDao::save);
        DutyGenerator.generate().forEach(dutyDao::save);
        PeopleOnDutyGenerator.generate().forEach(peopleOnDutyDao::save);

    }
}
