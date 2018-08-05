package com.skyapps.bennyapp.tenders;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.skyapps.bennyapp.Objects.Item;
import com.skyapps.bennyapp.Objects.Tender;
import com.skyapps.bennyapp.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class privateTenders extends Fragment {
    ////////////new/////////////

    private int tenderCounter = 0;
    private TextView counter;
    ////////////////////////////


    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<Tender> listDataHeader;
    HashMap<Tender, List<Item>> listDataChild;
    private ProgressDialog mProgressDialog;

    int lastPosition = -1;


    ImageButton filterStartDate;
    ImageButton filterEndDate;
    Button searchBtn;
    Button resetSearchBtn;

    CheckBox lowCheckBox;
    CheckBox highCheckBox;

    long startDateSelcted, endDateSelcted;

    private DatePickerDialog.OnDateSetListener start_dateListener, end_dateListener;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_private_tenders, container, false);

        counter = (TextView) view.findViewById(R.id.counter_tender) ;

        expListView = view.findViewById(R.id.privateList);

        filterStartDate = (ImageButton) view.findViewById(R.id.filter_start_date_btn) ;
        filterEndDate = (ImageButton) view.findViewById(R.id.filter_end_date_btn);
        searchBtn = (Button) view.findViewById(R.id.search_btn);
        resetSearchBtn = (Button) view.findViewById(R.id.reset_filter_btn);

        lowCheckBox = (CheckBox) view.findViewById(R.id.checkBox_dhifot_low);
        highCheckBox = (CheckBox) view.findViewById(R.id.checkBox_dhifot_high);


        filterStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(getContext(), start_dateListener,year,month,day);
                dialog.show();
            }
        });

        start_dateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                String dayString = String.valueOf(day);
                String monthString = String.valueOf(month+1);

                if(dayString.length() == 1){
                    dayString = "0" + dayString;
                }
                if(monthString.length() == 1){
                    monthString = "0" + monthString;
                }

                String date =  dayString + "/" + monthString + "/" + year;
                startDateSelcted = convertStringToDate(date);
                Log.e("start_selcted_date",date);
            }
        };

        filterEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*android.support.v4.app.DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getFragmentManager(),"date picker");*/

                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(getContext(), end_dateListener,year,month, day);
                dialog.show();


            }
        });

        end_dateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                String dayString = String.valueOf(day);
                String monthString = String.valueOf(month+1);

                if(dayString.length() == 1){
                    dayString = "0" + dayString;
                }
                if(monthString.length() == 1){
                    monthString = "0" + monthString;
                }

                String date =  dayString + "/" + monthString + "/" + year;
                endDateSelcted = convertStringToDate(date);
                Log.e("end_selcted_date",date);
            }
        };
        listDataHeader = new ArrayList<Tender>();
        listDataChild = new HashMap<Tender, List<Item>>();

        Firebase.setAndroidContext(getContext());
        final Firebase myFirebaseRef = new Firebase("https://tenders-83c71.firebaseio.com/Tenders/" + getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getString("category","") + "/");
        final Firebase userFirebise = new Firebase("https://tenders-83c71.firebaseio.com/users/" + getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getString("username","") + "/TenderWin");
        mProgressDialog = new ProgressDialog(getContext());
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage("אנא המתן...");
        mProgressDialog.show();



        // load all tenders
        myFirebaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                int i = 0;
                listDataHeader.clear();
                listDataChild.clear();


                for (final DataSnapshot postSnapshot : snapshot.getChildren()) {

                    for (int num=1; num<=snapshot.child(postSnapshot.getKey()).getChildrenCount(); num++) {
                        tenderCounter ++;
                        listDataHeader.add(new Tender(snapshot.child(postSnapshot.getKey()).child("מכרז"+num).child("mqt").getValue() + "", postSnapshot.getKey(),
                            snapshot.child(postSnapshot.getKey()).child("מכרז"+num).child("name").getValue() + "",
                            snapshot.child(postSnapshot.getKey()).child("מכרז"+num).child("Info").child("startTender").getValue() + "",
                            snapshot.child(postSnapshot.getKey()).child("מכרז"+num).child("Info").child("endTender").getValue() + "",
                            snapshot.child(postSnapshot.getKey()).child("מכרז"+num).child("Info").child("timeStart").getValue() + "",
                            snapshot.child(postSnapshot.getKey()).child("מכרז"+num).child("Info").child("timeEnd").getValue() + ""




                    ));


                        List<Item> list = new ArrayList<Item>();
                    list.add(new Item(postSnapshot.getKey() + "",
                            snapshot.child(postSnapshot.getKey()).child("מכרז"+num).child("contact").getValue() + "",
                            snapshot.child(postSnapshot.getKey()).child("מכרז"+num).child("phone").getValue() + "",
                            snapshot.child(postSnapshot.getKey()).child("מכרז"+num).child("mail").getValue() + "",
                            num));

                        listDataChild.put(listDataHeader.get(i), list); // Header, Child data*/


                        i++;
                        //ExpandableListAdapter.countAll = i;

                    }

                }

                listAdapter = new ExpandableListAdapter(getContext(), listDataHeader, listDataChild);
                expListView.setAdapter(listAdapter);

                counter.setText("(" +listDataHeader.size()+ ")");
                mProgressDialog.dismiss();

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });



        lowCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

            if(lowCheckBox.isChecked()) {
                highCheckBox.setChecked(false);
                myFirebaseRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        int i = 0;
                        listDataHeader.clear();
                        listDataChild.clear();


                        for (final DataSnapshot postSnapshot : snapshot.getChildren()) {

                            for (int num = 1; num <= snapshot.child(postSnapshot.getKey()).getChildrenCount(); num++) {
                                if (snapshot.child(postSnapshot.getKey()).child("מכרז" + num).child("dhifot").getValue().equals("נמוכה") ) {
                                    listDataHeader.add(new Tender(snapshot.child(postSnapshot.getKey()).child("מכרז" + num).child("mqt").getValue() + "", postSnapshot.getKey(),
                                            snapshot.child(postSnapshot.getKey()).child("מכרז" + num).child("name").getValue() + "",
                                            snapshot.child(postSnapshot.getKey()).child("מכרז" + num).child("Info").child("startTender").getValue() + "",
                                            snapshot.child(postSnapshot.getKey()).child("מכרז" + num).child("Info").child("endTender").getValue() + "",
                                            snapshot.child(postSnapshot.getKey()).child("מכרז" + num).child("Info").child("timeStart").getValue() + "",
                                            snapshot.child(postSnapshot.getKey()).child("מכרז" + num).child("Info").child("timeEnd").getValue() + ""


                                    ));


                                    List<Item> list = new ArrayList<Item>();
                                    list.add(new Item(postSnapshot.getKey() + "",
                                            snapshot.child(postSnapshot.getKey()).child("מכרז" + num).child("contact").getValue() + "",
                                            snapshot.child(postSnapshot.getKey()).child("מכרז" + num).child("phone").getValue() + "",
                                            snapshot.child(postSnapshot.getKey()).child("מכרז" + num).child("mail").getValue() + "",
                                            num));
                                    listDataChild.put(listDataHeader.get(i), list); // Header, Child data*/
                                    i++;
                                    //ExpandableListAdapter.countAll = i;
                                }
                            }

                        }

                        listAdapter = new ExpandableListAdapter(getContext(), listDataHeader, listDataChild);
                        expListView.setAdapter(listAdapter);

                        counter.setText(" ( " +listDataHeader.size()+ " ) ");
                        mProgressDialog.dismiss();

                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });

            }
            }
        });

        highCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

            if(highCheckBox.isChecked()){
                lowCheckBox.setChecked(false);
                myFirebaseRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    int i = 0;
                    listDataHeader.clear();
                    listDataChild.clear();


                    for (final DataSnapshot postSnapshot : snapshot.getChildren()) {

                        for (int num=1; num<=snapshot.child(postSnapshot.getKey()).getChildrenCount(); num++) {
                            if (snapshot.child(postSnapshot.getKey()).child("מכרז" + num).child("dhifot").getValue().equals("גבוהה")) {
                                listDataHeader.add(new Tender(snapshot.child(postSnapshot.getKey()).child("מכרז" + num).child("mqt").getValue() + "", postSnapshot.getKey(),
                                        snapshot.child(postSnapshot.getKey()).child("מכרז" + num).child("name").getValue() + "",
                                        snapshot.child(postSnapshot.getKey()).child("מכרז" + num).child("Info").child("startTender").getValue() + "",
                                        snapshot.child(postSnapshot.getKey()).child("מכרז" + num).child("Info").child("endTender").getValue() + "",
                                        snapshot.child(postSnapshot.getKey()).child("מכרז" + num).child("Info").child("timeStart").getValue() + "",
                                        snapshot.child(postSnapshot.getKey()).child("מכרז" + num).child("Info").child("timeEnd").getValue() + ""


                                ));


                                List<Item> list = new ArrayList<Item>();
                                list.add(new Item(postSnapshot.getKey() + "",
                                        snapshot.child(postSnapshot.getKey()).child("מכרז" + num).child("contact").getValue() + "",
                                        snapshot.child(postSnapshot.getKey()).child("מכרז" + num).child("phone").getValue() + "",
                                        snapshot.child(postSnapshot.getKey()).child("מכרז" + num).child("mail").getValue() + "",
                                        num));
                                listDataChild.put(listDataHeader.get(i), list); // Header, Child data*/
                                i++;
                                //ExpandableListAdapter.countAll = i;

                            }
                        }
                    }

                    listAdapter = new ExpandableListAdapter(getContext(), listDataHeader, listDataChild);
                    expListView.setAdapter(listAdapter);

                    counter.setText(" ( " +listDataHeader.size()+ " ) ");
                    mProgressDialog.dismiss();

                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
            }
            }
        });






        // reset filter
        resetSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myFirebaseRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        int i = 0;
                        listDataHeader.clear();
                        listDataChild.clear();

                        startDateSelcted = 0;
                        endDateSelcted = 0;

                        lowCheckBox.setChecked(false);
                        highCheckBox.setChecked(false);


                        for (final DataSnapshot postSnapshot : snapshot.getChildren()) {

                            for (int num=1; num<=snapshot.child(postSnapshot.getKey()).getChildrenCount(); num++) {

                                listDataHeader.add(new Tender(snapshot.child(postSnapshot.getKey()).child("מכרז"+num).child("mqt").getValue() + "", postSnapshot.getKey(),
                                        snapshot.child(postSnapshot.getKey()).child("מכרז"+num).child("name").getValue() + "",
                                        snapshot.child(postSnapshot.getKey()).child("מכרז"+num).child("Info").child("startTender").getValue() + "",
                                        snapshot.child(postSnapshot.getKey()).child("מכרז"+num).child("Info").child("endTender").getValue() + "",
                                        snapshot.child(postSnapshot.getKey()).child("מכרז"+num).child("Info").child("timeStart").getValue() + "",
                                        snapshot.child(postSnapshot.getKey()).child("מכרז"+num).child("Info").child("timeEnd").getValue() + ""


                                ));


                                List<Item> list = new ArrayList<Item>();
                                list.add(new Item(postSnapshot.getKey() + "",
                                        snapshot.child(postSnapshot.getKey()).child("מכרז"+num).child("contact").getValue() + "",
                                        snapshot.child(postSnapshot.getKey()).child("מכרז"+num).child("phone").getValue() + "",
                                        snapshot.child(postSnapshot.getKey()).child("מכרז"+num).child("mail").getValue() + "",
                                        num));
                                listDataChild.put(listDataHeader.get(i), list); // Header, Child data*/
                                i++;
                                //ExpandableListAdapter.countAll = i;

                            }

                        }

                        listAdapter = new ExpandableListAdapter(getContext(), listDataHeader, listDataChild);
                        expListView.setAdapter(listAdapter);

                        counter.setText(" ( " +listDataHeader.size()+ " ) ");
                        mProgressDialog.dismiss();

                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }

                });
            }
        });


        // search tenders by start and end date selected from the date picker dialogs
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                myFirebaseRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        int i = 0;
                        listDataHeader.clear();
                        listDataChild.clear();

                        for (final DataSnapshot postSnapshot : snapshot.getChildren()) {


                            for (int num=1; num<=snapshot.child(postSnapshot.getKey()).getChildrenCount(); num++) {
                                Long startCurrent = convertStringToDate(snapshot.child(postSnapshot.getKey()).child("מכרז"+num).child("Info").child("startTender").getValue() + "");
                                Long endCurrent = convertStringToDate(snapshot.child(postSnapshot.getKey()).child("מכרז"+num).child("Info").child("endTender").getValue() + "");

                                Date currentTime  = Calendar.getInstance().getTime();
                                /*Log.e("start_current_date",snapshot.child(postSnapshot.getKey()).child("מכרז"+num).child("Info").child("startTender").getValue() + "");
                                Log.e("start_current",startCurrent+"" );
                                Log.e("start_current_d",new Date(startCurrent)+"");
                                Log.e("start_selcted_date", new Date(startDateSelcted)+"");
                                Log.e("start_selcted",startDateSelcted+"");*/

                                if((startDateSelcted - currentTime.getTime() <= startCurrent - currentTime.getTime()) && endDateSelcted - currentTime.getTime() >= endCurrent - currentTime.getTime()
                                    || (startDateSelcted == 0 && endDateSelcted- currentTime.getTime() >= endCurrent- currentTime.getTime())
                                        || (startDateSelcted- currentTime.getTime() <= startCurrent- currentTime.getTime() && endDateSelcted==0)) {

                                    listDataHeader.add(new Tender(snapshot.child(postSnapshot.getKey()).child("מכרז" + num).child("mqt").getValue() + "", postSnapshot.getKey(),
                                            snapshot.child(postSnapshot.getKey()).child("מכרז" + num).child("name").getValue() + "",
                                            snapshot.child(postSnapshot.getKey()).child("מכרז" + num).child("Info").child("startTender").getValue() + "",
                                            snapshot.child(postSnapshot.getKey()).child("מכרז" + num).child("Info").child("endTender").getValue() + "",
                                            snapshot.child(postSnapshot.getKey()).child("מכרז" + num).child("Info").child("timeStart").getValue() + "",
                                            snapshot.child(postSnapshot.getKey()).child("מכרז" + num).child("Info").child("timeEnd").getValue() + ""


                                    ));


                                    List<Item> list = new ArrayList<Item>();
                                    list.add(new Item(postSnapshot.getKey() + "",
                                            snapshot.child(postSnapshot.getKey()).child("מכרז" + num).child("contact").getValue() + "",
                                            snapshot.child(postSnapshot.getKey()).child("מכרז" + num).child("phone").getValue() + "",
                                            snapshot.child(postSnapshot.getKey()).child("מכרז" + num).child("mail").getValue() + "",
                                            num));
                                    listDataChild.put(listDataHeader.get(i), list); // Header, Child data*/
                                    i++;
                                    //ExpandableListAdapter.countAll = i;
                                }
                            }

                        }

                        listAdapter = new ExpandableListAdapter(getContext(), listDataHeader, listDataChild);
                        expListView.setAdapter(listAdapter);

                        counter.setText(" ( " +listDataHeader.size()+ " ) ");
                        mProgressDialog.dismiss();
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });
            }
        });

        final android.widget.SearchView search = (android.widget.SearchView) view.findViewById(R.id.byname);

        search.setQueryHint("שם חברה");
        search.setFocusable(false);
        search.setIconified(false);
        search.clearFocus();
        search.requestFocusFromTouch();

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {
                listDataHeader.clear();
                listDataChild.clear();

                if (newText.equals("")) {
                    //listDataHeader.clear();

                    myFirebaseRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            int i = 0;
                            for (final DataSnapshot postSnapshot : snapshot.getChildren()) {

                                for (int num=1; num<=snapshot.child(postSnapshot.getKey()).getChildrenCount(); num++) {
                                    Log.e("bdika : " , postSnapshot.getKey()+" , " + snapshot.child(postSnapshot.getKey()).child("מכרז"+num).child("name").getValue());



                                    listDataHeader.add(new Tender(snapshot.child(postSnapshot.getKey()).child("מכרז"+num).child("mqt").getValue() + "", postSnapshot.getKey(),
                                            snapshot.child(postSnapshot.getKey()).child("מכרז"+num).child("name").getValue() + "",// (long) postSnapshot.child("Info").child("timer").getValue(),
                                            snapshot.child(postSnapshot.getKey()).child("מכרז"+num).child("Info").child("startTender").getValue() + "",
                                            snapshot.child(postSnapshot.getKey()).child("מכרז"+num).child("Info").child("endTender").getValue() + "",
                                            snapshot.child(postSnapshot.getKey()).child("מכרז"+num).child("Info").child("timeStart").getValue() + "",
                                            snapshot.child(postSnapshot.getKey()).child("מכרז"+num).child("Info").child("timeEnd").getValue() + ""


                                    ));


                                    List<Item> list = new ArrayList<Item>();
                                    list.add(new Item(postSnapshot.getKey() + "",
                                            snapshot.child(postSnapshot.getKey()).child("מכרז"+num).child("contact").getValue() + "",
                                            snapshot.child(postSnapshot.getKey()).child("מכרז"+num).child("phone").getValue() + "",
                                            snapshot.child(postSnapshot.getKey()).child("מכרז"+num).child("mail").getValue() + "",
                                            num));
                                    listDataChild.put(listDataHeader.get(i), list); // Header, Child data*/
                                    i++;
                                }

                            }

                            listAdapter = new ExpandableListAdapter(getContext(), listDataHeader, listDataChild);
                            expListView.setAdapter(listAdapter);

                            counter.setText(" ( " +listDataHeader.size()+ " ) ");
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
                        //data = new ArrayList<Post>();

                        if (newText.equals("")) {
                            listDataHeader.clear();
                        }

                        ProgressDialog progress = new ProgressDialog(getContext());
                        progress.setMessage("אנא המתן ...");
                        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progress.setIndeterminate(true);
                        progress.setProgress(0);
                        progress.setMax(3000);
                        progress.show();
                        //number = snapshot.getChildrenCount();

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



                                                listDataHeader.add(new Tender(snapshot.child(postSnapshot.getKey()).child("מכרז"+num).child("mqt").getValue() + "", postSnapshot.getKey(),
                                                        snapshot.child(postSnapshot.getKey()).child("מכרז"+num).child("name").getValue() + "",// (long) postSnapshot.child("Info").child("timer").getValue(),
                                                        snapshot.child(postSnapshot.getKey()).child("מכרז"+num).child("Info").child("startTender").getValue() + "",
                                                        snapshot.child(postSnapshot.getKey()).child("מכרז"+num).child("Info").child("endTender").getValue() + "",
                                                        snapshot.child(postSnapshot.getKey()).child("מכרז"+num).child("Info").child("timeStart").getValue() + "",
                                                        snapshot.child(postSnapshot.getKey()).child("מכרז"+num).child("Info").child("timeEnd").getValue() + ""


                                                ));


                                                List<Item> list = new ArrayList<Item>();
                                                list.add(new Item(postSnapshot.getKey() + "",
                                                        snapshot.child(postSnapshot.getKey()).child("מכרז"+num).child("contact").getValue() + "",
                                                        snapshot.child(postSnapshot.getKey()).child("מכרז"+num).child("phone").getValue() + "",
                                                        snapshot.child(postSnapshot.getKey()).child("מכרז"+num).child("mail").getValue() + "",
                                                        num));
                                                listDataChild.put(listDataHeader.get(i), list); // Header, Child data*/
                                                i++;
                                            }

                                        }
                                    }
                                }
                            } catch (Exception e) {

                            }

                            expListView.setAdapter(listAdapter);

                        }
                        counter.setText(" ( " +listDataHeader.size()+ " ) ");
                        progress.dismiss();
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                    }
                });

                return false;
            }
        });

        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                if (lastPosition != -1
                        && groupPosition != lastPosition) {
                    expListView.collapseGroup(lastPosition);
                }
                lastPosition = groupPosition;
            }
        });


       /* expListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view.setSelected(true);

            }
        });*/



        return view;

    }






    // convert selcted date on date picker dialog to a long
    // for filtering tenders
    public long convertStringToDate(String date){

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date convertedDate = null;

        try {
            convertedDate = dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return convertedDate.getTime();
    }



}
