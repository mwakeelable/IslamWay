package com.trianglz.islamlogic.ui;

import android.content.Context;
import android.content.Intent;
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

public class CategoryActivity extends AppCompatActivity {
    List<String> categories;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    public static int categoryPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.categoryRecyclerView);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);
        databaseAccess.open();
        categories = databaseAccess.getCategories();
        recyclerView.setAdapter(new CategoryAdapter(this));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cv;
        private TextView CagtegoryName;

        public ViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.categoryCardView);
            cv.setCardElevation(0);
            Typeface face=Typeface.createFromAsset(CategoryActivity.this.getAssets(),"fonts/mobily.ttf");
            CagtegoryName = (TextView) itemView.findViewById(R.id.categoryName);
            CagtegoryName.setTypeface(face);

        }

        private void setData(String name) {
            CagtegoryName.setText(name);
        }
    }

    class CategoryAdapter extends RecyclerView.Adapter<ViewHolder> {
        private LayoutInflater mLayoutInflater;

        public CategoryAdapter(Context context) {
            mLayoutInflater = LayoutInflater.from(context);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(mLayoutInflater
                    .inflate(R.layout.activity_cat_item, parent, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            final String name = categories.get(position);
            holder.setData(name);
            holder.cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    categoryPosition = position + 1;
                    Intent intent = new Intent(CategoryActivity.this, AzkarActivity.class);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return categories.size();
        }
    }

}
