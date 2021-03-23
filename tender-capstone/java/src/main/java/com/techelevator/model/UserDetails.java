package com.techelevator.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

public class UserDetails {
    private long userId;
    private String address;
    private String city;
    private String state;
    private int zip;
    private int default_radius;
    private List<Integer> activeCategoryId;
    private String searchCategories;

    public UserDetails() {
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getZip() {
        return zip;
    }

    public void setZip(int zip) {
        this.zip = zip;
    }

    public int getDefault_radius() {
        return default_radius;
    }

    public void setDefault_radius(int default_radius) {
        this.default_radius = default_radius;
    }

    public List<Integer> getActiveCategoryId() {
        return activeCategoryId;
    }

    public void setActiveCategoryId(List<Integer> activeCategoryId) {
        this.activeCategoryId = activeCategoryId;
    }

    public String getSearchCategories() {
        return searchCategories;
    }

    public void setSearchCategories(String searchCategories) {
        this.searchCategories = searchCategories;
    }

}
