package com.trianglz.islamlogic.ui;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.trianglz.islamlogic.Models.DatabaseAccess;
import com.trianglz.islamlogic.R;

import java.util.List;

public class AzkarActivity extends AppCompatActivity {
    List<String> azkar;
    List<String> hints;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    CategoryActivity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_azkar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.azkarRecyclerView);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(new AzkarAdapter(this));
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(activity);
        databaseAccess.open();
        azkar = databaseAccess.getAzkar();
        hints = databaseAccess.getHint();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cv;
        private TextView azkarName;
        private TextView zekrHint;

        public ViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.azkarCardView);
            cv.setCardElevation(0);

            Typeface face=Typeface.createFromAsset(getAssets(),"fonts/mobily.ttf");

            azkarName = (TextView) itemView.findViewById(R.id.azkarName);
            azkarName.setTypeface(face);

            Typeface hintFace = Typeface.DEFAULT.createFromAsset(getAssets(),"fonts/mobily.ttf");
            zekrHint = (TextView) itemView.findViewById(R.id.zekrHint);
            zekrHint.setTypeface(hintFace);
        }

        private void setData(String zekr, String hint) {
            azkarName.setText(zekr);
            zekrHint.setText(hint);
        }
    }

    class AzkarAdapter extends RecyclerView.Adapter<ViewHolder> {
        private LayoutInflater mLayoutInflater;

        public AzkarAdapter(Context context) {
            mLayoutInflater = LayoutInflater.from(context);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(mLayoutInflater
                    .inflate(R.layout.azkar_item, parent, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            final String name = azkar.get(position);
            final String hint = hints.get(position);
            holder.setData(name, hint);
        }

        @Override
        public int getItemCount() {
            return azkar.size();
        }
    }
}
