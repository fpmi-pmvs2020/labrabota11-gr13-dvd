package com.task.fbresult.db.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.task.fbresult.db.DBHelper;

import java.util.List;

public abstract class Dao<T> {

    private static final String LOG_TAG = "db_log";
    DBHelper dbHelper = DBHelper.getInstance();

    public List<T> get(String query){

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor c = db.rawQuery(query, null);

        List<T> parseCursor = parseCursor(c);
        c.close();

        return parseCursor;
    }

    public void save(T t){

        ContentValues cv = getContentValues(t);

        SQLiteDatabase db  = dbHelper.getWritableDatabase();

        long rowID  = db.insert(getTableName(), null, cv);
        updateId(t, rowID);

        Log.d(LOG_TAG, "--- Insert in "+getTableName()+": ---rowID: "+rowID+"    "+t);
    }

    public void update(T t){
        ContentValues cv = getContentValues(t);

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.update(getTableName(), cv, "id = "+getID(t), null);
        Log.d(LOG_TAG, getTableName()+"--- update in "+t);

    }

    public void delete(T t){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int delCount = db.delete(getTableName(), "id = "+ getID(t), null);
        Log.d(LOG_TAG, getTableName()+"--- delete in :"+t);
    }


    abstract String getTableName();
    abstract ContentValues getContentValues(T t);
    abstract long getID(T t);
    abstract List<T> parseCursor(Cursor c);
    abstract void updateId(T t, long id);
}
