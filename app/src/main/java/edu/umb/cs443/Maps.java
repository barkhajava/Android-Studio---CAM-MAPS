package edu.umb.cs443;


import android.app.Activity;
import android.content.ClipData;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.SparseArray;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;
import com.google.maps.android.ui.SquareTextView;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
//import com.example.android;


public class Maps extends Menu implements OnMapReadyCallback {

    private TextView mStatusView;
    private TextView mStatusAllView;
    public GoogleMap mMap;
    private ArrayList<Bitmap> mBitmapsSelected;
    private ArrayList<Uri> mArrayUri;
    private ImageView capturedImage;
    URI photoUri;
    public final static int PICK_PHOTO_CODE = 1046;
    int numberOfFiles;
    File[] files;
    private int mtype = 0;
    Double lat;
    Double lon;
    String title;
    public ClusterManager<clustermarker> clusterManager;
    private List<clustermarker> items = new ArrayList<>();

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mapsview);
        //    capturedImage = (ImageView) findViewById(R.id.capturedImage);
        MapFragment mFragment = ((MapFragment) getFragmentManager().findFragmentById(R.id.map));
        mFragment.getMapAsync(this);

        // Get count of images on the specified path
        File dir = new File("/storage/emulated/0/DCIM/Camera/");
        files = dir.listFiles();
        if (files != null) {

            numberOfFiles = files.length;
        }
    }


    @Override
    public void onMapReady(GoogleMap map) {
        this.mMap = map;

        this.mMap.getUiSettings().setZoomControlsEnabled(true);
        this.mMap.getUiSettings().setZoomGesturesEnabled(true);
        this.mMap.getUiSettings().setAllGesturesEnabled(true);
        clusterManager = new ClusterManager<>(this, mMap);
        clusterManager.setRenderer(new MyClusterItemRenderer(this, mMap, clusterManager));
        ////////////////////////

        if (mMap != null) {


            for (int i = 0; i < numberOfFiles; i++) {
                Bitmap bitmap = BitmapFactory.decodeFile(String.valueOf(files[i]));
                Bitmap smallMarker = Bitmap.createScaledBitmap(bitmap, 104, 104, false);
                ContentValues values = new ContentValues();
                //  CameraUpdate umb = CameraUpdateFactory.newLatLng(new LatLng(42.313895, -71.038833));
                String filePath = files[i].getAbsolutePath();
                Cursor cursor = getContentResolver().query(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        new String[]{MediaStore.Images.Media.LATITUDE, MediaStore.Images.Media.LONGITUDE, MediaStore.MediaColumns.DISPLAY_NAME},
                        MediaStore.Images.Media.DATA + "=? ",
                        new String[]{filePath}, null);
                if (cursor != null && cursor.moveToFirst()) {
                    lat = cursor.getDouble(cursor.getColumnIndex(MediaStore.Images.Media.LATITUDE));
                    lon = cursor.getDouble(cursor.getColumnIndex(MediaStore.Images.Media.LONGITUDE));
                    title = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME));

                    cursor.close();

                }


                //   mMap.setOnCameraIdleListener(clusterManager);
                // mMap.setOnMarkerClickListener(clusterManager);
                //mMap.setOnInfoWindowClickListener(clusterManager);
                mMap.setOnCameraIdleListener(clusterManager);
                mMap.setOnMarkerClickListener(clusterManager);
                   mMap.addMarker(new MarkerOptions()
                       .title(title)
                       .position(new LatLng(lat, lon))
                     .icon(BitmapDescriptorFactory.fromBitmap(smallMarker)));


                clusterManager.addItem(new clustermarker(new LatLng(lat, lon)));
                //  clustermarker offsetItem = new clustermarker(new LatLng(lat, lon));
                //clusterManager.addItem(offsetItem);
                //   clustermarker offsetItem = new clustermarker(new LatLng(lat, lon));
                // clusterManager.addItem(offsetItem);

//mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                //   @Override
                //  public boolean onMarkerClick(Marker smallMarker) {
                //addPersonItems();
                //    clusterManager.getMarkerCollection().getMarkers().size();

                //  return true;
                //}
//});
                mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                    @Override
                    public void onMapLongClick(LatLng latLng) {

                    }
                });
                //

            }
            //s clusterManager.cluster();

            clusterManager.cluster();
        }


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void b1(View v) {

        if (mMap != null) {
            if (mtype == 0) {
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                mtype = 1;
            } else if (mtype == 1) {
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                mtype = 2;
            } else {
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                mtype = 0;
            }

        }
    }


    private void addPersonItems() {
// Set some lat/lng coordinates to start with.

        // Add ten cluster items in close proximity, for purposes of this example.
        // for (int i = 0; i < 10; i++) {
        //    double offset = 1 / 60d;
        //  lat = lat + offset;
        //lon = lon + offset;


        //clusterManager.addItem(new clustermarker(new LatLng(lat,lon)));
        // clusterManager.
        //clusterManager.cluster();
    }


    /**
     * Draws profile photos inside markers (using IconGenerator).
     * When there are multiple people in the cluster, draw multiple photos (using MultiDrawable).
     */
    public class MyClusterItemRenderer extends DefaultClusterRenderer<clustermarker> implements ClusterManager.OnClusterClickListener<clustermarker> {

        public  IconGenerator mIconGenerator;
        public  ShapeDrawable mColoredCircleBackground;
        public  float mDensity;

        private SparseArray<BitmapDescriptor> mIcons = new SparseArray();

        public MyClusterItemRenderer(Context context, GoogleMap map, ClusterManager<clustermarker> clusterManager) {
            super(context, map, clusterManager);
            mIconGenerator = new IconGenerator(context);
            mColoredCircleBackground = new ShapeDrawable(new OvalShape());
            mDensity = context.getResources().getDisplayMetrics().density;

            SquareTextView squareTextView = new SquareTextView(context);
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(-2, -2);
            squareTextView.setLayoutParams(layoutParams);
            squareTextView.setId(com.google.maps.android.R.id.amu_text);
            int twelveDpi = (int) (12.0F * this.mDensity);
            squareTextView.setPadding(twelveDpi, twelveDpi, twelveDpi, twelveDpi);
            this.mIconGenerator.setContentView(squareTextView);

            mIconGenerator.setTextAppearance(com.google.maps.android.R.style.amu_Bubble_TextAppearance_Dark);

            ShapeDrawable outline = new ShapeDrawable(new OvalShape());
            outline.getPaint().setColor(Color.parseColor("#FFFFFF"));
            LayerDrawable background = new LayerDrawable(new Drawable[]{outline, this.mColoredCircleBackground});
            int strokeWidth = (int) (this.mDensity * 3.0F);
            background.setLayerInset(1, strokeWidth, strokeWidth, strokeWidth, strokeWidth);
            mIconGenerator.setBackground(background);




    }

        @Override
        protected boolean shouldRenderAsCluster(Cluster<clustermarker> cluster) {
            return cluster.getSize() >= 1;
        }


        @Override
        protected void onBeforeClusterRendered(Cluster<clustermarker> cluster, MarkerOptions markerOptions) {
            int bucket = this.getBucket(cluster);
            BitmapDescriptor descriptor = (BitmapDescriptor)this.mIcons.get(bucket);
            if(descriptor == null) {
                this.mColoredCircleBackground.getPaint().setColor(this.getColor(bucket));
                descriptor = BitmapDescriptorFactory.fromBitmap(this.mIconGenerator.makeIcon(String.valueOf(cluster.getSize())));
                this.mIcons.put(bucket, descriptor);
            }

            markerOptions.icon(descriptor);
        }

        @Override
        public boolean onClusterClick(Cluster<clustermarker> cluster) {
            // Show a toast with some info when the cluster is clicked.
            String firstName = cluster.getItems().iterator().next().getTitle();
            //Toast.makeText(this, cluster.getSize() + " (including " + firstName + ")", Toast.LENGTH_SHORT).show();

            // Zoom in the cluster. Need to create LatLngBounds and including all the cluster items
            // inside of bounds, then animate to center of the bounds.

            // Create the builder to collect all essential cluster items for the bounds.
            LatLngBounds.Builder builder = LatLngBounds.builder();
            for (ClusterItem item : cluster.getItems()) {
                builder.include(item.getPosition());
            }
            // Get the LatLngBounds
            final LatLngBounds bounds = builder.build();

            // Animate camera to the bounds
            try {
                mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
            } catch (Exception e) {
                e.printStackTrace();
            }

            return true;
        }
    }
    }