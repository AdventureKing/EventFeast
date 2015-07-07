package com.example.daddyz.turtleboys.eventfeed;

import java.util.ArrayList;

/**
 * Created by zachary.rodriguez on 7/6/2015.
 */
public class gEventObject {

    private String internal_id = "";
    private String external_id = "";
    private String datasource = "";
    private String event_external_url = "";
    private String title = "";
    private String description = "";
    private String notes = "";
    private String timezone = "";
    private String timezone_abbr = "";
    private String start_time = "";
    private String end_time = "";
    private ArrayList<String> start_date_month = new ArrayList<>();
    private ArrayList<String> start_date_day = new ArrayList<>();
    private ArrayList<String> start_date_year = new ArrayList<>();
    private ArrayList<String> start_date_time = new ArrayList<>();
    private ArrayList<String> end_date_month = new ArrayList<>();
    private ArrayList<String> end_date_day = new ArrayList<>();
    private ArrayList<String> end_date_year = new ArrayList<>();
    private ArrayList<String> end_date_time = new ArrayList<>();
    private String venue_external_id = "";
    private String venue_external_url = "";
    private String venue_name = "";
    private String venue_display = "";
    private String venue_address = "";
    private String state_name = "";
    private String city_name = "";
    private String postal_code = "";
    private String country_name = "";
    private Boolean all_day = false;
    private String price_range = "";
    private String is_free = null;
    private ArrayList<String> major_genre = new ArrayList<>();
    private ArrayList<String> minor_genre = new ArrayList<>();
    private double latitude = 0;
    private double longitude = 0;
    private ArrayList<gEventPerformerObject> performers = new ArrayList<>();
    private ArrayList<gEventImageObject> images = new ArrayList<>();

    public String getInternal_id() {
        return internal_id;
    }

    public void setInternal_id(String internal_id) {
        this.internal_id = internal_id;
    }

    public String getExternal_id() {
        return external_id;
    }

    public void setExternal_id(String external_id) {
        this.external_id = external_id;
    }

    public String getDatasource() {
        return datasource;
    }

    public void setDatasource(String datasource) {
        this.datasource = datasource;
    }

    public String getEvent_external_url() {
        return event_external_url;
    }

    public void setEvent_external_url(String event_external_url) {
        this.event_external_url = event_external_url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getTimezone_abbr() {
        return timezone_abbr;
    }

    public void setTimezone_abbr(String timezone_abbr) {
        this.timezone_abbr = timezone_abbr;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public ArrayList<String> getStart_date_month() {
        return start_date_month;
    }

    public void setStart_date_month(ArrayList<String> start_date_month) {
        this.start_date_month = start_date_month;
    }

    public ArrayList<String> getStart_date_day() {
        return start_date_day;
    }

    public void setStart_date_day(ArrayList<String> start_date_day) {
        this.start_date_day = start_date_day;
    }

    public ArrayList<String> getStart_date_year() {
        return start_date_year;
    }

    public void setStart_date_year(ArrayList<String> start_date_year) {
        this.start_date_year = start_date_year;
    }

    public ArrayList<String> getStart_date_time() {
        return start_date_time;
    }

    public void setStart_date_time(ArrayList<String> start_date_time) {
        this.start_date_time = start_date_time;
    }

    public ArrayList<String> getEnd_date_month() {
        return end_date_month;
    }

    public void setEnd_date_month(ArrayList<String> end_date_month) {
        this.end_date_month = end_date_month;
    }

    public ArrayList<String> getEnd_date_day() {
        return end_date_day;
    }

    public void setEnd_date_day(ArrayList<String> end_date_day) {
        this.end_date_day = end_date_day;
    }

    public ArrayList<String> getEnd_date_year() {
        return end_date_year;
    }

    public void setEnd_date_year(ArrayList<String> end_date_year) {
        this.end_date_year = end_date_year;
    }

    public ArrayList<String> getEnd_date_time() {
        return end_date_time;
    }

    public void setEnd_date_time(ArrayList<String> end_date_time) {
        this.end_date_time = end_date_time;
    }

    public String getVenue_external_id() {
        return venue_external_id;
    }

    public void setVenue_external_id(String venue_external_id) {
        this.venue_external_id = venue_external_id;
    }

    public String getVenue_external_url() {
        return venue_external_url;
    }

    public void setVenue_external_url(String venue_external_url) {
        this.venue_external_url = venue_external_url;
    }

    public String getVenue_name() {
        return venue_name;
    }

    public void setVenue_name(String venue_name) {
        this.venue_name = venue_name;
    }

    public String getVenue_display() {
        return venue_display;
    }

    public void setVenue_display(String venue_display) {
        this.venue_display = venue_display;
    }

    public String getVenue_address() {
        return venue_address;
    }

    public void setVenue_address(String venue_address) {
        this.venue_address = venue_address;
    }

    public String getState_name() {
        return state_name;
    }

    public void setState_name(String state_name) {
        this.state_name = state_name;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getPostal_code() {
        return postal_code;
    }

    public void setPostal_code(String postal_code) {
        this.postal_code = postal_code;
    }

    public String getCountry_name() {
        return country_name;
    }

    public void setCountry_name(String country_name) {
        this.country_name = country_name;
    }

    public Boolean getAll_day() {
        return all_day;
    }

    public void setAll_day(Boolean all_day) {
        this.all_day = all_day;
    }

    public String getPrice_range() {
        return price_range;
    }

    public void setPrice_range(String price_range) {
        this.price_range = price_range;
    }

    public String getIs_free() {
        return is_free;
    }

    public void setIs_free(String is_free) {
        this.is_free = is_free;
    }

    public ArrayList<String> getMajor_genre() {
        return major_genre;
    }

    public void setMajor_genre(ArrayList<String> major_genre) {
        this.major_genre = major_genre;
    }

    public ArrayList<String> getMinor_genre() {
        return minor_genre;
    }

    public void setMinor_genre(ArrayList<String> minor_genre) {
        this.minor_genre = minor_genre;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public ArrayList<gEventPerformerObject> getPerformers() {
        return performers;
    }

    public void setPerformers(ArrayList<gEventPerformerObject> performers) {
        this.performers = performers;
    }

    public ArrayList<gEventImageObject> getImages() {
        return images;
    }

    public void setImages(ArrayList<gEventImageObject> images) {
        this.images = images;
    }
}
