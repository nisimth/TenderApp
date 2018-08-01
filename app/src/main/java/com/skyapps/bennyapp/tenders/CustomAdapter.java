package com.skyapps.bennyapp.tenders;

import android.app.Activity;
import android.content.Context;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.skyapps.bennyapp.Objects.Tender;
import com.skyapps.bennyapp.R;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class CustomAdapter extends ArrayAdapter<Tender> {
    private ArrayList<Tender> dataSet;
    Context mContext;

    public CustomAdapter(ArrayList<Tender> data, Context context) {
        super(context, R.layout.list_group, data);
        this.dataSet = data;
        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater= ((Activity) mContext).getLayoutInflater();
        View rowView=inflater.inflate(R.layout.list_group, null,true);

        Tender tender = getItem(position);
        TextView masad = (TextView) rowView.findViewById(R.id.masad);
        TextView name = (TextView) rowView.findViewById(R.id.name);
        TextView project = (TextView) rowView.findViewById(R.id.project);
        final TextView time = (TextView) rowView.findViewById(R.id.time);

        masad.setText(tender.getMasad());

        project.setText(tender.getEmail());




        if (mContext.getSharedPreferences("BennyApp" , Context.MODE_PRIVATE).getString("premium" , "").equals("")){
            time.setText("למינויים בלבד");
            name.setText("למינויים בלבד");
        } else{
            name.setText(tender.getName());



            CountDownTimer c = new CountDownTimer(tender.calcEnds(), 1000) {

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
                    //mTextField.setText("done!");
                }

            };
            if (tender.calcStarts()  >= 0) {
                time.setText("טרם\nהתחיל");

            } else if (tender.calcEnds() <= 0) {
                time.setText("עבר הזמן");
            }
            else if (tender.calcEnds() <= TimeUnit.HOURS.toMillis(2)) {
                time.setText("עומד\n להגמר");
                //time.setTextColor(Color.RED);

            }
            else {
                time.setText("פעיל");
                //time.setTextColor(Color.GREEN);
                //c.start();

            }

        }


        Log.e("pubtender" ,tender.getMasad().toString() + " " + time.getText().toString() +"");

        return rowView;
    }


}
