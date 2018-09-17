package com.skyapps.bennyapp.tenders;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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

public class ExpandableListWinAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<Tender> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<Tender, List<Item>> _listDataChild;

    private int count = 0 ;
    private int countAll = 5;

    public ExpandableListWinAdapter(Context context, List<Tender> listDataHeader,
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
        //// send using Intent "final" to TabActivity for hide some layouts
        convertView.findViewById(R.id.details).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(_context,TabsActivity.class);
                i.putExtra("name" , item.getName());
                i.putExtra("Final", "Final");
                _context.getSharedPreferences("BennyApp" , Context.MODE_PRIVATE).edit().putString("company" , item.getCompany()).commit();
                _context.getSharedPreferences("BennyApp", Context.MODE_PRIVATE).edit().putInt("num", item.getNum()).commit();

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
        Log.e("count" , count+"");
        count++;

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

        time.setText("סגור");



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
