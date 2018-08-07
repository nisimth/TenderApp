package com.skyapps.bennyapp.tenders.tabs;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.skyapps.bennyapp.R;
import com.skyapps.bennyapp.SelectPhotoDialog;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static android.app.Activity.RESULT_OK;


public class DetailsTab extends Fragment implements SelectPhotoDialog.OnPhotoSelectedListener {

    private EditText editMqt, editName, editNameProject, editAddress, editContact, editPhone,
            editEmail, editCredit, editMaam, editDhifot, editHovala;
    private ImageButton uploadFromCam;
    private ImageButton uploadFromGallery;
    private ImageButton uploadPdf;

    private TextView dateStart, timeStart, dateEnd, timeEnd, timer;
    ///////// new 28.07.2018 ////////////
    private Uri pdfUrl ;
    String pdfUrlString  = null;
    private static final int FILES_PERMISSION_CODE = 9 ;
    private static final int FILES_REQUEST_CODE = 100 ;
    private ImageButton pdfWebViewBtn;
    /////////////////////////////////////
    private TextView amountOftenders;
    ////////////////////////////////////
    private static final int CAMERA_REQUEST_CODE = 69 ;
    private static final int GALLERY_REQUEST_CODE = 70 ;


    private ImageView image; /// now using this
    private Button btn;
    private String companyName;
    private ProgressDialog mProgressDialog;


    private Uri mImageUri;
    DatabaseReference dRef;
    String url;
    static EditText editComments, editAddressForSend;

    @Override
    public void getImagePath(Uri path) {

    }

    @Override
    public void getImageBitmap(Bitmap bitmap) {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_details_tab, container, false);
        dateStart = view.findViewById(R.id.dateStart);
        timeStart = view.findViewById(R.id.timeStart);
        dateEnd = view.findViewById(R.id.dateEnd);
        timeEnd = view.findViewById(R.id.timeEnd);
        timer = view.findViewById(R.id.timer);
        editMqt = view.findViewById(R.id.editMqt);
        editName = view.findViewById(R.id.editName);
        editNameProject = view.findViewById(R.id.editNameProject);
        editAddress = view.findViewById(R.id.editAddress);
        editContact = view.findViewById(R.id.editContact);
        editPhone = view.findViewById(R.id.editPhone);
        editEmail = view.findViewById(R.id.editEmail);
        editCredit = view.findViewById(R.id.editCredit);
        editMaam = view.findViewById(R.id.editMaam);
        editDhifot = view.findViewById(R.id.editDhifot);
        editHovala = view.findViewById(R.id.editHovala);
        editComments = view.findViewById(R.id.editComments);
        editAddressForSend = view.findViewById(R.id.editAddressForSend);
        amountOftenders = view.findViewById(R.id.numtender);
        image = view.findViewById(R.id.image);
        mProgressDialog = new ProgressDialog(getContext());
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage("אנא המתן...");
        mProgressDialog.show();

        /// get the company name  ////
        companyName = getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getString("company", "");

