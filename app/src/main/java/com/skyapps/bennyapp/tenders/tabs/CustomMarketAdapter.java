package com.skyapps.bennyapp.tenders.tabs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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

public class CustomMarketAdapter extends ArrayAdapter<ItemMarket> {
    private ArrayList<ItemMarket> dataSet;
    Context mContext;
    private String dateStart, timeStart, dateEnd, timeEnd;

    public CustomMarketAdapter(ArrayList<ItemMarket> data, Context context) {
        super(context, R.layout.marketitem, data);
        this.dataSet = data;
        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater= ((Activity) mContext).getLayoutInflater();
        View rowView=inflater.inflate(R.layout.marketitem, null,true);

        TextView number = (TextView) rowView.findViewById(R.id.number);
        TextView name = (TextView) rowView.findViewById(R.id.name);
        TextView mount = (TextView) rowView.findViewById(R.id.mount);
        TextView price = (TextView) rowView.findViewById(R.id.price);
        ImageView img = (rowView.findViewById(R.id.gotoPrice));
        img.getLayoutParams().width = 200;
        img.getLayoutParams().height = 200;

        final ItemMarket item = getItem(position);
        number.setText(item.getNumber()+"");
        name.setText(item.getName());
        mount.setText(item.getMount()+"");

        Log.e("the price : " , item.getPrice()+"");
        if (!item.getPrice().equals("")){
            price.setText( " ₪"+item.getPrice());
        } else {
            price.setText("לא תומחר");
        }

        try {
            if (TabsActivity.finall.equals("Final")) {

                rowView.findViewById(R.id.gotoPrice).setVisibility(View.INVISIBLE);

            }
        } catch (Exception e){

        }


        final ImageView gotoPrice = rowView.findViewById(R.id.gotoPrice);

        ///////////// check if tender is loss and hide timer layout /////////////////
        final Firebase userFirebise1 = new Firebase("https://tenders-83c71.firebaseio.com/users/" +
                getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getString("username","") + "/Tenders/" +
                getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getString("company","")+
                "/מכרז" + getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getInt("num",0));

        userFirebise1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue()!=null && dataSnapshot.getValue().equals("loss")) {
                    gotoPrice.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        ///////////// check if tender is win and hide gotoPrice button/////////////////
        final Firebase userFirebise2 = new Firebase("https://tenders-83c71.firebaseio.com/users/" +
                getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getString("username","") + "/TenderWin/" +
                getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getString("company","")+
                "/מכרז" + getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getInt("num",0));

        userFirebise2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue()!=null && dataSnapshot.getValue().equals("win")){
                    gotoPrice.setVisibility(View.INVISIBLE);

                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


        ///////////// check if tender not started yet/over and hide gotoPrice button /////////////////
        Firebase.setAndroidContext(getContext());
        final Firebase tenderFireBase = new Firebase("https://tenders-83c71.firebaseio.com/Tenders/" +
                getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getString("category","") + "/" +
                getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getString("company","") +
                "/" + "מכרז" + getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getInt("num",0));

        tenderFireBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String startTender = dataSnapshot.child("Info").child("startTender").getValue()+"";
                String endTender =  dataSnapshot.child("Info").child("endTender").getValue()+"";
                String timeStart =  dataSnapshot.child("Info").child("timeStart").getValue()+"";
                String timeEnd =  dataSnapshot.child("Info").child("timeEnd").getValue()+"";

                if(calcTimer(startTender,timeStart) >= 0){
                    gotoPrice.setVisibility(View.INVISIBLE);

                }
                else if(calcTimer(endTender,timeEnd) <= 0){
                    gotoPrice.setVisibility(View.INVISIBLE);
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

        rowView.findViewById(R.id.gotoPrice).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext() , Mifrat.class);
                i.putExtra("mqt" , item.getMqt());
                i.putExtra("details" , item.getDetails());
                i.putExtra("size" , item.getSize());
                i.putExtra("mount" , item.getMount());
                i.putExtra("num" , item.getNumber());
                i.putExtra("name" , item.getName());
                i.putExtra("price", item.getPrice());
                getContext().startActivity(i);
            }
        });

        return rowView;
    }
}
