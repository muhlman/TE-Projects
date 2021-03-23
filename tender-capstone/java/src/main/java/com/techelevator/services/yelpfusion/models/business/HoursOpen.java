package com.techelevator.services.yelpfusion.models.business;

public class HoursOpen {

    private boolean is_overnight;
    private String start;
    private String end;
    private int day;

    public HoursOpen() {
    }

    public boolean isIs_overnight() {
        return is_overnight;
    }

    public void setIs_overnight(boolean is_overnight) {
        this.is_overnight = is_overnight;
    }

    public String getStart() {
        return start;
    }

    public void setStart(int start) {
        String openTime = "";

        if (start >= 1300) {
            openTime = String.valueOf(start - 1200) + " PM";
        } else {
            openTime = String.valueOf(start) + " AM";
        }

        if (openTime.length() == 7) {
           openTime = openTime.substring(0,2) + ":" + openTime.substring(2);
        } else {
            openTime = openTime.substring(0,1) + ":" + openTime.substring(1);
        }

        this.start = openTime;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(int end) {
        String endTime = "";

        if (end >= 1300) {
            endTime = String.valueOf(end - 1200) + " PM";
        } else if(end == 0000) {
            endTime = "1200 AM";
        } else {
            endTime = String.valueOf(end) + " AM";
        }

        if (endTime.length() == 7) {
            endTime = endTime.substring(0,2) + ":" + endTime.substring(2);
        } else {
            endTime = endTime.substring(0,1) + ":" + endTime.substring(1);
        }

        this.end = endTime;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }
}
