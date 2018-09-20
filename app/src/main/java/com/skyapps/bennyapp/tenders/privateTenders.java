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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Spinner;
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
import java.util.concurrent.TimeUnit;


public class privateTenders extends Fragment {

    //////////// counter of all tenders/////////////
    //private int tenderCounter = 0;
    private TextView counter;
    ////////////////////////////


    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<Tender> listDataHeader;
    HashMap<Tender, List<Item>> listDataChild;
    private ProgressDialog mProgressDialog;

    int lastPosition = -1;
    //// filters ///
    ImageButton filterStartDate;
    ImageButton filterEndDate;
    Button searchBtn;
    Button resetSearchBtn;

    CheckBox lowCheckBox;
    CheckBox highCheckBox;

    /*CheckBox yetToStart;
    CheckBox active;
    CheckBox isEnding;
    CheckBox ended;*/
    long startDateSelcted, endDateSelcted;
    /////////////////////////////////////////////////////
    private DatePickerDialog.OnDateSetListener start_dateListener, end_dateListener;
    ////////////
    Spinner statusSpinner;



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

        /*yetToStart = (CheckBox) view.findViewById(R.id.checkBox_yet_to_start);
        active = (CheckBox) view.findViewById(R.id.checkbox_active);
        isEnding = (CheckBox) view.findViewById(R.id.checkbox_isEnding);
        ended = (CheckBox) view.findViewById(R.id.checkbox_ended);*/

        statusSpinner = (Spinner) view.findViewById(R.id.spinner);

        Firebase.setAndroidContext(getContext());
        final Firebase myFirebaseRef = new Firebase("https://tenders-83c71.firebaseio.com/Tenders/" + getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getString("category","") + "/");

/////////////// tender status spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),R.array.status,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(adapter);
        statusSpinner.setSelected(false);
        statusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                String text = parent.getItemAtPosition(position).toString();

                if (text.equals("הכל")){
                    lowCheckBox.setChecked(false);
                    highCheckBox.setChecked(false);
                    myFirebaseRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            int i = 0;
                            listDataHeader.clear();
                            listDataChild.clear();

                            for (final DataSnapshot postSnapshot : snapshot.getChildren()) {
                                for (int num=1; num<=snapshot.child(postSnapshot.getKey()).getChildrenCount(); num++) {
                                    //tenderCounter ++;
                                    String status;

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
                                            num ));



                                    listDataChild.put(listDataHeader.get(i), list); // Header, Child data*/

                                    i++;

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
                }

