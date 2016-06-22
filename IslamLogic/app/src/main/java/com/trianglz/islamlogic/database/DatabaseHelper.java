package com.trianglz.islamlogic.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.trianglz.islamlogic.model.Hadith;
import com.trianglz.islamlogic.model.Prayer;
import com.trianglz.islamlogic.model.RamadanSchedule;

import java.util.ArrayList;


public class DatabaseHelper extends SQLiteOpenHelper {


    private static final int DATABASE_VERSION = 1;

    //  Database name
    static String DATABASE_NAME = "prayerDB";
    SQLiteDatabase db;

    //  Table For Prayer time
    private static final String PRAYER_TABLE = "prayer";
    private static final String RAMADAN_SCHEDULE_TABLE = "ramadan_schedule";
    private static final String HADITH_TABLE = "hadith_table";

    //  fields for Prayer time table
    private static final String ID = "id";
    private static final String COLUMN_PRAYER_NAMES = "prayer_time";
    private static final String COLUMN_PRAYER_TIMES = "prayer_name";

    //  fields for Ramadan Schedule table
    private static final String COLUMN_RAMADAN_DATE = "ramadan_date";
    private static final String COLUMN_SUHUR_TIME = "suhur_time";
    private static final String COLUMN_IFTAR_TIME = "iftar_time";

    //  fields for hadith table
    private static final String _ID = "_id";
    private static final String COLUMN_HADITH_DETAILS = "hadith_details";
    private static final String COLUMN_HADITH_TITLES = "hadith_titles";

    //  Table create
    private static final String
            PRAYER_TABLE_CREATE = "CREATE TABLE " + PRAYER_TABLE + "(" + ID
            + " INTEGER PRIMARY KEY autoincrement, " + COLUMN_PRAYER_NAMES
            + " TEXT, " + COLUMN_PRAYER_TIMES + " TEXT)";
    private static final String
            RAMADAN_TABLE_CREATE = "CREATE TABLE " + RAMADAN_SCHEDULE_TABLE + "(" + ID
            + " INTEGER PRIMARY KEY autoincrement, " + COLUMN_RAMADAN_DATE
            + " TEXT, " + COLUMN_SUHUR_TIME + " TEXT, " + COLUMN_IFTAR_TIME + " TEXT)";
    private static final String
            HADITH_TABLE_CREATE = "CREATE TABLE " + HADITH_TABLE + "(" + _ID
            + " INTEGER PRIMARY KEY autoincrement, " + COLUMN_HADITH_DETAILS
            + " TEXT, " + COLUMN_HADITH_TITLES + " TEXT)";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(PRAYER_TABLE_CREATE);
        db.execSQL(RAMADAN_TABLE_CREATE);
        db.execSQL(HADITH_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query1 = "DROP TABLE IF EXIST " + PRAYER_TABLE;
        String query2 = "DROP TABLE IF EXIST " + RAMADAN_SCHEDULE_TABLE;
        String query3 = "DROP TABLE IF EXIST " + HADITH_TABLE;
        db.execSQL(query1);
        db.execSQL(query2);
        db.execSQL(query3);
        this.onCreate(db);

    }

    public void insertPrayer(ArrayList<Prayer> prayers) {
        deletePreviousData();
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        long count = 0;
        for (Prayer prayer :
                prayers) {
            values.put(COLUMN_PRAYER_NAMES, prayer.getPrayerName());
            values.put(COLUMN_PRAYER_TIMES, prayer.getPrayerTime());
            db.insert(PRAYER_TABLE, null, values);
            count++;
        }
//        Log.e("INFO", "One rowinserter " + count);
        db.close();
    }


    public ArrayList<Prayer> getPrayer() {
        ArrayList<Prayer> contactList = new ArrayList<Prayer>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + PRAYER_TABLE;

        db = getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Prayer prayer = new Prayer();
                prayer.setPrayerName(cursor.getString(1));
                prayer.setPrayerTime(cursor.getString(2));
                // Adding contact to list
                contactList.add(prayer);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }

    public void deletePreviousData() {
        db = getWritableDatabase();
        db.execSQL("delete from " + PRAYER_TABLE);
    }

    public void insertSchedule(ArrayList<RamadanSchedule> schedules) {
//        Log.e("Schedules Size", schedules.size() + " ");
        db = this.getWritableDatabase();
        long count = 0;
        ContentValues values = new ContentValues();
        for (RamadanSchedule schedule :
                schedules) {
            values.put(COLUMN_RAMADAN_DATE, schedule.getDate());
            values.put(COLUMN_SUHUR_TIME, schedule.getSuhur_time());
            values.put(COLUMN_IFTAR_TIME, schedule.getIftar_time());
            db.insert(RAMADAN_SCHEDULE_TABLE, null, values);
            count++;
        }
//        long count = db.insert(RAMADAN_SCHEDULE_TABLE, null, values);

//        Log.e("INFO", "One rowinserter " + count);
        db.close();
    }

    public ArrayList<RamadanSchedule> getRamadanSchedules() {
        ArrayList<RamadanSchedule> ramadanSchedules = new ArrayList<RamadanSchedule>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + RAMADAN_SCHEDULE_TABLE;

        db = getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                RamadanSchedule ramadanSchedule = new RamadanSchedule();
                ramadanSchedule.setDate(cursor.getString(1));
                ramadanSchedule.setSuhur_time(cursor.getString(2));
                ramadanSchedule.setIftar_time(cursor.getString(3));
                // Adding contact to list
                ramadanSchedules.add(ramadanSchedule);
            } while (cursor.moveToNext());
        }

        // return contact list
        return ramadanSchedules;
    }

    public void insertHadith(ArrayList<Hadith> hadiths) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        long count = 0;
        for (Hadith hadith :
                hadiths) {
            values.put(COLUMN_HADITH_DETAILS, hadith.getHadithDetails());
            values.put(COLUMN_HADITH_TITLES, hadith.getHadithTitles());
            db.insert(HADITH_TABLE, null, values);
            count++;
        }
//        Log.e("INFO", "One rowinserted for hadith " + count);
        db.close();
    }


    public ArrayList<Hadith> getHadith() {
        ArrayList<Hadith> hadithArrayList = new ArrayList<Hadith>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + HADITH_TABLE;

        db = getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Hadith hadith = new Hadith();
                hadith.setHadithDetails(cursor.getString(cursor.getColumnIndex(COLUMN_HADITH_DETAILS)));
                hadith.setHadithTitles(cursor.getString(cursor.getColumnIndex(COLUMN_HADITH_TITLES)));
                hadithArrayList.add(hadith);
            } while (cursor.moveToNext());
        }

        // return hadith arry list
        return hadithArrayList;
    }

}
