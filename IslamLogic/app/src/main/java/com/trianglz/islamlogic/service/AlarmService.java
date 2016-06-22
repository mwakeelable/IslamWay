package com.trianglz.islamlogic.service;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.trianglz.islamlogic.R;
import com.trianglz.islamlogic.data.StaticData;
import com.trianglz.islamlogic.reciever.AlarmReceiver;

import java.util.Calendar;


public class AlarmService extends Service {
    long alarmTime;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("Service", "Service Created");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("Service", "Service Started");
        SharedPreferences preferences = getApplicationContext().getSharedPreferences(StaticData.KEY_PREFERENCE,getApplicationContext().MODE_PRIVATE);
        SharedPreferences.Editor editor =preferences.edit();
        boolean isAlarmTime =  preferences.getBoolean(StaticData.IS_ALARM_TIME,false);
        if(!isAlarmTime) {
            if(isAlarmTime()) {
                notifiy();
                Log.e("SERVICE (IF)","Inside isAlarmTime()");
                setNextAlarm();
                editor.putBoolean(StaticData.IS_ALARM_TIME,true);
                editor.commit();
            }

        }else{
            Log.e("SERVICE (ELSE)","NEXT ALARM TIME");
            notify();
            setNextAlarm();
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("Service","Service Destroyed");
    }

    private boolean isAlarmTime(){
        Calendar calendar = Calendar.getInstance();
        SharedPreferences preferences = getSharedPreferences(StaticData.KEY_PREFERENCE,MODE_PRIVATE);
        alarmTime = preferences.getLong(StaticData.NEXT_PRAYER_TIME,0);
        long currTime=calendar.getTimeInMillis();
        Log.e("ALARM TIME",alarmTime +"");
        Log.e("CURRENT TIME",currTime +"");
        if(alarmTime<currTime){
            long difference=currTime-alarmTime;
            if(difference < 10000){
                return true;
            }else{
                return false;
            }
        }else {
            return false;
        }
    }

    public void notifiy() {
        Notification.Builder builder;
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder = new Notification.Builder(getApplicationContext())
                .setContentTitle("NOTIFICATION")
                .setContentText("It's time for prayer")
                .setSmallIcon(R.drawable.ic_arrow)
                .setSound(soundUri)
                .setLights(0xFF0000FF, 300, 1000);

        NotificationManager notificationManager = (NotificationManager) getSystemService(getApplicationContext().NOTIFICATION_SERVICE);
        long[] v = {500, 1000};
        builder.setVibrate(v);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            notificationManager.notify(1, builder.build());
        }

    }

    public void setNextAlarm(){
        Calendar calendar = Calendar.getInstance();
        long currTime=calendar.getTimeInMillis();
        SharedPreferences preferences = getSharedPreferences(StaticData.KEY_PREFERENCE,MODE_PRIVATE);
        long alarmTime_fajr = preferences.getLong(StaticData.PRAYER_TIME_FAJR,0);
        Log.e("alarmTime_fajr",alarmTime_fajr+"");
        long alarmTime_dohr = preferences.getLong(StaticData.PRAYER_TIME_DUHR,0);
        Log.e("alarmTime_dohr",alarmTime_dohr+"");
        long alarmTime_asr = preferences.getLong(StaticData.PRAYER_TIME_ASR,0);
        Log.e("alarmTime_asr",alarmTime_asr+"");
        long alarmTime_maghrib = preferences.getLong(StaticData.PRAYER_TIME_MAGRIB,0);
        long alarmTime_isha = preferences.getLong(StaticData.PRAYER_TIME_ISHA,0);
//            alarmManager.set(AlarmManager.RTC, time1, pendingIntent);
        Intent myIntent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        if(alarmTime_fajr == currTime){
            alarmManager.set(AlarmManager.RTC, alarmTime_fajr, pendingIntent);
        }else if(alarmTime_dohr == currTime){
            alarmManager.set(AlarmManager.RTC, alarmTime_dohr, pendingIntent);
        }else if(alarmTime_asr == currTime){
            alarmManager.set(AlarmManager.RTC, alarmTime_asr, pendingIntent);
        }else if(alarmTime_maghrib == currTime){
            alarmManager.set(AlarmManager.RTC, alarmTime_maghrib, pendingIntent);
        }else if(alarmTime_isha ==currTime){
            alarmManager.set(AlarmManager.RTC, alarmTime_isha, pendingIntent);
        }

    }
}
