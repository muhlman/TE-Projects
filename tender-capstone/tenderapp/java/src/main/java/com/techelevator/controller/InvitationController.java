package com.techelevator.controller;

import java.util.List;

import com.techelevator.model.invitation.InviteeVotes;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.techelevator.dao.invitation.InvitationDAO;
import com.techelevator.dao.invitation.InviteRestaurantsDAO;
import com.techelevator.dao.invitation.InviteeDAO;
import com.techelevator.model.invitation.Invitation;
import com.techelevator.model.invitation.InviteRestaurants;
import com.techelevator.model.invitation.Invitee;

@RestController
@CrossOrigin
@RequestMapping(value = "/invitation")
public class InvitationController {
	
	private final InvitationDAO invitationDAO;
	private final InviteeDAO inviteeDAO;
	private final InviteRestaurantsDAO inviteRestaurantsDAO;
	
	public InvitationController(InvitationDAO invitationDAO, InviteeDAO inviteeDAO, InviteRestaurantsDAO inviteRestaurantsDAO) {
		this.invitationDAO = invitationDAO;
		this.inviteeDAO = inviteeDAO;
		this.inviteRestaurantsDAO = inviteRestaurantsDAO;
	}

	@RequestMapping(value = "/{inviteId}", method = RequestMethod.GET)
	public Invitation getInviteById(@PathVariable Long inviteId) {
		return invitationDAO.getInviteById(inviteId);
	}

	@RequestMapping(value = "/user/{creatorId}", method = RequestMethod.GET)
	public List<Invitation> findInviteByUserId(@PathVariable int creatorId) {
		return invitationDAO.findInviteByUserId(creatorId);
	}

	@ResponseStatus(HttpStatus.CREATED)
	@RequestMapping(method = RequestMethod.POST)
	public Invitation create(@RequestBody Invitation invitation) {
		return invitationDAO.create(invitation);
	}
	
	@RequestMapping(value = "/invite/{inviteId}", method = RequestMethod.GET)
	public List<Invitee> getInviteeById(@PathVariable Long inviteId) {
		return inviteeDAO.getInviteeById(inviteId);
	}

	@RequestMapping(value = "/invitee", method = RequestMethod.PUT)
	public Invitee updateInviteeStatus(@RequestBody Invitee invitee) {
		return inviteeDAO.updateInviteeStatus(invitee);
	}

	@RequestMapping(value = "/restaurant/{inviteId}", method = RequestMethod.GET)
	public List<InviteRestaurants> getInviteRestaurantById(@PathVariable Long inviteId) {
		return inviteRestaurantsDAO.getInviteRestaurantById(inviteId);
	}

	@RequestMapping(value = "/invitee/{uniqueId}", method = RequestMethod.GET)
	public Invitee getInviteeByUniqueId(@PathVariable String uniqueId) {
		return inviteeDAO.getInviteeByUniqueId(uniqueId);
	}

	@RequestMapping(value = "/thumbsup", method = RequestMethod.PUT)
	public boolean voteThumbsUp(@RequestBody InviteeVotes inviteeVotes) {
				inviteRestaurantsDAO.voteThumbsUp(inviteeVotes);
				return true;
	}

	@RequestMapping(value = "/thumbsdown", method = RequestMethod.PUT)
	public boolean voteThumbsDown(@RequestBody InviteeVotes inviteeVotes) {
		inviteRestaurantsDAO.voteThumbsDown(inviteeVotes);
		return true;
	}

	@RequestMapping(value = "/removevote", method = RequestMethod.PUT)
	public boolean removeVote(@RequestBody InviteeVotes inviteeVotes) {
		inviteRestaurantsDAO.removeVote(inviteeVotes);
		return true;
	}

	@RequestMapping(value = "/removeallvotes", method = RequestMethod.PUT)
	public boolean removeAllVotes(@RequestBody InviteeVotes inviteeVotes) {
		inviteRestaurantsDAO.removeAllVotesForUser(inviteeVotes);
		return true;
	}

}
