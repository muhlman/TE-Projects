package com.techelevator.services.yelpfusion.models.review;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Review {
    private String id;
    private String url;
    @JsonProperty("text")
    private String reviewSlug;
    private double rating;
    private String time_created;
    private ReviewUser user;

    public Review() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getReviewSlug() {
        return reviewSlug;
    }

    public void setReviewSlug(String reviewSlug) {
        this.reviewSlug = reviewSlug;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getTime_created() {
        return time_created;
    }

    public void setTime_created(String time_created) {
        this.time_created = time_created;
    }

    public ReviewUser getUser() {
        return user;
    }

    public void setUser(ReviewUser user) {
        this.user = user;
    }
}
