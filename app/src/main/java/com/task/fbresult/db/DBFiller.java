package com.task.fbresult.db;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.task.fbresult.generators.DatesGenerator;
import com.task.fbresult.generators.DatesToWorkersLinksGenerator;
import com.task.fbresult.generators.Generator;
import com.task.fbresult.generators.UsersGenerator;

public class DBFiller {
    SQLiteDatabase db;
    DBHelper dbHelper;

    public DBFiller(SQLiteDatabase db, DBHelper dbHelper){
        this.db = db;
        this.dbHelper = dbHelper;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void fillData() {
        fillAndPrintColumnOfTable(db,"workers",
                new String[]{"name"}, UsersGenerator::generate);
        fillAndPrintColumnOfTable(db,"dates",
                new String[]{"cur_date"}, DatesGenerator::generate);
        fillAndPrintColumnOfTable(db,"dates_to_workers_con",
                new String[]{"date_id","worker_id"}, DatesToWorkersLinksGenerator::generate);
    }

    private void fillAndPrintColumnOfTable(SQLiteDatabase db, String tableName,
                                           String[] columnNames, Generator generator){
        String[][]values = generator.generate();
        insertValues(db, values, tableName, columnNames);
        dbHelper.writeTable(db,tableName);
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
