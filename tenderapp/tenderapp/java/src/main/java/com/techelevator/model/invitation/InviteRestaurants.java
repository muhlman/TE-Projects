package com.techelevator.model.invitation;

public class InviteRestaurants {
	private long inviteId;
	private String yelpId;
	private int thumbsUp;
	private int thumbsDown;

public InviteRestaurants() {
	
}

public long getInviteId() {
	return inviteId;
}

public void setInviteId(long inviteId) {
	this.inviteId = inviteId;
}

public String getYelpId() {
	return yelpId;
}

public void setYelpId(String yelpId) {
	this.yelpId = yelpId;
}

public int getThumbsUp() {
	return thumbsUp;
}

public void setThumbsUp(int thumbsUp) {
	this.thumbsUp = thumbsUp;
}

public int getThumbsDown() {
	return thumbsDown;
}

public void setThumbsDown(int thumbsDown) {
	this.thumbsDown = thumbsDown;
}



}