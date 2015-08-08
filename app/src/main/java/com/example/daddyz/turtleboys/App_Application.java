package com.example.daddyz.turtleboys;

import android.app.Application;
import android.location.Location;
import android.util.Log;

import com.example.daddyz.turtleboys.subclasses.GigUser;
import com.example.daddyz.turtleboys.subclasses.LocationFinder;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseInstallation;
import com.parse.ParseUser;

/**
 * Created by snow on 6/6/2015.
 */
public class App_Application extends Application {
    private Location currentLocation;
    private String currentAddress;

    private Boolean userInfoUpdateFlag = false;

    @Override public void onCreate() {
        Parse.enableLocalDatastore(this);

        //subclasses
        ParseUser.registerSubclass(GigUser.class);

        Parse.initialize(this, "3YwirBYrXg3tZ9nLiCR5F75FyWR2shvOIVHixtjX", "ERFTgdWUdDO2vjuwwNVasuG8rybwagtUfxWhe4dJ");
        ParseInstallation.getCurrentInstallation().saveInBackground();

        // Set users current location based off of userHome
        findCurrentLocation();
    }

    public void findCurrentLocation(){
        LocationFinder.LocationResult locationResult = new LocationFinder.LocationResult(){
            @Override
            public void gotLocation(Location location){
                LocationFinder myLocation = new LocationFinder();
                setCurrentAddress(myLocation.getAddressFromLocation(getApplicationContext(), location));
                setCurrentLocation(location);
                Log.i("location:",location.toString());

                // Update userHome parse data store value
                ParseGeoPoint geoPoint = new ParseGeoPoint();
                geoPoint.setLatitude(location.getLatitude());
                geoPoint.setLongitude(location.getLongitude());

                if(null != ParseUser.getCurrentUser()){
                    GigUser currentUser = ParseUser.createWithoutData(GigUser.class, ParseUser.getCurrentUser().getObjectId());
                    currentUser.setUserHome(geoPoint);
                    try{
                        currentUser.save();
                        Log.i("Updated", "User Home" + currentUser.getFirstName());
                    } catch(ParseException e){
                        e.printStackTrace();
                    }
                }
            }
        };
        LocationFinder myLocation = new LocationFinder();
        myLocation.getLocation(this.getApplicationContext(), locationResult);
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }

    public String getCurrentAddress() {
        return currentAddress;
    }

    public void setCurrentAddress(String currentAddress) {
        this.currentAddress = currentAddress;
    }

    public Boolean getUserInfoUpdateFlag(){
        return userInfoUpdateFlag;
    }

    public void setUserInfoUpdateFlag(Boolean newFlagValue){
        this.userInfoUpdateFlag = newFlagValue;
    }
}
