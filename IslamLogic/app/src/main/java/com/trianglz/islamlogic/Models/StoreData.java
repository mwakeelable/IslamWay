package com.trianglz.islamlogic.Models;

import android.content.Context;
import android.content.SharedPreferences;

public class StoreData {
    String DATABASE_NAME = "sand.ubicall";
    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public StoreData(Context ctx) {
        super();
        this.context = ctx;
        sharedPreferences = context.getSharedPreferences(DATABASE_NAME,
                Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

    }

    public String getRamadan() {
        return sharedPreferences.getString("ramadan", "");
    }

    public int getTheme() {
        return sharedPreferences.getInt("theme", 0);
    }

    public void setLongitude(float longitude) {
        editor.putFloat("longitude", longitude);
        editor.apply();
    }

    public float getLongitude() {
        return sharedPreferences.getFloat("longitude", 0);
    }

    public void setLatitude(float latitude) {
        editor.putFloat("latitude", latitude);
        editor.apply();
    }

    public float getLatitude() {
        return sharedPreferences.getFloat("latitude", 0);
    }

    public void setAltitude(float altitude) {
        editor.putFloat("altitude", altitude);
        editor.apply();
    }

    public float getAltitude() {
        return sharedPreferences.getFloat("altitude", 0);
    }
}