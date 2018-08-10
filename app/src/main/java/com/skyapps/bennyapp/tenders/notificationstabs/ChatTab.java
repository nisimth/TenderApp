package com.skyapps.bennyapp.tenders.notificationstabs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.skyapps.bennyapp.Objects.ItemMarket;
import com.skyapps.bennyapp.R;
import com.skyapps.bennyapp.tenders.tabs.CustomMarketAdapter;
import com.skyapps.bennyapp.tenders.tabs.TabsActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;


public class ChatTab extends Fragment {
    private ListView list;
    private CustomNotificationAdapter customNotificationAdapter;
    private ArrayList<ItemNotification> listData;
    private Firebase myFirebaseRef;
    private long countFullItems;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_list_tabs, container, false);
        //((TextView)view.findViewById(R.id.title)).setText("הודעות הצ'אטים שלי");
        list = view.findViewById(R.id.list);
        listData = new ArrayList<ItemNotification>();

        Firebase.setAndroidContext(getContext());//FireBase , Upload Data from firebase to EditTexts...
        myFirebaseRef = new Firebase("https://tenders-83c71.firebaseio.com/users/" +
                getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getString("username", "") + "/MyNotifications/chats");

        myFirebaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                listData.clear();


                try {


                    for (final DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                        //for (int i = 1; i <= dataSnapshot.child(postSnapshot.getKey()).getChildrenCount(); i++) {


                            //if (postSnapshot.getKey().equals("chats")){
                                Log.e("whats the key : " , postSnapshot.getKey()+"");
                            String username = (String) dataSnapshot.child(postSnapshot.getKey()).child("username").getValue();
                            String message = (String) dataSnapshot.child(postSnapshot.getKey()).child("message").getValue();
                            String type = postSnapshot.getKey();
                            // TODO
                            String mqtNum = (String) dataSnapshot.child(postSnapshot.getKey()).child("mqt").getValue();

                            long numberTender = (long) dataSnapshot.child(postSnapshot.getKey()).child("num").getValue();

                            /////
                            // TODO
                            listData.add(new ItemNotification(username, message, type,mqtNum,numberTender));
                            //
                    }
                    customNotificationAdapter = new CustomNotificationAdapter(listData , getContext());
                    list.setAdapter(customNotificationAdapter);

                } catch (Exception e){
                    Toast.makeText(getContext(), "ישנה שגיאה", Toast.LENGTH_SHORT).show();
                    Log.e("the e is : " , e.toString());
                }



            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ItemNotification item = (ItemNotification) parent.getItemAtPosition(position);
                //Toast.makeText(getContext(), item.getUsername(), Toast.LENGTH_SHORT).show();

                Intent i = new Intent(getContext() , TabsActivity.class);
                i.putExtra("send_msg" , "chat");
                getContext().getSharedPreferences("BennyApp" , Context.MODE_PRIVATE).edit().putString("company" , item.getUsername()).commit();
                getContext().getSharedPreferences("BennyApp" , Context.MODE_PRIVATE).edit().putInt("num" , Integer.parseInt(""+item.getNumberTender())).commit();
                startActivity(i);

            }
        });


        return view;
    }


}
