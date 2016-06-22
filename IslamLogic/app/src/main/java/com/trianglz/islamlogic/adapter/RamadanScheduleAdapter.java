package com.trianglz.islamlogic.adapter;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.trianglz.islamlogic.R;
import com.trianglz.islamlogic.model.RamadanSchedule;

import java.util.ArrayList;



/**
 * Created by User on 27-Apr-16.
 */
public class RamadanScheduleAdapter extends RecyclerView.Adapter<RamadanScheduleAdapter.RamadanScheduleViewHolder> {

    ArrayList<RamadanSchedule> schedules;
    Context context;
    AppCompatActivity activity;

    public RamadanScheduleAdapter(ArrayList<RamadanSchedule> schedules, Context context, AppCompatActivity activity) {
        this.schedules = schedules;
        this.context = context;
        this.activity = activity;
    }

    @Override
    public RamadanScheduleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.schedule_row, parent, false);
        return new RamadanScheduleViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RamadanScheduleViewHolder holder, final int position) {
        final RamadanSchedule ramadanSchedule = schedules.get(position);
        holder.tvDate.setText(ramadanSchedule.getDate());
        holder.tvSuhurTime.setText(ramadanSchedule.getSuhur_time());
        holder.tvIftarTime.setText(ramadanSchedule.getIftar_time());
    }

    @Override
    public int getItemCount() {
        return schedules.size();
    }

    public static class RamadanScheduleViewHolder extends RecyclerView.ViewHolder {

        protected TextView tvDate;
        protected TextView tvSuhurTime;
        protected TextView tvIftarTime;

        public RamadanScheduleViewHolder(View itemView) {
            super(itemView);
            tvDate = (TextView) itemView.findViewById(R.id.tvDate);
            tvSuhurTime = (TextView) itemView.findViewById(R.id.tvSuhurTime);
            tvIftarTime = (TextView) itemView.findViewById(R.id.tvIftarTime);
        }
    }

}
