package com.skyapps.bennyapp.tenders;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.SearchView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.skyapps.bennyapp.Objects.Item;
import com.skyapps.bennyapp.Objects.Tender;
import com.skyapps.bennyapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class privateTenders extends Fragment {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<Tender> listDataHeader;
    HashMap<Tender, List<Item>> listDataChild;
    private ProgressDialog mProgressDialog;

    int lastPosition = -1;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_private_tenders, container, false);

        expListView = view.findViewById(R.id.privateList);

        //Toast.makeText(getContext(), getActivity().getTitle() +"", Toast.LENGTH_SHORT).show();
        //Log.e("TalHere: " , getActivity().getTitle()+"");


        listDataHeader = new ArrayList<Tender>();
        listDataChild = new HashMap<Tender, List<Item>>();

        Firebase.setAndroidContext(getContext());
        final Firebase myFirebaseRef = new Firebase("https://tenders-83c71.firebaseio.com/Tenders/" + getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getString("category","") + "/");
        mProgressDialog = new ProgressDialog(getContext());
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage("אנא המתן...");
        mProgressDialog.show();

        myFirebaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                int i = 0;
                listDataHeader.clear();
                listDataChild.clear();


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


                mProgressDialog.dismiss();

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

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


        return view;

    }



}
