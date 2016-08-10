package com.recker.flymanhua.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.recker.flymanhua.datas.RecortSort;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by recker on 16/6/15.
 */
public class DimensionDAOImpl {
    private static DimensionDAOImpl instance;
    private DBHelper mHelper;

    private DimensionDAOImpl(){}
    private DimensionDAOImpl(Context context) {
        mHelper = new DBHelper(context);
    }

    public static DimensionDAOImpl getInstance(Context context) {
        if (instance == null) {
            synchronized (DimensionDAOImpl.class) {
                if (instance == null) {
                    instance = new DimensionDAOImpl(context);
                }
            }
        }
        return instance;
    }

    public void insertData(RecortSort data) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.execSQL("insert into dimension(dimension_id, sortId, dimensionTitle, sortTile, date, netImg, sort, author)"
                +"values(?, ?, ?, ?, ?, ?, ?, ?)", new Object[]{data.getDimensionId(), data.getSortId(),
                data.getDimensionTitle(), data.getSortTile(), data.getDate(), data.getNetImg(),
                data.getSort(), data.getAuthor()});

        db.close();
    }

    public void deleteData(int id) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.execSQL(
                "delete from dimension where _id = ?",
                new Object[]{id});
        db.close();
    }

    public void deleteAllData() {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.execSQL("delete from dimension");
        db.close();
    }

    public List<RecortSort> findData() {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        List<RecortSort> list = new ArrayList<>();

        Cursor cursor = db.rawQuery("select * from dimension order by _id desc limit ?, ?",
                new String[] {"0", "10000"});
        while (cursor.moveToNext()) {
            RecortSort data = new RecortSort();

            data.set_id(cursor.getInt(cursor.getColumnIndex("_id")));
            data.setDimensionId(cursor.getInt(cursor.getColumnIndex("dimension_id")));
            data.setSortId(cursor.getInt(cursor.getColumnIndex("sortId")));
            data.setDimensionTitle(cursor.getString(cursor.getColumnIndex("dimensionTitle")));
            data.setSortTile(cursor.getString(cursor.getColumnIndex("sortTile")));
            data.setDate(cursor.getString(cursor.getColumnIndex("date")));
            data.setNetImg(cursor.getString(cursor.getColumnIndex("netImg")));
            data.setSort(cursor.getInt(cursor.getColumnIndex("sort")));
            data.setAuthor(cursor.getString(cursor.getColumnIndex("author")));

            list.add(data);
        }
        db.close();
        return list;
    }

}
