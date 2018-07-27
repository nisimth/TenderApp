package com.skyapps.bennyapp;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.skyapps.bennyapp.Objects.Suppliers;
import com.skyapps.bennyapp.tenders.TendersActivity;
import com.skyapps.bennyapp.tenders.tabs.TabsActivity;

public class MainActivity extends AppCompatActivity {
    private TextView register_text;
    private Button enter;
    private EditText username, password;
    private ProgressDialog mProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        register_text = (TextView) findViewById(R.id.register_text);
        register_text.setText(Html.fromHtml("<p>במידה ואינך רשום, להרשמה <a href='http://walla.co.il'>לחץ כאן</a></p>"));
        register_text.setMovementMethod(LinkMovementMethod.getInstance());

        if (!getSharedPreferences("BennyApp", MODE_PRIVATE).getString("username", "").equals("")){
            Log.e("the user : ",getSharedPreferences("BennyApp" , MODE_PRIVATE).getString("username", ""));
            getSharedPreferences("BennyApp" , MODE_PRIVATE).edit().putString("activity" , "").commit();

            startActivity(new Intent(MainActivity.this , TendersActivity.class));
            finish();
        }



        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        enter = (Button) findViewById(R.id.enter);

        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (username.getText().toString().equals("") && password.getText().toString().equals("")){

                    Toast.makeText(MainActivity.this, "ישנה בעיה באחד הפרטים, אנא נסה שנית", Toast.LENGTH_SHORT).show();
                    
                } else {
                    mProgressDialog = new ProgressDialog(MainActivity.this);
                    mProgressDialog.setCancelable(false);
                    mProgressDialog.setMessage("אנא המתן...");
                    mProgressDialog.show();

                    Firebase.setAndroidContext(MainActivity.this);
                    final Firebase myFirebaseRef = new Firebase("https://tenders-83c71.firebaseio.com/");

                    final Firebase ref = myFirebaseRef.child("users");



                    ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            for(DataSnapshot postSnapshot : snapshot.getChildren()){
                                Log.e("PostSnap, the key : " , postSnapshot.getKey());
                                Log.e("PostSnap, the password:" , postSnapshot.child("password").getValue()+"");


                                if (postSnapshot.getKey().equals(username.getText().toString()) && postSnapshot.child("password").getValue().equals(password.getText().toString())) {
                                    Log.e("PostSnap, Im Here" , snapshot.child(username.getText().toString()).child("firstname").getValue()+"" );
                                    if (snapshot.child(username.getText().toString()).child("firstname").getValue()!=null){
                                        getSharedPreferences("BennyApp", MODE_PRIVATE).edit().putString("firstname", snapshot.child(username.getText().toString()).child("firstname").getValue()+"").commit();
                                    }

                                    if (snapshot.child(username.getText().toString()).child("lastname").getValue()!=null){
                                        getSharedPreferences("BennyApp", MODE_PRIVATE).edit().putString("lastname", snapshot.child(username.getText().toString()).child("lastname").getValue()+"").commit();
                                    }

                                    if (snapshot.child(username.getText().toString()).child("phone").getValue()!=null){
                                        getSharedPreferences("BennyApp", MODE_PRIVATE).edit().putString("phone", snapshot.child(username.getText().toString()).child("phone").getValue()+"").commit();
                                    }

                                    if (snapshot.child(username.getText().toString()).child("email").getValue()!=null){
                                        getSharedPreferences("BennyApp", MODE_PRIVATE).edit().putString("email", snapshot.child(username.getText().toString()).child("email").getValue()+"").commit();
                                    }

                                    if (snapshot.child(username.getText().toString()).child("premium").getValue()!=null){
                                        getSharedPreferences("BennyApp", MODE_PRIVATE).edit().putString("premium", "premium").commit();
                                    } else {
                                        getSharedPreferences("BennyApp", MODE_PRIVATE).edit().putString("premium", "").commit();
                                    }


                                    startActivity(new Intent(MainActivity.this, TendersActivity.class));
                                    getSharedPreferences("BennyApp" , MODE_PRIVATE).edit().putString("username", username.getText().toString()).commit();
                                    getSharedPreferences("BennyApp" , MODE_PRIVATE).edit().putString("category", snapshot.child(username.getText().toString()).child("category").getValue()+"").commit();
                                   // Toast.makeText(MainActivity.this, "Your Category : " + snapshot.child(username.getText().toString()).child("category").getValue(), Toast.LENGTH_SHORT).show();
                                    finish();

                                    break;

                                } else {
                                    Log.e("PostSnap" , "HMM");
                                }




                            }


                            final Dialog deleteDialog = new Dialog(MainActivity.this);
                            deleteDialog.setContentView(R.layout.dialog7);
                            deleteDialog.findViewById(R.id.yes).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    deleteDialog.dismiss();
                                    String url = "http://www.walla.co.il";
                                    Intent i = new Intent(Intent.ACTION_VIEW);
                                    i.setData(Uri.parse(url));
                                    startActivity(i);

                                }
                            });
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        deleteDialog.show();
                                    } catch (Exception e){

                                    }
                                }
                            }, 1500);

                            mProgressDialog.dismiss();



                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });

                }



            }
        });

    }
}
