package com.skyapps.bennyapp.tenders.notificationstabs;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.skyapps.bennyapp.R;

import java.util.ArrayList;


public class NewTab extends Fragment {
    private ListView list;
    private CustomNotificationAdapter customNotificationAdapter;
    private ArrayList<ItemNotification> listData;
    private Firebase myFirebaseRef;
    private long countFullItems;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_list_tabs3, container, false);
        ((TextView)view.findViewById(R.id.title)).setText("מכרזים חדשים");
        list = view.findViewById(R.id.list);
        listData = new ArrayList<ItemNotification>();

        Firebase.setAndroidContext(getContext());//FireBase , Upload Data from firebase to EditTexts...
        myFirebaseRef = new Firebase("https://tenders-83c71.firebaseio.com/users/" +
                getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getString("username", "") + "/MyNotifications/newtenders");

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

                                listData.add(new ItemNotification(username, message, type));

                    }
                    customNotificationAdapter = new CustomNotificationAdapter(listData , getContext());
                    list.setAdapter(customNotificationAdapter);

                } catch (Exception e){

                }



            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


        return view;
    }


}
