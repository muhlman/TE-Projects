package com.techelevator.model.invitation;

import java.sql.Timestamp;
import java.util.List;

import com.techelevator.services.yelpfusion.models.business.BusinessDetails;

public class Invitee {
	private long inviteId;
	private String uniqueId;
	private int userId;
	private String name;
	private String email;
	private Boolean hasVoted;
	private String isAttending;
	private List<BusinessDetails> businessDetails;
	private Timestamp deadline;
	private boolean deadlinePassed;
	private String reservationDate;

	public Invitee() {
		
	}

	public long getInviteId() {
		return inviteId;
	}

	public void setInviteId(long inviteId) {
		this.inviteId = inviteId;
	}

	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Boolean getHasVoted() {
		return hasVoted;
	}

	public void setHasVoted(Boolean hasVoted) {
		this.hasVoted = hasVoted;
	}

	public String getIsAttending() {
		return isAttending;
	}

	public void setIsAttending(String isAttending) {
		this.isAttending = isAttending;
	}

	public Timestamp getDeadline() {
		return deadline;
	}

	public void setDeadline(Timestamp deadline) {
		this.deadline = deadline;
		Timestamp date = new Timestamp(System.currentTimeMillis());

		if (this.deadline.after(date)) {
			setDeadlinePassed(false);
		} else {
			setDeadlinePassed(true);
		}
	}

	public String getReservationDate() {
		return reservationDate;
	}

	public void setReservationDate(String reservationDate) {
		this.reservationDate = reservationDate;
	}

	public List<BusinessDetails> getBusinessDetails() {
		return businessDetails;
	}

	public void setBusinessDetails(List<BusinessDetails> businessDetails) {
		this.businessDetails = businessDetails;
	}

	public boolean isDeadlinePassed() {
		return deadlinePassed;
	}

	public void setDeadlinePassed(boolean deadlinePassed) {
		this.deadlinePassed = deadlinePassed;
	}
}
