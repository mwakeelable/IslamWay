package com.trianglz.islamlogic.utility;

import android.content.Context;
import android.content.SharedPreferences;

import com.trianglz.islamlogic.data.StaticData;
import com.trianglz.islamlogic.database.DatabaseHelper;
import com.trianglz.islamlogic.model.RamadanSchedule;

import java.util.ArrayList;

public class RamadanScheduleMaker {

    public static final long DAY_IN_MS = 24 * 60 * 60 * 1000;
    private static final long HOUR_IN_MS = 60 * 60 * 1000;
    private static final long MIN_IN_MS = 60 * 1000;
    private static final String TIME_SEPERATOR = ":";
    private static final String DATE_SEPERATOR = "-";

    int suhurHour = 0;
    int suhurMin = 0;
    int iftarHour = 0;
    int iftarMin = 0;
    int day = 0;
    int month = 0;
    int year = 0;

    long firstFazrTime;
    long fazrMagribDiff;
    long maghribTime;

    Context context;

    public RamadanScheduleMaker(long firstFazrTime, Context context) {
        this.firstFazrTime = firstFazrTime;
        this.context = context;
        generateSchedule();
    }

    private void generateSchedule() {
        if(!isScheduleAvailable()) {
            if (checkSchedule()) {
                startScheduleGenerator(15 * HOUR_IN_MS);
            } else {
                startScheduleGenerator(fazrMagribDiff);
            }
        }
    }

    private boolean isScheduleAvailable(){
        SharedPreferences preferences = context.getSharedPreferences(StaticData.KEY_PREFERENCE,Context.MODE_PRIVATE);
        boolean isScheduleAvailable = preferences.getBoolean(StaticData.SCHEDULE_AVAILABLE, false);
        return isScheduleAvailable;
    }

    private boolean checkSchedule() {
        SharedPreferences preferences = context.getSharedPreferences(StaticData.KEY_PREFERENCE, Context.MODE_PRIVATE);
        this.maghribTime = preferences.getLong(StaticData.PRAYER_TIME_MAGRIB, 0);
        if (maghribTime > firstFazrTime) {
            fazrMagribDiff = maghribTime - firstFazrTime;
        } else {
            long fazrTime = firstFazrTime - DAY_IN_MS;
            fazrMagribDiff = maghribTime - fazrTime;
        }
        long eighteenHour = 17 * HOUR_IN_MS;
        if (fazrMagribDiff > eighteenHour) {
            return true;
        } else {
            return true;
        }
    }

    private void startScheduleGenerator(long fastingTime) {
        long suhurTime = firstFazrTime - (3 * MIN_IN_MS);
        long iftarTime = maghribTime;
        String suhurTimeStamp = ApplicationUtils.getTimeString(suhurTime);
        String iftarTimeStamp = ApplicationUtils.getTimeString(iftarTime);
        String startingDate = getRamadanStartingDate();
        suhurHour = Integer.parseInt(suhurTimeStamp.substring(0, 2));
        suhurMin = Integer.parseInt(suhurTimeStamp.substring(3, 5));
        iftarHour = Integer.parseInt(iftarTimeStamp.substring(0, 2));
        iftarMin = Integer.parseInt(iftarTimeStamp.substring(3, 5));
        day = Integer.parseInt(startingDate.substring(0, 2));
        month = Integer.parseInt(startingDate.substring(3, 5));
        year = Integer.parseInt(startingDate.substring(6, 10));
        startGenerator();
    }

    private String getRamadanStartingDate() {
        SharedPreferences preferences = context.getSharedPreferences(StaticData.KEY_PREFERENCE, Context.MODE_PRIVATE);
        String date = preferences.getString(StaticData.RAMADAN_DATE, "07-06-2016");
        return date;
    }

    private void startGenerator() {
//        Log.e("RAMADAN_SCHEDULE", "Suhur: " + suhurHour + ":" + suhurMin + " Iftar: " + iftarHour + TIME_SEPERATOR + iftarMin +
//                " Date: " + day + DATE_SEPERATOR + month + DATE_SEPERATOR + year);
        ArrayList<RamadanSchedule> ramadanSchedules = new ArrayList<>();
        for (int i=1; i<31; i++){
            RamadanSchedule ramadanSchedule = new RamadanSchedule();
            ramadanSchedule.setSuhur_time(generateSuhurTime(i));
            ramadanSchedule.setIftar_time(generateIftarTime(i));
            ramadanSchedule.setDate(generateDate());
            ramadanSchedules.add(ramadanSchedule);
        }
        saveSchedule(ramadanSchedules);
    }

    private String generateSuhurTime(int ramadanDay){
        if(ramadanDay==8||ramadanDay==16||ramadanDay== 20||ramadanDay== 23||ramadanDay== 26||ramadanDay== 28||ramadanDay== 30){
            suhurMin++;
            if(suhurMin==60){
                suhurHour++;
                suhurMin=0;
            }
        }
        String timeStamp = String.format("%02d:%02d AM",suhurHour, suhurMin);
        return timeStamp;
    }

    private String generateIftarTime(int ramadanDay){
        if(ramadanDay==8||ramadanDay==16||ramadanDay== 20||ramadanDay== 23||ramadanDay== 26||ramadanDay== 28||ramadanDay== 30){
            iftarMin++;
            if(iftarMin==60){
                iftarHour++;
                iftarMin = 0;
            }
        }
        String timeStamp = String.format("%02d:%02d PM",iftarHour, iftarMin);
        return timeStamp;
    }

    private String generateDate() {
        if(day==30){
            day = 1;
            month++;
        }else{
            day++;
        }
        String timeStamp = String.format("%02d-%02d-%04d",day,month,year);
        return timeStamp;
    }

    private void saveSchedule(ArrayList<RamadanSchedule> ramadanSchedules){
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        databaseHelper.insertSchedule(ramadanSchedules);
        SharedPreferences preferences = context.getSharedPreferences(StaticData.KEY_PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(StaticData.SCHEDULE_AVAILABLE,true);
        editor.commit();
    }

}
