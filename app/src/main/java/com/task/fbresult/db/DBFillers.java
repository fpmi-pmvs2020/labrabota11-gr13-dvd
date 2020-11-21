package com.task.fbresult.db;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.task.fbresult.db.dao.DutyDao;
import com.task.fbresult.db.dao.DutyTypesDao;
import com.task.fbresult.db.dao.PeopleOnDutyDao;
import com.task.fbresult.db.dao.PersonDao;
import com.task.fbresult.db.dao.RoleDao;
import com.task.fbresult.generators.DatesGenerator;
import com.task.fbresult.generators.DatesToWorkersLinksGenerator;
import com.task.fbresult.generators.DutyGenerator;
import com.task.fbresult.generators.DutyTypesGenerator;
import com.task.fbresult.generators.Generator;
import com.task.fbresult.generators.PeopleOnDutyGenerator;
import com.task.fbresult.generators.PersonGenerator;
import com.task.fbresult.generators.RoleGenerator;
import com.task.fbresult.generators.UsersGenerator;
import com.task.fbresult.model.Person;

import java.util.stream.Stream;

public class DBFillers {
    DBHelper dbHelper;

    public DBFillers(){
        this.dbHelper = dbHelper;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void cleanTables(){

        new DutyDao().delete(null);
        new PersonDao().delete(null);
        new DutyTypesDao().delete(null);
        new PeopleOnDutyDao().delete(null);
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

    private void fillAndPrintColumnOfTable(SQLiteDatabase db, String tableName,
                                           String[] columnNames, Generator generator){
//        String[][]values = generator.generate();
//        insertValues(db, values, tableName, columnNames);
        //dbHelper.writeTable(db,tableName);
    }

    private void insertValues(SQLiteDatabase db, String[][] values,
                              String tableName, String[] columnNames) {
        for (int i = 0; i < values[0].length; i++) {
            ContentValues cv = new ContentValues();
            for (int j = 0;j<values.length;j++)
                cv.put(columnNames[j],values[j][i]);
            db.insert(tableName, null, cv);
        }
    }
}
