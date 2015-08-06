package com.example.yoyohongkong;


import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DecimalFormat;
import java.util.StringTokenizer;

public class MapsActivity extends FragmentActivity implements GoogleMap.OnMarkerClickListener, LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    public GoogleMap mMap; // Might be null if Google Play services APK is not available.

    LocationManager locationManager;
    LocationProvider locationProvider;
    LooperThread thread;
    private Marker markers;
    private int r = 0;

    public boolean lock = false, act_lock = false;
    private double longitude1, longitude2, longitude3, longitude4, longitude5;
    private double latitude1, latitude2, latitude3, latitude4, latitude5;
    private String name1, name2, name3, name4, name5;

    private int q1_flag = 0, q2_flag = 0, q3_flag = 0, q4_flag = 0, q5_flag = 0;

    private int zoom, id, area_index;
    private Bundle bundle;
    private LayoutInflater inflater;
    private AlertDialog.Builder dialog;
    private double mylatitude, mylongitude;
    public GoogleApiClient mGoogleApiClient;
    public Location mLastLocation;

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;


    // boolean flag to toggle periodic location updates
    private boolean mRequestingLocationUpdates = false;
    private LocationRequest mLocationRequest;
    // Location updates intervals in sec
    private static int UPDATE_INTERVAL = 1000; // 5 sec
    private static int FATEST_INTERVAL = 1000; // 5 sec
    private static int DISPLACEMENT = 5; // 10 meters

    private KeyguardManager km;
    private  KeyguardManager.KeyguardLock kl;

    private PowerManager pm;
    private PowerManager.WakeLock mWakeLock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        pm = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock((PowerManager.PARTIAL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP| PowerManager.ON_AFTER_RELEASE), "bright");


        km = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
        kl = km.newKeyguardLock("My_App");

        initBundle();

        setUpMapIfNeeded();

        if (checkPlayServices()) {

            // Building the GoogleApi client
            buildGoogleApiClient();

            createLocationRequest();

        }


    }
