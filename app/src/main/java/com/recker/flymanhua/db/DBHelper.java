package com.recker.flymanhua.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by recker on 16/6/15.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "manhua.db";

    private static final int VERSION = 1;

    private static final String SQL_CREATE = "create table dimension(_id integer primary key autoincrement," +
            "dimension_id integer, sortId integer, dimensionTitle text, sortTile text, date text, " +
            "netImg text, localImg text, sort integer, author text)";

    private static final String SQL_DROP = "drop table exists dimension";

    public DBHelper(Context context) {
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
