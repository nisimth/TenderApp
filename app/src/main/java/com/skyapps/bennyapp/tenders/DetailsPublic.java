package com.skyapps.bennyapp.tenders;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.skyapps.bennyapp.R;

public class DetailsPublic extends AppCompatActivity {
    private TextView title;
    private String site;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_public);

        title = (TextView) findViewById(R.id.title);


        Firebase.setAndroidContext(this);

        /*InputMethodManager inputManager =
                (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(
                getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);*/

        final Firebase myFirebaseRef = new Firebase("https://tenders-83c71.firebaseio.com/TendersPublic/" +
                getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getString("category","")  + "/" + getIntent().getStringExtra("name")
        + "/" + getIntent().getStringExtra("tender"));

        myFirebaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ((EditText) findViewById(R.id.editMqt)).setText(dataSnapshot.child("mqt").getValue()+"");
                ((EditText) findViewById(R.id.editName)).setText(getIntent().getStringExtra("name")+"");
                ((EditText) findViewById(R.id.editNameProject)).setText(dataSnapshot.child("name").getValue()+"");
                ((EditText) findViewById(R.id.editAddress)).setText(dataSnapshot.child("address").getValue()+"");
                ((EditText) findViewById(R.id.editContact)).setText(dataSnapshot.child("contact").getValue()+"");
                ((EditText) findViewById(R.id.editPhone)).setText(dataSnapshot.child("phone").getValue()+"");
                ((EditText) findViewById(R.id.editEmail)).setText(dataSnapshot.child("email").getValue()+"");
                ((EditText) findViewById(R.id.editPrice)).setText(dataSnapshot.child("filesprice").getValue()+" ₪");
                ((EditText) findViewById(R.id.editDetails)).setText(dataSnapshot.child("details").getValue()+"");

                ((EditText) findViewById(R.id.editStartDate)).setText(dataSnapshot.child("startDate").getValue()+"");
                ((EditText) findViewById(R.id.editEndDate)).setText(dataSnapshot.child("endDateSelcted").getValue()+"");
                ((EditText) findViewById(R.id.editStartTime)).setText(dataSnapshot.child("startHour").getValue()+"");
                ((EditText) findViewById(R.id.editEndTime)).setText(dataSnapshot.child("endHour").getValue()+"");

                site = dataSnapshot.child("site").getValue()+"";

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });



        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(DetailsPublic.this, "הצעתך התקבלה!", Toast.LENGTH_SHORT).show();

                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("http://" + site));
                startActivity(i);

            }
        });

    }
}
