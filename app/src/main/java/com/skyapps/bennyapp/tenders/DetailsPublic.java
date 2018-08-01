package com.skyapps.bennyapp.tenders;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.skyapps.bennyapp.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DetailsPublic extends AppCompatActivity {
    private TextView title;
    private String site;

    private TextView dateStart, timeStart, dateEnd, timeEnd, timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_public);

        title = (TextView) findViewById(R.id.title);
        dateStart = findViewById(R.id.dateStart);
        timeStart = findViewById(R.id.timeStart);
        dateEnd = findViewById(R.id.dateEnd);
        timeEnd = findViewById(R.id.timeEnd);
        timer = findViewById(R.id.timer);

        Firebase.setAndroidContext(this);

        /*InputMethodManager inputManager =
                (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(
                getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);*/

        final Firebase myFirebaseRef = new Firebase("https://tenders-83c71.firebaseio.com/TendersPublic/" +
                getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getString("category","")  + "/" + getIntent().getStringExtra("name")
        + "/" + getIntent().getStringExtra("tender"));
        final LinearLayout d = findViewById(R.id.market_details);
        myFirebaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ((EditText) findViewById(R.id.editMqt)).setText(dataSnapshot.child("mqt").getValue()+"");
                ((EditText) findViewById(R.id.editName)).setText(getIntent().getStringExtra("name")+"");
                ((EditText) findViewById(R.id.editNameProject)).setText(dataSnapshot.child("name").getValue()+"");
                ((EditText) findViewById(R.id.editAddress)).setText(dataSnapshot.child("address").getValue()+"");
                ((EditText) findViewById(R.id.editContact)).setText(dataSnapshot.child("contact").getValue()+"");
                ((EditText) findViewById(R.id.editPhone)).setText(dataSnapshot.child("phone").getValue()+"");
                ((EditText) findViewById(R.id.editEmail)).setText(dataSnapshot.child("email").getValue()+"");
                ((EditText) findViewById(R.id.editPrice)).setText(dataSnapshot.child("filesprice").getValue()+" ₪");
                ((EditText) findViewById(R.id.editDetails)).setText(dataSnapshot.child("details").getValue()+"");

                ((EditText) findViewById(R.id.editStartDate)).setText(dataSnapshot.child("startDate").getValue()+"");
                ((EditText) findViewById(R.id.editEndDate)).setText(dataSnapshot.child("endDate").getValue()+"");
                ((EditText) findViewById(R.id.editStartTime)).setText(dataSnapshot.child("startHour").getValue()+"");
                ((EditText) findViewById(R.id.editEndTime)).setText(dataSnapshot.child("endHour").getValue()+"");

                dateStart.setText(dataSnapshot.child("startDate").getValue()+"");
                dateEnd.setText(dataSnapshot.child("endDate").getValue()+"");
                timeStart.setText(dataSnapshot.child("startHour").getValue()+"");
                timeEnd.setText(dataSnapshot.child("endHour").getValue()+"");
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

                site = dataSnapshot.child("site").getValue()+"";

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




        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(DetailsPublic.this, "הצעתך התקבלה!", Toast.LENGTH_SHORT).show();

                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("http://" + site));
                startActivity(i);

            }
        });

    }
}
