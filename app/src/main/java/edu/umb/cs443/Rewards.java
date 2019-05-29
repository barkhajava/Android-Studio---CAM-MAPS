package edu.umb.cs443;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;

public class Rewards extends  Menu{
    int traveller = 0;
    int numberOfFiles;
    String title;
    File[] files;
    int count = 0;
    ImageView iv;

    @Override
    protected void onStart() {
        super.onStart();
        setContentView(R.layout.rewards);
        // Photographer reward
        File dir = new File(Environment.getExternalStorageDirectory() + "/DCIM/Camera/");
        files = dir.listFiles();
        if (files != null) {
            numberOfFiles = files.length;

            if (numberOfFiles > 2) {


                ImageView iv = (ImageView) findViewById(R.id.imageView);
                iv.setImageResource(R.drawable.star);
                // iv.setImageDrawable(getResources().getDrawable(R.drawable.star));
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                TextView reward1 = (TextView) findViewById(R.id.reward1);
                reward1.setText("Photographer");

            }


            // Nerd Reward
            for (int i = 0; i < numberOfFiles; i++) {
                Bitmap bitmap = BitmapFactory.decodeFile(String.valueOf(files[i]));
                Bitmap smallMarker = Bitmap.createScaledBitmap(bitmap, 204, 204, false);
                ContentValues values = new ContentValues();
                //  CameraUpdate umb = CameraUpdateFactory.newLatLng(new LatLng(42.313895, -71.038833));
                String filePath = files[i].getAbsolutePath();
                Cursor cursor = getContentResolver().query(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        new String[]{MediaStore.Images.Media.LATITUDE, MediaStore.Images.Media.LONGITUDE, MediaStore.Images.Media.DESCRIPTION},
                        MediaStore.Images.Media.DATA + "=? ",
                        new String[]{filePath}, null);
                if (cursor != null && cursor.moveToFirst()) {
                     title = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DESCRIPTION));

                }

if(title != null )
{
    if (title.equals("Library")) {
        count++;
    }
}
            }
            if (count>1)
            {
                ImageView iv = (ImageView) findViewById(R.id.imageView2);
                iv.setImageResource(R.drawable.star);
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                TextView reward1 = (TextView) findViewById(R.id.reward2);
                reward1.setText("Nerd");
            }
        }


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




    }
}
