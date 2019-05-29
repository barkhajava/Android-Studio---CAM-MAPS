package edu.umb.cs443;

import android.content.Context;
import android.content.Intent;
import android.app.Activity;
import android.media.Image;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class MainActivity extends FragmentActivity  {
    public TextView ytemp;
    String xtemp;
    Double lat;
    Double lon;
    String icon;
    public static final String WIFI = "Wi-Fi";
    public static final String ANY = "Any";
    // Whether there is a Wi-Fi connection.
    private static boolean wifiConnected = false;
    // Whether there is a mobile connection.
    private static boolean mobileConnected = false;
    // Whether the display should be refreshed.
    public static boolean refreshDisplay = true;
    // The user's current network preference setting.
    public static String sPref = null;
    private static final String TAG = HttpHandler.class.getSimpleName();

    public final static String DEBUG_TAG = "edu.umb.cs443.MYMSG";
    public static String URL;

  private String stringUrl ;
    ImageView imageView;
    private GoogleMap mMap;



    public void startActivityMenu(View v) {
        Intent intent = new Intent(MainActivity.this, edu.umb.cs443.Menu.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        ImageView iv = (ImageView)findViewById(R.id.imageView);
       iv.setImageResource(R.drawable.name);
        //MapFragment mFragment = ((MapFragment) getFragmentManager().findFragmentById(R.id.map));
        //mFragment.getMapAsync(this);
    }


    //@Override
   // public void onMapReady(GoogleMap map) {
      //  this.mMap = map;
    //}
    @Override
    public void onStart() {
        super.onStart();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void loadPage() {
        updateConnectedFlags();
        Log.i("myTag", sPref + " " + Boolean.toString(wifiConnected));


        if (((sPref.equals(ANY)) && (wifiConnected || mobileConnected))
                || ((sPref.equals(WIFI)) && (wifiConnected))) {
            // AsyncTask subclass
        //    new DownloadXmlTask().execute(URL);

        } else {
            showErrorPage();
        }
    }

    // Checks the network connection and sets the wifiConnected and mobileConnected
    // variables accordingly.
    private void updateConnectedFlags() {
        ConnectivityManager connMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeInfo = connMgr.getActiveNetworkInfo();

        if (activeInfo != null && activeInfo.isConnected()) {
            wifiConnected = activeInfo.getType() == ConnectivityManager.TYPE_WIFI;
            mobileConnected = activeInfo.getType() == ConnectivityManager.TYPE_MOBILE;
        } else {
            wifiConnected = false;
            mobileConnected = false;
        }
    }
    // Displays an error if the app is unable to load content.
    private void showErrorPage() {
        setContentView(R.layout.main);

        // The specified network connection is not available. Displays error message.
        WebView myWebView = (WebView) findViewById(R.id.webview);
        myWebView.loadData(getResources().getString(R.string.connection_error),
                "text/html", null);
    }



        //@Override
       // protected void onPostExecute(String s) {
        //    super.onPostExecute(s);
       //      stringUrl = "http://openweathermap.org/img/w/"+icon+".png";

         //   CameraUpdate center=CameraUpdateFactory.newLatLng(new LatLng(lat, lon));
           // CameraUpdate zoom=CameraUpdateFactory.zoomTo(12);

            //if (mMap!=null) {
              //  mMap.moveCamera(center);
               // mMap.animateCamera(zoom);
               // mMap.addMarker(new MarkerOptions()
                      // .getIcon()
                  //      .position(new LatLng(lat, lon)));
            //}

        //}

    }

