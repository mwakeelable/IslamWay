package com.trianglz.islamlogic.fragment;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.trianglz.islamlogic.R;
import com.trianglz.islamlogic.data.StaticData;
import com.trianglz.islamlogic.database.DatabaseHelper;
import com.trianglz.islamlogic.model.Hadith;
import com.trianglz.islamlogic.model.Prayer;
import com.trianglz.islamlogic.reciever.AlarmReceiver;
import com.trianglz.islamlogic.utility.ApplicationUtils;
import com.trianglz.islamlogic.utility.RamadanScheduleMaker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;



/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    TextView tvFajrTime, tvDohrTime, tvAsrTime, tvMaghribTime, tvIshaTime, tvHadithFull, tv_more,
            tvIfterTime, tvSehriTIme, tvIfter, tvSehri, tvNextPrayer, tvNextPrayTime, tvWeekDay, tvDate, tvHour,
            tvMinute, tvSecond, tvHadit, tvNPTR, tvSecondText, fajr, dohr, asr, magrib, isha;

    LinearLayout llIfter, llSehri, fajr_layout, duhur_layout, asr_layout, maghrib_layout, isha_layout;
    RelativeLayout rlFragmentBg, rlBottomLayout;

    long fazrWaqtMs, sunriseMs, dohrWaqtMs, asrWaqtMs, maghribWaqtMs, maghribEnd, ishaWaqtMs, dayEnd;
    Context context;

    int day_state = 0;
    Hadith momentsHadith;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Reading all contacts
        day_state = ApplicationUtils.getDayState();
        context = getActivity();
        // Log.e("Time", day_state+"");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
        // Inflate the layout for this fragment

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUI(view);
        setBackgroundNdBarColor();
        setTypeface();
        setPrayerTime();
        setDayEnd();
        setNextPrayerTime();
        setIfterTime();
        setTvSehriTIme();
