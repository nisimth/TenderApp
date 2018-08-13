package com.skyapps.bennyapp.tenders;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.skyapps.bennyapp.FireBaseBackgroundService;
import com.skyapps.bennyapp.MainActivity;
import com.skyapps.bennyapp.R;
import com.skyapps.bennyapp.support.ManageActivity;
import com.skyapps.bennyapp.support.SupportedActivity;
import com.skyapps.bennyapp.tenders.notificationstabs.MyNotificationsActivity;

public class TendersActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TextView name_user;
    private ImageView image_user;

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tenders);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_menu_foreground);
        setSupportActionBar(toolbar);


        if (getSharedPreferences("BennyApp" , MODE_PRIVATE).getString("activity" , "").equals("tabs")
                || getSharedPreferences("BennyApp" , MODE_PRIVATE).getString("activity" , "").equals("tabs1")){
            finish();
        }


        ///// if no internet show dialog with message " no internet "
        if (isNetworkConnected() == false) {
            final Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.dialog4);

            Button btn = (Button) dialog.findViewById(R.id.btn);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();

                }
            });
            dialog.show();
        }

        Intent i = new Intent("mybroad.tal");
        Log.e("swipeActivity","mybroad...");
        sendBroadcast(i);


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, null, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);


        name_user = navigationView.getHeaderView(0).findViewById(R.id.name_user);
        //name_user.setText(getSharedPreferences("BennyApp" , MODE_PRIVATE).getString("username", ""));
        name_user.setText(getSharedPreferences("BennyApp" , MODE_PRIVATE).getString("firstname", "") + " "
                + getSharedPreferences("BennyApp" , MODE_PRIVATE).getString("lastname", ""));
        image_user = navigationView.getHeaderView(0).findViewById(R.id.image_user);


        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        //////////////// 13.08.2018 //////////////////
        try {
            if(getIntent().getStringExtra("to_public").equals("public")){
                mViewPager.setCurrentItem(1);
            }
        } catch (Exception e){

        }


    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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
            //drawer.closeDrawer(Gravity.START);
            startActivity(new Intent(this , TendersActivity.class)); finish();
        } else if( id == R.id.public_tenders ){
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
                    Toast.makeText(TendersActivity.this, "התנתקת בהצלחה", Toast.LENGTH_LONG).show();
                    getSharedPreferences("BennyApp" , MODE_PRIVATE).edit().clear().commit();
                    startActivity(new Intent(TendersActivity.this , MainActivity.class)); finish();
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

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch(position){
                case 0:
                    fragment = new privateTenders();
                    break;
                case 1:
                    fragment = new publicTenders();
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void verifyPermissions(){
        String[] permission = {Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),permission[0])== PackageManager.PERMISSION_GRANTED
        && ContextCompat.checkSelfPermission(this.getApplicationContext(),permission[1])== PackageManager.PERMISSION_GRANTED
        && ContextCompat.checkSelfPermission(this.getApplicationContext(),permission[2])== PackageManager.PERMISSION_GRANTED )
        {

        }
        else{
            ActivityCompat.requestPermissions(TendersActivity.this,permission,7);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        verifyPermissions();
    }
}
