package com.skyapps.bennyapp;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

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
    private long send ;
    private long win ;
    private long loss ;
    private BarChart barChart;





    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);

        barChart = (BarChart)findViewById(R.id.static_bar);
        final ArrayList<BarEntry> barEntries = new ArrayList<>();


        Firebase.setAndroidContext(this);
        final Firebase myFirebaseRef = new Firebase("https://tenders-83c71.firebaseio.com/");
        final String username = getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getString("username", "");


        myFirebaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                //////// count win tenders
                DataSnapshot snapshot1 = snapshot.child("users").child(username).child("TenderWin");
                for (DataSnapshot postSnapshot : snapshot1.getChildren()){
                   for (int j = 0 ; j < snapshot1.child(postSnapshot.getKey()).getChildrenCount() ; j++){
                       win++;
                   }
                }

                //////// count loss and send tenders
                DataSnapshot snapshot2 = snapshot.child("users").child(username).child("Tenders");
                for (DataSnapshot postSnapshot : snapshot2.getChildren()){
                    for (int j = 1 ; j <= snapshot2.child(postSnapshot.getKey()).getChildrenCount() ; j++){
                        Log.e("stam",snapshot2.child(postSnapshot.getKey()).child("מכרז"+j) +"");

                        // if tender is loss
                        if(snapshot2.child(postSnapshot.getKey()).child("מכרז"+j).getValue().equals("loss")){

                        loss++;
                        }
                        // if tender is send
                        else if (snapshot2.child(postSnapshot.getKey()).child("מכרז"+j).getValue().equals("sended")){
                            send++;
                        }
                    }
                }

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

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
}
