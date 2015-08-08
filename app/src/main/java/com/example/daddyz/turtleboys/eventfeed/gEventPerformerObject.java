package com.example.daddyz.turtleboys.eventfeed;

/**
 * Created by zachary.rodriguez on 7/6/2015.
 */
public class gEventPerformerObject {
    private String performer_external_id;
    private String performer_external_url;
    private String performer_external_image_url;
    private String performer_name;
    private String performer_short_bio;

    public String getPerformer_short_bio() {
        return performer_short_bio;
    }

    public void setPerformer_short_bio(String performer_short_bio) {
        this.performer_short_bio = performer_short_bio;
    }

    public String getPerformer_external_id() {
        return performer_external_id;
    }

    public void setPerformer_external_id(String performer_external_id) {
        this.performer_external_id = performer_external_id;
    }

    public String getPerformer_external_url() {
        return performer_external_url;
    }

    public void setPerformer_external_url(String performer_external_url) {
        this.performer_external_url = performer_external_url;
    }

    public String getPerformer_external_image_url() {
        return performer_external_image_url;
    }

    public void setPerformer_external_image_url(String performer_external_image_url) {
        this.performer_external_image_url = performer_external_image_url;
    }

    public String getPerformer_name() {
        return performer_name;
    }

    public void setPerformer_name(String performer_name) {
        this.performer_name = performer_name;
    }
}
