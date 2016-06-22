package com.trianglz.islamlogic.geolocation;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.widget.Toast;

import com.trianglz.islamlogic.data.StaticData;
import com.trianglz.islamlogic.utility.ApplicationUtils;


public class GPSTracker extends Service implements LocationListener {

    private final Context mContext;
    private final Activity activity;

    // flag for GPS status
    boolean isGPSEnabled = false;

    // flag for network status
    boolean isNetworkEnabled = false;

    // flag for GPS status
    boolean canGetLocation = false;

    Location location = null; // location
    double latitude; // latitude
    double longitude; // longitude

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute

    // Declaring a Location Manager
    protected LocationManager locationManager;

    public GPSTracker(Context context, Activity activity) {
        this.mContext = context;
        this.activity = activity;
        getLocation(activity);
    }

    public Location getLocation(Activity activity) {
        try {
            locationManager = (LocationManager) mContext
                    .getSystemService(LOCATION_SERVICE);

            // getting GPS status
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
                if (!ApplicationUtils.checkPreviousLocation(activity)) {
                    showSettingsAlert();
                } else {
                    SharedPreferences preferences = activity.getSharedPreferences(StaticData.KEY_PREFERENCE, MODE_PRIVATE);
                    double lat = Double.parseDouble(preferences.getString(StaticData.PREV_LATTITUDE, "0.0"));
                    double lon = Double.parseDouble(preferences.getString(StaticData.PREV_LONGITUDE, "0.0"));
                    location = new Location(LOCATION_SERVICE);
                    if (lat != 0.0 && lon != 0.0) {
                        location.setLongitude(lon);
                        location.setLatitude(lat);
                    }

                }
//                SharedPreferences preferences  = activity.getSharedPreferences(StaticData.KEY_PREFERENCE, MODE_PRIVATE);
//                double lat = Double.parseDouble(preferences.getString(StaticData.PREV_LATTITUDE,"0.0"));
//                double lon = Double.parseDouble(preferences.getString(StaticData.PREV_LONGITUDE,"0.0"));
//                if(lat!=0.0&&lon!=0.0) {
//                    location.setLatitude(lat);
//                    location.setLongitude(lon);
//                }
            } else {
                if (ApplicationUtils.checkPermission(activity)) {
                    this.canGetLocation = true;
                    if (isNetworkEnabled) {
                        locationManager.requestLocationUpdates(
                                LocationManager.NETWORK_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
//                        Log.e("Network", "Network Enabled");
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                    // if GPS Enabled get lat/long using GPS Services
                    if (isGPSEnabled) {
                        if (location == null) {
                            locationManager.requestLocationUpdates(
                                    LocationManager.GPS_PROVIDER,
                                    MIN_TIME_BW_UPDATES,
                                    MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
//                            Log.e("GPS", "GPS Enabled");
                            if (locationManager != null) {
                                location = locationManager
                                        .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                if (location != null) {
                                    latitude = location.getLatitude();
                                    longitude = location.getLongitude();
                                }
                            }
                        }
                    }
                } else {
                    Toast.makeText(mContext, "Need location to get prayer time. Cannot continue without location", Toast.LENGTH_SHORT);
                    ApplicationUtils.requestPermission(activity);
                }
            }

        } catch (
                final Exception e
                )

        {
            e.printStackTrace();
        }

        return location;
    }


    /**
     * Stop using GPS listener Calling this function will stop using GPS in your
     * app
     */
    public void stopUsingGPS() {
        if (locationManager != null) {
            if (ApplicationUtils.checkPermission(activity)) {
                locationManager.removeUpdates(GPSTracker.this);
            }
        }
    }

    /**
     * Function to get latitude
     */
    public double getLatitude() {
        if (location != null) {
            latitude = location.getLatitude();
        }

        // return latitude
        return latitude;
    }

    /**
     * Function to get longitude
     */
    public double getLongitude() {
        if (location != null) {
            longitude = location.getLongitude();
        }

        // return longitude
        return longitude;
    }

    /**
     * Function to check GPS/wifi enabled
     *
     * @return boolean
     */
    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    /**
     * Function to show settings alert dialog On pressing Settings button will
     * lauch Settings Options
     */
//    public void showSettingsAlert() {
//        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(
//                mContext);
//
//        // Setting Dialog Title
//        alertDialog.setTitle("GPS settings");
//
//        // Setting Dialog Message
//        alertDialog
//                .setMessage("GPS is not enabled. Do you want to go to settings menu?");
//
//        // On pressing Settings button
//        alertDialog.setPositiveButton("Settings",
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        final Intent intent = new Intent(
//                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                        mContext.startActivity(intent);
//                    }
//                });
//
//        // on pressing cancel button
//        alertDialog.setNegativeButton("Cancel",
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.cancel();
//                    }
//                });
//
//        // Showing Alert Message
//        alertDialog.show();
//    }
    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    private void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
        alertDialog.setTitle("Location's not available!!");
        alertDialog.setMessage("This application needs location to calculate Salah times. To continue with Let's Pray press \"Settings\" and enable location.");
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                activity.startActivityForResult(intent, ApplicationUtils.GPS_REQUEST_CODE);
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                activity.finish();
                Toast.makeText(activity, "Need location to calculate salah time", Toast.LENGTH_LONG).show();
            }
        });
        alertDialog.show();
    }

}