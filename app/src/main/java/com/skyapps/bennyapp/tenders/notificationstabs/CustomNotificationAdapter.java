package com.skyapps.bennyapp.tenders.notificationstabs;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.skyapps.bennyapp.R;

import java.util.ArrayList;

public class CustomNotificationAdapter extends ArrayAdapter<ItemNotification> {
    private ArrayList<ItemNotification> dataSet;
    Context mContext;

    public CustomNotificationAdapter(ArrayList<ItemNotification> data, Context context) {
        super(context, R.layout.notificationitem, data);
        this.dataSet = data;
        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater= ((Activity) mContext).getLayoutInflater();
        View rowView=inflater.inflate(R.layout.notificationitem, null,true);

        TextView username = (TextView) rowView.findViewById(R.id.username);
        TextView message = (TextView) rowView.findViewById(R.id.message);
        // TODO
        TextView tenderNum = (TextView) rowView.findViewById(R.id.tender_num);
        /////
        TextView date = (TextView) rowView.findViewById(R.id.date);

        final ItemNotification item = getItem(position);
        username.setText(item.getUsername()+"");
        message.setText(item.getMessage());
        tenderNum.setText(item.getMqtNum());


        String str1 = item.getType().replace("_", " ");
        String str2 = str1.replace("-", "/");

        date.setText(str2);

       /* if (item.getType().equals("private")){

            rowView.setBackgroundColor(Color.YELLOW);

        }*/


        return rowView;
    }
}

