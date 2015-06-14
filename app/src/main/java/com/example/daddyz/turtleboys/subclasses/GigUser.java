package com.example.daddyz.turtleboys.subclasses;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseUser;

/**
 * Created by snow on 6/14/2015.
 */
@ParseClassName("_User")
public class GigUser extends ParseUser {


    public GigUser(){

    }


    //getters
    public ParseFile getUserImage(){
        ParseFile user = getParseFile("userImage");
        return user;
    }


    //setters
    public void setUserImage(ParseFile file){
        put("userImage",file);

    }

}
