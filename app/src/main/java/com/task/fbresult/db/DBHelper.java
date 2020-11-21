package com.task.fbresult.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.task.fbresult.R;
import com.task.fbresult.model.Duty;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private Context context;
    private static DBHelper dbHelper;

    private static final String LOG_TAG = "myLogs";

    private DBHelper(@Nullable Context context, @Nullable String name,
                    @Nullable SQLiteDatabase.CursorFactory factory,
                    int version) {

        super(context, name, factory, version);
        this.context = context;
    }


    public static DBHelper getInstance(@Nullable Context context, @Nullable String name,
                                @Nullable SQLiteDatabase.CursorFactory factory,
                                int version){
        if(dbHelper==null)
            dbHelper = new DBHelper(context, name, factory, version);

        return dbHelper;
    }

    public DBRequester getDBRequester(){
        return new DBRequester(getReadableDatabase());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate(SQLiteDatabase db) {

        InputStream inputStream = context.getResources().openRawResource(R.raw.db);

        try {
            String queries = IOUtils.toString(inputStream);
            for(String query: queries.split(";"))
                db.execSQL(query);
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*
        db.execSQL("create table dates (" +
                "id integer primary key autoincrement," +
                "cur_date date);");
        db.execSQL("create table workers(" +
                "id integer primary key autoincrement," +
                "name text);");
        db.execSQL("create table dates_to_workers_con(" +
                "id integer primary key autoincrement," +
                "date_id integer references dates (id) on delete cascade," +
                "worker_id integer references workers (id) on delete cascade);");*/

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
