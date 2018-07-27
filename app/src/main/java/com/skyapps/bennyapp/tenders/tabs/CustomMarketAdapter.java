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

import com.skyapps.bennyapp.Objects.ItemMarket;
import com.skyapps.bennyapp.R;

import java.util.ArrayList;

public class CustomMarketAdapter extends ArrayAdapter<ItemMarket> {
    private ArrayList<ItemMarket> dataSet;
    Context mContext;

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
            price.setText(item.getPrice() + "₪");
        } else {
            price.setText("לא תומחר");
        }

        try {
            if (TabsActivity.finall.equals("Final")) {

                rowView.findViewById(R.id.gotoPrice).setVisibility(View.INVISIBLE);

            }
        } catch (Exception e){

        }


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
