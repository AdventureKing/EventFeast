
package com.example.daddyz.turtleboys.maps;

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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.daddyz.turtleboys.R;
import com.example.daddyz.turtleboys.eventfeed.gEventObject;
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
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
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

    /**
     * Used to keep track of whether geofences were added.
     */
    private boolean mGeofencesAdded;
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

            //mGeofencePendingIntent = null;

            // Get the value of mGeofencesAdded from SharedPreferences. Set to false as a default.
            //mGeofencesAdded = mSharedPreferences.getBoolean(Constants.GEOFENCES_ADDED_KEY, false);

            // Get the geofences used. Geofence data is hard coded in this sample.
            // populateGeofenceList();
            // getGeofencePendingIntent();
            // getGeofencingRequest();

            //addGeofencesButtonHandler(this);

            //Get the UI widgets.
            //mAddGeofencesButton = (Button) findViewById(R.id.add_geofences_button);
            //mRemoveGeofencesButton = (Button) findViewById(R.id.remove_geofences_button);

            //mSharedPreferences = getSharedPreferences(Constants.SHARED_PREFERENCES_NAME,
            //MODE_PRIVATE);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();

        //locationManager.requestLocationUpdates(2000, 10, LocationManager.GPS_PROVIDER, locationListener);
        //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, time, distance,  locationListener);

    }

    @Override
    protected void onStart() {
        super.onStart();
        // Connect the client.
        mGoogleApiClient.connect();

        //event object stuff
       /* mQueue = VolleyRequestQueue.getInstance(null)
                .getRequestQueue();
        String url = "http://45.55.142.106/prod/ws/rest/findEvents/San%20Antonio?city=San%20Antonio";
        final VolleyJSONObjectRequest jsonRequest = new VolleyJSONObjectRequest(Request.Method
                .GET, url,
                new JSONObject(), this, this);
        jsonRequest.setTag(REQUEST_TAG);
        mQueue.add(jsonRequest);

       /* mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mQueue.add(jsonRequest);
            }
        });*/


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

        /*if(isBetterLocation(myLocation, location)){
            myLocation = location;
        }*/

        //You had this as int. It is advised to have Lat/Long as double.
        double lat = location.getLatitude();
        double lng = location.getLongitude();

        System.out.println(venueLat);
        System.out.println(venueLng);

        if(mapCenter == null && venueLat == 0.0 && venueLng != 0.0){
            myLat = lat;
            myLng = lng;
            System.out.println(myLat);
            System.out.println(myLng);
            mapCenter = new LatLng(myLat, myLng);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(mapCenter));
        }
        if(mapCenter == null && venueLat != 0.0 && venueLng != 0.0){
            myLat = lat;
            myLng = lng;
            mapCenter = midPoint(myLat, myLng, venueLat, venueLng);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(mapCenter));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
        }

    }


    //Get Address From Location
     /*   Geocoder geoCoder = new Geocoder(this, Locale.getDefault());
        StringBuilder builder = new StringBuilder();
        try {
            List<Address> address = geoCoder.getFromLocation(lat, lng, 1);
            int maxLines = address.get(0).getMaxAddressLineIndex();
            for (int i = 0; i < maxLines; i++) {
                String addressStr = address.get(0).getAddressLine(i);
                builder.append(addressStr);
                builder.append(" ");
            }

            String finalAddress = builder.toString(); //This is the complete address.

            //latituteField.setText(String.valueOf(lat));
            //longitudeField.setText(String.valueOf(lng));
            setCurrentAddress(finalAddress);
            //Toast.makeText(getApplicationContext(), finalAddress, Toast.LENGTH_SHORT).show();


        } catch (IOException e) {}
        catch (NullPointerException e) {}

    }*/



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

            if(null == desc){

                try{
                    // Get the Bundle Object
                    Bundle bundleObject = getIntent().getExtras();

                    // Get ArrayList Bundle
                    ArrayList<gEventObject> classObject = (ArrayList<gEventObject>) bundleObject.getSerializable("eventfeedList");

                    for(int index = 0; index < classObject.size(); index++){

                        gEventObject Object = classObject.get(index);
                        Toast.makeText(getApplicationContext(), "Id is :"+Object.getTitle(), Toast.LENGTH_SHORT).show();
                    }
                } catch(Exception e){
                    e.printStackTrace();
                }

            } else if(null != desc && null != addr){
                venueLat = myIntent.getDoubleExtra("lat", 0.0);
                venueLng = myIntent.getDoubleExtra("lon", 0.0);

                mMap.addMarker(new MarkerOptions().position(new LatLng(venueLat, venueLng)).title(desc).snippet(addr));
            } else{
                venueLat = 0.0;
                venueLng = 0.0;
            }

            //for(int i = 0; i < eventfeedList.size(); i++){
            // gEventObject event = new gEventObject();
            //    mMap.addMarker(new MarkerOptions().position(new LatLng(event.getLatitude(), event.getLongitude())).title(event.getTitle()).snippet(event.getVenue_address()));
            //}


            //Get Markers For Map
            /* gEventObject object = new gEventObject();
            if(eventFeedList.get(0) != null) {
                object = eventFeedList.get(0);
            }*/
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
        System.out.println(Math.toDegrees(lat3) + " " + Math.toDegrees(lon3));
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




    /*
    *
    *
    *
    *
    *
    *
    *
    *
    * Geofence Stuff
    *
    *
    *
    *
    *
    *
    *
     */


    private PendingIntent getGeofencePendingIntent() {
        // Reuse the PendingIntent if we already have it.
        if (mGeofencePendingIntent != null) {
            return mGeofencePendingIntent;
        }
        Intent intent = new Intent(this, GeofenceTransitionsIntentService.class);
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when
        // calling addGeofences() and removeGeofences().
        return PendingIntent.getService(this, 0, intent, PendingIntent.
                FLAG_UPDATE_CURRENT);
    }

    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(mGeofenceList);
        return builder.build();
    }


    /**
     * This sample hard codes geofence data. A real app might dynamically create geofences based on
     * the user's location.
     */
    public void populateGeofenceList() {
        for (Map.Entry<String, LatLng> entry : Constants.BAY_AREA_LANDMARKS.entrySet()) {

            mGeofenceList.add(new Geofence.Builder()
                    // Set the request ID of the geofence. This is a string to identify this
                    // geofence.
                    .setRequestId(entry.getKey())

                            // Set the circular region of this geofence.
                    .setCircularRegion(
                            entry.getValue().latitude,
                            entry.getValue().longitude,
                            Constants.GEOFENCE_RADIUS_IN_METERS
                    )

                            // Set the expiration duration of the geofence. This geofence gets automatically
                            // removed after this period of time.
                    .setExpirationDuration(Constants.GEOFENCE_EXPIRATION_IN_MILLISECONDS)

                            // Set the transition types of interest. Alerts are only generated for these
                            // transition. We track entry and exit transitions in this sample.
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                            Geofence.GEOFENCE_TRANSITION_EXIT)

                            // Create the geofence.
                    .build());
        }
    }




    public void addGeofencesButtonHandler(View view) {
        if (!mGoogleApiClient.isConnected()) {
            Toast.makeText(this, getString(R.string.not_connected), Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            LocationServices.GeofencingApi.addGeofences(
                    mGoogleApiClient,
                    // The GeofenceRequest object.
                    getGeofencingRequest(),
                    // A pending intent that that is reused when calling removeGeofences(). This
                    // pending intent is used to generate an intent when a matched geofence
                    // transition is observed.
                    getGeofencePendingIntent()
            ).setResultCallback((ResultCallback<Status>) this); // Result processed in onResult().
        } catch (SecurityException securityException) {
            // Catch exception generated if the app does not use ACCESS_FINE_LOCATION permission.
            // logSecurityException(securityException);
        }
    }



    /**
     * Runs when the result of calling addGeofences() and removeGeofences() becomes available.
     * Either method can complete successfully or with an error.
     *
     * Since this activity implements the {@link ResultCallback} interface, we are required to
     * define this method.
     *
     * @param status The Status returned through a PendingIntent when addGeofences() or
     *               removeGeofences() get called.
     */
    public void onResult(Status status) {
        if (status.isSuccess()) {
            // Update state and save in shared preferences.
            mGeofencesAdded = !mGeofencesAdded;
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putBoolean(Constants.GEOFENCES_ADDED_KEY, mGeofencesAdded);
            editor.commit();

            // Update the UI. Adding geofences enables the Remove Geofences button, and removing
            // geofences enables the Add Geofences button.
            setButtonsEnabledState();

            Toast.makeText(
                    this,
                    getString(mGeofencesAdded ? R.string.geofences_added :
                            R.string.geofences_removed),
                    Toast.LENGTH_SHORT
            ).show();
        } else {
            // Get the status code for the error and log it using a user-friendly message.
            String errorMessage = GeofenceErrorMessages.getErrorString(this,
                    status.getStatusCode());
            Log.e(TAG, errorMessage);
        }
    }






    /**
     * Ensures that only one button is enabled at any time. The Add Geofences button is enabled
     * if the user hasn't yet added geofences. The Remove Geofences button is enabled if the
     * user has added geofences.
     */
    private void setButtonsEnabledState() {
        if (mGeofencesAdded) {
            mAddGeofencesButton.setEnabled(false);
            mRemoveGeofencesButton.setEnabled(true);
        } else {
            mAddGeofencesButton.setEnabled(true);
            mRemoveGeofencesButton.setEnabled(false);
        }
    }



    /*


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
        // obj.setStart_date_day(8);
        //obj.setStart_date_month("9");
        // obj.setStart_date_year("2015");
        obj.setStart_time("7:00 PM");
        obj.setVenue_name("Sunset Station");
        obj.setVenue_address("1427 Gladstone");
        obj.setEvent_external_url("urlpath");

        eventFeedList.add(obj);

    }

    public void setCurrentLocation(Location location){
        currentLocation = location;
    }

    public Location getCurrentLocation(){
        return currentLocation;
    }

    public void setCurrentAddress(String address){
        currentAddress = address;
    }

    public String getCurrentAddress(){
        return currentAddress;
    }

    public ArrayList<gEventObject> getEventfeedList() {
        return eventfeedList;
    }

    public void setEventfeedList(ArrayList<gEventObject> eventfeedList) {
        this.eventfeedList = eventfeedList;
    }
}

