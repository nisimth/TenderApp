package com.skyapps.bennyapp.support;

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
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.skyapps.bennyapp.MainActivity;
import com.skyapps.bennyapp.R;
import com.skyapps.bennyapp.tenders.MyTendersActivity;
import com.skyapps.bennyapp.tenders.TendersActivity;
import com.skyapps.bennyapp.tenders.TyototActivity;
import com.skyapps.bennyapp.tenders.WinTendersActivity;
import com.skyapps.bennyapp.tenders.notificationstabs.MyNotificationsActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SupportedActivity extends AppCompatActivity   implements NavigationView.OnNavigationItemSelectedListener{
    private String dahof = "";
    private EditText editDate, editHour, title, pirot;
    private DrawerLayout drawer;
    private TextView name_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);

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
        name_user.setText(getSharedPreferences("BennyApp" , MODE_PRIVATE).getString("username", ""));

        ////// setup the current date of the day //////
        editDate = findViewById(R.id.editDate);
        Calendar c = Calendar.getInstance();

        SimpleDateFormat df = new SimpleDateFormat("d/M/yyyy");
        String formattedDate = df.format(c.getTime());
        editDate.setText(formattedDate);
        ////////////////////////////////////////////////////////////////
        ////// setup the current time of the day ///////////////////////
        editHour = findViewById(R.id.editHour);
        SimpleDateFormat df2 = new SimpleDateFormat("HH:mm");
        String formattedDate2 = df2.format(c.getTime());
        editHour.setText(formattedDate2);

        final TextView high_dahof = findViewById(R.id.high_dahof);
        final TextView medium_dahof = findViewById(R.id.medium_dahof);
        final TextView low_dahof = findViewById(R.id.low_dahof);
        ////////////////////////////////////////////////////////////////
        //////////// painting the choosen "dhifut" ////////////////////
        high_dahof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                high_dahof.setBackgroundColor(Color.YELLOW);
                medium_dahof.setBackgroundColor(Color.WHITE);
                low_dahof.setBackgroundColor(Color.WHITE);
                dahof = "גבוהה";
            }
        });

        medium_dahof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                high_dahof.setBackgroundColor(Color.WHITE);
                medium_dahof.setBackgroundColor(Color.YELLOW);
                low_dahof.setBackgroundColor(Color.WHITE);
                dahof = "בינונית";
            }
        });

        low_dahof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                low_dahof.setBackgroundColor(Color.YELLOW);
                medium_dahof.setBackgroundColor(Color.WHITE);
                high_dahof.setBackgroundColor(Color.WHITE);
                dahof = "נמוכה";
            }
        });
        ///////////////////////////////////////////////////////////////

        title = findViewById(R.id.title); // the title of the message
        pirot = findViewById(R.id.pirot); // thr description of the message

        Firebase.setAndroidContext(this);
        final Firebase myFirebaseRef = new Firebase("https://tenders-83c71.firebaseio.com/support/"
                + getSharedPreferences("BennyApp" , MODE_PRIVATE).getString("username", ""));

        findViewById(R.id.send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (editDate.getText().toString().equals("")){
                    Toast.makeText(SupportedActivity.this, "אנא מלא את התאריך כראוי", Toast.LENGTH_SHORT).show();
                } else if (editHour.getText().toString().equals("")){
                    Toast.makeText(SupportedActivity.this, "אנא מלא את השעה כראוי", Toast.LENGTH_SHORT).show();
                } else if (dahof.equals("")){
                    Toast.makeText(SupportedActivity.this, "אנא סמן את רמת הדחיפות", Toast.LENGTH_SHORT).show();
                } else if (title.getText().toString().equals("")){
                    Toast.makeText(SupportedActivity.this, "אנא מלא את נושא הפנייה", Toast.LENGTH_SHORT).show();
                } else if (pirot.getText().toString().equals("")){
                    Toast.makeText(SupportedActivity.this, "אנא מלא את תיאור הפנייה כראוי", Toast.LENGTH_SHORT).show();
                } else {
                    Date currentTime = Calendar.getInstance().getTime();
                    myFirebaseRef.child(currentTime.toString()).child("Date").setValue(editDate.getText().toString());
                    myFirebaseRef.child(currentTime.toString()).child("Hour").setValue(editHour.getText().toString());
                    myFirebaseRef.child(currentTime.toString()).child("Urgency").setValue(dahof);
                    myFirebaseRef.child(currentTime.toString()).child("Title").setValue(title.getText().toString());
                    myFirebaseRef.child(currentTime.toString()).child("Details").setValue(pirot.getText().toString());
                    Toast.makeText(SupportedActivity.this, "תודה רבה, פנייתך התקבלה!", Toast.LENGTH_SHORT).show();
                    findViewById(R.id.supportform).setVisibility(View.INVISIBLE);
                    findViewById(R.id.messageform).setVisibility(View.VISIBLE);

                    findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(SupportedActivity.this , MainActivity.class));
                            finish();
                        }
                    });
                }




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
        } else if (id == R.id.tyotot) {
            startActivity(new Intent(this , TyototActivity.class)); finish();
        } else if (id == R.id.my_tenders) {
            startActivity(new Intent(this , MyTendersActivity.class)); finish();
        } else if (id == R.id.win_tenders) {
            startActivity(new Intent(this , WinTendersActivity.class)); finish();
        } else if (id == R.id.notifications) {
            startActivity(new Intent(this , MyNotificationsActivity.class)); finish();

        } else if (id == R.id.support) {
            drawer.closeDrawer(Gravity.START);
        } else if (id == R.id.manage) {
            startActivity(new Intent(this , ManageActivity.class)); finish();

        } else if (id == R.id.disconnect){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("התנתקות מהמשתמש");
            builder.setMessage("האם אתה בטוח שברצונך להתנתק מהמשתמש?");
            builder.setPositiveButton("כן", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(SupportedActivity.this, "התנתקת בהצלחה", Toast.LENGTH_LONG).show();
                    getSharedPreferences("BennyApp" , MODE_PRIVATE).edit().clear().commit();
                    startActivity(new Intent(SupportedActivity.this , MainActivity.class)); finish();
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