        Firebase.setAndroidContext(getContext());//FireBase , Upload Data from fireBase to EditTexts...
        final Firebase myFirebaseRef = new Firebase("https://tenders-83c71.firebaseio.com/");
        final Firebase ref = myFirebaseRef.child("Tenders/" + getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE)
                .getString("category","") + "/" );
        final Firebase ref2 = myFirebaseRef.child("users");
        final Firebase ref3 = myFirebaseRef.child("Tenders").child(getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getString("category","")).child(getContext().getSharedPreferences("BennyApp",Context.MODE_PRIVATE).getString("company","")).child("מכרז" + getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getInt("num", 0)).child("Info");

        ((EditText)view.findViewById(R.id.category)).setText(getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE)
                .getString("category",""));


        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    if (postSnapshot.getKey().equals(companyName)) {

                        Log.e("testing deatils : " , getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getInt("num", 0)+"");

                        editMqt.setText(postSnapshot.child("מכרז" + getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getInt("num", 0)).child("mqt").getValue() + "");
                        editName.setText(companyName + "");
                        editNameProject.setText(postSnapshot.child("מכרז" + getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getInt("num", 0)).child("name").getValue() + "");
                        editAddress.setText(postSnapshot.child("מכרז" + getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getInt("num", 0)).child("address").getValue() + "");
                        editContact.setText(postSnapshot.child("מכרז" + getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getInt("num", 0)).child("contact").getValue() + "");
                        editPhone.setText(postSnapshot.child("מכרז" + getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getInt("num", 0)).child("phone").getValue() + "");
                        editEmail.setText(postSnapshot.child("מכרז" + getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getInt("num", 0)).child("mail").getValue() + "");
                        editCredit.setText(postSnapshot.child("מכרז" + getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getInt("num", 0)).child("credit").getValue() + "");
                        editMaam.setText(postSnapshot.child("מכרז" + getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getInt("num", 0)).child("maam").getValue() + "");
                        editDhifot.setText(postSnapshot.child("מכרז" + getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getInt("num", 0)).child("dhifot").getValue() + "");
                        editHovala.setText(postSnapshot.child("מכרז" + getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getInt("num", 0)).child("transit").getValue() + "");
                        editAddressForSend.setText(postSnapshot.child("מכרז" + getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getInt("num", 0)).child("addressforsend").getValue() + "");
                        editComments.setText(postSnapshot.child("מכרז" + getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getInt("num", 0)).child("comments").getValue() + "");
                        amountOftenders.setText(postSnapshot.child("מכרז" + getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getInt("num", 0)).child("mqt").getValue() + "");



                        break;
                    }
                }

                mProgressDialog.dismiss();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        final ImageButton galleryBtn = view.findViewById(R.id.uploadImageFromGallery);
        final ImageButton cameraBtn = view.findViewById(R.id.cam);
        final TextView galleryTxt = view.findViewById(R.id.gallery_txt);
        final TextView cameraTxt = view.findViewById(R.id.cam_txt);
        final ImageButton pdfBtn = view.findViewById(R.id.uploadPdf);
        try {
            //// check if it Tender win if yes : hides d layout
            if (TabsActivity.finall.equals("Final")) {
                galleryBtn.setVisibility(View.INVISIBLE);
                cameraBtn.setVisibility(View.INVISIBLE);
                galleryTxt.setVisibility(View.INVISIBLE);
                cameraTxt.setVisibility(View.INVISIBLE);
                pdfBtn.setVisibility(View.INVISIBLE);
            }
        } catch (Exception e){

        }

///////////// check if tender is loss and hide timer layout /////////////////
        final Firebase userFirebise1 = new Firebase("https://tenders-83c71.firebaseio.com/users/" +
                getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getString("username","") + "/Tenders/" +
                getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getString("company","")+
                "/מכרז" + getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getInt("num",0));

        userFirebise1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue()!=null && dataSnapshot.getValue().equals("loss")) {
                    galleryBtn.setVisibility(View.INVISIBLE);
                    cameraBtn.setVisibility(View.INVISIBLE);
                    galleryTxt.setVisibility(View.INVISIBLE);
                    cameraTxt.setVisibility(View.INVISIBLE);
                    pdfBtn.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

///////////// check if tender is win and hide buttons /////////////////
        final Firebase userFirebise2 = new Firebase("https://tenders-83c71.firebaseio.com/users/" +
                getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getString("username","") + "/TenderWin/" +
                getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getString("company","")+
                "/מכרז" + getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getInt("num",0));

        userFirebise2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue()!=null && dataSnapshot.getValue().equals("win")){
                    galleryBtn.setVisibility(View.INVISIBLE);
                    cameraBtn.setVisibility(View.INVISIBLE);
                    galleryTxt.setVisibility(View.INVISIBLE);
                    cameraTxt.setVisibility(View.INVISIBLE);
                    pdfBtn.setVisibility(View.INVISIBLE);

                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


        ///////////// check if tender not started yet/over and hide buttons/////////////////
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
                    galleryBtn.setVisibility(View.INVISIBLE);
                    cameraBtn.setVisibility(View.INVISIBLE);
                    galleryTxt.setVisibility(View.INVISIBLE);
                    cameraTxt.setVisibility(View.INVISIBLE);
                    pdfBtn.setVisibility(View.INVISIBLE);

                }
                else if(calcTimer(endTender,timeEnd) <= 0){
                    galleryBtn.setVisibility(View.INVISIBLE);
                    cameraBtn.setVisibility(View.INVISIBLE);
                    galleryTxt.setVisibility(View.INVISIBLE);
                    cameraTxt.setVisibility(View.INVISIBLE);
                    pdfBtn.setVisibility(View.INVISIBLE);
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


/////////////////////////////// retrive image url from fireBase //////////////////////////////////////////
        ref2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Log.e( "כמ:" , postSnapshot.getKey());
                    try {
                        if (postSnapshot.getKey().equals(getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE)
                                .getString("username", ""))) {
                            url = postSnapshot.child(companyName).child("מכרז" + getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getInt("num", 0)).child("Image").getValue()+"";
                            Glide.with(getContext()).load(postSnapshot.child(companyName).child("מכרז" + getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getInt("num", 0)).child("Image").getValue()).into(image);
                        }
                        if (postSnapshot.getKey().equals(getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE)
                                .getString("username", ""))) {
                            pdfUrlString = postSnapshot.child(companyName).child("מכרז" + getContext().getSharedPreferences("BennyApp",
                                    Context.MODE_PRIVATE).getInt("num", 0)).child("Pdf").getValue()+"";


                        }


                    } catch (Exception e){

                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


//////////////////////////// chose from gallery //////////////////////////////////////
        uploadFromGallery = view.findViewById(R.id.uploadImageFromGallery);
        uploadFromGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openGallery = new Intent(Intent.ACTION_GET_CONTENT);
                openGallery.setType("image/*"); // Intent that opens the gallery
                startActivityForResult(openGallery, GALLERY_REQUEST_CODE);
            }
        });
