package com.techelevator.services.yelpfusion.models.business;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Photos {

    private String photoURL;

    public Photos(String photos) {
        this.photoURL = photos;

    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }
}
