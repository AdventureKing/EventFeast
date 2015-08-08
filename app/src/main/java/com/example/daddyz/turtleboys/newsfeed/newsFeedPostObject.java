package com.example.daddyz.turtleboys.newsfeed;

/**
 * Created by zachrdz on 8/7/15.
 */
public class newsFeedPostObject {
    String userId;
    String eventId;
    String message;
    boolean isExternalEvent;
    double userLat;
    double userLong;

    public newsFeedPostObject(){
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isExternalEvent() {
        return isExternalEvent;
    }

    public void setIsExternalEvent(boolean isExternalEvent) {
        this.isExternalEvent = isExternalEvent;
    }

    public double getUserLat() {
        return userLat;
    }

    public void setUserLat(double userLat) {
        this.userLat = userLat;
    }
}
