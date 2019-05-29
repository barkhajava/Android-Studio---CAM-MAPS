package edu.umb.cs443;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.location.LocationServices;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class SaveImage extends Menu implements LocationListener {
    private ImageView capturedImage;
    private Button btnsaveCamera;
    Bitmap bp;
    Spinner spinner;
    private static final int Image_Capture_Code = 100;

    Uri file;

    @Override
    protected void onStart() {
        super.onStart();


        // Request permission to access location
        int MY_PERMISSIONS_REQUEST_LOCATION = 0;
        ActivityCompat.requestPermissions(SaveImage.this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                MY_PERMISSIONS_REQUEST_LOCATION);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        ActivityCompat.requestPermissions(SaveImage.this,new String[] {Manifest.permission.CAMERA}, 0);
        startActivityForResult(intent, Image_Capture_Code);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        ActivityCompat.requestPermissions(SaveImage.this,new String[] {Manifest.permission.CAMERA}, 0);
        if (requestCode == Image_Capture_Code) {
            if (resultCode == RESULT_OK) {
                bp = (Bitmap) data.getExtras().get("data");
                setContentView(R.layout.camerasave);
                capturedImage = (ImageView) findViewById(R.id.capturedImage);
                 spinner = (Spinner) findViewById(R.id.drop_down);


                // Create an ArrayAdapter using the string array and a default spinner layout
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                        R.array.drop_down, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
                spinner.setAdapter(adapter);
                capturedImage.setImageBitmap(bp);


            }



        } else if (resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }
    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        parent.getItemAtPosition(pos);
        Spinner spinner = (Spinner) findViewById(R.id.drop_down);
       // spinner.setOnItemSelectedListener(pos);
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }
    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    public void save_image(View view){


        int MY_PERMISSIONS_REQUEST_READ_CONTACTS =0;

        // Request permission to save images
        ActivityCompat.requestPermissions(SaveImage.this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                MY_PERMISSIONS_REQUEST_READ_CONTACTS);

        String timeStamp = new SimpleDateFormat("yyyMMdd_HHmmss", Locale.ENGLISH).format(new Date());
        //Bitmap bp = (Bitmap) data.getExtras().get("data");

        String text = spinner.getSelectedItem().toString();
        File mFile = new File("/storage/emulated/0/DCIM/Camera/IMG_"+timeStamp+".jpeg");
        if (!mFile.exists()) {
            try {
                mFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            FileOutputStream fOut = new FileOutputStream(mFile);
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bp.compress(Bitmap.CompressFormat.JPEG, 100, fOut);

            LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
            Location location = null;
            try{
                location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }
            catch(SecurityException e){
                e.printStackTrace();
            }

            double longitude = location.getLongitude();
            double latitude = location.getLatitude();

            ContentValues values = new ContentValues();

            values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
            values.put(MediaStore.MediaColumns.DATA, mFile.getAbsolutePath());
            values.put(MediaStore.MediaColumns.DISPLAY_NAME, text);
            values.put(MediaStore.Images.Media.LATITUDE, latitude);
            values.put(MediaStore.Images.Media.LONGITUDE, longitude);
            values.put(MediaStore.Images.Media.DATE_ADDED, timeStamp);
            values.put(MediaStore.Images.Media.DESCRIPTION, text);

            getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            Toast.makeText(this, "Image Saved!", Toast.LENGTH_LONG).show();
            this.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK));
            this.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK));
            //String imgSaved=MediaStore.Images.Media.insertImage(getContentResolver(),bp, mFile.getName(), "drawing");
            //   this.sendBroadcast(galleryIntent);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }  catch (IOException ex) {
            ex.printStackTrace();
        }


    }
}
