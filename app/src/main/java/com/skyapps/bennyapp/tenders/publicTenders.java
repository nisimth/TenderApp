package com.skyapps.bennyapp.tenders;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.skyapps.bennyapp.Objects.Tender;
import com.skyapps.bennyapp.R;

import java.util.ArrayList;


public class publicTenders extends Fragment {
    private ArrayList<Tender> tenderArrayList;
    private ProgressDialog mProgressDialog;
    private Activity activity;

    private TextView tenderCounter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_public_tenders, container, false);;

        final ListView list = view.findViewById(R.id.publicList);
        tenderArrayList= new ArrayList<>();
        tenderCounter = (TextView) view.findViewById(R.id.counter_tender);

        Firebase.setAndroidContext(getContext());
        final Firebase myFirebaseRef = new Firebase("https://tenders-83c71.firebaseio.com/TendersPublic/" + getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getString("category",""));
        mProgressDialog = new ProgressDialog(getContext());
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage("אנא המתן...");
        mProgressDialog.show();

        myFirebaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                int i = 0;
                tenderArrayList.clear();

                for(DataSnapshot postSnapshot : snapshot.getChildren()){

                    for (int j = 0 ; j < snapshot.child(postSnapshot.getKey()).getChildrenCount() ; j++) {

                        Log.e("publictal", postSnapshot.child("מכרז" + (j + 1)).child("name").getValue() + "");

                        tenderArrayList.add(new Tender(snapshot.child(postSnapshot.getKey()).child("מכרז" + (j + 1)).child("mqt").getValue() + "",
                                postSnapshot.getKey(),
                                postSnapshot.child("מכרז" + (j + 1)).child("name").getValue() + "",
                                snapshot.child(postSnapshot.getKey()).child("מכרז" + (j + 1)).child("startDate").getValue() + "",
                                snapshot.child(postSnapshot.getKey()).child("מכרז" + (j + 1)).child("endDate").getValue() + "",
                                snapshot.child(postSnapshot.getKey()).child("מכרז" + (j + 1)).child("startHour").getValue() + "",
                                snapshot.child(postSnapshot.getKey()).child("מכרז" + (j + 1)).child("endHour").getValue() + ""
                                , (j+1)));


                    }



                    i++;
                }

                CustomAdapter adapter = new CustomAdapter(tenderArrayList, getContext());
                list.setAdapter(adapter);
                tenderCounter.setText("(" +tenderArrayList.size()+ ")");
                mProgressDialog.dismiss();

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


        final Firebase myf = new Firebase("https://tenders-83c71.firebaseio.com/");

        final Firebase ref = myf.child("users/" + getContext().getSharedPreferences("BennyApp",Context.MODE_PRIVATE).getString("username",""));

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for(DataSnapshot postSnapshot : snapshot.getChildren()){

                    try {
                        if (snapshot.child("premium").getValue() != null) {
                            Log.e("premium is: ", "premium");
                            getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).edit().putString("premium", "premium").commit();
                        } else {
                            Log.e("premium is: ", "null");
                            getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).edit().putString("premium", "").commit();
                        }
                    } catch (Exception e){
                        //Toast.makeText(getContext(), "ישנה שגיאה במערכת, נסה לשמור שנית בבקשה.", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {



                //Primium
                if (getContext().getSharedPreferences("BennyApp" , Context.MODE_PRIVATE).getString("premium" , "").equals("premium")){
                    Tender t = (Tender) parent.getItemAtPosition(position);
                    Intent i = new Intent(getContext(),DetailsPublic.class);
                    i.putExtra("name" , t.getName());
                    i.putExtra("tender" , "מכרז" + t.getNum());
                    startActivity(i);
                } else {
                    final Dialog dialog = new Dialog(getContext());
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCancelable(false);
                    dialog.setContentView(R.layout.dialog7);

                    TextView text = (TextView) dialog.findViewById(R.id.txt_dia);
                    text.setText("אינך רשאי להיכנס לאיזור זה, מפני שאינך ספק פרימיום.");

                    Button dialogButton = (Button) dialog.findViewById(R.id.yes);
                    dialogButton.setText("חזור");
                    dialogButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    dialog.show();
                }


            }
        });

        return view;
    }

}
