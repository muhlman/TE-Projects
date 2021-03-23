package com.techelevator.services.yelpfusion.models.business;

import com.techelevator.model.invitation.InviteeVotes;
import com.techelevator.services.yelpfusion.models.review.Review;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class BusinessDetails {

    private String id;
    private String alias;
    private String name;
    private String image_url;
    private String is_closed;
    private String url;
    private int review_count;
    private Categories[] categories;
    private float rating;
    private Location location;
    private Transactions[] transactions;
    private String price;
    private Coordinates coordinates;
    private String phone;
    private String display_phone;
    private double distance;
    private List<Photos> photos;
    private Hours[] hours;
    private Review[] reviews;
    private int day;
    private int totalThumbsUp;
    private int totalThumbsDown;
    private InviteeVotes inviteeVotes;
    private String yelpId;

    public BusinessDetails() {
        setDay();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
        setYelpId(id);
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getIs_closed() {
        return is_closed;
    }

    public void setIs_closed(String is_closed) {
        this.is_closed = is_closed;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getReview_count() {
        return review_count;
    }

    public void setReview_count(int review_count) {
        this.review_count = review_count;
    }

    public Categories[] getCategories() {
        return categories;
    }

    public void setCategories(Categories[] categories) {
        this.categories = categories;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Transactions[] getTransactions() {
        return transactions;
    }

    public void setTransactions(Transactions[] transactions) {
        this.transactions = transactions;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDisplay_phone() {
        return display_phone;
    }

    public void setDisplay_phone(String display_phone) {
        this.display_phone = display_phone;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public List<Photos> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photos> photos) {
        this.photos = photos;
    }

    public Hours[] getHours() {
        return hours;
    }

    public void setHours(Hours[] hours) {
        this.hours = hours;
    }

    public Review[] getReviews() {
        return reviews;
    }

    public void setReviews(Review[] reviews) {
        this.reviews = reviews;
    }

    public int getDay() {
        return day;
    }

    public void setDay() {
        LocalDate date = LocalDate.now();
        DayOfWeek day = date.getDayOfWeek();

        this.day = day.getValue() - 1;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getTotalThumbsUp() {
        return totalThumbsUp;
    }

    public void setTotalThumbsUp(int totalThumbsUp) {
        this.totalThumbsUp = totalThumbsUp;
    }

    public int getTotalThumbsDown() {
        return totalThumbsDown;
    }

    public void setTotalThumbsDown(int totalThumbsDown) {
        this.totalThumbsDown = totalThumbsDown;
    }

    public InviteeVotes getInviteeVotes() {
        return inviteeVotes;
    }

    public void setInviteeVotes(InviteeVotes inviteeVotes) {
        this.inviteeVotes = inviteeVotes;
    }

    public String getYelpId() {
        return yelpId;
    }

    public void setYelpId(String yelpId) {
        this.yelpId = yelpId;
    }

    @Override
    public String toString() {
        return "BusinessDetails{" +
                "id='" + id + '\'' +
                ", alias='" + alias + '\'' +
                ", name='" + name + '\'' +
                ", image_url='" + image_url + '\'' +
                ", is_closed='" + is_closed + '\'' +
                ", url='" + url + '\'' +
                ", review_count=" + review_count +
                ", categories=" + Arrays.toString(categories) +
                ", rating=" + rating +
                ", location=" + location +
                ", transactions=" + Arrays.toString(transactions) +
                ", price='" + price + '\'' +
                ", coordinates=" + coordinates +
                ", phone='" + phone + '\'' +
                ", display_phone='" + display_phone + '\'' +
                ", distance=" + distance +
                ", photos=" + photos +
                ", hours=" + Arrays.toString(hours) +
                '}';
    }
}
