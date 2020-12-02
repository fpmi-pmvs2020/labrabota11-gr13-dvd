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

    //region fields
    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "duties_db";
    public static final String DB_LOG = "myDbLogs";
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
    public static final String PERSON_ROLE_COLUMN =             "RoleId";
    public static final String PERSON_IMAGE_COLUMN =             "Image";

    public static final String DUTY_TABLE =                     "Duties";
    public static final String DUTY_ID_COLUMN =                 "Id";
    public static final String DUTY_FROM_COLUMN =               "DutyFrom";
    public static final String DUTY_TO_COLUMN =                 "DutyTo";
    public static final String DUTY_TYPE_FK_COLUMN =            "DutyTypeId";
    public static final String DUTY_MAX_PEOPLE_COLUMN =         "MaxPeopleOnDuty";

    public static final String ROLE_TABLE =                     "Roles";
    public static final String ROLE_ID_COLUMN =                 "Id";
    public static final String ROLE_NAME_COLUMN =               "Name";

    public static final String TYPES_TABLE =                    "DutyTypes";
    public static final String TYPES_ID_COLUMN =                "Id";
    public static final String TYPES_TITLE_COLUMN =             "TypeName";
    //endregion fields

    private DBHelper(@Nullable Context context, @Nullable String name,
                    @Nullable SQLiteDatabase.CursorFactory factory,
                    int version) {
        super(context, name, factory, version);
    }


    public static DBHelper getInstance(@Nullable Context context,
                                       @Nullable SQLiteDatabase.CursorFactory factory){
        if(dbHelper==null)
            dbHelper = new DBHelper(context, DB_NAME, factory, DB_VERSION);

        return dbHelper;
    }

    public static DBHelper getInstance(){

        if (dbHelper == null){
            throw new RuntimeException("context don't exist, use another newInstant(_context)");
        }
        return dbHelper;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate(SQLiteDatabase db) {
        String typeTable = "create table " + TYPES_TABLE + "("+
                TYPES_ID_COLUMN +           " integer primary key autoincrement," +
                TYPES_TITLE_COLUMN +        " text not null);";

        String roleTable = "create table " + ROLE_TABLE + "("+
                ROLE_ID_COLUMN +           " integer primary key autoincrement," +
                ROLE_NAME_COLUMN +        " text not null);";

        String dutyTable = "create table " + DUTY_TABLE + "("+
                DUTY_ID_COLUMN +                " integer primary key autoincrement," +
                DUTY_FROM_COLUMN +              " text not null," +
                DUTY_TO_COLUMN +                " text not null," +
                DUTY_TYPE_FK_COLUMN +           " integer references "+TYPES_TABLE+" ("+TYPES_ID_COLUMN+")," +
                DUTY_MAX_PEOPLE_COLUMN +        " integer not null);";

        String personTable = "create table " + PERSON_TABLE + "("+
                PERSON_ID_COLUMN +               " integer primary key autoincrement," +
                PERSON_LOGIN_COLUMN +            " text not null," +
                PERSON_FIO_COLUMN +              " text," +
                PERSON_TEL_COLUMN +              " text," +
                PERSON_ADDRESS_COLUMN +          " text," +
                PERSON_BIRTH_COLUMN +            " text," +
                PERSON_IMAGE_COLUMN +            " BLOB," +
                PERSON_ROLE_COLUMN +             " integer references "+ROLE_TABLE+" ("+ROLE_ID_COLUMN+")"+");";

        String personOnDuty = "create table " + PER_ON_DUTY_TABLE + "("+
                PER_ON_DUTY_ID_COLUMN +          " integer primary key autoincrement," +
                PER_ON_DUTY_PERSON_FK_COLUMN +   " integer references "+PERSON_TABLE+" ("+PERSON_ID_COLUMN+")," +
                PER_ON_DUTY_DUTY_FK_COLUMN +     " integer references "+DUTY_TABLE+" ("+DUTY_ID_COLUMN+")," +
                PER_ON_DUTY_FROM_COLUMN +        " text not null," +
                PER_ON_DUTY_TO_COLUMN +          " text not null);";

        db.execSQL(typeTable);
        db.execSQL(roleTable);
        db.execSQL(dutyTable);
        db.execSQL(personOnDuty);
        db.execSQL(personTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