//////////////////////// chose from camera ///////////////////////////////////////////
        uploadFromCam = view.findViewById(R.id.cam);
        uploadFromCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( PermissionChecker.checkSelfPermission( getContext(), Manifest.permission.CAMERA ) != PackageManager.PERMISSION_GRANTED ) {

                    ActivityCompat.requestPermissions((Activity) getContext(), new String[] {  Manifest.permission.CAMERA  },CAMERA_REQUEST_CODE );
                }
                else {
                    invokeCamera();
                }
            }
        });

//////////////////////////////////////////////////////////////////////////////////////
        uploadPdf = view.findViewById(R.id.uploadPdf);
        uploadPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission( getContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE ) == PackageManager.PERMISSION_GRANTED ){
                    selectPdf();
                }else{
                    ActivityCompat.requestPermissions((Activity) getContext(), new String[] {  Manifest.permission.READ_EXTERNAL_STORAGE  },FILES_PERMISSION_CODE );
                }
             /*if(pdfUrl != null){
                 pdfLoader(pdfUrl);
             }*/

            }

        });

/////////// open webView on click button /////////////////////
        pdfWebViewBtn = view.findViewById(R.id.pdf_icon);
        pdfWebViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pdfUrlString != null){
                    try {
                        openWebPage(pdfUrlString);
                    }catch (Exception e){
                        Log.e("the pdf_error:" , e.toString());
                        Toast.makeText(getContext(),"לא נבחר קובץ",Toast.LENGTH_SHORT).show();
                    }
                }




            }
        });

//////////////////////////////////////////////////////////////////////////////
        view.findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TabsActivity.viewPager.setCurrentItem(1);
            }
        });

