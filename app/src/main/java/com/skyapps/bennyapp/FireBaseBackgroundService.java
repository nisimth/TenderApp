package com.skyapps.bennyapp;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.skyapps.bennyapp.tenders.notificationstabs.MyNotificationsActivity;

public class FireBaseBackgroundService extends IntentService {

    private Firebase f;
    private ValueEventListener handler;
    private String typeNO ;

    private NotificationManager notifManager;

    public FireBaseBackgroundService() {
        super("");
    }


    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        Log.e("test","app in " + "https://tenders-83c71.firebaseio.com/notification/" +
                getSharedPreferences("BennyApp" , MODE_PRIVATE).getString("username", ""));
        Log.e("Service","mybroad...");
        Firebase.setAndroidContext(this);
        final Firebase ref = new Firebase("https://tenders-83c71.firebaseio.com/users/" +
                getSharedPreferences("BennyApp" , MODE_PRIVATE).getString("username", "")).child("MyNotifications");

        Firebase myFirebaseRef = new Firebase("https://tenders-83c71.firebaseio.com/notification/"
                + getSharedPreferences("BennyApp" , MODE_PRIVATE).getString("username", ""));

        myFirebaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Log.e("test","app change in " + "https://tenders-83c71.firebaseio.com/notification/"
                        + getSharedPreferences("BennyApp" , MODE_PRIVATE).getString("username", ""));
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


                           // company name       the message from fireBase -> tenders-83c71 -> notification
    private void postNotif(String notifString , String messageString) {

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
        ///// the first IF condition is for Android Oreo version only !!!
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        //you must create the notification channel before posting any notifications on Android 8.0 and higher
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

            // TODO open the right fragment form list view

            intent = new Intent(this, MyNotificationsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

            //builder.setContentTitle("הודעה חדשה מ" + notifString + " מאפליקציית המכרזים!")
            builder.setContentTitle("התראה חדשה מ-WIZBIZ")// required
                    //.setSmallIcon(android.R.drawable.ic_popup_reminder) // required
                    .setSmallIcon(R.mipmap.app_logo_round)
                    .setContentText(messageString)  // required
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(messageString))
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setTicker(notifString)
                    .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
        } else {

            builder = new NotificationCompat.Builder(this);

            intent = new Intent(this, MyNotificationsActivity.class); // TODO
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

            Bitmap appLogo = BitmapFactory.decodeResource(getResources(),R.mipmap.app_logo_round);
            //builder.setContentTitle("הודעה חדשה מ" + notifString + " מאפליקציית המכרזים!")
            builder.setContentTitle("התראה חדשה מ-WIZBIZ")// required
                    .setSmallIcon(R.mipmap.app_logo_round) // required
                    .setContentText(notifString)  // required
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setTicker(notifString)
                    .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400})
                    .setPriority(Notification.PRIORITY_HIGH)
                    /////// new //////
                    .setLargeIcon(appLogo)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                    //.setColor(Color.GREEN)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(messageString)).build() ;
            //////////////////;
        } // else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

        Notification notification = builder.build();
        notifManager.notify(NOTIFY_ID, notification);

        Firebase myFirebaseRef = new Firebase("https://tenders-83c71.firebaseio.com/notification/"
                + getSharedPreferences("BennyApp" , MODE_PRIVATE).getString("username", ""));
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
