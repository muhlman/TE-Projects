package com.techelevator.dao.invitation;

import java.sql.Array;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.techelevator.model.invitation.InviteRestaurants;
import com.techelevator.model.invitation.Invitee;
import com.techelevator.services.yelpfusion.YelpFusion;
import com.techelevator.services.yelpfusion.models.business.BusinessDetails;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import com.techelevator.model.invitation.Invitation;

@Component
public class InvitationSqlDAO implements InvitationDAO {

    private JdbcTemplate jdbcTemplate;
    private InviteeDAO inviteeDAO;
    private InviteRestaurantsDAO inviteRestaurantsDAO;
    private YelpFusion yelpFusion;

    public InvitationSqlDAO(JdbcTemplate jdbcTemplate, InviteeDAO inviteeDAO, InviteRestaurantsDAO inviteRestaurantsDAO, YelpFusion yelpFusion) {
        this.jdbcTemplate = jdbcTemplate;
        this.inviteeDAO = inviteeDAO;
        this.inviteRestaurantsDAO = inviteRestaurantsDAO;
        this.yelpFusion = yelpFusion;
    }

    @Override
    public Invitation getInviteById(Long inviteId) {
        Invitation invite = new Invitation();
        List<InviteRestaurants> restaurants = new ArrayList<>();
        List<String> yelpId = new ArrayList<>();
        List<BusinessDetails> businessDetails = new ArrayList<>();
        SqlRowSet results = jdbcTemplate.queryForRowSet("SELECT * FROM invitation where invite_id = ?", inviteId);
        while (results.next()) {
            invite = mapRowToInvitation(results);
            invite.setInvitees(inviteeDAO.getInviteeById(inviteId));
            restaurants = inviteRestaurantsDAO.getInviteRestaurantById(inviteId);
            invite.setRestaurantChoices(restaurants);
        }

        for (InviteRestaurants restaurant : restaurants) {
            String id = restaurant.getYelpId();
            yelpId.add(id);
        }

        invite.setBusinessDetails(yelpFusion.getBusinessDetailsForInvite(yelpId));

        for (BusinessDetails details : invite.getBusinessDetails()) {
            details.setTotalThumbsUp(getUpVotes(details.getYelpId(), inviteId));
            details.setTotalThumbsDown(getDownVotes(details.getYelpId(), inviteId));
        }

        return invite;
    }

    @Override
    public List<Invitation> findInviteByUserId(int creatorId) {
        List<Invitation> inviteList = new ArrayList<>();
        String sql = "SELECT * FROM invitation WHERE creator_user_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, creatorId);
        while (results.next()) {
            Invitation invite = mapRowToInvitation(results);
            invite.setInvitees(inviteeDAO.getInviteeById(invite.getInviteId()));
            invite.setRestaurantChoices(inviteRestaurantsDAO.getInviteRestaurantById(invite.getInviteId()));


            String sql2 = "SELECT yelp_id FROM invitation_restaurant WHERE invite_id = ?";

            SqlRowSet results2 = jdbcTemplate.queryForRowSet(sql2, invite.getInviteId());
            List<String> yelpId = new ArrayList<>();
            while (results2.next()) {
                yelpId.add(results2.getString("yelp_id"));
            }
            invite.setBusinessDetails(yelpFusion.getBusinessDetailsForInvite(yelpId));


            inviteList.add(invite);

        }
        return inviteList;



    }


    @Override
    public Invitation create(Invitation invitation) {

        String sql = "INSERT INTO invitation (invite_name, location, radius, creator_user_id, deadline, reservation_date_time) VALUES (?,?,?,?,?,?) RETURNING invite_id";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, invitation.getInviteName(), invitation.getLocation(), invitation.getRadius(),
                invitation.getCreatorId(), invitation.getDeadline(), invitation.getReservationDate());
        if (results.next()) {
            Long invite_id = results.getLong("invite_id");
            invitation.setInviteId(invite_id);
            invitation = makeInviteeList(invitation, invite_id);
            invitation = makeInvitationRestaurants(invitation, invite_id);

            String sql2 = "SELECT yelp_id FROM invitation_restaurant WHERE invite_id = ?";

            SqlRowSet results2 = jdbcTemplate.queryForRowSet(sql2, invitation.getInviteId());
            List<String> yelpId = new ArrayList<>();
            while (results2.next()) {
                yelpId.add(results2.getString("yelp_id"));
            }
            invitation.setBusinessDetails(yelpFusion.getBusinessDetailsForInvite(yelpId));


            return invitation;
        } else {
            throw new RuntimeException("Unable to create your invitation");
        }
    }


    private Invitation mapRowToInvitation(SqlRowSet rs) {

        Invitation invite = new Invitation();

        invite.setInviteName(rs.getString("invite_name"));
        invite.setInviteId(rs.getLong("invite_id"));
        invite.setLocation(rs.getString("location"));
        invite.setRadius(rs.getInt("radius"));
        invite.setCreatorId(rs.getInt("creator_user_id"));
        Timestamp deadline = rs.getTimestamp("deadline");
        if (deadline != null) {
            invite.setDeadline(deadline);
        }
        Timestamp reservationDate = rs.getTimestamp("reservation_date_time");
        if (reservationDate != null) {
            invite.setReservationDate(reservationDate);
        }

        //todo call invitee and restaurant daos to get what we need to build a full invite list.
        return invite;

    }


    private Invitation makeInviteeList(Invitation invitation, long inviteId) {
        List<Invitee> suppliedInvitees = invitation.getInvitees();
        List<Invitee> invitees = new ArrayList<>();

        for (Invitee invitee : suppliedInvitees) {
            invitee.setInviteId(inviteId);
            Invitee newInvitee = inviteeDAO.createInvitee(invitee);
            invitees.add(newInvitee);
        }
        invitation.setInvitees(invitees);
        return invitation;
    }

    private Invitation makeInvitationRestaurants(Invitation invitation, long inviteId) {
        List<InviteRestaurants> suppliedRestaurants = invitation.getRestaurantChoices();
        List<InviteRestaurants> updatedRestaurantList = new ArrayList<>();

        for (InviteRestaurants restaurant : suppliedRestaurants) {
            restaurant.setInviteId(inviteId);
            InviteRestaurants newRestaurant = inviteRestaurantsDAO.create(restaurant);
            updatedRestaurantList.add(newRestaurant);
        }
        invitation.setRestaurantChoices(updatedRestaurantList);

        return invitation;
    }

    private int getUpVotes(String yelpId, long inviteId) {
        int upVotes = 0;

        String sql = "SELECT total_thumbs_up FROM invitation_restaurant WHERE invite_id = ? AND yelp_id = ?";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, inviteId, yelpId);
        while (result.next()) {
            upVotes = result.getInt("total_thumbs_up");
        }

        return upVotes;
    }

    private int getDownVotes(String yelpId, long inviteId) {
        int upVotes = 0;

        String sql = "SELECT total_thumbs_down FROM invitation_restaurant WHERE invite_id = ? AND yelp_id = ?";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, inviteId, yelpId);
        while (result.next()) {
            upVotes = result.getInt("total_thumbs_down");
        }

        return upVotes;
    }



}
