package com.example.daddyz.turtleboys;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseInstallation;

/**
 * Created by snow on 6/6/2015.
 */
public class App_Application extends Application {

    @Override public void onCreate() {

        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "3YwirBYrXg3tZ9nLiCR5F75FyWR2shvOIVHixtjX", "ERFTgdWUdDO2vjuwwNVasuG8rybwagtUfxWhe4dJ");
        ParseInstallation.getCurrentInstallation().saveInBackground();
    }
}
