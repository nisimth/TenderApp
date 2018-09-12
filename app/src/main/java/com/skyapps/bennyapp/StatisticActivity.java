package com.skyapps.bennyapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.skyapps.bennyapp.support.ManageActivity;
import com.skyapps.bennyapp.support.SupportedActivity;
import com.skyapps.bennyapp.tenders.MyTendersActivity;
import com.skyapps.bennyapp.tenders.TendersActivity;
import com.skyapps.bennyapp.tenders.TyototActivity;
import com.skyapps.bennyapp.tenders.WinTendersActivity;
import com.skyapps.bennyapp.tenders.notificationstabs.MyNotificationsActivity;

import java.util.ArrayList;

public class StatisticActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private long send ;
    private long win ;
    private long loss ;
    private BarChart barChart;
    private DrawerLayout drawer;
    private TextView name_user;

    private TextView s , w , l ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);
        ///////////////////////////////////////////////////////

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

        ///////////////////////////////////////////////////////

        barChart = (BarChart)findViewById(R.id.static_bar);
        final ArrayList<BarEntry> barEntries = new ArrayList<>();
       /* s = (TextView)findViewById(R.id.tender_send);
        s.setText("סך המכרזים שהגשתי :"+ (win+loss+send));
        w = (TextView)findViewById(R.id.tender_win);
        w.setText("סך המכרזים שניצחתי :"+ (win));
        l = (TextView)findViewById(R.id.tender_loss);
        l.setText("סך המכרזים שהפסדתי :"+ (loss));*/


        Firebase.setAndroidContext(this);
        final Firebase myFirebaseRef = new Firebase("https://tenders-83c71.firebaseio.com/");
        final String username = getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getString("username", "");


        myFirebaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                //////// count win tenders
                DataSnapshot snapshot1 = snapshot.child("users").child(username).child("TenderWin");
                for (DataSnapshot postSnapshot : snapshot1.getChildren()){
                    for (int j = 0 ; j < snapshot1.child(postSnapshot.getKey()).getChildrenCount() ; j++){
                        win++;
                    }
                }

                //////// count loss and send tenders
                DataSnapshot snapshot2 = snapshot.child("users").child(username).child("Tenders");
                for (DataSnapshot postSnapshot : snapshot2.getChildren()){
                    for (int j = 1 ; j <= snapshot2.child(postSnapshot.getKey()).getChildrenCount() ; j++){
                        Log.e("stam",snapshot2.child(postSnapshot.getKey()).child("מכרז"+j) +"");

                        // if tender is loss
                        if(snapshot2.child(postSnapshot.getKey()).child("מכרז"+j).getValue().equals("loss")){

                            loss++;
                        }
                        // if tender is send
                        else if (snapshot2.child(postSnapshot.getKey()).child("מכרז"+j).getValue().equals("sended")){
                            send++;
                        }
                    }
                }

                long total = win + loss + send ;

                s = (TextView)findViewById(R.id.tender_send);
                s.setText("סך המכרזים שהגשתי :"+"     "  +(total));
                w = (TextView)findViewById(R.id.tender_win);
                w.setText("סך המכרזים שניצחתי :"+"     "+(win));
                l = (TextView)findViewById(R.id.tender_loss);
                l.setText("סך המכרזים שהפסדתי :"+"   "+(loss));

                barEntries.add(new BarEntry(total,0));
                barEntries.add(new BarEntry(win,1));
                barEntries.add(new BarEntry(loss,2));




                BarDataSet barDataSet = new BarDataSet(barEntries,"");
                barDataSet.setColor(Color.YELLOW);
                int[] colors = new int[] {Color.rgb(0, 0, 230),
                                          Color.rgb(0, 255, 153),
                                          Color.rgb(230, 46, 0)};
                barDataSet.setColors(colors);




                ArrayList<String> categoryList = new ArrayList<>();
                categoryList.add("מכרזים\nשניגשתי");
                categoryList.add("מכרזים\nשניצחתי");
                categoryList.add("מכרזים\nשהפסדתי");
                //categoryList.add("מכרזים\nב");


                BarData data = new BarData(categoryList,barDataSet);
                barChart.setData(data);
                barChart.setBackgroundColor(Color.rgb(230, 255, 255));

                barChart.setDescription("");
                //barChart.setDescriptionColor();
                barChart.setTouchEnabled(true);
                barChart.setDragEnabled(true);
                barChart.setScaleEnabled(true);
                barChart.invalidate(); // refresh

                Legend legend = barChart.getLegend();
                /// the color & size "string from above"
                legend.setTextSize(12f);
                legend.setTextColor(Color.RED);


            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }



    //////////////////////// all this part handles with side menu /////////////////////////////
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
        }else if( id == R.id.public_tenders ){
            Intent i = new Intent(this, TendersActivity.class);
            i.putExtra("to_public", "public");
            startActivity(i); finish();
            ///////////////////////////////////////////////////////////////////
        } else if (id == R.id.tyotot) {
            startActivity(new Intent(this , TyototActivity.class)); finish();
        } else if (id == R.id.my_tenders) {
            startActivity(new Intent(this , MyTendersActivity.class)); finish();
        } else if (id == R.id.win_tenders) {
            startActivity(new Intent(this , WinTendersActivity.class)); finish();
        } else if (id == R.id.notifications) {
            startActivity(new Intent(this , MyNotificationsActivity.class)); finish();

        } else if (id == R.id.my_stats) {
            drawer.closeDrawer(Gravity.START);
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
                    Toast.makeText(StatisticActivity.this, "התנתקת בהצלחה", Toast.LENGTH_LONG).show();
                    getSharedPreferences("BennyApp" , MODE_PRIVATE).edit().clear().commit();
                    startActivity(new Intent(StatisticActivity.this , MainActivity.class)); finish();
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
