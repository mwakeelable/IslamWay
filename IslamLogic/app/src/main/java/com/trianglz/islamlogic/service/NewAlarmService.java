package com.trianglz.islamlogic.service;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.trianglz.islamlogic.R;
import com.trianglz.islamlogic.data.StaticData;
import com.trianglz.islamlogic.database.DatabaseHelper;
import com.trianglz.islamlogic.model.Prayer;
import com.trianglz.islamlogic.reciever.AlarmReceiver;
import com.trianglz.islamlogic.ui.MainActivity;
import com.trianglz.islamlogic.utility.ApplicationUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class NewAlarmService extends IntentService {
    public AlarmManager alarmManager;
    Intent alarmIntent;
    //   PendingIntent pendingIntent;
    private static final int NOTIFICATION_ID = 1;
    private static final String TAG = "Let's Pray";
    private NotificationManager notificationManager;
    private PendingIntent pendingIntent, pendingIntent2;
    private static final String NOTIFICATION_MSG = " waqt countdown started";
    private static final long MINIMUM_DIFF = 60000;

    public NewAlarmService() {
        super("NewAlarmService");
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        // don't notify if they've played in last 24 hr
//        Log.e(TAG, "Alarm Service has started.");
        Context context = this.getApplicationContext();
        long alarmTime = intent.getLongExtra(StaticData.ALARM_TIME, 0);
        long newDay = getSharedPreferences(StaticData.KEY_PREFERENCE, Context.MODE_PRIVATE).getLong(StaticData.NEW_DAY,0);
//        Log.e("ALARM_TIME", alarmTime + "");
//        Log.e("NEW_DAY", newDay + "");
        long currentTime = System.currentTimeMillis();
        long diff;
        if (alarmTime > currentTime) {
            diff = alarmTime - currentTime;
        } else {
            diff = currentTime - alarmTime;
        }

//        Log.e("ALARM TIME", alarmTime + "");
//        Log.e("CURRENT TIME", currentTime + "");
//        Log.e("TIME_DIFF", diff + "");

        if (diff >= 0 && diff < MINIMUM_DIFF) {
            if (alarmTime == newDay) {
                //Update new days prayertimes here
                updatePrayerTimes();
            } else {
                generateNotification(context);
            }
        }
        setNextPrayerAlarm();

    }

    private void generateNotification(Context context) {
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Intent mIntent = new Intent(this, MainActivity.class);
        pendingIntent = PendingIntent.getActivity(context, 0, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Resources res = this.getResources();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        builder.setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_stat_notification)
                .setLargeIcon(BitmapFactory.decodeResource(res, R.mipmap.ic_launcher))
                .setTicker(res.getString(R.string.notification_title))
                .setAutoCancel(true)
                .setContentTitle(res.getString(R.string.notification_title))
                .setContentText(getNotificationText())
                .setSound(soundUri);
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        notificationManager.notify(NOTIFICATION_ID, builder.build());
//        Log.e(TAG, "Notification generated");
    }

    private void setNextPrayerAlarm() {
        SharedPreferences preferences = getSharedPreferences(StaticData.KEY_PREFERENCE, MODE_PRIVATE);
        long currAlarm = preferences.getLong(StaticData.ALARM_TIME, 0);
        long nextAlarm = getNextAlarm(currAlarm);

//        Log.e("CURRENT ALARM TIME", currAlarm + "");
//        Log.e("NEXT ALARM TIME", nextAlarm + "");
        Intent myIntent = new Intent(this, AlarmReceiver.class);
        myIntent.putExtra(StaticData.ALARM_TIME, nextAlarm);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, nextAlarm, pendingIntent);
        } else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, nextAlarm, pendingIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, nextAlarm, pendingIntent);
        }
        saveNextAlarm(nextAlarm);
    }

    private void saveNextAlarm(long alamrTime) {
        SharedPreferences preferences = getSharedPreferences(StaticData.KEY_PREFERENCE, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(StaticData.ALARM_TIME, alamrTime);
        editor.commit();
    }

    private long getNextAlarm(long currAlarm) {
        SharedPreferences preferences = getSharedPreferences(StaticData.KEY_PREFERENCE, MODE_PRIVATE);
        long fazr = preferences.getLong(StaticData.PRAYER_TIME_FAJR, 0);
        long duhr = preferences.getLong(StaticData.PRAYER_TIME_DUHR, 0);
        long asr = preferences.getLong(StaticData.PRAYER_TIME_ASR, 0);
        long maghrib = preferences.getLong(StaticData.PRAYER_TIME_MAGRIB, 0);
        long isha = preferences.getLong(StaticData.PRAYER_TIME_ISHA, 0);
        long newDay = preferences.getLong(StaticData.NEW_DAY, 0);
        if (currAlarm == fazr) {
            return duhr;
        } else if (currAlarm == duhr) {
            return asr;
        } else if (currAlarm == asr) {
            return maghrib;
        } else if (currAlarm == maghrib) {
            return isha;
        } else if (currAlarm == isha) {
            return newDay;
        } else if (currAlarm == newDay) {
            return fazr;
        } else {
            return newDay;
        }
    }

    private String getNotificationText() {
        SharedPreferences preferences = getSharedPreferences(StaticData.KEY_PREFERENCE, MODE_PRIVATE);
        long currAlarm = preferences.getLong(StaticData.ALARM_TIME, 0);
        long fazr = preferences.getLong(StaticData.PRAYER_TIME_FAJR, 0);
        long duhr = preferences.getLong(StaticData.PRAYER_TIME_DUHR, 0);
        long asr = preferences.getLong(StaticData.PRAYER_TIME_ASR, 0);
        long maghrib = preferences.getLong(StaticData.PRAYER_TIME_MAGRIB, 0);
        long isha = preferences.getLong(StaticData.PRAYER_TIME_ISHA, 0);
        if (currAlarm == fazr) {
            return "Fajr" + NOTIFICATION_MSG;
        } else if (currAlarm == duhr) {
            return "Zuhr" + NOTIFICATION_MSG;
        } else if (currAlarm == asr) {
            return "Asr" + NOTIFICATION_MSG;
        } else if (currAlarm == maghrib) {
            return "Maghrib" + NOTIFICATION_MSG;
        } else if (currAlarm == isha) {
            return "Isha" + NOTIFICATION_MSG;
        } else {
            return "Fajr" + NOTIFICATION_MSG;
        }
    }

    public void updatePrayerTimes() {
        long fazrWaqtMs = 0, sunriseMs = 0, dohrWaqtMs = 0, asrWaqtMs = 0, maghribWaqtMs = 0, maghribEnd = 0, ishaWaqtMs = 0;
        DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
        ArrayList<Prayer> prayers = databaseHelper.getPrayer();
        for (int i = 0; i < prayers.size(); i++) {
            if (i == 0) {
                fazrWaqtMs = ApplicationUtils.getPrayerTimeInMs(prayers.get(i).getPrayerTime().toString());
//                Log.e("Fajr In Ms", fazrWaqtMs + "");
            }
            if (i == 1) {
                sunriseMs = ApplicationUtils.getPrayerTimeInMs(prayers.get(i).getPrayerTime().toString());
//                Log.e("Sunruse In Ms", sunriseMs + "");
            }
            if (i == 2) {
                dohrWaqtMs = ApplicationUtils.getPrayerTimeInMs(prayers.get(i).getPrayerTime().toString());
//                Log.e("Dohr In Ms", dohrWaqtMs + "");
            }
            if (i == 3) {
                asrWaqtMs = ApplicationUtils.getPrayerTimeInMs(prayers.get(i).getPrayerTime().toString());
//                Log.e("Asr In Ms", asrWaqtMs + "");
            }
            if (i == 4) {
                maghribWaqtMs = ApplicationUtils.getPrayerTimeInMs(prayers.get(i).getPrayerTime().toString());
//                Log.e("Maghrib In Ms", maghribWaqtMs + "");
                maghribEnd = maghribWaqtMs + 1000 * 60 * 45;
//                Log.e("Maghrib End", maghribEnd + "");
            }
            if (i == 5) {
                ishaWaqtMs = ApplicationUtils.getPrayerTimeInMs(prayers.get(i).getPrayerTime().toString());
//                Log.e("Isha In Ms", ishaWaqtMs + "");
            }
        }
        saveAlarm(fazrWaqtMs, dohrWaqtMs, asrWaqtMs, maghribWaqtMs, ishaWaqtMs);
        setNextPrayerAlarm();

    }

    private void saveAlarm(long alarmTimeFajr, long alarmTimeDuhr, long alarmTimeAsr, long alarmTimeMagrib, long alarmTimeIsha) {
        SharedPreferences preferences = getSharedPreferences(StaticData.KEY_PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(StaticData.PRAYER_TIME_FAJR, alarmTimeFajr);
        editor.putLong(StaticData.PRAYER_TIME_DUHR, alarmTimeDuhr);
        editor.putLong(StaticData.PRAYER_TIME_ASR, alarmTimeAsr);
        editor.putLong(StaticData.PRAYER_TIME_MAGRIB, alarmTimeMagrib);
        editor.putLong(StaticData.PRAYER_TIME_ISHA, alarmTimeIsha);
        editor.putLong(StaticData.NEW_DAY, getNewDay());
        editor.commit();
    }

    private long getNewDay() {
        Calendar calendar = Calendar.getInstance();
        String dateString = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH) + " " + "23:59:59";
        SimpleDateFormat dtFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date postDate = ApplicationUtils.formatDate(dateString, dtFormat);
        calendar.setTime(postDate);
        long newDay = calendar.getTimeInMillis() + StaticData.TEN_MINUTE;
        return newDay;
    }

}