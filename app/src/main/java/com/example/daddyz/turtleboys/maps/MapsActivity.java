
package com.example.daddyz.turtleboys.maps;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.daddyz.turtleboys.R;
import com.example.daddyz.turtleboys.eventfeed.gEventObject;
import com.example.daddyz.turtleboys.registration_activity;
import com.example.daddyz.turtleboys.subclasses.GigUser;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;




public class MapsActivity extends FragmentActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,Response.Listener,
        Response.ErrorListener {

    private final String TAG = "MyAwesomeMapApp";
    private TextView mLocationView;
    // Buttons for kicking off the process of adding or removing geofences.
    private Button mAddGeofencesButton;
    private Button mRemoveGeofencesButton;
    private RequestQueue mQueue;
    private ArrayList<gEventObject> eventfeedList;
    private ArrayList<String> eventStringArray;
    private TextView tvDisplayDate;
    private DatePicker dpResult;
    private Button btnChangeDate;
    private DialogFragment eventDateSelector;
    private Date eventDate;
    private EditText eventDateText;

    private int year;
    private int month;
    private int day;


    static final int DATE_DIALOG_ID = 999;

    /**
     * Used to keep track of whether geofences were added.
     */
    private boolean mGeofencesAdded;
    private String tempString;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private LocationManager locationManager ;
    private long time = 2000;
    private LatLng  mapCenter = null;
    private float distance = (float) 10.00;
    public static final String REQUEST_TAG = "User List Fragment";
    /**
     * Used to persist application state about whether geofences were added.
     */
    private SharedPreferences mSharedPreferences;
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private static final int TWO_MINUTES = 1000 * 60 * 2;
    private Location currentLocation = null;
    private LocationListener locationListener;
    private String s, lat, lon, currentAddress;
    private Double myLat, myLng, venueLat, venueLng = 0.0;
    private String[] split,coordinates;
    private Location myLocation, newLocation = null;
    protected ArrayList<Geofence> mGeofenceList = new ArrayList<Geofence>();
    private ArrayList<gEventObject> eventFeedList = new ArrayList<gEventObject>();
    private LatLng mGeofenceLatLng = new LatLng(29.382798, -98.529470);
    /**
     * Radius of the Geofence in meters.
     */
    private int mRadius = 80;

    private double l,g;
    /**
     * Used when requesting to add or remove geofences.
     */
    private PendingIntent mGeofencePendingIntent;

    public MapsActivity(){

    }

    public MapsActivity(ArrayList<gEventObject> eventFeedList){
        this.eventfeedList = eventFeedList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (GigUser.getCurrentUser() == null){
            buildGoogleApiClient();
        }else {
            setContentView(R.layout.activity_maps);
            //setContentView(mLocationView);
            // mLocationView = new TextView(this);

            setUpEventList();
            setUpMapIfNeeded();

            // Kick off the request to build GoogleApiClient.
            buildGoogleApiClient();

            //setCurrentDateOnView();
            //addListenerOnButton();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Connect the client.
        mGoogleApiClient.connect();

    }

    @Override
    protected void onStop() {
        // Disconnecting the client invalidates it.
        mGoogleApiClient.disconnect();
        super.onStop();

        if (mQueue != null) {
            mQueue.cancelAll(REQUEST_TAG);
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(1000); // Update location every second

        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "GoogleApiClient connection has been suspend");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "GoogleApiClient connection has failed");
    }

    @Override
    public void onLocationChanged(Location location) {

        //You had this as int. It is advised to have Lat/Long as double.
        double lat = location.getLatitude();
        double lng = location.getLongitude();


        if(mapCenter == null && (venueLat == 0.0 || venueLat == null) && (venueLng == null || venueLng == 0.0)){
            myLat = lat;
            myLng = lng;
            mapCenter = new LatLng(myLat, myLng);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(mapCenter));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
        }
        if(mapCenter == null && venueLat != 0.0 && venueLng != 0.0){
            myLat = lat;
            myLng = lng;
            mapCenter = midPoint(myLat, myLng, venueLat, venueLng);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(mapCenter));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
        }

    }

    /**
     * Builds a GoogleApiClient. Uses the {@code #addApi} method to request the LocationServices API.
     */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
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


    private void setUpMap() {

        // Enable MyLocation Layer of Google Map
        mMap.setMyLocationEnabled(true);

        // Get LocationManager object from System Service LOCATION_SERVICE
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // set map type
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        // Get the name of the best provider
        String networkProvider = locationManager.NETWORK_PROVIDER;
        String gpsProvider = locationManager.GPS_PROVIDER;

        // Get Best Current Location
        myLocation = locationManager.getLastKnownLocation(networkProvider);
        newLocation = locationManager.getLastKnownLocation(gpsProvider);
        boolean loc = isBetterLocation(myLocation, newLocation);

        if(loc == true){
            myLocation = newLocation;
        }

        if(myLocation != null) {
            // Get latitude of the current location
            double latitude = myLocation.getLatitude();

            // Get longitude of the current location
            double longitude = myLocation.getLongitude();

            // Create a LatLng object for the current location
            LatLng latLng = new LatLng(latitude, longitude);

            // Show the current location in Google Map
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

            // Zoom in the Google Map
            mMap.animateCamera(CameraUpdateFactory.zoomTo(14));


            //Add Marker For event
            Intent myIntent = getIntent();
            String desc = myIntent.getStringExtra("desc");
            String addr = myIntent.getStringExtra("addr");
            gEventObject eObject = (gEventObject) myIntent.getParcelableExtra("event1");
            venueLat = myIntent.getDoubleExtra("lat", 0.0);
            venueLng = myIntent.getDoubleExtra("lon", 0.0);
            mMap.addMarker(new MarkerOptions().position(new LatLng(venueLat, venueLng)).title(desc).snippet(addr));


            eventStringArray = myIntent.getStringArrayListExtra("eventArrayOfStrings");
            if(eventStringArray != null && eventStringArray.size() > 0) {
                for (int i = 0; i < eventStringArray.size(); i++) {
                    split = eventStringArray.get(i).split(" /// ");
                    mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(split[1]), Double.parseDouble(split[2]))).title(split[0]).snippet(split[3]));

                }
            }
        }
    }


    public static LatLng midPoint(double lat1,double lon1,double lat2,double lon2){

        LatLng newCenter = null;
        double dLon = Math.toRadians(lon2 - lon1);

        //convert to radians
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);
        lon1 = Math.toRadians(lon1);

        double Bx = Math.cos(lat2) * Math.cos(dLon);
        double By = Math.cos(lat2) * Math.sin(dLon);
        double lat3 = Math.atan2(Math.sin(lat1) + Math.sin(lat2), Math.sqrt((Math.cos(lat1) + Bx) * (Math.cos(lat1) + Bx) + By * By));
        double lon3 = lon1 + Math.atan2(By, Math.cos(lat1) + Bx);

        //print out in degrees
        newCenter = new LatLng(Math.toDegrees(lat3), Math.toDegrees(lon3));
        return newCenter;
    }



    /** Determines whether one Location reading is better than the current Location fix
     * @param location  The new Location that you want to evaluate
     * @param currentBestLocation  The current Location fix, to which you want to compare the new one
     */
    protected boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }

    /** Checks whether two providers are the same */
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }

    // display current date
    public void setCurrentDateOnView() {

        //tvDisplayDate = (TextView) findViewById(R.id.tvDate);
        //dpResult = (DatePicker) findViewById(R.id.dpResult);

        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        // set current date into textview
        tvDisplayDate = new TextView(getApplicationContext());
        tvDisplayDate.setText(new StringBuilder()
                // Month is 0 based, just add 1
              .append(month + 1).append("-").append(day).append("-").append(year).append(" "));

        // set current date into datepicker
        dpResult.init(year, month, day, null);

    }

    public void addListenerOnButton() {

       /* btnChangeDate = (Button) findViewById(R.id.datePicker);

        btnChangeDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Button Pressed", Toast.LENGTH_SHORT).show();
                showDialog(DATE_DIALOG_ID);

            }

        });*/

        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        eventDate = new Date(year,month,day);

        eventDateSelector = new registration_activity.DatePickerFragment();
        eventDateText = (EditText) findViewById(R.id.eventDate);
        eventDateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eventDateSelector.registerForContextMenu(eventDateText);
                eventDateSelector.show(getFragmentManager(), "Date Picker");
            }
        });

        String[] dates = eventDateText.getText().toString().split("/");
        if ( dates.length < 3 ) {
            Toast.makeText(getApplicationContext(), "Select Event Date" , Toast.LENGTH_SHORT).show();
            return;
        }

        int tempMonth=Integer.parseInt(dates[0]);
        tempMonth -= 1;
        int tempYear =Integer.parseInt(dates[2]);
        tempYear -= 1900;
        int tempDay = Integer.parseInt(dates[1]);



        eventDate = new Date(tempYear,tempMonth,tempDay);

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                // set date picker as current date
                return new DatePickerDialog(this, datePickerListener,
                        year, month,day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener datePickerListener
            = new DatePickerDialog.OnDateSetListener() {


        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;
            tvDisplayDate = new TextView(getApplicationContext());
            dpResult = new DatePicker(getApplicationContext());
            Toast.makeText(getApplicationContext(), "Event Works" , Toast.LENGTH_SHORT).show();


            // set selected date into textview
            tvDisplayDate.setText(new StringBuilder().append(month + 1)
                    .append("-").append(day).append("-").append(year)
                    .append(" "));

            // set selected date into datepicker also
            dpResult.init(year, month, day, null);



        }
    };


    /* Volley Stuff */

    @Override
    public void onResponse(Object response) {
        //loadEvents(response);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        //  mTextView.setText(error.getMessage());
    }

/*
*
*
*
*
*
*
*
* Setters and Getters
*
*
*
*
*
*
 */


    private void setUpEventList(){

        gEventObject obj = new gEventObject();

        obj.setDescription("desc");
        obj.setCity_name("city");
        obj.setState_name("state");
        obj.setLatitude(29.421264);
        obj.setLongitude(-98.478011);
        obj.setStart_time("7:00 PM");
        obj.setVenue_name("Sunset Station");
        obj.setVenue_address("1427 Gladstone");
        obj.setEvent_external_url("urlpath");

        eventFeedList.add(obj);

    }

}

