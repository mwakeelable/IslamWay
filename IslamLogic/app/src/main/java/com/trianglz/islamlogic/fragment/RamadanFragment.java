package com.trianglz.islamlogic.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trianglz.islamlogic.R;
import com.trianglz.islamlogic.adapter.RamadanScheduleAdapter;
import com.trianglz.islamlogic.database.DatabaseHelper;
import com.trianglz.islamlogic.model.RamadanSchedule;

import java.util.ArrayList;


public class RamadanFragment extends Fragment {

    RecyclerView rvSuhurIftarSchedule;
    RamadanScheduleAdapter ramadanScheduleAdapter;
    ArrayList<RamadanSchedule> ramadanSchedules;

    Context context;

    public RamadanFragment() {
        // Required empty public constructor
    }

    public static RamadanFragment newInstance() {
        RamadanFragment fragment = new RamadanFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        ramadanSchedules = databaseHelper.getRamadanSchedules();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ramadan, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUI(view);
    }

    private void initUI(View view){
        rvSuhurIftarSchedule = (RecyclerView) view.findViewById(R.id.rvSuhurIftarSchedule);
        rvSuhurIftarSchedule.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(context);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rvSuhurIftarSchedule.setLayoutManager(llm);
        ramadanScheduleAdapter = new RamadanScheduleAdapter(ramadanSchedules,context,(AppCompatActivity) getActivity());
        rvSuhurIftarSchedule.setAdapter(ramadanScheduleAdapter);
    }

}
