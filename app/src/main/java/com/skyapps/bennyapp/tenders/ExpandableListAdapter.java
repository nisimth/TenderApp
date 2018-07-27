package com.skyapps.bennyapp.tenders;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.skyapps.bennyapp.Objects.Item;
import com.skyapps.bennyapp.Objects.Tender;
import com.skyapps.bennyapp.R;
import com.skyapps.bennyapp.tenders.tabs.TabsActivity;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<Tender> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<Tender, List<Item>> _listDataChild;




    public ExpandableListAdapter(Context context, List<Tender> listDataHeader,
                                 HashMap<Tender, List<Item>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final Item item = (Item) getChild(groupPosition , childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item, null);
        }

        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView phone = (TextView) convertView.findViewById(R.id.phone);
        TextView email = (TextView) convertView.findViewById(R.id.email);



        name.setText(item.getName());
        phone.setText(item.getPhone());
        email.setText(item.getEmail());
        Log.e("item.getCompany()" , item.getCompany());


        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", item.getPhone(), null));
                _context.startActivity(intent);
            }
        });

        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto",item.getEmail(), null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "מעוניין במכרז " + item.getName());
                _context.startActivity(Intent.createChooser(emailIntent, "Send email..."));;
            }
        });


            convertView.findViewById(R.id.details).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent i = new Intent(_context, TabsActivity.class);
                    i.putExtra("name", item.getName());
                    _context.getSharedPreferences("BennyApp", Context.MODE_PRIVATE).edit().putString("company", item.getCompany()).commit();
                    _context.getSharedPreferences("BennyApp", Context.MODE_PRIVATE).edit().putInt("num", item.getNum()).commit();
                    //_context.getSharedPreferences("BennyApp", Context.MODE_PRIVATE).edit().putInt("tender", item.getTender()).commit();
                    Log.e("the num now : " , item.getNum()+"");
                    _context.startActivity(i);
                }
            });


        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        final Tender tender = (Tender) getGroup(groupPosition);
        //Log.e("count", count + "");
        //count++;

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }

        TextView masad = (TextView) convertView.findViewById(R.id.masad);
        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView project = (TextView) convertView.findViewById(R.id.project);
        final TextView time = (TextView) convertView.findViewById(R.id.time);


        masad.setText(tender.getMasad());
        name.setText(tender.getName());
        project.setText(tender.getProject());

        //if (tender.getTime() > ??)
        //else time.setText("הזמן נגמר");



        CountDownTimer c = new CountDownTimer(tender.getTime(), 1000) {

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
                        time.setText(minutes + ":" + seconds);
                    } else {
                        time.setText(hours + ":" + minutes + ":" + seconds);
                    }
                } else if (hours == 0) {
                    time.setText(minutes + ":" + seconds);
                } else {
                    time.setText(days + " ימים , " + hours + ":" + minutes + ":" + seconds);
                }



            }

            public void onFinish() {

                time.setText("עבר הזמן");
            }

        };


        try {
            if (tender.calcTimer(tender.getStartTender(), tender.getStartTime()) >= 0) {
                time.setText("טרם \n התחיל");

            } else if (tender.getTime() <= 0) {
                time.setText("עבר הזמן");
            }
        } catch (Exception e){

        }
        if (time.getText().toString().equals("שעה ותאריך")) {

            c.start();

        }
        Log.e("the tender: " , time.getText().toString()+"");
        return convertView;
    }



    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}
