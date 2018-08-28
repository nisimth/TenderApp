package com.skyapps.bennyapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class StatisticActivity extends AppCompatActivity {
    private TextView totalWinTenders;
    private TextView totalSendedTenders;
    private TextView toatlLossTenders;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);


        totalWinTenders = (TextView) findViewById(R.id.win_sum);

    }
}
