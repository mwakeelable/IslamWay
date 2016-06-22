package com.trianglz.islamlogic.ui;

import android.content.Context;
import android.hardware.GeomagneticField;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.trianglz.islamlogic.Models.StoreData;
import com.trianglz.islamlogic.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class QiblaActivity extends AppCompatActivity implements SensorEventListener {
    private ImageView compass, image;
    SensorManager sensorManager;
    private static final String PATTERN = "#.###";
    private static DecimalFormat sDecimalFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qibla);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        try {
            sDecimalFormat = new DecimalFormat(PATTERN);
        } catch (AssertionError ae) {
            NumberFormat format = NumberFormat.getInstance(Locale.US);
            if (format instanceof DecimalFormat) {
                sDecimalFormat = (DecimalFormat) format;
                sDecimalFormat.applyPattern(PATTERN);
            }
        }
        sensorManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);

        compass = (ImageView) findViewById(R.id.imgCompass);
        image = (ImageView) findViewById(R.id.image);
        setColors(compass, image);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    private void setColors(ImageView img, ImageView img2) {
        switch (new StoreData(this).getTheme()) {
            case 1:
                img.setColorFilter(this.getResources().getColor(R.color.color_line_one));
                img2.setColorFilter(this.getResources().getColor(R.color.color_line_one));
                break;
            case 2:
                img.setColorFilter(this.getResources().getColor(R.color.color_line_two));
                img2.setColorFilter(this.getResources().getColor(R.color.color_line_two));

                break;
            case 3:
                img.setColorFilter(this.getResources().getColor( R.color.color_line_three));
                img2.setColorFilter(this.getResources().getColor( R.color.color_line_three));

                break;
            case 4:
                img.setColorFilter(this.getResources().getColor( R.color.color_line_four));
                img2.setColorFilter(this.getResources().getColor( R.color.color_line_four));
                break;
            case 5:
                img.setColorFilter(this.getResources().getColor( R.color.color_line_five));
                img2.setColorFilter(this.getResources().getColor( R.color.color_line_five));
                break;
            case 6:
                img.setColorFilter(this.getResources().getColor( R.color.color_line_six));
                img2.setColorFilter(this.getResources().getColor( R.color.color_line_six));
                break;
            case 7:
                img.setColorFilter(this.getResources().getColor( R.color.color_line_seven));
                img2.setColorFilter(this.getResources().getColor( R.color.color_line_seven));
                break;
            case 8:
                img.setColorFilter(this.getResources().getColor( R.color.color_line_eight));
                img2.setColorFilter(this.getResources().getColor( R.color.color_line_eight));
                break;
            case 9:
                img.setColorFilter(this.getResources().getColor( R.color.color_line_nine));
                img2.setColorFilter(this.getResources().getColor( R.color.color_line_nine));
                break;
            case 10:
                img.setColorFilter(this.getResources().getColor( R.color.color_line_ten));
                img2.setColorFilter(this.getResources().getColor( R.color.color_line_ten));
                break;
            default:
                img.setColorFilter(this.getResources().getColor( R.color.color_line_nine));
                img2.setColorFilter(this.getResources().getColor( R.color.color_line_nine));

                break;
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Location location = new Location("user");
        location.setLatitude(new StoreData(this).getLatitude());
        location.setLongitude(new StoreData(this).getLongitude());
        Location kabbaLocation = new Location("Kaba");
        kabbaLocation.setLongitude(39.823333);
        kabbaLocation.setLatitude(21.423333);
        float azimuth = event.values[0];
        float baseAzimuth = azimuth;
        GeomagneticField geoField = new GeomagneticField(Double
                .valueOf(new StoreData(this).getLatitude()).floatValue(), Double
                .valueOf(new StoreData(this).getLongitude()).floatValue(),
                Double.valueOf(new StoreData(this).getAltitude()).floatValue(),
                System.currentTimeMillis());
        azimuth -= geoField.getDeclination();

        float bearTo = location.bearingTo(kabbaLocation);

        if (bearTo < 0) {
            bearTo = bearTo + 360;

        }
        float direction = bearTo - azimuth;

        if (direction < 0) {
            direction = direction + 360;
        }

        Animation an = new RotateAnimation(direction, azimuth,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);

        baseAzimuth = azimuth;

        an.setDuration(500);
        an.setRepeatCount(0);
        an.setFillAfter(true);

        compass.startAnimation(an);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
