package com.trianglz.islamlogic.Models;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.trianglz.islamlogic.ui.CategoryActivity;

import java.util.ArrayList;
import java.util.List;

public class DatabaseAccess {
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;
    private static DatabaseAccess instance;
    CategoryActivity cat_activity;
    public DatabaseAccess(CategoryActivity activity) {
        this.openHelper = new DatabaseOpenHelper(activity);
    }

    public static DatabaseAccess getInstance(CategoryActivity activity) {
        if (instance == null) {
            instance = new DatabaseAccess(activity);
        }
        return instance;
    }

    public void open() {
        this.database = openHelper.getWritableDatabase();
    }

    public void close() {
        if (database != null) {
            this.database.close();
        }
    }

    public List<String> getCategories() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT  name from category", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(0));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getAzkar() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("select description from zekr  where category_id = " + cat_activity.categoryPosition, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(0));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getHint() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("select hint from zekr  where category_id = " + cat_activity.categoryPosition, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(0));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }
}