/*
    final Runnable runnable = new Runnable() {
        public void run() {
            // TODO Auto-generated method stub
            // 需要背景作的事
            getdistance();
            try {
                for (int i = 0; i < 10; i++) {
                    Thread.sleep(1000);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
*/

    @Override
    protected void onStart() {
        super.onStart();
        loadPreference();
        //Toast.makeText(MapsActivity.this, String.valueOf(q1_flag) + String.valueOf(q2_flag) + String.valueOf(q3_flag) + String.valueOf(q4_flag) + String.valueOf(q5_flag), Toast.LENGTH_SHORT).show();

        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();

        }

    }


    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        act_lock = true;

    }


    /**
     * Method to display the location on UI
     */
    private void getdistance() {//***********************  HERE

        mLastLocation = LocationServices.FusedLocationApi
                .getLastLocation(mGoogleApiClient);

        if (mLastLocation != null) {
            mylatitude = mLastLocation.getLatitude();
            mylongitude = mLastLocation.getLongitude();
        }
        checkdistance();
    }


    /**
     * Creating google api client object
     */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    /**
     * Creating location request object
     */
    protected void createLocationRequest() {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FATEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    }

    /**
     * Method to verify google play services on the device
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "This device is not supported.", Toast.LENGTH_LONG)
                        .show();
                finish();
            }
            return false;
        }
        return true;
    }

    /**
     * Starting the location updates
     */
    protected void startLocationUpdates() {

        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);


    }

    /**
     * Stopping location updates
     */
    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
    }

    /**
     * Google api callback methods
     */
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i("GPS", "Connection failed: ConnectionResult.getErrorCode() = "
                + result.getErrorCode());
    }

    @Override
    public void onConnected(Bundle arg0) { //************   THREAD

        // Once connected with google api, get the location


        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }
        thread = new LooperThread();
        thread.start();

    }

    @Override
    public void onConnectionSuspended(int arg0) {
        mGoogleApiClient.connect();

    }


    @Override
    public void onLocationChanged(Location location) {
        // Assign the new location
        mLastLocation = location;
        getdistance();
        Toast.makeText(getApplicationContext(), "Location changed!",
                Toast.LENGTH_SHORT).show();

        // Displaying the new location on UI

    }


    @Override
    protected void onResume() {
        super.onResume();
        checkPlayServices();
        setUpMapIfNeeded();


        // Resuming the periodic location updates
        if (mGoogleApiClient.isConnected() && mRequestingLocationUpdates) {
            startLocationUpdates();
            act_lock = false;

        }


    }


    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */

    private void initBundle() {
        bundle = this.getIntent().getExtras();
        area_index = bundle.getInt("area");

        latitude1 = bundle.getDouble("latitude1");
        longitude1 = bundle.getDouble("longitude1");
        name1 = bundle.getString("Q1");

        latitude2 = bundle.getDouble("latitude2");
        longitude2 = bundle.getDouble("longitude2");
        name2 = bundle.getString("Q2");

        latitude3 = bundle.getDouble("latitude3");
        longitude3 = bundle.getDouble("longitude3");
        name3 = bundle.getString("Q3");

        latitude4 = bundle.getDouble("latitude4");
        longitude4 = bundle.getDouble("longitude4");
        name4 = bundle.getString("Q4");

        latitude5 = bundle.getDouble("latitude5");
        longitude5 = bundle.getDouble("longitude5");
        name5 = bundle.getString("Q5");

    }

    public void setUpMap() {


        zoom = bundle.getInt("zoom");


        LatLng area = new LatLng(latitude2, longitude2);
        mMap.setMyLocationEnabled(true);
        //mMap.setOnMarkerClickListener(this);

        mMap.setInfoWindowAdapter(new myinfoadapter());

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(area, zoom));

        markers = mMap.addMarker(new MarkerOptions()//1
                .position(new LatLng(latitude1, longitude1))
                .title(name1));

        markers = mMap.addMarker(new MarkerOptions()//2
                .position(new LatLng(latitude2, longitude2))
                .title(name2));

        markers = mMap.addMarker(new MarkerOptions()//3
                .position(new LatLng(latitude3, longitude3))
                .title(name3));

        markers = mMap.addMarker(new MarkerOptions()//4
                .position(new LatLng(latitude4, longitude4))
                .title(name4));

        markers = mMap.addMarker(new MarkerOptions()//5
                .position(new LatLng(latitude5, longitude5))
                .title(name5));


    }


    @Override//useless
    public boolean onMarkerClick(Marker marker) {//useless

        inflater = LayoutInflater.from(MapsActivity.this);
        final View v = inflater.inflate(R.layout.dialog, null);
        dialog = new AlertDialog.Builder(MapsActivity.this);
        ImageView image_dialog = (ImageView) (v.findViewById(R.id.image_dialog));
        String title = marker.getTitle();
        checkImage(title);


        image_dialog.setImageResource(checkImage(title));

        dialog.setTitle(title)
                .setView(v)
                .setPositiveButton("back", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();


        return false;

    }


    private int checkImage(String marker_title) {//image
        switch (marker_title) {

            case "Avenue of Stars":
                return R.drawable.a1q1;

            case "Harbour city":
                return R.drawable.a1q2;

            case "Hong Kong Museum of Art":
                return R.drawable.a1q3;

            case "Hong Kong Clock Tower":
                return R.drawable.a1q4;

            case "Ferry Pier":
                return R.drawable.a1q5;

            ///
            case "City Express":
                return R.drawable.a2q1;

            case "Festival Walk":
                return R.drawable.a2q2;

            case "Student Residence of CityU":
                return R.drawable.a2q3;

            case "CityU AC3":
                return R.drawable.a2q4;

            case "City University of Hong Kong":
                return R.drawable.a2q5;

            /////
            case "Cheung Po Tsai Cave":
                return R.drawable.a3q1;

            case "Cheung Chau Public Pier":
                return R.drawable.a3q2;

            case "Sai Wan Tin Hau Temple":
                return R.drawable.a3q3;

            case "Pak Tai Temple":
                return R.drawable.a3q4;

            case "Kate Dessert":
                return R.drawable.a3q5;

            /////
            case "Chinese University of Hong Kong":
                return R.drawable.a4q1;

            case "Shing Mun River":
                return R.drawable.a4q2;

            case "Buddhas monastery":
                return R.drawable.a4q3;

            case "Sha Tin Racecourse":
                return R.drawable.a4q4;

            case "Hong Kong Heritage Museum":
                return R.drawable.a4q5;

        }
        return id;
    }

    public int checkdistance() {
        thread.interrupt();
        if (CalculationByDistance(mylatitude, mylongitude, latitude1, longitude1) <= 0.025 && q1_flag == 0 && !act_lock) {

            q_a(1);
        } else if (CalculationByDistance(mylatitude, mylongitude, latitude2, longitude2) <= 0.025 && q2_flag == 0 && !act_lock) {

            q_a(2);
        } else if (CalculationByDistance(mylatitude, mylongitude, latitude3, longitude3) <= 0.025 && q3_flag == 0 && !act_lock) {

            q_a(3);
        } else if (CalculationByDistance(mylatitude, mylongitude, latitude4, longitude4) <= 0.025 && q4_flag == 0 && !act_lock) {

            q_a(4);
        } else if (CalculationByDistance(mylatitude, mylongitude, latitude5, longitude5) <= 0.025 && q5_flag == 0 && !act_lock) {

            q_a(5);
        }


        if (lock || act_lock) {

        } else {
            thread = new LooperThread();
            thread.start();
        }

        return 0;
    }


    private void q_a(final int index) {
        lock = true;
        final String[] items = getResources().getStringArray(getquetion_array(area_index, index));
        String q = getResources().getString(getquetion_string(area_index, index));



        mWakeLock.acquire();
        kl.disableKeyguard();


        AlertDialog dialog = new AlertDialog.Builder(getApplicationContext())
                .setTitle(q)
                .setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        if (items[which].equals(getResources().getString(getanswer_string(area_index, index)))) {
                            Toast.makeText(MapsActivity.this, "correct", Toast.LENGTH_SHORT).show();

                            savePreferences(index);
                            //Toast.makeText(MapsActivity.this, String.valueOf(q1_flag) + String.valueOf(q2_flag) + String.valueOf(q3_flag) + String.valueOf(q4_flag) + String.valueOf(q5_flag), Toast.LENGTH_SHORT).show();
                            loadPreference();
                            //Toast.makeText(MapsActivity.this, String.valueOf(q1_flag) + String.valueOf(q2_flag) + String.valueOf(q3_flag) + String.valueOf(q4_flag) + String.valueOf(q5_flag), Toast.LENGTH_SHORT).show();
                            kl.reenableKeyguard();
                            mWakeLock.release();
                            lock = false;
                            thread = new LooperThread();
                            thread.start();


                        } else {
                            Toast.makeText(MapsActivity.this, "Incorrect", Toast.LENGTH_SHORT).show();
                            kl.reenableKeyguard();
                            mWakeLock.release();
                            lock = false;
                            thread = new LooperThread();
                            thread.start();

                        }
                    }
                }).create();

        dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        dialog.show();


    }

    public void savePreferences(int q_index) {
        SharedPreferences preferences = getSharedPreferences(String.valueOf(area_index), MODE_PRIVATE);
        if (q_index == 1) {
            preferences.edit().putInt("Q1", 1).commit();
            q1_flag = 1;
        } else if (q_index == 2) {
            preferences.edit().putInt("Q2", 1).commit();
            q2_flag = 1;
        } else if (q_index == 3) {
            preferences.edit().putInt("Q3", 1).commit();
            q3_flag = 1;
        } else if (q_index == 4) {
            preferences.edit().putInt("Q4", 1).commit();
            q4_flag = 1;
        } else if (q_index == 5) {
            preferences.edit().putInt("Q5", 1).commit();
            q5_flag = 1;
        }
        //Toast.makeText(MapsActivity.this, String.valueOf(q1_flag)+String.valueOf(q2_flag)+String.valueOf(q3_flag)+String.valueOf(q4_flag)+String.valueOf(q5_flag) , Toast.LENGTH_SHORT).show();
    }

    public void loadPreference() {
        SharedPreferences preferences = getSharedPreferences(String.valueOf(area_index), MODE_PRIVATE);
        q1_flag = preferences.getInt("Q1", 0);
        q2_flag = preferences.getInt("Q2", 0);
        q3_flag = preferences.getInt("Q3", 0);
        q4_flag = preferences.getInt("Q4", 0);
        q5_flag = preferences.getInt("Q5", 0);

    }

    public double CalculationByDistance(double latitude1, double longitude1, double latitude2, double longitude2) {
        int Radius = 6371;//radius of earth in Km
        double lat1 = latitude1;
        double lat2 = latitude2;
        double lon1 = longitude1;
        double lon2 = longitude2;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        Integer kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        Integer meterInDec = Integer.valueOf(newFormat.format(meter));
        System.out.println("Radius Value " + valueResult + "   KM  " + kmInDec + " Meter   " + meterInDec);

        return Radius * c;
    }

    public int getquetion_array(int A, int Q) {

        if (A == 1 && Q == 1)
            return R.array.Tsim_Sha_Tsui_Q1;
        else if (A == 1 && Q == 2)
            return R.array.Tsim_Sha_Tsui_Q2;
        else if (A == 1 && Q == 3)
            return R.array.Tsim_Sha_Tsui_Q3;
        else if (A == 1 && Q == 4)
            return R.array.Tsim_Sha_Tsui_Q4;
        else if (A == 1 && Q == 5)
            return R.array.Tsim_Sha_Tsui_Q5;

        else if (A == 2 && Q == 1)
            return R.array.cityu_Q1;
        else if (A == 2 && Q == 2)
            return R.array.cityu_Q2;
        else if (A == 2 && Q == 3)
            return R.array.cityu_Q3;
        else if (A == 2 && Q == 4)
            return R.array.cityu_Q4;
        else if (A == 2 && Q == 5)
            return R.array.cityu_Q5;

        else if (A == 3 && Q == 1)
            return R.array.Cheung_Chau_Q1;
        else if (A == 3 && Q == 2)
            return R.array.Cheung_Chau_Q2;
        else if (A == 3 && Q == 3)
            return R.array.Cheung_Chau_Q3;
        else if (A == 3 && Q == 4)
            return R.array.Cheung_Chau_Q4;
        else if (A == 3 && Q == 5)
            return R.array.Cheung_Chau_Q5;

        return 0;

    }

    public int getquetion_string(int A, int Q) {

        if (A == 1 && Q == 1)
            return R.string.Tsim_Sha_Tsui_Q1;
        else if (A == 1 && Q == 2)
            return R.string.Tsim_Sha_Tsui_Q2;
        else if (A == 1 && Q == 3)
            return R.string.Tsim_Sha_Tsui_Q3;
        else if (A == 1 && Q == 4)
            return R.string.Tsim_Sha_Tsui_Q4;
        else if (A == 1 && Q == 5)
            return R.string.Tsim_Sha_Tsui_Q5;

        else if (A == 2 && Q == 1)
            return R.string.cityu_Q1;
        else if (A == 2 && Q == 2)
            return R.string.cityu_Q2;
        else if (A == 2 && Q == 3)
            return R.string.cityu_Q3;
        else if (A == 2 && Q == 4)
            return R.string.cityu_Q4;
        else if (A == 2 && Q == 5)
            return R.string.cityu_Q5;

        else if (A == 3 && Q == 1)
            return R.string.Cheung_Chau_Q1;
        else if (A == 3 && Q == 2)
            return R.string.Cheung_Chau_Q2;
        else if (A == 3 && Q == 3)
            return R.string.Cheung_Chau_Q3;
        else if (A == 3 && Q == 4)
            return R.string.Cheung_Chau_Q4;
        else if (A == 3 && Q == 5)
            return R.string.Cheung_Chau_Q5;

        return 0;

    }

    public int getanswer_string(int A, int Q) {

        if (A == 1 && Q == 1)
            return R.string.Tsim_Sha_Tsui_A1;
        else if (A == 1 && Q == 2)
            return R.string.Tsim_Sha_Tsui_A2;
        else if (A == 1 && Q == 3)
            return R.string.Tsim_Sha_Tsui_A3;
        else if (A == 1 && Q == 4)
            return R.string.Tsim_Sha_Tsui_A4;
        else if (A == 1 && Q == 5)
            return R.string.Tsim_Sha_Tsui_A5;

        else if (A == 2 && Q == 1)
            return R.string.cityu_A1;
        else if (A == 2 && Q == 2)
            return R.string.cityu_A2;
        else if (A == 2 && Q == 3)
            return R.string.cityu_A3;
        else if (A == 2 && Q == 4)
            return R.string.cityu_A4;
        else if (A == 2 && Q == 5)
            return R.string.cityu_A5;

        else if (A == 3 && Q == 1)
            return R.string.Cheung_Chau_A1;
        else if (A == 3 && Q == 2)
            return R.string.Cheung_Chau_A2;
        else if (A == 3 && Q == 3)
            return R.string.Cheung_Chau_A3;
        else if (A == 3 && Q == 4)
            return R.string.Cheung_Chau_A4;
        else if (A == 3 && Q == 5)
            return R.string.Cheung_Chau_A5;

        return 0;

    }


    public class myinfoadapter implements GoogleMap.InfoWindowAdapter {
        @Override
        public View getInfoWindow(Marker marker) {
            return null;
        }

        @Override
        public View getInfoContents(Marker marker) {

            View infoWindow = getLayoutInflater().inflate(R.layout.myinfowindows, null);

            TextView title = ((TextView) infoWindow.findViewById(R.id.infotext));
            title.setText(marker.getTitle());

            ImageView imageView = (ImageView) infoWindow.findViewById(R.id.infoimage);
            imageView.setImageResource(checkImage(marker.getTitle()));


            return infoWindow;


        }
    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //r = r + 1;
            //Toast.makeText(MapsActivity.this, String.valueOf(r), Toast.LENGTH_SHORT).show();

            getdistance();


        }
    };

    class LooperThread extends Thread {

        public void run() {
            Looper.prepare();
            try {
                thread.sleep(5000);
                handler.sendEmptyMessage(0);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Looper.loop();
        }


    }


}