//        setHadith();
//        addClickListeners();
    }

    private void initUI(View view) {
        tvFajrTime = (TextView) view.findViewById(R.id.tvFajrTime);
        tvAsrTime = (TextView) view.findViewById(R.id.tvAsrTime);
        tvDohrTime = (TextView) view.findViewById(R.id.tvDohrTime);
        tvMaghribTime = (TextView) view.findViewById(R.id.tvMaghribTime);
        tvIshaTime = (TextView) view.findViewById(R.id.tvIshaTime);
        fajr = (TextView) view.findViewById(R.id.fajr);
        dohr = (TextView) view.findViewById(R.id.dohr);
        asr = (TextView) view.findViewById(R.id.asr);
        magrib = (TextView) view.findViewById(R.id.magrib);
        isha = (TextView) view.findViewById(R.id.isha);
//        tvHadithFull = (TextView) view.findViewById(R.id.tvHadithFull);
//        tv_more = (TextView) view.findViewById(R.id.tvMore);
        tvIfter = (TextView) view.findViewById(R.id.tvIftar);
        tvSehri = (TextView) view.findViewById(R.id.tvSeheri);
        tvIfterTime = (TextView) view.findViewById(R.id.tvIftarTime);
        tvSehriTIme = (TextView) view.findViewById(R.id.tvSeheriTime);
        tvNextPrayer = (TextView) view.findViewById(R.id.tvNextPrayer);
        tvNextPrayTime = (TextView) view.findViewById(R.id.tvNextPrayTime);
        tvWeekDay = (TextView) view.findViewById(R.id.tvWeekDay);
        tvDate = (TextView) view.findViewById(R.id.tvDate);
        tvHour = (TextView) view.findViewById(R.id.tvHour);
        tvMinute = (TextView) view.findViewById(R.id.tvMinute);
        tvSecond = (TextView) view.findViewById(R.id.tvSecond);
//        tvHadit = (TextView) view.findViewById(R.id.tvHadit);
        tvNPTR = (TextView) view.findViewById(R.id.tvNPTR);
        tvSecondText = (TextView) view.findViewById(R.id.tvSecondText);
        llIfter = (LinearLayout) view.findViewById(R.id.llIfter);
        llSehri = (LinearLayout) view.findViewById(R.id.llSehri);
        rlFragmentBg = (RelativeLayout) view.findViewById(R.id.rlFragmentBg);
        rlBottomLayout = (RelativeLayout) view.findViewById(R.id.rlBottomLayout);
        fajr_layout = (LinearLayout) view.findViewById(R.id.fajr_layout);
        duhur_layout = (LinearLayout) view.findViewById(R.id.duhur_layout);
        asr_layout = (LinearLayout) view.findViewById(R.id.asr_layout);
        maghrib_layout = (LinearLayout) view.findViewById(R.id.maghrib_layout);
        isha_layout = (LinearLayout) view.findViewById(R.id.isha_layout);
    }


    private void setTypeface() {
        tvHour.setTypeface(ApplicationUtils.setTimeTypeface(context));
        tvMinute.setTypeface(ApplicationUtils.setTimeTypeface(context));
        tvSecond.setTypeface(ApplicationUtils.setTimeTypeface(context));

        tvNextPrayer.setTypeface(ApplicationUtils.setTimeTypeface(context));
        tvNextPrayTime.setTypeface(ApplicationUtils.setTimeTypeface(context));
        tvWeekDay.setTypeface(ApplicationUtils.setTimeTypeface(context));
        tvDate.setTypeface(ApplicationUtils.setTimeTypeface(context));

        tvIfter.setTypeface(ApplicationUtils.setTimeTypeface(context));
        tvIfterTime.setTypeface(ApplicationUtils.setTimeTypeface(context));
        tvSehri.setTypeface(ApplicationUtils.setTimeTypeface(context));
        tvSehriTIme.setTypeface(ApplicationUtils.setTimeTypeface(context));
        tvSecondText.setTypeface(ApplicationUtils.setTimeDateTypeface(context));
        fajr.setTypeface(ApplicationUtils.setTimeTypeface(context));
        dohr.setTypeface(ApplicationUtils.setTimeTypeface(context));
        asr.setTypeface(ApplicationUtils.setTimeTypeface(context));
        magrib.setTypeface(ApplicationUtils.setTimeTypeface(context));
        isha.setTypeface(ApplicationUtils.setTimeTypeface(context));
        tvFajrTime.setTypeface(ApplicationUtils.setTimeTypeface(context));
        tvDohrTime.setTypeface(ApplicationUtils.setTimeTypeface(context));
        tvAsrTime.setTypeface(ApplicationUtils.setTimeTypeface(context));
        tvMaghribTime.setTypeface(ApplicationUtils.setTimeTypeface(context));
        tvIshaTime.setTypeface(ApplicationUtils.setTimeTypeface(context));

//        tvHadithFull.setTypeface(ApplicationUtils.setHadithDetailTypeface(context));
        tvNPTR.setTypeface(ApplicationUtils.setHadithDetailTypeface(context));
    }

    private void setBackgroundNdBarColor() {

        switch (day_state) {
            case ApplicationUtils.MORNING:
//                tvHadit.setBackgroundResource(R.drawable.hadit_of_the_day_tv_morning);
                rlBottomLayout.setBackgroundColor(getResources().getColor(R.color.morningrlBottomLayout));
                break;

            case ApplicationUtils.NOON:
//                tvHadit.setBackgroundResource(R.drawable.hadit_of_the_day_tv_day);
                rlBottomLayout.setBackgroundColor(getResources().getColor(R.color.afternoonrlBottomLayout));
                break;

            case ApplicationUtils.EVENING:
//                tvHadit.setBackgroundResource(R.drawable.hadit_of_the_day_tv_evening);
                rlBottomLayout.setBackgroundColor(getResources().getColor(R.color.eveningrlBottomLayout));
                break;

            case ApplicationUtils.NIGHT:
//                tvHadit.setBackgroundResource(R.drawable.hadit_of_the_day_tv_night);
                rlBottomLayout.setBackgroundColor(getResources().getColor(R.color.nightrlBottomLayout));
                break;
        }
    }

    private void setNextPrayerTime() {
        Calendar calendar = Calendar.getInstance();
        long currentTimeMs = calendar.getTimeInMillis();
//        Log.e("CurrentTime", currentTimeMs + "");

        if (currentTimeMs < fazrWaqtMs) {
            setCurrentPrayer("Fajr");
            setDrawableGreenCircle(fajr_layout);
            setDrawableWhiteCircle(duhur_layout, asr_layout, maghrib_layout, isha_layout);
            setNextPrayer("Fajr", tvFajrTime.getText().toString(), fazrWaqtMs);
            setCountDown(fazrWaqtMs - currentTimeMs);
            setWeekDay(false);
        } else if (currentTimeMs >= fazrWaqtMs && currentTimeMs < sunriseMs) {
            setCurrentPrayer("Fajr");
            setDrawableGreenCircle(fajr_layout);
            setDrawableWhiteCircle(duhur_layout, asr_layout, maghrib_layout, isha_layout);
            setNextPrayer("Zuhr", tvDohrTime.getText().toString(), dohrWaqtMs);
            setCountDown(sunriseMs - currentTimeMs);
            setWeekDay(false);
        } else if (currentTimeMs >= sunriseMs && currentTimeMs < dohrWaqtMs) {
            setCurrentPrayer("Zuhr");
            setDrawableGreenCircle(duhur_layout);
            setDrawableWhiteCircle(fajr_layout, asr_layout, maghrib_layout, isha_layout);
            setNextPrayer("Zuhr", tvDohrTime.getText().toString(), dohrWaqtMs);
            setCountDown(dohrWaqtMs - currentTimeMs);
            setWeekDay(false);
        } else if (currentTimeMs >= dohrWaqtMs && currentTimeMs < asrWaqtMs) {
            setCurrentPrayer("Zuhr");
            setDrawableGreenCircle(duhur_layout);
            setDrawableWhiteCircle(fajr_layout, asr_layout, maghrib_layout, isha_layout);
            setNextPrayer("Asr", tvAsrTime.getText().toString(), asrWaqtMs);
            setCountDown(asrWaqtMs - currentTimeMs);
            setWeekDay(false);
        } else if (currentTimeMs >= asrWaqtMs && currentTimeMs < maghribWaqtMs) {
            setCurrentPrayer("Asr");
            setDrawableGreenCircle(asr_layout);
            setDrawableWhiteCircle(fajr_layout, duhur_layout, maghrib_layout, isha_layout);
            setNextPrayer("Maghrib", tvMaghribTime.getText().toString(), maghribWaqtMs);
            setCountDown(maghribWaqtMs - currentTimeMs);
            setWeekDay(false);
        } else if (currentTimeMs >= maghribWaqtMs && currentTimeMs < maghribEnd) {
            setCurrentPrayer("Maghrib");
            setDrawableGreenCircle(maghrib_layout);
            setDrawableWhiteCircle(fajr_layout, asr_layout, duhur_layout, isha_layout);
            setNextPrayer("Isha", tvIshaTime.getText().toString(), ishaWaqtMs);
            setCountDown(maghribEnd - currentTimeMs);
            setWeekDay(false);
        } else if (currentTimeMs >= maghribEnd && currentTimeMs < ishaWaqtMs) {
            setCurrentPrayer("Isha");
            setDrawableGreenCircle(isha_layout);
            setDrawableWhiteCircle(fajr_layout, asr_layout, maghrib_layout, duhur_layout);
            setNextPrayer("Isha", tvIshaTime.getText().toString(), ishaWaqtMs);
            setCountDown(ishaWaqtMs - currentTimeMs);
            setWeekDay(false);
        } else if (currentTimeMs >= ishaWaqtMs && currentTimeMs < dayEnd) {
            setCurrentPrayer("Isha");
            setDrawableGreenCircle(isha_layout);
            setDrawableWhiteCircle(fajr_layout, asr_layout, maghrib_layout, duhur_layout);
            setNextPrayer("Far", tvFajrTime.getText().toString(), fazrWaqtMs);
            setCountDown(dayEnd - currentTimeMs);
            setWeekDay(true);
        }else if(currentTimeMs>dayEnd){
            updatePrayerTimes();
            setNextPrayerTime();
        }


    }
    public void updatePrayerTimes(){
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
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
    }

    private void addClickListeners() {
        tv_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApplicationUtils.openHadithDialog(getActivity(), momentsHadith);
            }
        });
    }

    private void setWeekDay(boolean isNextDay) {
        if (!isNextDay) {
            tvWeekDay.setText(getDay());
            tvDate.setText(getDate());
        } else {
            String day = getNextDay(getDay());
            String date = getNextDate();
            tvWeekDay.setText(day);
            tvDate.setText(date);
        }
    }

    private void setDrawableWhiteCircle(LinearLayout ll1, LinearLayout ll2, LinearLayout ll3, LinearLayout ll4) {
        ll1.setBackgroundResource(R.drawable.border_white_bg_transparent_white_rounded);
        ll2.setBackgroundResource(R.drawable.border_white_bg_transparent_white_rounded);
        ll3.setBackgroundResource(R.drawable.border_white_bg_transparent_white_rounded);
        ll4.setBackgroundResource(R.drawable.border_white_bg_transparent_white_rounded);
    }

    private void setDrawableGreenCircle(LinearLayout linearLayout) {
        linearLayout.setBackgroundResource(R.drawable.border_green_bg_transparent_green_rounded);
    }

    public void setPrayerTime() {
        DatabaseHelper databaseHelper = new DatabaseHelper(getActivity());
        ArrayList<Prayer> prayers = databaseHelper.getPrayer();
        for (int i = 0; i < prayers.size(); i++) {
            if (i == 0) {
                tvFajrTime.setText(prayers.get(i).getPrayerTime().toString());
                fazrWaqtMs = ApplicationUtils.getPrayerTimeInMs(prayers.get(i).getPrayerTime().toString());
//                Log.e("Fajr In Ms", fazrWaqtMs + "");
            }
            if (i == 1) {
                sunriseMs = ApplicationUtils.getPrayerTimeInMs(prayers.get(i).getPrayerTime().toString());
//                Log.e("Sunruse In Ms", sunriseMs + "");
            }
            if (i == 2) {
                tvDohrTime.setText(prayers.get(i).getPrayerTime().toString());
                dohrWaqtMs = ApplicationUtils.getPrayerTimeInMs(prayers.get(i).getPrayerTime().toString());
                Log.e("Dohr In Ms", dohrWaqtMs + "");
            }
            if (i == 3) {
                tvAsrTime.setText(prayers.get(i).getPrayerTime().toString());
                asrWaqtMs = ApplicationUtils.getPrayerTimeInMs(prayers.get(i).getPrayerTime().toString());
                Log.e("Asr In Ms", asrWaqtMs + "");
            }
            if (i == 4) {
                tvMaghribTime.setText(prayers.get(i).getPrayerTime().toString());
                maghribWaqtMs = ApplicationUtils.getPrayerTimeInMs(prayers.get(i).getPrayerTime().toString());
//                Log.e("Maghrib In Ms", maghribWaqtMs + "");
                maghribEnd = maghribWaqtMs + 1000 * 60 * 45;
//                Log.e("Maghrib End", maghribEnd + "");
            }
            if (i == 5) {
                tvIshaTime.setText(prayers.get(i).getPrayerTime().toString());
                ishaWaqtMs = ApplicationUtils.getPrayerTimeInMs(prayers.get(i).getPrayerTime().toString());
//                Log.e("Isha In Ms", ishaWaqtMs + "");
            }
        }
        saveAlarm(fazrWaqtMs, dohrWaqtMs, asrWaqtMs, maghribWaqtMs, ishaWaqtMs);

    }

    private void saveAlarm(long alarmTimeFajr, long alarmTimeDuhr, long alarmTimeAsr, long alarmTimeMagrib, long alarmTimeIsha) {
        SharedPreferences preferences = context.getSharedPreferences(StaticData.KEY_PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(StaticData.PRAYER_TIME_FAJR, alarmTimeFajr);
        editor.putLong(StaticData.PRAYER_TIME_DUHR, alarmTimeDuhr);
        editor.putLong(StaticData.PRAYER_TIME_ASR, alarmTimeAsr);
        editor.putLong(StaticData.PRAYER_TIME_MAGRIB, alarmTimeMagrib);
        editor.putLong(StaticData.PRAYER_TIME_ISHA, alarmTimeIsha);
        editor.putLong(StaticData.NEW_DAY, getNewDay());
        editor.commit();
    }

    private long getNewDay(){
        setDayEnd();
        long newDay = dayEnd+StaticData.TEN_MINUTE;
        return newDay;
    }

    private String getDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("dd/MM/yyyy ");
        String strDate = mdformat.format(calendar.getTime());
        return strDate;
    }

    private String getNextDate() {
        Calendar calendar = Calendar.getInstance();
        long today = calendar.getTimeInMillis();
        long nextDay = today + RamadanScheduleMaker.DAY_IN_MS;
        calendar.setTimeInMillis(nextDay);
        SimpleDateFormat mdformat = new SimpleDateFormat("dd/MM/yyyy ");
        String strDate = mdformat.format(calendar.getTime());
        return strDate;
    }

    private String getNextDay(String today) {
        if (today.equalsIgnoreCase(StaticData.SATURDAY)) {
            return StaticData.SUNDAY;
        } else if (today.equalsIgnoreCase(StaticData.SUNDAY)) {
            return StaticData.MONDAY;
        } else if (today.equalsIgnoreCase(StaticData.MONDAY)) {
            return StaticData.TUESDAY;
        } else if (today.equalsIgnoreCase(StaticData.TUESDAY)) {
            return StaticData.WEDNESDAY;
        } else if (today.equalsIgnoreCase(StaticData.WEDNESDAY)) {
            return StaticData.THURSDAY;
        } else if (today.equalsIgnoreCase(StaticData.THURSDAY)) {
            return StaticData.FRIDAY;
        } else if (today.equalsIgnoreCase(StaticData.FRIDAY)) {
            return StaticData.SATURDAY;
        } else {
            return today;
        }
    }

    private String getDay() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date d = new Date();
        String dayOfTheWeek = sdf.format(d);
        return dayOfTheWeek;
    }

    private void setNextPrayer(String prayerName, String prayerTime, long alarmTime) {
        tvNextPrayer.setText(prayerName);
        tvNextPrayTime.setText(prayerTime);

        SharedPreferences preferences = context.getSharedPreferences(StaticData.KEY_PREFERENCE, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
//        Log.e("NEXT PRAYER TIME", alarmTime + "");
        boolean isAlarm = preferences.getBoolean(StaticData.IS_ALARMED, false);
        if (!isAlarm) {
            setAlarm(alarmTime);
            editor.putLong(StaticData.ALARM_TIME, alarmTime);
            editor.putBoolean(StaticData.IS_ALARMED, true);
            editor.commit();
        }
    }

    private void setAlarm(long alarmTime) {
        Intent myIntent = new Intent(context, AlarmReceiver.class);
        myIntent.putExtra(StaticData.ALARM_TIME, alarmTime);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, myIntent, PendingIntent.FLAG_CANCEL_CURRENT);
//        Log.e("NEXT ALARM TIME", alarmTime + "");
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alarmTime, pendingIntent);
        } else if(Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmTime, pendingIntent);
        }else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, alarmTime, pendingIntent);
        }
    }

    private void setCurrentPrayer(String prayerName) {
        tvNPTR.setText(prayerName + " Time Remaining");
    }

    private void setIfterTime() {
        tvIfterTime.setText(tvMaghribTime.getText().toString());
    }

    private void setTvSehriTIme() {
        tvSehriTIme.setText(tvFajrTime.getText().toString());
    }

    private void setHadith() {
        momentsHadith = ApplicationUtils.getHadithForMoment(context);
        tvHadithFull.setText(momentsHadith.getHadithDetails());
//        Log.e("HADITH_NOW", momentsHadith.getHadithDetails());
    }

    private void setCountDown(long waqt) {
//        Log.e("Countdown", "Begin");
        CounterClass timer = new CounterClass(waqt,
                1000, context);
        timer.start();
    }

    private void setDayEnd() {
        Calendar calendar = Calendar.getInstance();
        String dateString = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH) + " " + "23:59:59";
        SimpleDateFormat dtFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date postDate = ApplicationUtils.formatDate(dateString, dtFormat);
        calendar.setTime(postDate);
        dayEnd = calendar.getTimeInMillis();
        Log.e("DayEnd", dayEnd + "");
    }

    public class CounterClass extends CountDownTimer {
        private static final long HOUR_DIVISOR = 3600000;
        private static final long MIN_DIVISOR = 60000;
        private static final long SEC_DIVISOR = 1000;
        Context context;

        public CounterClass(long millisInFuture, long countDownInterval, Context context) {
            super(millisInFuture, countDownInterval);
            this.context = context;
        }

        @Override
        public void onTick(long millisUntilFinished) {
            long millis = millisUntilFinished;
            long hour = millis / HOUR_DIVISOR;
            long remaining = (millis % HOUR_DIVISOR);
            long minute = remaining / MIN_DIVISOR;
            remaining = remaining % MIN_DIVISOR;
            long second = remaining / SEC_DIVISOR;
            String hourStr = String.format("%02d", hour);
            String minStr = String.format("%02d", minute);
            String secStr = String.format("%02d", second);
//            Log.e("Time Remaining",hourStr+":"+minStr+":"+secStr);
            tvHour.setText(hourStr);
            tvMinute.setText(minStr);
            tvSecond.setText(secStr);
        }

        @Override
        public void onFinish() {
            setNextPrayerTime();
        }

    }
}
