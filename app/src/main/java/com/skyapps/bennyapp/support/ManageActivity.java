package com.skyapps.bennyapp.support;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.skyapps.bennyapp.MainActivity;
import com.skyapps.bennyapp.R;
import com.skyapps.bennyapp.StatisticActivity;
import com.skyapps.bennyapp.tenders.MyTendersActivity;
import com.skyapps.bennyapp.tenders.TendersActivity;
import com.skyapps.bennyapp.tenders.TyototActivity;
import com.skyapps.bennyapp.tenders.WinTendersActivity;
import com.skyapps.bennyapp.tenders.notificationstabs.MyNotificationsActivity;

public class ManageActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener {
    private EditText editFirstName, editLastName, editPhone , editEmail;

    private DrawerLayout drawer; // side menu
    private TextView name_user;  // the name that apperice on side menu ( the userName )

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);

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
        name_user.setText(getSharedPreferences("BennyApp" , MODE_PRIVATE).getString("firstname", "") + " "
                + getSharedPreferences("BennyApp" , MODE_PRIVATE).getString("lastname", ""));



        editFirstName = findViewById(R.id.editFirstName);
        editLastName = findViewById(R.id.editLastName);
        editPhone = findViewById(R.id.editPhone);
        editEmail = findViewById(R.id.editEmail);

        editFirstName.setText(getSharedPreferences("BennyApp" , MODE_PRIVATE).getString("firstname", ""));
        editLastName.setText(getSharedPreferences("BennyApp" , MODE_PRIVATE).getString("lastname", ""));
        editPhone.setText(getSharedPreferences("BennyApp" , MODE_PRIVATE).getString("phone", ""));
        editEmail.setText(getSharedPreferences("BennyApp" , MODE_PRIVATE).getString("email", ""));

        Firebase.setAndroidContext(this); // call to fireBase
        final Firebase myFirebaseRef = new Firebase("https://tenders-83c71.firebaseio.com/users/"
                + getSharedPreferences("BennyApp" , MODE_PRIVATE).getString("username", ""));

        myFirebaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e("test : " , dataSnapshot.child("firstname").hasChildren()+"");
                if (dataSnapshot.child("firstname").getValue()!=null)
                    editFirstName.setText(dataSnapshot.child("firstname").getValue()+"");

                if (dataSnapshot.child("lastname").getValue()!=null)
                    editLastName.setText(dataSnapshot.child("lastname").getValue()+"");

                if (dataSnapshot.child("phone").getValue()!=null)
                    editPhone.setText(dataSnapshot.child("phone").getValue()+"");

                if (dataSnapshot.child("email").getValue()!=null)
                    editEmail.setText(dataSnapshot.child("email").getValue()+"");
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        //////////////// onClick metohd for saving data--Button ///////////////////////////
        findViewById(R.id.enter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ///////// if the fields are empty ////////
                if (editFirstName.getText().toString().equals("")){
                    Toast.makeText(ManageActivity.this, "אנא מלא את השם הפרטי כראוי", Toast.LENGTH_SHORT).show();
                } else if (editLastName.getText().toString().equals("")){
                    Toast.makeText(ManageActivity.this, "אנא מלא את שם המשפחה כראוי", Toast.LENGTH_SHORT).show();
                } else if (editPhone.equals("")){
                    Toast.makeText(ManageActivity.this, "אנא מלא את מספר הטלפון כראוי", Toast.LENGTH_SHORT).show();
                } else if (editEmail.getText().toString().equals("")){
                    Toast.makeText(ManageActivity.this, "אנא מלא את הא-מייל כראוי", Toast.LENGTH_SHORT).show();
                } else {
                    /////////// when user insert his data & saving in fireBase //////////
                    myFirebaseRef.child("firstname").setValue(editFirstName.getText().toString());
                    myFirebaseRef.child("lastname").setValue(editLastName.getText().toString());
                    myFirebaseRef.child("phone").setValue(editPhone.getText().toString());
                    myFirebaseRef.child("email").setValue(editEmail.getText().toString());
                    Toast.makeText(ManageActivity.this, "פרטיך עודכנו!", Toast.LENGTH_SHORT).show();
                    //////////// ???? /////////////////
                    getSharedPreferences("BennyApp" , MODE_PRIVATE).edit()
                            .putString("firstname",editFirstName.getText().toString()).commit();
                    getSharedPreferences("BennyApp" , MODE_PRIVATE)
                            .edit().putString("lastname",editLastName.getText().toString()).commit();
                    getSharedPreferences("BennyApp" , MODE_PRIVATE)
                            .edit().putString("editphone",editPhone.getText().toString()).commit();
                    getSharedPreferences("BennyApp" , MODE_PRIVATE)
                            .edit().putString("editemail",editEmail.getText().toString()).commit();

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
        } else if (id == R.id.support) {
            startActivity(new Intent(this , SupportedActivity.class)); finish();
        } else if (id == R.id.notifications) {
            startActivity(new Intent(this , MyNotificationsActivity.class)); finish();

        } else if (id == R.id.my_stats) {
            startActivity(new Intent(this , StatisticActivity.class)); finish();

        } else if (id == R.id.manage) {
            drawer.closeDrawer(Gravity.START);
        } else if (id == R.id.disconnect){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("התנתקות מהמשתמש");
            builder.setMessage("האם אתה בטוח שברצונך להתנתק מהמשתמש?");
            builder.setPositiveButton("כן", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(ManageActivity.this, "התנתקת בהצלחה", Toast.LENGTH_LONG).show();
                    getSharedPreferences("BennyApp" , MODE_PRIVATE).edit().clear().commit();
                    startActivity(new Intent(ManageActivity.this , MainActivity.class)); finish();
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

