package edu.umb.cs443;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.FileOutputStream;



public class Menu extends Activity {
    private String mActivityName;
    private TextView mStatusView;
    private TextView mStatusAllView;
    final int REQUEST_IMAGE_CAPTURE = 1;


    private ImageView capturedImage;
    private Button btnCamera;
    private Button btnMaps;
    private int GALLERY = 1;
    String ImagePath;
    private static final String IMAGE_DIRECTORY = "/";
    public String pictureFilePath;
    String currentPhotoPath;
    Uri file;
    static final int REQUEST_PICTURE_CAPTURE = 1;

    private FileOutputStream outStream;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        mActivityName = "Menu";
        btnCamera = (Button) findViewById(R.id.button_3);
        btnMaps = (Button) findViewById(R.id.button_4);
        capturedImage = (ImageView) findViewById(R.id.capturedImage);

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openCamera();
                //choosePhotoFromGallery();
            }
        });


    }

    private void openCamera() {
        Intent intent = new Intent(Menu.this, edu.umb.cs443.SaveImage.class);
        startActivity(intent);

    }


    public void startActivityMaps(View view) {
        Intent intent = new Intent(Menu.this, edu.umb.cs443.Maps.class);
        startActivity(intent);
    }


    public void startActivityRewards(View view) {
        Intent intent = new Intent(Menu.this, edu.umb.cs443.Rewards.class);
        startActivity(intent);
    }
    public void finishActivityMenu(View v) {
        Menu.this.finish();
    }


}
