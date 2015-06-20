package com.example.daddyz.turtleboys.subclasses;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.util.Date;

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
    public Date getBirthday(){
        Date user = getDate("birthday");
        return user;
    }
    public String getFirstName(){
        String first = getString("firstName");
        return first;
    }
    public String getLastName(){
        String first = getString("lastName");
        return first;
    }


    //setters
    public void setUserImage(ParseFile file){
        put("userImage",file);

    }
    public void setBirthday(Date date){
        put("birthday",date);
    }
    public void setFirstName(String first){
        put("firstName",first);
    }
    public void setLastName(String last){
        put("lastName",last);
    }


}
