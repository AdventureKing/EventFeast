package com.example.daddyz.turtleboys;

import android.app.Application;

import com.example.daddyz.turtleboys.subclasses.GigUser;
import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParseUser;

/**
 * Created by snow on 6/6/2015.
 */
public class App_Application extends Application {

    private Boolean userInfoUpdateFlag = false;

    @Override public void onCreate() {



        Parse.enableLocalDatastore(this);


        //subclasses
        ParseUser.registerSubclass(GigUser.class);

        Parse.initialize(this, "3YwirBYrXg3tZ9nLiCR5F75FyWR2shvOIVHixtjX", "ERFTgdWUdDO2vjuwwNVasuG8rybwagtUfxWhe4dJ");
        ParseInstallation.getCurrentInstallation().saveInBackground();
    }

    public Boolean getUserInfoUpdateFlag(){
        return userInfoUpdateFlag;
    }

    public void setUserInfoUpdateFlag(Boolean newFlagValue){
        this.userInfoUpdateFlag = newFlagValue;
    }
}
