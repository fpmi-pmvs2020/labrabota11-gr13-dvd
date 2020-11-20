package com.task.fbresult.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.task.fbresult.model.Duty;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private static DBHelper dbHelper;

    public static final String LOG_TAG = "myLogs";

    public static final String PER_ON_DUTY_TABLE =              "PeopleOnDuty";
    public static final String PER_ON_DUTY_ID_COLUMN =          "Id";
    public static final String PER_ON_DUTY_PERSON_FK_COLUMN =   "PersonId";
    public static final String PER_ON_DUTY_DUTY_FK_COLUMN =     "DutyId";
    public static final String PER_ON_DUTY_FROM_COLUMN =        "OnDutyFrom";
    public static final String PER_ON_DUTY_TO_COLUMN =          "OnDutyTo";

    public static final String PERSON_TABLE =                   "People";
    public static final String PERSON_ID_COLUMN =               "Id";
    public static final String PERSON_LOGIN_COLUMN =            "Login";
    public static final String PERSON_FIO_COLUMN =              "FIO";
    public static final String PERSON_TEL_COLUMN =              "Telephone";
    public static final String PERSON_ADDRESS_COLUMN =          "Address";
    public static final String PERSON_BIRTH_COLUMN =            "BirthDate";
    public static final String PERSON_ROLE_COLUMN =             "Role";

    public static final String DUTY_TABLE =                     "Duties";
    public static final String DUTY_ID_COLUMN =                 "Id";
    public static final String DUTY_FROM_COLUMN =               "DutyFrom";
    public static final String DUTY_TO_COLUMN =                 "DutyTo";
    public static final String DUTY_TYPE_FK_COLUMN =            "DutyTypeId";
    public static final String DUTY_MAX_PEOPLE_COLUMN =         "MaxPeopleOnDuty";

    public static final String TYPES_TABLE =                    "DutyTypes";
    public static final String TYPES_ID_COLUMN =                "Id";
    public static final String DUTY_TITLE_COLUMN =              "TypeName";


    private DBHelper(@Nullable Context context, @Nullable String name,
                    @Nullable SQLiteDatabase.CursorFactory factory,
                    int version) {
        super(context, name, factory, version);
    }


    public static DBHelper getInstance(@Nullable Context context, @Nullable String name,
                                @Nullable SQLiteDatabase.CursorFactory factory,
                                int version){
        if(dbHelper==null)
            dbHelper = new DBHelper(context, name, factory, version);

        return dbHelper;
    }

    public static DBHelper newInstance(){

        if (dbHelper == null){
            throw new RuntimeException("context don't exist, use another newInstant(_context)");
        }
        return dbHelper;
    }

    public DBRequester getDBRequester(){
        return new DBRequester(getReadableDatabase());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table dates (" +
                "id integer primary key autoincrement," +
                "cur_date date);");
        db.execSQL("create table workers(" +
                "id integer primary key autoincrement," +
                "name text);");
        db.execSQL("create table dates_to_workers_con(" +
                "id integer primary key autoincrement," +
                "date_id integer references dates (id) on delete cascade," +
                "worker_id integer references workers (id) on delete cascade);");

        DBFiller dbFiller = new DBFiller(db,this);
        dbFiller.fillData();
    }

    void writeTable(SQLiteDatabase db, String tableName) {
        Cursor c = db.rawQuery("select * from " + tableName, null);
        logCursor(c, "Table "+tableName);
        c.close();
    }

    private void logCursor(Cursor c, String title) {
        if (c != null) {
            if (c.moveToFirst()) {
                Log.d(LOG_TAG, title + ". " + c.getCount() + " rows");
                StringBuilder sb = new StringBuilder();
                do {
                    sb.setLength(0);
                    for (String cn : c.getColumnNames()) {
                        sb.append(cn)
                                .append(" = ")
                                .append(c.getString(c.getColumnIndex(cn)))
                                .append("; ");
                    }
                    Log.d(LOG_TAG, sb.toString());
                } while (c.moveToNext());
            }
        } else Log.d(LOG_TAG, title + ". Cursor is null");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
