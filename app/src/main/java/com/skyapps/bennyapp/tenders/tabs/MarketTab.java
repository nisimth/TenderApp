package com.skyapps.bennyapp.tenders.tabs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.skyapps.bennyapp.Objects.ItemMarket;
import com.skyapps.bennyapp.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;


public class MarketTab extends Fragment {
    private ListView list;
    private TextView dateStart, timeStart, dateEnd, timeEnd, timer , all_prices;
    private EditText editComments;

    private CustomMarketAdapter customMarketAdapter;
    private ArrayList<ItemMarket> listData;

    private long countFullItems;
    private long counter;

    private long prices = 0;
    private String url = "";

    private Firebase myFirebaseRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_market_tab, container, false);

        list = view.findViewById(R.id.list);
        listData = new ArrayList<ItemMarket>();

        dateStart = view.findViewById(R.id.dateStart);
        timeStart = view.findViewById(R.id.timeStart);
        dateEnd = view.findViewById(R.id.dateEnd);
        timeEnd = view.findViewById(R.id.timeEnd);
        timer = view.findViewById(R.id.timer);
        all_prices = view.findViewById(R.id.all_prices);
        editComments = view.findViewById(R.id.editComments);
        /*ImageView img = view.findViewById(R.id.gotoPrice);
        img.getLayoutParams().height = 20;
        img.getLayoutParams().width = 20;*/



        final ImageView image = view.findViewById(R.id.image);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.dialog5);

                ImageView img = (ImageView) dialog.findViewById(R.id.imageview);
                Glide.with(getContext()).load(url).into(img);


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

        //Log.e("taltestme : " , getContext().getSharedPreferences("BennyApp",Context.MODE_PRIVATE).getString("company",""));


        Firebase.setAndroidContext(getContext());//FireBase , Upload Data from firebase to EditTexts...
        myFirebaseRef = new Firebase("https://tenders-83c71.firebaseio.com/");
        //Log.e("taltest : " , getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getString("category",""));
        //Log.e("taltest : " , getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getString("company",""));
        final Firebase ref = myFirebaseRef.child("Tenders/" + getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getString("category","")).child(getContext().getSharedPreferences("BennyApp",Context.MODE_PRIVATE).getString("company","")).child("מכרז" + getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getInt("num", 0)).child("Items");
        final Firebase ref2 = myFirebaseRef.child("users/" + getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getString("username","")).child(getContext().getSharedPreferences("BennyApp",Context.MODE_PRIVATE).getString("company","")).child("מכרז" + getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getInt("num", 0));
        final Firebase ref4 = myFirebaseRef.child("users/" + getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getString("username","")).child("TenderWin");

        final LinearLayout d = view.findViewById(R.id.market_details);

        try {
            //// check if it Tender win if yes : hides d layout
            if (TabsActivity.finall.equals("Final")) {
                d.setVisibility(View.INVISIBLE);
            }
        } catch (Exception e){

        }

        final LinearLayout layoutadd = view.findViewById(R.id.layoutadd);
        final String username = getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getString("username", "");

        ref4.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int i = 0;
                /*long count = dataSnapshot.getChildrenCount();

                for (int i = 0; i <= count; i++) {
                    Log.e("the children : ", count + ", the name : " + dataSnapshot.child().getKey());

                }*/

                Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();
                int length = (int) dataSnapshot.getChildrenCount();
                String[] sampleString = new String[length];
                while(i < length) {
                    sampleString[i] = iterator.next().getValue().toString();


                    try {
                        if (sampleString[i].equals(getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getString("company", ""))) {
                            Log.e("im here: ", "yes!!!");
                            d.setVisibility(View.INVISIBLE);


                        } else {
                            Log.e("im here: ", "no...");
                            //layoutadd.setVisibility(View.VISIBLE);

                        }
                    } catch (Exception e){

                    }

                    i++;


                }


            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });



        myFirebaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                prices = 0;
                //listData.removeAll();
                listData.clear();


                try {

                    long count = dataSnapshot.child("Tenders").child(getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getString("category", ""))
                            .child(getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getString("company", "")).child("מכרז" + getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getInt("num", 0)).child("Items").getChildrenCount();
                    countFullItems = dataSnapshot.child("Tenders").child(getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getString("category", ""))
                            .child(getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getString("company", "")).child("מכרז" + getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getInt("num", 0)).child("Items").getChildrenCount();


                    url = (String) dataSnapshot.child("Tenders").child(getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getString("category", ""))
                            .child(getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getString("company", "")).child("image").getValue();

                    Glide.with(getContext()).load(url).into(image);



                    for (int i = 1; i <= count; i++){


                        int number = i;
                        String name = (String) dataSnapshot.child("Tenders").child(getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getString("category",""))
                                .child(getContext().getSharedPreferences("BennyApp",Context.MODE_PRIVATE).getString("company","")).child("מכרז" + getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getInt("num", 0)).child("Items").child("item" + i).child("name").getValue();

                        long mount = (long) dataSnapshot.child("Tenders").child(getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getString("category",""))
                                .child(getContext().getSharedPreferences("BennyApp",Context.MODE_PRIVATE).getString("company","")).child("מכרז" + getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getInt("num", 0)).child("Items").child("item" + i).child("mount").getValue();

                        String price = (String) dataSnapshot.child("users").child(getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getString("username",""))
                                .child(getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getString("company","")).child("מכרז" + getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getInt("num", 0)).child("Item" + i).child("Price").getValue();


                        String details =  (String) dataSnapshot.child("Tenders").child(getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getString("category",""))
                                .child(getContext().getSharedPreferences("BennyApp",Context.MODE_PRIVATE).getString("company","")).child("מכרז" + getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getInt("num", 0)).child("Items").child("item" + i).child("details").getValue();

                        long mqt = (long) dataSnapshot.child("Tenders").child(getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getString("category",""))
                                .child(getContext().getSharedPreferences("BennyApp",Context.MODE_PRIVATE).getString("company","")).child("מכרז" + getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getInt("num", 0)).child("Items").child("item" + i).child("mqt").getValue();

                        String size =  (String) dataSnapshot.child("Tenders").child(getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getString("category",""))
                                .child(getContext().getSharedPreferences("BennyApp",Context.MODE_PRIVATE).getString("company","")).child("מכרז" + getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getInt("num", 0)).child("Items").child("item" + i).child("size").getValue();


                        if (price!="" || price!=null || !price.equals(null) || !price.equals("null")){
                            try {
                                listData.add(new ItemMarket(number, name, mount, (Long.parseLong(price) * mount) + "", details, mqt, size));

                                prices = (Long.parseLong(price) * mount) + prices;
                                all_prices.setText("סה''כ עלות תימחור: " + prices + " ₪");
                            } catch (Exception e){
                                listData.add(new ItemMarket(number, name, mount, "", details, mqt, size));

                            }
                        } else {
                            listData.add(new ItemMarket(number , name , mount , "", details , mqt , size));
                        }


                    }
                    customMarketAdapter = new CustomMarketAdapter(listData , getContext());
                    list.setAdapter(customMarketAdapter);

                } catch (Exception e){

                }



            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });



        final Firebase ref3 = myFirebaseRef.child("Tenders").child(getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getString("category","")).child(getContext().getSharedPreferences("BennyApp",Context.MODE_PRIVATE).getString("company","")).child("מכרז" + getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getInt("num", 0)).child("Info");

        ref3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //Log.e("the datasnap : " , dataSnapshot)

                dateStart.setText(dataSnapshot.child("startTender").getValue()+"");
                dateEnd.setText(dataSnapshot.child("endTender").getValue()+"");
                timeStart.setText(dataSnapshot.child("timeStart").getValue()+"");
                timeEnd.setText(dataSnapshot.child("timeEnd").getValue()+"");

                long timerFireBase = calcTimer(dateEnd.getText().toString(),timeEnd.getText().toString());
                //long timerFireBase = 10000000;

                if(calcTimer(dateStart.getText().toString(),timeStart.getText().toString())>=0){
                    timer.setText("טרם התחיל");
                }
                else if(timerFireBase<=0){
                    timer.setText("עבר הזמן");
                }
                else {

                    new CountDownTimer(timerFireBase, 1000) {

                        public void onTick(long millisUntilFinished) {
                            long days = TimeUnit.MILLISECONDS.toDays(millisUntilFinished);
                            millisUntilFinished -= TimeUnit.DAYS.toMillis(days);

                            long hours = TimeUnit.MILLISECONDS.toHours(millisUntilFinished);
                            millisUntilFinished -= TimeUnit.HOURS.toMillis(hours);

                            long minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished);
                            millisUntilFinished -= TimeUnit.MINUTES.toMillis(minutes);

                            long seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished);


                            if (days == 0) {
                                if (hours == 0) {
                                    timer.setText(minutes + ":" + seconds);
                                } else {
                                    timer.setText(hours + ":" + minutes + ":" + seconds);
                                }
                            } else if (hours == 0) {
                                timer.setText(minutes + ":" + seconds);
                            } else {
                                timer.setText(days + " ימים , " + hours + ":" + minutes + ":" + seconds);
                            }

                        }

                        public void onFinish() {
                            timer.setText("עבר הזמן");
                        }

                    }.start();
                }


            }
            private Long calcTimer(String endDate, String endTime)  {
                String time = endDate + " " + endTime;
                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm");

                Date d = null;
                Date currentDate = Calendar.getInstance().getTime();
                Long diff = null;
                try {
                    d = df.parse(time);
                    diff = d.getTime() - currentDate.getTime();
                } catch (ParseException e) {
                    e.printStackTrace();
                }




                return diff;
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        myFirebaseRef.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    try {
                        if (dataSnapshot.child(getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getString("username", ""))
                                .child(getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getString("company", ""))
                                .child("מכרז" + getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getInt("num", 0))
                                .child(getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getString("company", "") + "Comments").getValue() != null) {

                            editComments.setText(dataSnapshot.child(getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getString("username", ""))
                                    .child(getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getString("company", ""))
                                    .child("מכרז" + getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getInt("num", 0))
                                    .child(getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getString("company", "") + "Comments").getValue() + "");

                        } else {

                            myFirebaseRef.child("users").child(getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getString("username", ""))
                                    .child(getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getString("company", ""))
                                    .child("מכרז" + getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getInt("num", 0))
                                    .child(getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getString("company", "") + "Comments").setValue(editComments.getText().toString());

                        }

                        if (dataSnapshot.child(getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getString("username", ""))
                                .child(getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getString("company", ""))
                                .child("מכרז" + getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getInt("num", 0))
                                .child("Pdf").getValue() != null) {


                        } else {

                            myFirebaseRef.child("users").child(getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getString("username", ""))
                                    .child(getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getString("company", ""))
                                    .child("מכרז" + getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getInt("num", 0))
                                    .child("Pdf").setValue("empty");


                        }


                        if (dataSnapshot.child(getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getString("username", ""))
                                .child(getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getString("company", ""))
                                .child("מכרז" + getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getInt("num", 0))
                                .child("Image").getValue() != null) {


                        } else {



                            myFirebaseRef.child("users").child(getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getString("username", ""))
                                    .child(getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getString("company", ""))
                                    .child("מכרז" + getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getInt("num", 0))
                                    .child("Image").setValue("empty");


                        }

                    } catch (Exception e){
                        Log.e("taltestme" , e.toString());

                    }

                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        final View btntyoye = view.findViewById(R.id.tyotaTender);
        view.findViewById(R.id.tyotaTender).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e("testing : " , "מכרז" +  + getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getInt("num", 0));
                myFirebaseRef.child("users").child(getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getString("username", ""))
                        .child("Tyotot").child(getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getString("company", ""))
                        .child("מכרז" + getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getInt("num", 0)).setValue("tyota");

                myFirebaseRef.child("users").child(getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getString("username", ""))
                        .child(getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getString("company", ""))
                        .child("מכרז" + getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getInt("num", 0))
                        .child(getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getString("company", "") + "Comments").setValue(editComments.getText().toString());

                myFirebaseRef.child("users").child(getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getString("username", ""))
                        .child("Tenders").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int i = 0;

                        Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();
                        int length = (int) dataSnapshot.getChildrenCount();
                        String[] sampleString = new String[length];
                        while(i < length) {
                            sampleString[i] = iterator.next().getValue().toString();


                            try {
                                if (sampleString[i].equals(getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getString("company", ""))) {
                                    Log.e("im here: ", "yes!!!");

                                    myFirebaseRef.child("users").child(getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getString("username", ""))
                                            .child("Tyotot").child(sampleString[i]).removeValue();

                                    //btntyoye.setVisibility(View.INVISIBLE);

                                } else {
                                    Log.e("im here: ", "no...");

                                }
                            } catch (Exception e){

                            }

                            i++;


                        }


                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });

                Toast.makeText(getContext(), "ההצעה נשמרה ברשימת הטיוטות שלך!", Toast.LENGTH_SHORT).show();

            }
        });

        Firebase refcounter = new Firebase("https://tenders-83c71.firebaseio.com/users/" +
                getContext().getSharedPreferences("BennyApp" , Context.MODE_PRIVATE).getString("username","")
                + "/" + getContext().getSharedPreferences("BennyApp" , Context.MODE_PRIVATE).getString("company","")
                + "/" + "מכרז" + getContext().getSharedPreferences("BennyApp" , Context.MODE_PRIVATE).getInt("num",0));


        refcounter.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                counter = dataSnapshot.getChildrenCount() - 3; // TODO -3
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


        view.findViewById(R.id.addTender).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if (counter!=countFullItems){
                    Log.e("trying the sp : " , "מכרז"+getContext().getSharedPreferences("BennyApp" , Context.MODE_PRIVATE).getInt("num",0)+"");
                    Log.e("trying countFullItems :" , countFullItems+"");
                    Log.e("trying counter : " , counter+"");

                    Toast.makeText(getContext(), "אנא תמחר את כל הפריטים קודם לכן", Toast.LENGTH_SHORT).show();

                } else {
                    myFirebaseRef.child("users").child(getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getString("username", ""))
                            .child(getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getString("company", ""))
                            .child("מכרז" + getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getInt("num", 0))
                            .child(getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getString("company", "") + "Comments").setValue(editComments.getText().toString());

                    myFirebaseRef.child("users").child(getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getString("username", ""))
                            .child("Tenders").child(getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getString("company", ""))
                            .child("מכרז" + getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getInt("num", 0)).setValue("sended");

                    myFirebaseRef.child("users").child(getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getString("username", ""))
                            .child("Tyotot").child(getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getString("company", ""))
                            .child("מכרז" + getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getInt("num", 0)).removeValue();

                    Toast.makeText(getContext(), "ההצעה הוגשה לחברה!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        return view;
    }


}
