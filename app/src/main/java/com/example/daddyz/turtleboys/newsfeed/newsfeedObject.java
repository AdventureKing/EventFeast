package com.example.daddyz.turtleboys.newsfeed;

import com.example.daddyz.turtleboys.eventfeed.gEventObject;

/**
 * Created by snow on 7/11/2015.
 */
public class newsfeedObject extends gEventObject {
    private String postImageUrl;
    private int likeCount;
    private String title;
    private String description;
    private String username;

    public String getPostImageUrl() {
        return postImageUrl;
    }

    public void setPostImageUrl(String postImageUrl) {
        this.postImageUrl = postImageUrl;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
