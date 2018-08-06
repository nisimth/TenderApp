package com.skyapps.bennyapp.tenders.tabs;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.skyapps.bennyapp.R;
import com.skyapps.bennyapp.tenders.tabs.MarketTab;

public class PriceItem extends AppCompatActivity {
    EditText editPrice , editSum , editPriceUser, editComments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price_item);

        final int num = getIntent().getIntExtra("num" , 0);
        ((TextView)findViewById(R.id.title)).setText("הצעת מחיר - פריט מס' " + num);


        editPrice = findViewById(R.id.editPrice);
        editSum = findViewById(R.id.editSum);
        editPriceUser = findViewById(R.id.editPriceUser);
        editComments = findViewById(R.id.editComments);

        final String comments = getIntent().getStringExtra("comments");
        editPriceUser.setText(getIntent().getStringExtra("priceUser"));


        final int mount = getIntent().getIntExtra("mount",0);

        findViewById(R.id.cal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    int price = Integer.parseInt(editPrice.getText().toString());
                    editSum.setText("" + (mount * price));
                } catch (Exception e){
                    Toast.makeText(PriceItem.this, "אנא הזן מחיר תחילה", Toast.LENGTH_SHORT).show();
                }
            }
        });


        Firebase.setAndroidContext(this);
        final Firebase myFirebaseRef = new Firebase("https://tenders-83c71.firebaseio.com/users/" + getSharedPreferences("BennyApp" , MODE_PRIVATE).getString("username","")
        + "/" + getSharedPreferences("BennyApp" , MODE_PRIVATE).getString("company","") + "/מכרז"  + getSharedPreferences("BennyApp", MODE_PRIVATE).getInt("num", 0) + "/Item" + num );

        myFirebaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.child("Comments2").getValue()!=null)
                    editComments.setText(dataSnapshot.child("Comments2").getValue()+"");

                if (dataSnapshot.child("Price").getValue()!=null)
                    editPrice.setText(dataSnapshot.child("Price").getValue()+"");

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                //MarketTab.counter++;

                if (editPrice.getText().toString().equals("") || editPrice.getText().toString().equals("0")){
                    Toast.makeText(PriceItem.this, "אנא הזן מחיר תקין", Toast.LENGTH_SHORT).show();
                } else {
                    myFirebaseRef.child("Comments1").setValue(comments);
                    myFirebaseRef.child("Comments2").setValue(editComments.getText().toString());
                    myFirebaseRef.child("Price").setValue(editPrice.getText().toString());
                    myFirebaseRef.child("Mount").setValue(mount);

                    Toast.makeText(PriceItem.this, "הפרטים נשמרו!", Toast.LENGTH_SHORT).show();
                    getSharedPreferences("BennyApp" , MODE_PRIVATE).edit().putString("PriceItem", "?").commit();
                    finish();

                }

            }
        });


    }

}
