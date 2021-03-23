package com.techelevator.services.yelpfusion.models.business;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Categories {
    @JsonProperty("alias")
    private String alias;
    @JsonProperty("title")
    private String title;

    public Categories() {

    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
