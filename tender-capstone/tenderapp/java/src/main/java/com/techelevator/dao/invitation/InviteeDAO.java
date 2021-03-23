package com.techelevator.dao.invitation;


import java.util.List;

import com.techelevator.model.invitation.Invitee;

public interface InviteeDAO {

    List<Invitee> getInviteeById(Long inviteId);

    Invitee createInvitee(Invitee invitee);

    Invitee updateInviteeStatus(Invitee invitee);

    Invitee getInviteeByUniqueId(String uniqueId);

    
}
