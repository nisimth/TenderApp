package com.skyapps.bennyapp.tenders.notificationstabs;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.skyapps.bennyapp.R;
import com.skyapps.bennyapp.tenders.tabs.TabsActivity;

import java.util.ArrayList;
import java.util.Collections;


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

                                Log.e("whats the key : " , postSnapshot.getKey()+"");
                            String username = (String) dataSnapshot.child(postSnapshot.getKey()).child("username").getValue();
                            String message = (String) dataSnapshot.child(postSnapshot.getKey()).child("message").getValue();
                            String type = postSnapshot.getKey();

                            String mqtNum = (String) dataSnapshot.child(postSnapshot.getKey()).child("mqt").getValue();

                            long numberTender = (long) dataSnapshot.child(postSnapshot.getKey()).child("num").getValue();


                            // add the notification object on top of the array list - index 0
                            listData.add(new ItemNotification(username, message, type,mqtNum,numberTender));

                    }
                    Collections.reverse(listData);
                    customNotificationAdapter = new CustomNotificationAdapter(listData , getContext());
                    list.setAdapter(customNotificationAdapter);

                } catch (Exception e){
                    //Toast.makeText(getContext(), "ישנה שגיאה", Toast.LENGTH_SHORT).show();
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
