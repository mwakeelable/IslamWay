package com.trianglz.islamlogic.ui;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.trianglz.islamlogic.R;
import com.trianglz.islamlogic.database.DatabaseHelper;
import com.trianglz.islamlogic.geolocation.GPSTracker;
import com.trianglz.islamlogic.model.Prayer;
import com.trianglz.islamlogic.utility.ApplicationUtils;
import com.trianglz.islamlogic.utility.PrayTime;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class SplashActivity extends AppCompatActivity {
    private CoordinatorLayout coordinatorLayout;
    double latitude;
    double longitude;
    int day_state = 0;
    DatabaseHelper helper = new DatabaseHelper(this);
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        day_state = ApplicationUtils.getDayState();
        setThemeAccordingToDayState();
        setContentView(R.layout.activity_splash);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .coordinatorLayout);
        context = this;
        GPSTracker gpsTracker = new GPSTracker(context, this);
        latitude = gpsTracker.getLatitude();
        longitude = gpsTracker.getLongitude();
        if (latitude != 0.0 && longitude != 0.0) {      //Need to handle if location is sent null
            setPrayerTImes(latitude, longitude);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ApplicationUtils.GPS_REQUEST_CODE) {
            GPSTracker gpsTracker = new GPSTracker(context, this);
            latitude = gpsTracker.getLatitude();
            longitude = gpsTracker.getLongitude();
            if (latitude != 0.0 && longitude != 0.0) {
                setPrayerTImes(latitude, longitude);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case ApplicationUtils.PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    /**
                     * We are good, turn on monitoring
                     */
                    if (ApplicationUtils.checkPermission(this)) {
                        GPSTracker gpsTracker = new GPSTracker(context, this);
                        longitude = gpsTracker.getLongitude();
                        latitude = gpsTracker.getLatitude();
                        if (latitude != 0.0 && longitude != 0.0) {
                            setPrayerTImes(latitude, longitude);
                        }
                    } else {
                        ApplicationUtils.requestPermission(this);
                    }
                } else {
                    /**
                     * No permissions, block out all activities that require a location to function
                     */
                    Toast.makeText(this, "Sorry. Cannot continue without location", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
        }
    }

//    GPSTracker.SettingNotEnabledCallback settingNotEnabledCallback = new GPSTracker.SettingNotEnabledCallback() {
//        @Override
//        public void settingsNotEnabled() {
//            SharedPreferences preferences  = getSharedPreferences(StaticData.KEY_PREFERENCE, MODE_PRIVATE);
//            double lat = Double.parseDouble(preferences.getString(StaticData.PREV_LATTITUDE,"0.0"));
//            double lon = Double.parseDouble(preferences.getString(StaticData.PREV_LONGITUDE,"0.0"));
//            if(lat!=0.0&&lon!=0.0) {
//                setPrayerTImes(lat,lon);
//            }
//        }
//    };

    private void setThemeAccordingToDayState(){
        switch (day_state) {
            case ApplicationUtils.MORNING:
                setTheme(R.style.MorningTheme);
                break;
            case ApplicationUtils.NOON:
                setTheme(R.style.AfterNoonTheme);
                break;
            case ApplicationUtils.EVENING:
                setTheme(R.style.EveningTheme);
                break;
            case ApplicationUtils.NIGHT:
                setTheme(R.style.NightTheme);
                break;
        }
    }

    private void setPrayerTImes(double latitude, double longitude) {
        ApplicationUtils.saveLatLong(latitude, longitude, context);
//        Log.e("Latitude", latitude + "");
//        Log.e("Longitude", longitude + "");
        double timezone = (Calendar.getInstance().getTimeZone()
                .getOffset(Calendar.getInstance().getTimeInMillis()))
                / (1000 * 60 * 60);
        PrayTime prayers = new PrayTime();

        prayers.setTimeFormat(prayers.Time12);
        prayers.setCalcMethod(prayers.Makkah);
        prayers.setAsrJuristic(prayers.Shafii);
        prayers.setAdjustHighLats(prayers.AngleBased);
        int[] offsets = {0, 0, 0, 0, 0, 0, 0}; // {Fajr,Sunrise,Dhuhr,Asr,Sunset,Maghrib,Isha}
        prayers.tune(offsets);

        Date now = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);

        ArrayList prayerTimes = prayers.getPrayerTimes(cal, latitude,
                longitude, timezone);
        ArrayList prayerNames = prayers.getTimeNames();

        ArrayList<Prayer> prayerList = new ArrayList<>();
        for (int i = 0; i < prayerNames.size(); i++) {

            //Add Prayer time in Database
            if (i != 4) {
                Log.e("Prayer Time", prayerNames.get(i).toString() + " " + prayerTimes.get(i).toString());
                Prayer prayer = new Prayer();
                prayer.setPrayerName((String) prayerNames.get(i));
                prayer.setPrayerTime((String) prayerTimes.get(i));
                prayerList.add(prayer);
            } else {
                continue;
            }
        }
        helper.insertPrayer(prayerList);

        if (!ApplicationUtils.isHadithAvailable(context)) {
            ApplicationUtils.saveHadith(helper, context);
        }

        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
        finish();
    }

}
