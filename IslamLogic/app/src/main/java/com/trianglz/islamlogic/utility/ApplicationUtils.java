package com.trianglz.islamlogic.utility;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.trianglz.islamlogic.data.StaticData;
import com.trianglz.islamlogic.database.DatabaseHelper;
import com.trianglz.islamlogic.model.Hadith;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class ApplicationUtils {

    //lets.pray.muslims

    public static final int PERMISSION_REQUEST_CODE = 1;
    public static final int MORNING = 5;
    public static final int NOON = 14;
    public static final int EVENING = 17;
    public static final int NIGHT = 19;
    public static final int GPS_REQUEST_CODE = 2;


    /**
     * See if we have permissionf or locations
     *
     * @return boolean, true for good permissions, false means no permission
     */

    public static boolean checkPermission(Activity activity) {
        int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Request permissions from the user
     */
    public static void requestPermission(Activity activity) {

        /**
         * Previous denials will warrant a rationale for the user to help convince them.

        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_FINE_LOCATION)) {
            Toast.makeText(activity, "This app relies on location data for it's main functionality. Please enable GPS data to access all features.", Toast.LENGTH_LONG).show();
        } else {*/
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
//        }
    }

    public static int getDayState() {

        Calendar calendar = Calendar.getInstance();
        int time = calendar.get(Calendar.HOUR_OF_DAY);
        if (time >= MORNING && time < NOON) {
            return MORNING;
        } else if (time >= NOON && time < EVENING) {
            return NOON;
        } else if (time >= EVENING && time < NIGHT) {
            return EVENING;
        } else {
            return NIGHT;
        }
    }

    public static Date formatDate(String inputDate, SimpleDateFormat sdFormat) {
        Date dt = null;
        try {
            dt = sdFormat.parse(inputDate);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return dt;
    }

    public static long getPrayerTimeInMs(String time) {
  //      time = modifyTime(time);
        Log.e("ModifyTime",time+"");
        Calendar calendar = Calendar.getInstance();
        String dateString = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH) + " " + time;
        SimpleDateFormat dtFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
        Date postDate = formatDate(dateString, dtFormat);
        calendar.setTime(postDate);
        return calendar.getTimeInMillis();
    }

    private static String modifyTime(String time) {
        int hour = Integer.parseInt(time.substring(0, 2));
        String ampm = time.substring(6, 8);
        if (ampm.equalsIgnoreCase("am")) {
            return time;
        } else {
            if(hour == 12){
                return time;
            }else{
                hour = hour + 12;
                return hour + ":" + time.substring(3);
            }
        }
    }


    public static Typeface setTimeTypeface(Context context) {
        Typeface timeTypeface =
                Typeface.createFromAsset(context.getAssets(), "fonts/time.ttf");
        return timeTypeface;
    }

    public static Typeface setTimeDateTypeface(Context context) {
        Typeface timeTypeface =
                Typeface.createFromAsset(context.getAssets(), "fonts/time_date.ttf");
        return timeTypeface;
    }

    public static Typeface setIfterSehriTypeface(Context context) {
        Typeface timeTypeface =
                Typeface.createFromAsset(context.getAssets(), "fonts/ifterAndSehri.ttf");
        return timeTypeface;
    }

    public static Typeface setHadithDetailTypeface(Context context) {
        Typeface timeTypeface =
                Typeface.createFromAsset(context.getAssets(), "fonts/hadith_details.ttf");
        return timeTypeface;
    }

    public static boolean checkPreviousLocation(Context context){
        SharedPreferences preferences = context.getSharedPreferences(StaticData.KEY_PREFERENCE,Context.MODE_PRIVATE);
        boolean isPreviousLocation = preferences.getBoolean(StaticData.IS_PREV_LOC, false);
        return isPreviousLocation;
    }

    public static String getTimeString(long time){
        Date date = new Date(time);
        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm a");
        String timeStamp = formatter.format(date);
//        Log.e("TIMESTAMP",timeStamp);
        return timeStamp;
    }

    public static boolean isHadithAvailable(Context context){
        SharedPreferences preferences = context.getSharedPreferences(StaticData.KEY_PREFERENCE,Context.MODE_PRIVATE);
        boolean isHadithAvailable = preferences.getBoolean(StaticData.IS_HADITH_EXISTS,false);
        return isHadithAvailable;
    }

    public static void saveLatLong(double lat, double lon, Context context){
        SharedPreferences preferences = context.getSharedPreferences(StaticData.KEY_PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(StaticData.IS_PREV_LOC, true);
        editor.putString(StaticData.PREV_LATTITUDE, lat+"");
        editor.putString(StaticData.PREV_LONGITUDE, lon+"");
        editor.commit();
    }

    public static double getDataFromPreference(Context context, String key){
        SharedPreferences preferences = context.getSharedPreferences(StaticData.KEY_PREFERENCE, Context.MODE_PRIVATE);
        String data = preferences.getString(key,"0.0");
        double value = Double.parseDouble(data);
        return value;
    }

    public static void saveHadith(DatabaseHelper helper, Context context){
        ArrayList<Hadith> hadithArrayList = new ArrayList<>();
        for(int i=0;i<StaticData.HADITH_DETAILS.length;i++){
            Hadith hadith = new Hadith();
            hadith.setHadithDetails(StaticData.HADITH_DETAILS[i].toString());
            hadith.setHadithTitles(StaticData.HADITH_TITLES[i].toString());
            hadithArrayList.add(hadith);
        }
        helper.insertHadith(hadithArrayList);
        hadithSaved(context);
    }

    private static void hadithSaved(Context context){
        SharedPreferences preferences = context.getSharedPreferences(StaticData.KEY_PREFERENCE,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(StaticData.IS_HADITH_EXISTS, true);
        editor.commit();
    }

    public static Hadith getHadithForMoment(Context context){
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        ArrayList<Hadith> hadithArrayList = databaseHelper.getHadith();
        Random random = new Random();
        return hadithArrayList.get(random.nextInt(hadithArrayList.size()-1));
    }

    public static void openHadithDialog(final Activity activity, Hadith hadith) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
        alertDialog.setTitle("Todays Hadith");
        alertDialog.setMessage(hadith.getHadithDetails()+"  -  "+hadith.getHadithTitles());
        alertDialog.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

}
