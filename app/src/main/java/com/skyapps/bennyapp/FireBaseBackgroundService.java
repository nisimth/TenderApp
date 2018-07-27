package com.skyapps.bennyapp;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class FireBaseBackgroundService extends IntentService {

    private Firebase f;
    private ValueEventListener handler;
    private String tal, user2;

    private NotificationManager notifManager;

    public FireBaseBackgroundService() {
        super("");
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        Log.e("test","app in " + "https://tenders-83c71.firebaseio.com/notification/" + getSharedPreferences("BennyApp" , MODE_PRIVATE).getString("username", ""));
        Log.e("Service","mybroad...");
        Firebase.setAndroidContext(this);
        final Firebase ref = new Firebase("https://tenders-83c71.firebaseio.com/users/" + getSharedPreferences("BennyApp" , MODE_PRIVATE).getString("username", "")).child("MyNotifications");

        Firebase myFirebaseRef = new Firebase("https://tenders-83c71.firebaseio.com/notification/" + getSharedPreferences("BennyApp" , MODE_PRIVATE).getString("username", ""));
        myFirebaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Log.e("test","app change in " + "https://tenders-83c71.firebaseio.com/notification/" + getSharedPreferences("BennyApp" , MODE_PRIVATE).getString("username", ""));
                if (dataSnapshot.child("username").getValue()!=null && dataSnapshot.child("message").getValue()!=null) {

                        postNotif(dataSnapshot.child("username").getValue()+"" , dataSnapshot.child("message").getValue()+"");

                }


            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        return START_STICKY;

    }



    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
    }



    private void postNotif(String notifString , String messageString) {

        //Toast.makeText(this,  "הודעה חדשה , " + notifString, Toast.LENGTH_SHORT).show();

        final int NOTIFY_ID = 1002;

        // There are hardcoding only for show it's just strings
        String name = "my_package_channel";
        String id = "my_package_channel_1"; // The user-visible name of the channel.
        String description = "my_package_first_channel"; // The user-visible description of the channel.

        Intent intent;
        PendingIntent pendingIntent;
        NotificationCompat.Builder builder;

        if (notifManager == null) {
            notifManager =
                    (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = notifManager.getNotificationChannel(id);
            if (mChannel == null) {
                mChannel = new NotificationChannel(id, name, importance);
                mChannel.setDescription(description);
                mChannel.enableVibration(true);
                mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                notifManager.createNotificationChannel(mChannel);
            }
            builder = new NotificationCompat.Builder(this, id);

            intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

            builder.setContentTitle("הודעה חדשה מ" + notifString + " מאפליקציית המכרזים!")  // required
                    .setSmallIcon(android.R.drawable.ic_popup_reminder) // required
                    .setContentText(messageString)  // required
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setTicker(notifString)
                    .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
        } else {

            builder = new NotificationCompat.Builder(this);

            intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

            builder.setContentTitle("הודעה חדשה מ" + notifString + " מאפליקציית המכרזים!")                           // required
                    .setSmallIcon(android.R.drawable.ic_popup_reminder) // required
                    .setContentText(messageString)  // required
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setTicker(notifString)
                    .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400})
                    .setPriority(Notification.PRIORITY_HIGH);
        } // else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

        Notification notification = builder.build();
        notifManager.notify(NOTIFY_ID, notification);

        Firebase myFirebaseRef = new Firebase("https://tenders-83c71.firebaseio.com/notification/" + getSharedPreferences("BennyApp" , MODE_PRIVATE).getString("username", ""));
        myFirebaseRef.removeValue();


        try {
            Uri ringring = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), ringring);
            r.play();
            Log.e("Ring" , "Ring");
        } catch (Exception e) {
            e.printStackTrace();
        }



    }

}
