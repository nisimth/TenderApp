package com.skyapps.bennyapp;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import java.util.ArrayList;

public class StatisticActivity extends AppCompatActivity {
    private long send = 10 ;
    private long win = 6;
    private long loss  = 4 ;
    private BarChart barChart;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);

        Firebase.setAndroidContext(this);
        final Firebase myFirebaseRef = new Firebase("https://tenders-83c71.firebaseio.com/");
        final String username = getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getString("username", "");
        ///// reference for TenderWIn
        myFirebaseRef.child("users").child(username).child("TenderWin").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    win = dataSnapshot.getChildrenCount();
                }else{
                    win = 0 ;
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        ///// reference for TenderWIn
        myFirebaseRef.child("users").child(username).child("Tenders").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if( !dataSnapshot.child("TotalSend").exists() ){
                    send = 0 ;
                }else{
                    send = (long)dataSnapshot.child("TotalSend").getValue();
                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        ///// reference for Tender loss
        /*myFirebaseRef.child("users").child(username).child("Tenders").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                loss = (long)dataSnapshot.child("TotalLoss").getValue();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });*/



        barChart = (BarChart)findViewById(R.id.static_bar);
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(send,0));
        barEntries.add(new BarEntry(win,1));
        barEntries.add(new BarEntry(loss,2));
        BarDataSet barDataSet = new BarDataSet(barEntries,"מס' מכרזים");


        ArrayList<String> categoryList = new ArrayList<>();
        categoryList.add("מכרזים\nשניגשתי");
        categoryList.add("מכרזים\nשניצחתי");
        categoryList.add("מכרזים\nשהפסדתי");


        BarData data = new BarData(categoryList,barDataSet);
        barChart.setData(data);
        barChart.setDescription("");
        //barChart.setDescriptionColor();
        barChart.setTouchEnabled(true);
        barChart.setDragEnabled(true);
        barChart.setScaleEnabled(true);
        barChart.invalidate(); // refresh

        Legend legend = barChart.getLegend();
        /// the color & size "string from above"
        legend.setTextSize(12f);
        legend.setTextColor(Color.RED);


    }
}
