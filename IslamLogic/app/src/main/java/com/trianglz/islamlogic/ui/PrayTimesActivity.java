package com.trianglz.islamlogic.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.trianglz.islamlogic.R;
import com.trianglz.islamlogic.fragment.HomeFragment;
import com.trianglz.islamlogic.utility.ApplicationUtils;

public class PrayTimesActivity extends AppCompatActivity {
    int day_state = 0;
    FragmentManager fragmentManager;
    Context context;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        day_state = ApplicationUtils.getDayState();
        setThemeAccordingToDayState();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pray_times);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = this;
        setActionbarBackground();
        initFragmentManager();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setThemeAccordingToDayState() {
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

    private void initFragmentManager() {
        fragmentManager = getSupportFragmentManager();
        openHomeFragment();
    }

    private void openHomeFragment() {
        fragmentManager.beginTransaction().replace(R.id.flContent, HomeFragment.newInstance()).commit();
    }

    private void setActionbarBackground() {
        switch (day_state) {
            case ApplicationUtils.MORNING:
                toolbar.setBackgroundColor(getResources().getColor(R.color.morningActionbar));
                break;
            case ApplicationUtils.NOON:
                toolbar.setBackgroundColor(getResources().getColor(R.color.afternoonActionbar));
                break;
            case ApplicationUtils.EVENING:
                toolbar.setBackgroundColor(getResources().getColor(R.color.eveningActionbar));
                break;
            case ApplicationUtils.NIGHT:
                toolbar.setBackgroundColor(getResources().getColor(R.color.nightActionbar));
                break;
        }
    }
}
