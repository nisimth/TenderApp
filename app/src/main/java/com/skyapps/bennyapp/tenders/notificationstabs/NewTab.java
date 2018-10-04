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
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.skyapps.bennyapp.R;
import com.skyapps.bennyapp.tenders.DetailsPublic;
import com.skyapps.bennyapp.tenders.tabs.TabsActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;


public class NewTab extends Fragment {
    private ListView list;
    private CustomNotificationAdapter customNotificationAdapter;
    private ArrayList<ItemNotification> listData;
    private Firebase myFirebaseRef;
    private long countFullItems;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_list_tabs3, container, false);
        list = view.findViewById(R.id.list);
        listData = new ArrayList<ItemNotification>();

        Firebase.setAndroidContext(getContext());//FireBase , Upload Data from fireBase to EditTexts...
        // put the message about new income Tender in list view
        myFirebaseRef = new Firebase("https://tenders-83c71.firebaseio.com/users/" +
                getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getString("username", "")
                + "/MyNotifications/newtenders");

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
                            String privateorpublic = (String) dataSnapshot.child(postSnapshot.getKey()).child("type").getValue();

                            // add the notification object on top of the array list - index 0
                        listData.add(0,new ItemNotification(username, message, type,tenderNum,numberTender,privateorpublic));

                    }

                    /// order notifications by income date ( descending order )
                    Collections.reverse(listData);
                    /*Collections.sort(listData, new Comparator<ItemNotification>() {
                        @Override
                        public int compare(ItemNotification itemNotification, ItemNotification t1) {
                           return itemNotification.getDate().compareTo(t1.getDate());
                        }
                    });*/


                    /*Collections.sort(listData, new Comparator<ItemNotification>() {
                        @Override
                        public int compare(ItemNotification itemNotification, ItemNotification t1) {
                            return converStringToDate(itemNotification.getType()).compareTo(converStringToDate(t1.getType()));
                        }
                    });*/

                    customNotificationAdapter = new CustomNotificationAdapter(listData , getContext());
                    list.setAdapter(customNotificationAdapter);
                } catch (Exception e){
                    Log.e("app e is : " , e.toString());
                    //Toast.makeText(getContext(), "יש בעיה", Toast.LENGTH_SHORT).show();
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

                // checks if the new tender is private
                if (item.getPrivateorpublic().equals("private")) {

                    Intent i = new Intent(getContext(), TabsActivity.class);
                    i.putExtra("new_msg", "new");
                    getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).edit().putString("company", item.getUsername()).commit();
                    getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).edit().putInt("num", Integer.parseInt("" + item.getNumberTender())).commit();
                    startActivity(i);

                // checks if the new tender is public
                } else {
                    // check if the current user is premium
                    if (getContext().getSharedPreferences("BennyApp" , Context.MODE_PRIVATE).getString("premium" , "").equals("premium")){

                        Intent i = new Intent(getContext(), DetailsPublic.class);
                        i.putExtra("name" , item.getUsername());
                        i.putExtra("tender" , "מכרז" + Integer.parseInt("" + item.getNumberTender()));
                        startActivity(i);

                    // if the current user is not premium
                    } else {
                        Toast.makeText(getContext(), "אינך רשאי להיכנס לפרטי המכרז מכיוון שאינך ספק פרימיום", Toast.LENGTH_LONG).show();
                    }



                }


            }
        });


        return view;
    }

    private Date converStringToDate(String date){
        date.replace("_", " ");
        date.replace("-", "/");
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date d =null;
        try {
            d = df.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return d;
    }




}
