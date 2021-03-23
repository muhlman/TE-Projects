package com.techelevator.dao.invitation;

import java.util.List;

import com.techelevator.model.invitation.InviteRestaurants;
import com.techelevator.model.invitation.InviteeVotes;

public interface InviteRestaurantsDAO {

	List<InviteRestaurants> getInviteRestaurantById(Long inviteId);
	
	InviteRestaurants create(InviteRestaurants listOfChoices);
	
	boolean voteThumbsUp(InviteeVotes inviteeVote);
	
	boolean voteThumbsDown(InviteeVotes inviteeVote);

	boolean removeVote(InviteeVotes inviteeVote);

	void removeAllVotesForUser(InviteeVotes inviteeVote);
}
