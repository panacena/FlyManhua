package com.recker.flymanhua.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by recker on 16/7/13.
 */
public class DBSearchHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "search.db";

    private static final int VERSION = 1;

    private static final String SQL_CREATE = "create table search(_id integer primary key autoincrement," +
            "info text, date text)";

    private static final String SQL_DROP = "drop table exists search";

    public DBSearchHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(SQL_DROP);
        db.execSQL(SQL_CREATE);
    }

}
