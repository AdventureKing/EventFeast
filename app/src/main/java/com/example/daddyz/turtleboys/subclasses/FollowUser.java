package com.example.daddyz.turtleboys.subclasses;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.ParseGeoPoint;

import java.util.Date;

/**
 * Created by richardryangarcia on 7/17/15.
 */
public class FollowUser  {

    private int follow_id, status;
    private String follower_user_id;
    private String followed_user_id;

    public FollowUser(int follow_id, int status, String follower_user_id, String followed_user_id){
        this.follow_id = follow_id;
        this.status = status;
        this.follower_user_id = follower_user_id;
        this.followed_user_id = followed_user_id;
    }

    public FollowUser(){
        this.follow_id = 0;
        this.status = 0;
        this.follower_user_id=null;
        this.followed_user_id=null;
    }

    //setters
    private void setFollowId(int follow_id){
        this.follow_id = follow_id;
    }

    private void setStatus(int status){
        this.status = status;
    }

    private void setFollowerUserId(String follower_user_id){
        this.follower_user_id = follower_user_id;
    }

    private void setFollowedUserId(String followed_user_id){
        this.followed_user_id = followed_user_id;
    }

    //getters
    public int getFollowId(){
        return follow_id;
    }
    public int getStatus(){
        return status;
    }
    public String getFollowerUserId(){
        return follower_user_id;
    }
    public String getFollowedUserId(){
        return followed_user_id;
    }

}