////////////////////// Dialog that show the loaded img /////////////////////
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.dialog5);

                ImageView img = (ImageView) dialog.findViewById(R.id.imageview);
                Glide.with(getContext()).load(url).into(img);



                Button btn = (Button) dialog.findViewById(R.id.btn);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();

            }
        });
        // TODO //
        final ImageButton im = view.findViewById(R.id.pdf_icon);
        final int num = getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getInt("num", 0);


        //////////////////////// handle timer /////////////////
        ref3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                dateStart.setText(dataSnapshot.child("startTender").getValue()+"");
                dateEnd.setText(dataSnapshot.child("endTender").getValue()+"");
                timeStart.setText(dataSnapshot.child("timeStart").getValue()+"");
                timeEnd.setText(dataSnapshot.child("timeEnd").getValue()+"");

                long timerFireBase = calcTimer(dateEnd.getText().toString(),timeEnd.getText().toString());

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

        return view;
    }



    private void invokeCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent,CAMERA_REQUEST_CODE);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == CAMERA_REQUEST_CODE){
            if(grantResults[0] ==   PackageManager.PERMISSION_GRANTED){
                invokeCamera();
            }
        }
        if (requestCode == FILES_PERMISSION_CODE){
            if(grantResults[1] == PackageManager.PERMISSION_GRANTED){
                selectPdf();
            }
            else{
                Toast.makeText(getContext(),"אנא אשר גישה לאיחסון חיצוני",Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        mProgressDialog = new ProgressDialog(getContext());
        mProgressDialog.setCancelable(false);
        mProgressDialog.setTitle("אנא המתן...");
        mProgressDialog.setMessage("מעלה את התמונה שלך ושומר אותה...");
        mProgressDialog.show();

        /////// onActivityResult for camera & gallery ////////
        if( (requestCode == CAMERA_REQUEST_CODE &&  resultCode == RESULT_OK)
                || (requestCode == GALLERY_REQUEST_CODE &&  resultCode == RESULT_OK)  ){

            Boolean b;
            try {
                data.getData();
                b = true;
            } catch (Exception e){
                b = false;
            }
            if(b) {
                Uri imageUri = data.getData();
                if (imageUri != null) {
                    String imagePath = imageUri.getPath();
                    image.setImageURI(imageUri);
                    try {
                        Bitmap bitmapdata = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), imageUri);

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmapdata.compress(Bitmap.CompressFormat.PNG, 0, baos);


                        FirebaseStorage storage = FirebaseStorage.getInstance();
                        StorageReference storageRef = storage.getReferenceFromUrl("gs://tenders-83c71.appspot.com/");
                        final StorageReference ref = storageRef.child("Pictures/" +
                                getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getString("name", "") + "/" + getContext()
                                .getSharedPreferences("BennyApp", Context.MODE_PRIVATE)
                                .getString("company", "") + "/" + System.currentTimeMillis() + ".jpg");
                        UploadTask uploadTask = ref.putBytes(baos.toByteArray());
                        uploadTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                Log.e("hmmmmm filed... ", exception.toString());
                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                final Uri downloadUri = taskSnapshot.getDownloadUrl();
                                mProgressDialog.dismiss();
                                dRef = FirebaseDatabase.getInstance().getReference().child("users");

                                dRef.child(getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getString("username", ""))
                                        .child(getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getString("company", ""))
                                        .child("מכרז" + getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getInt("num", 0))
                                        .child("Image")
                                        .setValue(downloadUri.toString());

                                Glide.with(getContext()).load(downloadUri.toString()).into(image);
                            }
                        });
                    }catch (IOException e) {
                        Toast.makeText(getContext(), "ישנה שגיאה, נסה שנית", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        } else {
            mProgressDialog.dismiss();
        }

        //////  onActivityResult for selecting PDF file /////////
        if( requestCode == FILES_REQUEST_CODE && resultCode == RESULT_OK && data != null ){
            boolean c ;
            try{
                pdfUrl = data.getData();
                c = true ;
            }catch (Exception e)
            {
                Log.e("the pdf_error:" , e.toString());
                Toast.makeText(getContext(), "הPDF גדול מדיי, נסה שנית", Toast.LENGTH_SHORT).show();
                c = false ;
            }
            if ( c ){
                try {
                    pdfLoader(pdfUrl);
                    Toast.makeText(getContext(),"הקובץ עלה בהצלחה",Toast.LENGTH_LONG).show();
                }catch (Exception e){
                    Toast.makeText(getContext(), "ישנה שגיאה, נסה שנית", Toast.LENGTH_SHORT).show();
                }
            }

        }

    }
    ////////////////////////// Intent for picking PDF file /////////////////////////////////////////
    private void selectPdf(){
        Intent pdfIntent = new Intent();
        pdfIntent.setType("application/pdf");
        pdfIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(pdfIntent,FILES_REQUEST_CODE);
    }
    ///////////////////////// loading process of PDF file //////////////////////
    private void pdfLoader(Uri pdfUrl){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://tenders-83c71.appspot.com/");
        final StorageReference ref = storageRef.child("Pdf/" +
                getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getString("name", "") + "/" + getContext()
                .getSharedPreferences("BennyApp", Context.MODE_PRIVATE)
                .getString("company", "") + "/" + System.currentTimeMillis() + ".pdf");
        UploadTask uploadTask = ref.putFile(pdfUrl);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.e("something not ok ", exception.toString());
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                final Uri downloadUri = taskSnapshot.getDownloadUrl();
                mProgressDialog.dismiss();
                dRef = FirebaseDatabase.getInstance().getReference().child("users");

                dRef.child(getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getString("username", ""))
                        .child(getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getString("company", ""))
                        .child("מכרז" + getContext().getSharedPreferences("BennyApp", Context.MODE_PRIVATE).getInt("num", 0))
                        .child("Pdf")
                        .setValue(downloadUri.toString());
            }
        });


    }
    ///////// opens webView //////////////
    public void openWebPage(String url) {
        if(url != null) {
            Uri webPage = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, webPage);
            startActivity(intent);
        }
        else {
            Toast.makeText(getContext(),"לא נבחר קובץ",Toast.LENGTH_SHORT).show();
        }


    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////

    public static Bitmap scaleDown(Bitmap realImage, float maxImageSize,
                                   boolean filter) {
        float ratio = Math.min(
                maxImageSize / realImage.getWidth(),
                maxImageSize / realImage.getHeight());
        int width = Math.round(ratio * realImage.getWidth());
        int height = Math.round(ratio * realImage.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, 550,
                300, filter);
        realImage.recycle();
        return newBitmap;
    }



}