                else if(text.equals("פעיל")){
                    lowCheckBox.setChecked(false);
                    highCheckBox.setChecked(false);
                    myFirebaseRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            int i = 0;
                            listDataHeader.clear();
                            listDataChild.clear();

                            for (final DataSnapshot postSnapshot : snapshot.getChildren()) {


                                for (int num=1; num<=snapshot.child(postSnapshot.getKey()).getChildrenCount(); num++) {
                                    Long startCurrent = convertStringToDate(
                                            snapshot.child(postSnapshot.getKey()).child("מכרז"+num).child("Info").child("startTender").getValue() + "",
                                            snapshot.child(postSnapshot.getKey()).child("מכרז"+num).child("Info").child("timeStart").getValue() + ""
                                    );
                                    Long endCurrent = convertStringToDate(
                                            snapshot.child(postSnapshot.getKey()).child("מכרז"+num).child("Info").child("endTender").getValue() + "",
                                            snapshot.child(postSnapshot.getKey()).child("מכרז"+num).child("Info").child("timeEnd").getValue() + ""
                                    );

                                    Date currentTime  = Calendar.getInstance().getTime();
                                /*Log.e("start_current_date",snapshot.child(postSnapshot.getKey()).child("מכרז"+num).child("Info").child("startTender").getValue() + "");
                                Log.e("start_current",startCurrent+"" );
                                Log.e("start_current_d",new Date(startCurrent)+"");
                                Log.e("start_selcted_date", new Date(startDateSelcted)+"");
                                Log.e("start_selcted",startDateSelcted+"");*/

                                    if(startCurrent - currentTime.getTime() < 0 && endCurrent - currentTime.getTime() > 0){

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
                else if (text.equals("עומד להסגר")){
                    lowCheckBox.setChecked(false);
                    highCheckBox.setChecked(false);
                    myFirebaseRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            int i = 0;
                            listDataHeader.clear();
                            listDataChild.clear();

                            for (final DataSnapshot postSnapshot : snapshot.getChildren()) {


                                for (int num=1; num<=snapshot.child(postSnapshot.getKey()).getChildrenCount(); num++) {
                                    Long endCurrent = convertStringToDate(
                                            snapshot.child(postSnapshot.getKey()).child("מכרז"+num).child("Info").child("endTender").getValue() + "",
                                            snapshot.child(postSnapshot.getKey()).child("מכרז"+num).child("Info").child("timeEnd").getValue() + ""
                                    );

                                    Date currentTime  = Calendar.getInstance().getTime();
                                /*Log.e("start_current_date",snapshot.child(postSnapshot.getKey()).child("מכרז"+num).child("Info").child("startTender").getValue() + "");
                                Log.e("start_current",startCurrent+"" );
                                Log.e("start_current_d",new Date(startCurrent)+"");
                                Log.e("start_selcted_date", new Date(startDateSelcted)+"");
                                Log.e("start_selcted",startDateSelcted+"");*/

                                    if(endCurrent - currentTime.getTime()  <= TimeUnit.HOURS.toMillis(2) && endCurrent - currentTime.getTime()>0){

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
                                    Log.e("isending",endCurrent - currentTime.getTime()+ " " + TimeUnit.HOURS.toMillis(2)  +"");
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

                else if (text.equals("טרם התחיל")){
                    lowCheckBox.setChecked(false);
                    highCheckBox.setChecked(false);
                    myFirebaseRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            int i = 0;
                            listDataHeader.clear();
                            listDataChild.clear();

                            for (final DataSnapshot postSnapshot : snapshot.getChildren()) {


                                for (int num=1; num<=snapshot.child(postSnapshot.getKey()).getChildrenCount(); num++) {
                                    Long startCurrent = convertStringToDate(
                                            snapshot.child(postSnapshot.getKey()).child("מכרז"+num).child("Info").child("startTender").getValue() + "",
                                            snapshot.child(postSnapshot.getKey()).child("מכרז"+num).child("Info").child("timeStart").getValue() + ""
                                    );

                                    Date currentTime  = Calendar.getInstance().getTime();
                                /*Log.e("start_current_date",snapshot.child(postSnapshot.getKey()).child("מכרז"+num).child("Info").child("startTender").getValue() + "");
                                Log.e("start_current",startCurrent+"" );
                                Log.e("start_current_d",new Date(startCurrent)+"");
                                Log.e("start_selcted_date", new Date(startDateSelcted)+"");
                                Log.e("start_selcted",startDateSelcted+"");*/

                                    if(startCurrent - currentTime.getTime() >0 ){

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

                else if (text.equals("סגור")){
                    lowCheckBox.setChecked(false);
                    highCheckBox.setChecked(false);
                    myFirebaseRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            int i = 0;
                            listDataHeader.clear();
                            listDataChild.clear();

                            for (final DataSnapshot postSnapshot : snapshot.getChildren()) {


                                for (int num=1; num<=snapshot.child(postSnapshot.getKey()).getChildrenCount(); num++) {
                                    Long endCurrent = convertStringToDate(
                                            snapshot.child(postSnapshot.getKey()).child("מכרז"+num).child("Info").child("endTender").getValue() + "",
                                            snapshot.child(postSnapshot.getKey()).child("מכרז"+num).child("Info").child("timeEnd").getValue() + ""
                                    );

                                    Date currentTime  = Calendar.getInstance().getTime();
                                /*Log.e("start_current_date",snapshot.child(postSnapshot.getKey()).child("מכרז"+num).child("Info").child("startTender").getValue() + "");
                                Log.e("start_current",startCurrent+"" );
                                Log.e("start_current_d",new Date(startCurrent)+"");
                                Log.e("start_selcted_date", new Date(startDateSelcted)+"");
                                Log.e("start_selcted",startDateSelcted+"");*/

                                    if(endCurrent - currentTime.getTime() < 0){

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

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

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
                        //tenderCounter ++;
                        String status;

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
                            num ));



                        listDataChild.put(listDataHeader.get(i), list); // Header, Child data*/

                        i++;

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

        /// search by company name
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


        ////////// search by urgency
        lowCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

            if(lowCheckBox.isChecked()) {
                highCheckBox.setChecked(false);
                /*yetToStart.setChecked(false);
                active.setChecked(false);
                isEnding.setChecked(false);
                ended.setChecked(false);*/
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
                /*yetToStart.setChecked(false);
                active.setChecked(false);
                isEnding.setChecked(false);
                ended.setChecked(false);*/
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


        ////////// search by Tender status /////////////
/*
        yetToStart.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(yetToStart.isChecked()){
                    lowCheckBox.setChecked(false);
                    highCheckBox.setChecked(false);
                    active.setChecked(false);
                    isEnding.setChecked(false);
                    ended.setChecked(false);
                    myFirebaseRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            int i = 0;
                            listDataHeader.clear();
                            listDataChild.clear();

                            for (final DataSnapshot postSnapshot : snapshot.getChildren()) {


                                for (int num=1; num<=snapshot.child(postSnapshot.getKey()).getChildrenCount(); num++) {
                                    Long startCurrent = convertStringToDate(
                                            snapshot.child(postSnapshot.getKey()).child("מכרז"+num).child("Info").child("startTender").getValue() + "",
                                            snapshot.child(postSnapshot.getKey()).child("מכרז"+num).child("Info").child("timeStart").getValue() + ""
                                    );

                                    Date currentTime  = Calendar.getInstance().getTime();


                                    if(startCurrent - currentTime.getTime() >0 ){

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
                                        listDataChild.put(listDataHeader.get(i), list); // Header, Child data
                                        i++;
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

        active.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(active.isChecked()){
                    lowCheckBox.setChecked(false);
                    highCheckBox.setChecked(false);
                    yetToStart.setChecked(false);
                    isEnding.setChecked(false);
                    ended.setChecked(false);
                    myFirebaseRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            int i = 0;
                            listDataHeader.clear();
                            listDataChild.clear();

                            for (final DataSnapshot postSnapshot : snapshot.getChildren()) {


                                for (int num=1; num<=snapshot.child(postSnapshot.getKey()).getChildrenCount(); num++) {
                                    Long startCurrent = convertStringToDate(
                                            snapshot.child(postSnapshot.getKey()).child("מכרז"+num).child("Info").child("startTender").getValue() + "",
                                            snapshot.child(postSnapshot.getKey()).child("מכרז"+num).child("Info").child("timeStart").getValue() + ""
                                            );
                                    Long endCurrent = convertStringToDate(
                                            snapshot.child(postSnapshot.getKey()).child("מכרז"+num).child("Info").child("endTender").getValue() + "",
                                            snapshot.child(postSnapshot.getKey()).child("מכרז"+num).child("Info").child("timeEnd").getValue() + ""
                                            );

                                    Date currentTime  = Calendar.getInstance().getTime();


                                    if(startCurrent - currentTime.getTime() < 0 && endCurrent - currentTime.getTime() > 0){

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
                                        listDataChild.put(listDataHeader.get(i), list); // Header, Child data
                                        i++;
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

        isEnding.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(isEnding.isChecked()){
                    lowCheckBox.setChecked(false);
                    highCheckBox.setChecked(false);
                    yetToStart.setChecked(false);
                    active.setChecked(false);
                    ended.setChecked(false);
                    myFirebaseRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            int i = 0;
                            listDataHeader.clear();
                            listDataChild.clear();

                            for (final DataSnapshot postSnapshot : snapshot.getChildren()) {


                                for (int num=1; num<=snapshot.child(postSnapshot.getKey()).getChildrenCount(); num++) {
                                    Long endCurrent = convertStringToDate(
                                            snapshot.child(postSnapshot.getKey()).child("מכרז"+num).child("Info").child("endTender").getValue() + "",
                                            snapshot.child(postSnapshot.getKey()).child("מכרז"+num).child("Info").child("timeEnd").getValue() + ""
                                            );

                                    Date currentTime  = Calendar.getInstance().getTime();


                                    if(endCurrent - currentTime.getTime()  <= TimeUnit.HOURS.toMillis(2) && endCurrent - currentTime.getTime()>0){

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
                                        listDataChild.put(listDataHeader.get(i), list); // Header, Child data
                                        i++;

                                    }
                                    Log.e("isending",endCurrent - currentTime.getTime()+ " " + TimeUnit.HOURS.toMillis(2)  +"");
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

        ended.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(ended.isChecked()){
                    lowCheckBox.setChecked(false);
                    highCheckBox.setChecked(false);
                    yetToStart.setChecked(false);
                    active.setChecked(false);
                    isEnding.setChecked(false);
                    myFirebaseRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            int i = 0;
                            listDataHeader.clear();
                            listDataChild.clear();

                            for (final DataSnapshot postSnapshot : snapshot.getChildren()) {


                                for (int num=1; num<=snapshot.child(postSnapshot.getKey()).getChildrenCount(); num++) {
                                    Long endCurrent = convertStringToDate(
                                            snapshot.child(postSnapshot.getKey()).child("מכרז"+num).child("Info").child("endTender").getValue() + "",
                                            snapshot.child(postSnapshot.getKey()).child("מכרז"+num).child("Info").child("timeEnd").getValue() + ""
                                            );

                                    Date currentTime  = Calendar.getInstance().getTime();


                                    if(endCurrent - currentTime.getTime() < 0){

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
                                        listDataChild.put(listDataHeader.get(i), list); // Header, Child data
                                        i++;
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
*/



        // reset filter
        resetSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //// set status spinner to default
                statusSpinner.setSelection(0);
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
                        /*yetToStart.setChecked(false);
                        active.setChecked(false);
                        isEnding.setChecked(false);
                        ended.setChecked(false);*/
                        search.setQuery("",false);



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

                        lowCheckBox.setChecked(false);
                        highCheckBox.setChecked(false);
                        /*yetToStart.setChecked(false);
                        active.setChecked(false);
                        isEnding.setChecked(false);
                        ended.setChecked(false);*/

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

    /**
     * @param date
     * @param time
     * @return
     */
    public long convertStringToDate(String date, String time){
        String toConvertTime = date + " " + time;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date convertedDate = null;

        try {
            convertedDate = dateFormat.parse(toConvertTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return convertedDate.getTime();
    }



}
