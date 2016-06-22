package com.trianglz.islamlogic.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.trianglz.islamlogic.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void Clicker(View view) {
        switch (view.getId()) {
            case R.id.iv6kalma:
                Intent kalmas = new Intent(MainActivity.this, CategoryActivity.class);
                startActivity(kalmas);
                break;
            case R.id.ivqibladirction:
                Intent Qibla = new Intent(MainActivity.this, QiblaActivity.class);
                startActivity(Qibla);
                break;
            case R.id.ivprayertiming:
                Intent Pray = new Intent(MainActivity.this, PrayTimesActivity.class);
                startActivity(Pray);
                break;
        }
    }
}
