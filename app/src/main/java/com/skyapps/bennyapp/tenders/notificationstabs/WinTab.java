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


public class WinTab extends Fragment {
    private ListView list;
    private CustomNotificationAdapter customNotificationAdapter;
    private ArrayList<ItemNotification> listData;
    private Firebase myFirebaseRef;
    private long countFullItems;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_list_tabs2, container, false);
        //((TextView)view.findViewById(R.id.title)).setText("מכרזים שזכיתי בהם");
        list = view.findViewById(R.id.list);
        listData = new ArrayList<ItemNotification>();

        Firebase.setAndroidContext(getContext());//FireBase , Upload Data from firebase to EditTexts...
        myFirebaseRef = new Firebase("https://tenders-83c71.firebaseio.com/users/" +
                getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getString("username", "") + "/MyNotifications/winners");

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


                        String tenderNum = (String) dataSnapshot.child(postSnapshot.getKey()).child("mqt").getValue();
                        long numberTender = (long) dataSnapshot.child(postSnapshot.getKey()).child("num").getValue();


                        listData.add(new ItemNotification(username, message, type,tenderNum,numberTender));

                    }
                    Collections.reverse(listData);
                    customNotificationAdapter = new CustomNotificationAdapter(listData , getContext());
                    list.setAdapter(customNotificationAdapter);

                } catch (Exception e){

                }



            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });



            // TODO
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ItemNotification item = (ItemNotification) parent.getItemAtPosition(position);
                Intent in = new Intent(getContext() , TabsActivity.class);
                in.putExtra("win_msg" , "win");
                getContext().getSharedPreferences("BennyApp" , Context.MODE_PRIVATE).edit().putString("company" , item.getUsername()).commit();
                getContext().getSharedPreferences("BennyApp" , Context.MODE_PRIVATE).edit().putInt("num" , Integer.parseInt(""+item.getNumberTender())).commit();
                startActivity(in);

            }
        });

        return view;
    }


}
