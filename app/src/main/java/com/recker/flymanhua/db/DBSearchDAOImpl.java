package com.recker.flymanhua.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.recker.flymanhua.datas.SearchData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by recker on 16/7/13.
 */
public class DBSearchDAOImpl {
    private static DBSearchDAOImpl instance;
    private DBSearchHelper mHelper;

    private DBSearchDAOImpl(){}
    private DBSearchDAOImpl(Context context) {
        mHelper = new DBSearchHelper(context);
    }

    public static DBSearchDAOImpl getInstance(Context context) {
        if (instance == null) {
            synchronized (DBSearchDAOImpl.class) {
                if (instance == null) {
                    instance = new DBSearchDAOImpl(context);
                }
            }
        }
        return instance;
    }

    public void insertData(SearchData data) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.execSQL("insert into search(info, date)values(?,?)",
                new Object[]{data.getInfo(), data.getDate()});
        db.close();
    }

    public void deletedOneData(int id) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.execSQL(
                "delete from search where _id = ?",
                new Object[]{id});
        db.close();
    }

    public List<SearchData> getAllData() {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        List<SearchData> list = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from search order by _id desc limit ?, ?",
                new String[] {"0", "10000"});
        while (cursor.moveToNext()) {
            SearchData data = new SearchData();
            data.setId(cursor.getInt(cursor.getColumnIndex("_id")));
            data.setInfo(cursor.getString(cursor.getColumnIndex("info")));
            data.setDate(cursor.getString(cursor.getColumnIndex("date")));
            list.add(data);
        }
        db.close();
        return list;

    }

}
