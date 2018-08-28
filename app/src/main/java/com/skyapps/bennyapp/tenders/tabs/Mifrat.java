package com.skyapps.bennyapp.tenders.tabs;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.skyapps.bennyapp.R;

public class Mifrat extends AppCompatActivity {
    private EditText editMqt , editName ,editDetails , editMount , editSize , editPrice;
    private ImageView imageView;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mifrat);
        //// gets items details from customMarketAdapter Intent
        final int num = getIntent().getIntExtra("num" , 0);
        final long price = getIntent().getLongExtra("price" , 0);
        ((TextView)findViewById(R.id.title)).setText("מפרט - פריט מס' " + num );

        final String name = getIntent().getStringExtra("name");

        editName = findViewById(R.id.editName);
        editName.setText(name);

        editMqt = findViewById(R.id.editMqt);
        editDetails = findViewById(R.id.editDetails);
        editMount = findViewById(R.id.editMount);
        editSize = findViewById(R.id.editSize);
        editPrice = findViewById(R.id.editPrice);

        imageView = findViewById(R.id.image);

        editMqt.setText(getIntent().getLongExtra("mqt" , 0)+"");
        editDetails.setText(getIntent().getStringExtra("details"));
        editMount.setText(getIntent().getLongExtra("mount" , 0)+"");
        editSize.setText(getIntent().getStringExtra("size"));

        Firebase.setAndroidContext(this);
        final Firebase myFirebaseRef = new Firebase("https://tenders-83c71.firebaseio.com/Tenders/" + getSharedPreferences("BennyApp" , MODE_PRIVATE).getString("category","")
                + "/" + getSharedPreferences("BennyApp" , MODE_PRIVATE).getString("company","") + "/" + "מכרז" + getSharedPreferences("BennyApp", MODE_PRIVATE).getInt("num", 0) + "/Items/item" + num );

        Log.e("test price1 : " , getSharedPreferences("BennyApp" , MODE_PRIVATE).getString("category",""));
        Log.e("test price2 : " , getSharedPreferences("BennyApp" , MODE_PRIVATE).getString("company",""));



        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(Mifrat.this);
                dialog.setContentView(R.layout.dialog5);

                ImageView img = (ImageView) dialog.findViewById(R.id.imageview);
                Glide.with(Mifrat.this).load(url).into(img);


                Button btn = (Button) dialog.findViewById(R.id.btn);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //getActivity().finish();
                        dialog.dismiss();

                    }
                });

                dialog.show();

            }
        });


        myFirebaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                url = (String) dataSnapshot.child("image").getValue();

                Log.e("test price : " , dataSnapshot.child("price").getValue()+"");

                Glide.with(Mifrat.this).load(url).into(imageView);

                if (dataSnapshot.child("price").getValue()!=null)
                    editPrice.setText(dataSnapshot.child("price").getValue()+"");


            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });



        /// pass data onclick priceitemsBtn to PriceItem
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Intent i = new Intent(Mifrat.this , PriceItem.class);
                    i.putExtra("num" , num);
                    i.putExtra("price" , price);
                    i.putExtra("priceUser" , editPrice.getText().toString());
                    i.putExtra("mount" , Integer.parseInt(editMount.getText().toString()));
                    startActivity(i);

               
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (getSharedPreferences("BennyApp" , MODE_PRIVATE).getString("PriceItem", "").equals("?")){
            finish();
            getSharedPreferences("BennyApp" , MODE_PRIVATE).edit().putString("PriceItem", "").commit();

        }

    }
}
