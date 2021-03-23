package com.techelevator.model.invitation;

public class InviteeVotes {

    private boolean thumbs_up;
    private boolean thumbs_down;
    private int inviteId;
    private String uniqueId;
    private String yelpId;

    public InviteeVotes() {
    }

    public boolean isThumbs_up() {
        return thumbs_up;
    }

    public void setThumbs_up(boolean thumbs_up) {
        this.thumbs_up = thumbs_up;
    }

    public boolean isThumbs_down() {
        return thumbs_down;
    }

    public void setThumbs_down(boolean thumbs_down) {
        this.thumbs_down = thumbs_down;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public int getInviteId() {
        return inviteId;
    }

    public void setInviteId(int inviteId) {
        this.inviteId = inviteId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getYelpId() {
        return yelpId;
    }

    public void setYelpId(String yelpId) {
        this.yelpId = yelpId;
    }
}
