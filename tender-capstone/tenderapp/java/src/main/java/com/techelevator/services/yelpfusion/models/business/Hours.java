package com.techelevator.services.yelpfusion.models.business;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Clock;
import java.time.DayOfWeek;
import java.time.LocalDate;

public class Hours {
    @JsonProperty("open")
    private HoursOpen[] hoursopen;
    private String hours_type;
    private boolean is_open_now;



    public Hours() {
    }

    public HoursOpen[] getHoursopen() {
        return hoursopen;
    }

    public void setHoursopen(HoursOpen[] hoursopen) {
        this.hoursopen = hoursopen;
    }

    public String getHours_type() {
        return hours_type;
    }

    public void setHours_type(String hours_type) {
        this.hours_type = hours_type;
    }

    public boolean isIs_open_now() {
        return is_open_now;
    }

    public void setIs_open_now(boolean is_open_now) {
        this.is_open_now = is_open_now;
    }



}
