package com.skyapps.bennyapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/// broadcastReceiver that listening all the time background
public class StartFirebaseAtBoot extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("BootCast","mybroad...");
        context.startService(new Intent(context, FireBaseBackgroundService.class));
    }
}