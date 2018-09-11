package com.skyapps.bennyapp.tenders;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.skyapps.bennyapp.MainActivity;
import com.skyapps.bennyapp.Objects.Item;
import com.skyapps.bennyapp.Objects.Tender;
import com.skyapps.bennyapp.R;
import com.skyapps.bennyapp.support.ManageActivity;
import com.skyapps.bennyapp.support.SupportedActivity;
import com.skyapps.bennyapp.tenders.notificationstabs.MyNotificationsActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyTendersActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ExpandableListAdapter listAdapter;
    ExpandableListView listTenders;
    List<Tender> listDataHeader;
    HashMap<Tender, List<Item>> listDataChild;

    private ProgressDialog mProgressDialog;

    private DrawerLayout drawer;
    private TextView name_user;

    private TextView tenderCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_tenders);

        listTenders = findViewById(R.id.listTenders);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_menu_foreground);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, null, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        //drawer.openDrawer(Gravity.START);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);

        name_user = navigationView.getHeaderView(0).findViewById(R.id.name_user);
        //name_user.setText(getSharedPreferences("BennyApp" , MODE_PRIVATE).getString("username", ""));
        name_user.setText(getSharedPreferences("BennyApp" , MODE_PRIVATE).getString("firstname", "") + " "
                + getSharedPreferences("BennyApp" , MODE_PRIVATE).getString("lastname", ""));


        listDataHeader = new ArrayList<Tender>();
        listDataChild = new HashMap<Tender, List<Item>>();

        tenderCounter = (TextView) findViewById(R.id.counter_tender);

        Firebase.setAndroidContext(this);
        final Firebase myFirebaseRef = new Firebase("https://tenders-83c71.firebaseio.com/");
        final String username = getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getString("username", "");
        mProgressDialog = new ProgressDialog(MyTendersActivity.this);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage("אנא המתן...");
        mProgressDialog.show();

        ///// checks if the category & username correct and load the tender
        myFirebaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                int k = 0;

                DataSnapshot dataSnapshot2 = snapshot.child("users").child(username).child("Tenders");
                listDataChild.clear();
                listDataHeader.clear();

                for(DataSnapshot postSnapshot : dataSnapshot2.getChildren()){


                    Log.e("tyota: count " , dataSnapshot2.child(postSnapshot.getKey()).getChildrenCount()+"");


                    for(DataSnapshot postSnapshot2 : dataSnapshot2.child(postSnapshot.getKey()).getChildren()){
                        Log.e("tyota : " + dataSnapshot2.child(postSnapshot.getKey()).getKey() , dataSnapshot2.child(postSnapshot.getKey()).child(postSnapshot2.getKey()).getKey());
                        int i = 0;
                        Log.e("tyota : " , snapshot.child("Tenders").child(getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getString("category", ""))
                                .child(postSnapshot.getKey()).child(postSnapshot2.getKey()).getKey()+"");
                        Log.e("taltal" , (i + 1)+"");

                        int num = 0;
                        for (DataSnapshot postSnapshot3 : snapshot.child("Tenders/" + getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getString("category", ""))
                                .child(postSnapshot.getKey()).getChildren()) {

                            if (postSnapshot3.getKey().equals(snapshot.child("Tenders/" + getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getString("category", ""))
                                    .child(postSnapshot.getKey()).child(postSnapshot2.getKey()).getKey())){

                                listDataHeader.add(new Tender(snapshot.child("Tenders/" + getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getString("category", "")).child(postSnapshot.getKey()).child(postSnapshot2.getKey()).child("mqt").getValue() + "",
                                        postSnapshot.getKey(), snapshot.child("Tenders/" + getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getString("category", "")).child(postSnapshot.getKey()).child(postSnapshot2.getKey()).child("name").getValue() + "",
                                        snapshot.child("Tenders/" + getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getString("category", "")).child(postSnapshot.getKey()).child(postSnapshot2.getKey()).child("Info").child("startTender").getValue() + "",
                                        snapshot.child("Tenders/" + getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getString("category", "")).child(postSnapshot.getKey()).child(postSnapshot2.getKey()).child("Info").child("endTender").getValue() + "",
                                        snapshot.child("Tenders/" + getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getString("category", "")).child(postSnapshot.getKey()).child(postSnapshot2.getKey()).child("Info").child("timeStart").getValue() + "",
                                        snapshot.child("Tenders/" + getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getString("category", "")).child(postSnapshot.getKey()).child(postSnapshot2.getKey()).child("Info").child("timeEnd").getValue() + ""
                                ));

                                List<Item> list = new ArrayList<Item>();
                                list.add(new Item(postSnapshot.getKey() + "",
                                        snapshot.child("Tenders/" + getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getString("category", "")).child(postSnapshot.getKey()).child(postSnapshot2.getKey()).child("contact").getValue() + "",
                                        snapshot.child("Tenders/" + getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getString("category", "")).child(postSnapshot.getKey()).child(postSnapshot2.getKey()).child("phone").getValue() + "",
                                        snapshot.child("Tenders/" + getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getString("category", "")).child(postSnapshot.getKey()).child(postSnapshot2.getKey()).child("mail").getValue() + "",
                                        (num+1)));
                                listDataChild.put(listDataHeader.get(k), list); // Header, Child data
                                i++;
                                k++;

                            } else {

                                num++;
                            }


                        }

                    }
                }

                listAdapter = new ExpandableListAdapter(MyTendersActivity.this, listDataHeader, listDataChild);
                listTenders.setAdapter(listAdapter);

                /// the counter of the all tenders
                tenderCounter.setText("(" +listDataHeader.size()+ ")");
                mProgressDialog.dismiss();

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });




    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        drawer.openDrawer(Gravity.START);
        return true;

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.all_tenders) {
            startActivity(new Intent(this , TendersActivity.class));finish();
        } else if( id == R.id.public_tenders ){
            Intent i = new Intent(this, TendersActivity.class);
            i.putExtra("to_public", "public");
            startActivity(i); finish();
            ///////////////////////////////////////////////////////////////////
        } else if (id == R.id.tyotot) {
            startActivity(new Intent(this , TyototActivity.class)); finish();
        } else if (id == R.id.my_tenders) {
            drawer.closeDrawer(Gravity.START);
        } else if (id == R.id.win_tenders) {
            startActivity(new Intent(this , WinTendersActivity.class)); finish();

        } else if (id == R.id.notifications) {
            startActivity(new Intent(this , MyNotificationsActivity.class)); finish();

        } else if (id == R.id.support) {
            startActivity(new Intent(this , SupportedActivity.class)); finish();

        } else if (id == R.id.manage) {
            startActivity(new Intent(this , ManageActivity.class)); finish();

        } else if (id == R.id.disconnect){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("התנתקות מהמשתמש");
            builder.setMessage("האם אתה בטוח שברצונך להתנתק מהמשתמש?");
            builder.setPositiveButton("כן", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(MyTendersActivity.this, "התנתקת בהצלחה", Toast.LENGTH_LONG).show();
                    getSharedPreferences("BennyApp" , MODE_PRIVATE).edit().clear().commit();
                    startActivity(new Intent(MyTendersActivity.this , MainActivity.class)); finish();
                }
            });
            builder.setNegativeButton("לא", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
