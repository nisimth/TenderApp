package com.skyapps.bennyapp.tenders;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.skyapps.bennyapp.MainActivity;
import com.skyapps.bennyapp.Objects.Tender;
import com.skyapps.bennyapp.R;

import java.util.ArrayList;


public class publicTenders extends Fragment {
    private ArrayList<Tender> tenderArrayList;
    private ProgressDialog mProgressDialog;
    private Activity activity;
    CustomAdapter adapter;

    private TextView tenderCounter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_public_tenders, container, false);;

        final ListView list = view.findViewById(R.id.publicList);
        tenderArrayList= new ArrayList<>();
        tenderCounter = (TextView) view.findViewById(R.id.counter_tender);
        final LinearLayout searchLayout = view.findViewById(R.id.searchpublic);

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

                adapter = new CustomAdapter(tenderArrayList, getContext());
                list.setAdapter(adapter);
                tenderCounter.setText("(" +tenderArrayList.size()+ ")");
                mProgressDialog.dismiss();

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        /////////////// pirsomet /////////////////////

        if(getContext().getSharedPreferences("BennyApp" , Context.MODE_PRIVATE).getString("premium" , "").equals("premium")){

        }else{
            final Dialog pirsometDialog = new Dialog(getContext());
            pirsometDialog.setContentView(R.layout.persomet_dialog);
            pirsometDialog.findViewById(R.id.close_pirsomet).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pirsometDialog.dismiss();
                }
            });
            pirsometDialog.findViewById(R.id.pirsomet_image).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = "http://www.wizbiz.co.il";
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
                        pirsometDialog.show();
                    } catch (Exception e){

                    }
                }
            }, 2800);

            mProgressDialog.dismiss();

        }

        //////////////////////////////////////////////



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
                            searchLayout.setVisibility(View.INVISIBLE);
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

        final android.widget.SearchView search = (android.widget.SearchView) view.findViewById(R.id.bynamepublic);

        search.setQueryHint("שם חברה");
        search.setFocusable(false);
        search.setIconified(false);
        search.clearFocus();
        search.requestFocusFromTouch();

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {
                tenderArrayList.clear();
                if (newText.equals("")){
                    myFirebaseRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            int i = 0;
                            tenderArrayList.clear();

                            for(DataSnapshot postSnapshot : snapshot.getChildren()){

                                for (int j = 0 ; j < snapshot.child(postSnapshot.getKey()).getChildrenCount() ; j++) {


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

                            adapter = new CustomAdapter(tenderArrayList, getContext());
                            list.setAdapter(adapter);
                            tenderCounter.setText("(" +tenderArrayList.size()+ ")");
                            mProgressDialog.dismiss();
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });



                }
                myFirebaseRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (newText.equals("")) {
                            tenderArrayList.clear();
                        }
                        ProgressDialog progress = new ProgressDialog(getContext());
                        progress.setMessage("אנא המתן ...");
                        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progress.setIndeterminate(true);
                        progress.setProgress(0);
                        progress.setMax(3000);
                        progress.show();

                        int i = 0;
                        for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                            Log.e("PostSnap: " + newText, postSnapshot.getKey() + "");
                            //Log.e("PostSnap: " + newText.toLowerCase(), postSnapshot.getKey() + "");


                            try {
                                String str = postSnapshot.getKey();
                                for (int j = 0; j < str.length(); j++) {
                                    for (int k = 0; k <= str.length(); k++) {

                                        if (str.substring(j, k).equals(newText) || str.substring(j, k).toLowerCase().equals(newText)) {

                                            for (int num=1; num<=snapshot.child(postSnapshot.getKey()).getChildrenCount(); num++) {
                                                Log.e("bdika : " , postSnapshot.getKey()+" , " + snapshot.child(postSnapshot.getKey()).child("מכרז"+num).child("name").getValue());



                                                tenderArrayList.add(new Tender(snapshot.child(postSnapshot.getKey()).child("מכרז" + (j + 1)).child("mqt").getValue() + "",
                                                        postSnapshot.getKey(),
                                                        postSnapshot.child("מכרז" + (j + 1)).child("name").getValue() + "",
                                                        snapshot.child(postSnapshot.getKey()).child("מכרז" + (j + 1)).child("startDate").getValue() + "",
                                                        snapshot.child(postSnapshot.getKey()).child("מכרז" + (j + 1)).child("endDate").getValue() + "",
                                                        snapshot.child(postSnapshot.getKey()).child("מכרז" + (j + 1)).child("startHour").getValue() + "",
                                                        snapshot.child(postSnapshot.getKey()).child("מכרז" + (j + 1)).child("endHour").getValue() + ""
                                                        , (j+1)));

                                                i++;
                                            }

                                        }
                                    }
                                }
                            } catch (Exception e) {

                            }

                            list.setAdapter(adapter);

                        }
                        tenderCounter.setText("(" +tenderArrayList.size()+ ")");
                        progress.dismiss();
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });
                return false;
            }
        });



        return view;
    }

}
