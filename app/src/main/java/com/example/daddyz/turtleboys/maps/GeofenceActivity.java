package com.example.daddyz.turtleboys.maps;

import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.daddyz.turtleboys.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class GeofenceActivity extends FragmentActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener{

    private static final String TAG = "Geofence notice" ;
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    /**
     * Coordinates for the Geofence.
     */
    private LatLng mGeofenceLatLng = new LatLng(29.435474, -98.481296);

    /**
     * Radius of the Geofence in meters.
     */
    private int mRadius = 80;

    /**
     * The Geofence object.
     */
    private Geofence mGeofence;

    private GoogleApiClient mLocationClient;

    /**
     * Used to set the priority and intervals of the location requests.
     */
    private LocationRequest mLocationRequest;

    /**
     * Visuals
     */
    private CircleOptions mCircleOptions;
    private Circle mCircle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geofence);

        /**
         * We create a new LocationClient which is used as an entry point for Google's location
         * related APIs. The first parameter is the context, the second is
         * GooglePlayServicesClient.ConnectionCallbacks, and the third is
         * GooglePlayServicesClient.OnConnectionFailedListener. Since we implemented both listeners
         * on the MainActivity class, we pass 'this' for the second and third parameters.
         */

        mLocationClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        /**
         * With the LocationRequest, we can set the quality of service. For example, the priority
         * and intervals.
         */
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        mLocationRequest.setInterval(3600000);
        mLocationRequest.setFastestInterval(60000);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Connect to the location APIs.
        mLocationClient.connect();
    }

    protected void onStop() {
        // Disconnect from the location APIs.
        mLocationClient.disconnect();
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (GooglePlayServicesUtil.isGooglePlayServicesAvailable(this) == ConnectionResult.SUCCESS) {
            setUpMapIfNeeded();
        } else {
            GooglePlayServicesUtil.getErrorDialog(
                    GooglePlayServicesUtil.isGooglePlayServicesAvailable(this),
                    this, 0);
        }
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the
        // map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map)).getMap();

            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the
     * camera. In this case, we just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap}
     * is not null.
     */
    private void setUpMap() {
        // Centers the camera over the building and zooms int far enough to
        // show the floor picker.
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(mGeofenceLatLng.latitude, mGeofenceLatLng.longitude), 18));
        // Hide labels.
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mMap.setIndoorEnabled(false);
        mMap.setMyLocationEnabled(true);

        // Adding visuals.
        mCircleOptions = new CircleOptions()
                .center(mGeofenceLatLng).radius(mRadius).fillColor(0x40ff0000)
                .strokeColor(Color.TRANSPARENT).strokeWidth(2);
        mCircle = mMap.addCircle(mCircleOptions);

    }

    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.v("GEOFENCE", "Connection to LocationClient failed!");
    }

    @Override
    public void onConnected(Bundle arg0) {

        Log.v("GEOFENCE", "Connected to location services.");

        ArrayList<Geofence> geofences = new ArrayList<Geofence>();

        /**
         * The addGeofences function requires that the Geofences be in a List, so there can be
         * multiple geofences. For this example we will only need one.
         */
        mGeofence = new Geofence.Builder()
                .setRequestId("Geofence")
                        // There are three types of Transitions.
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_DWELL | Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
                        // Set the geofence location and radius.
                .setCircularRegion(mGeofenceLatLng.latitude, mGeofenceLatLng.longitude, mRadius)
                        // How long the geofence will remain in place.
                .setExpirationDuration((1000 * 60) * 60)
                        // This is required if you specify GEOFENCE_TRANSITION_DWELL when setting the transition types.
                .setLoiteringDelay(1000)
                .build();

        /**
         * Adding the geofence to the ArrayList, which will be passed as the first parameter
         * to the LocationClient object's addGeofences function.
         */
        geofences.add(mGeofence);

        /**
         * We're creating a PendingIntent that references the ReceiveTransitionsIntentService class
         * in conjunction with the geofences.
         */
        Intent intent = new Intent(this, ReceiveTransitionsIntentService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        /**
         * We want this (MainActivity) to handle location updates.(see onLocationChanged function)
         */
       // mLocationClient.requestLocationUpdates(mLocationRequest, this);
        /**
         * Adding the Geofences and PendingIntent to the LocationClient and setting this
         * (MainActivity) to handle onAddGeofencesResult. The pending intent, which is the
         * ReceiveTransitionsIntentService, is what gets utilized when one of the transitions
         * that was specified in the geofence is fired.
         */
        //mLocationClient.addGeofences(geofences, pendingIntent, this);

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    public void onDisconnected() {
        Log.v("GEOFENCE", "Disconnected");
    }

    @Override
    public void onLocationChanged(Location location) {
        /**
         * Location data is passed back to this function.
         */
        Toast.makeText(this, "Location Changed: " + location.getLatitude() + ", " + location.getLongitude(), Toast.LENGTH_LONG).show();
    }

    public void onAddGeofencesResult(int statusCode, String[] geofenceRequestIds) {

        switch(statusCode) {
            case LocationStatusCodes.SUCCESS:
                Log.v(TAG, "Successfully added Geofence.");
                break;
            case LocationStatusCodes.ERROR:
                Log.v(TAG, "Error adding Geofence.");
                break;
            case LocationStatusCodes.GEOFENCE_TOO_MANY_GEOFENCES:
                Log.v(TAG, "Too many geofences.");
                break;
            case LocationStatusCodes.GEOFENCE_TOO_MANY_PENDING_INTENTS:
                Log.v(TAG, "Too many pending intents.");
                break;
        }
    }

}
