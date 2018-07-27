package com.skyapps.bennyapp;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.PermissionChecker;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SelectPhotoDialog extends DialogFragment{
///
    private static final String FRAGMENT_TAG = "SelectPhotoDialog" ;
    private static final int PICK_FILE_REQUEST_CODEa = 1234 ;
    private static final int CAMERA_REQUEST_CODEa = 4321 ;

    public interface OnPhotoSelectedListener{
        void getImagePath(Uri path);
        void getImageBitmap(Bitmap bitmap);
    }

    OnPhotoSelectedListener mOnPhotoSelectedListener ;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choose_image,container,false);
        getDialog().setTitle("upload dialog");

        TextView selectPic = (TextView)view.findViewById(R.id.dialogChoosePic);
        selectPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Intent.ACTION_GET_CONTENT);
                intent1.setType("image/*");
                startActivityForResult(intent1, PICK_FILE_REQUEST_CODEa);
            }
        });

        TextView selectCam = (TextView)view.findViewById(R.id.dialogChooseCam);
        selectCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( PermissionChecker.checkSelfPermission( getContext(), android.Manifest.permission.CAMERA ) != PackageManager.PERMISSION_GRANTED ) {

                    ActivityCompat.requestPermissions((Activity) getContext(), new String[] {  Manifest.permission.CAMERA  },CAMERA_REQUEST_CODEa );
                }
                else {
                    invokeCamera();
                }
            }
        });


        return view ;
    }

    private void invokeCamera() {
        Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent2, CAMERA_REQUEST_CODEa);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // for gallery
        if(requestCode == PICK_FILE_REQUEST_CODEa && resultCode == Activity.RESULT_OK){
            Uri selectedImageUri = data.getData();
            // next we send to details fragment
            mOnPhotoSelectedListener.getImagePath(selectedImageUri);
            getDialog().dismiss();
        }

        //for camera
        if(requestCode == CAMERA_REQUEST_CODEa && resultCode == Activity.RESULT_OK){
            Bitmap bitmap ;
            bitmap = (Bitmap) data.getExtras().get("data");
            mOnPhotoSelectedListener.getImageBitmap(bitmap);
            getDialog().dismiss();
        }
    }

    @Override
    public void onAttach(Context context) {
        try {
            mOnPhotoSelectedListener = (OnPhotoSelectedListener)getTargetFragment();
        }catch (ClassCastException e){

        }
        super.onAttach(context);

    }
}
