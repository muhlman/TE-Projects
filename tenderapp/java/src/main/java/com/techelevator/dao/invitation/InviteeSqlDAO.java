package com.techelevator.dao.invitation;

import com.techelevator.model.invitation.InviteRestaurants;
import com.techelevator.model.invitation.InviteeVotes;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.model.invitation.Invitee;
import com.techelevator.services.yelpfusion.YelpFusion;

import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class InviteeSqlDAO implements InviteeDAO {

    private JdbcTemplate jdbcTemplate;
    private YelpFusion yelpFusion;

    public InviteeSqlDAO(JdbcTemplate jdbcTemplate, YelpFusion yelpFusion) {
        this.jdbcTemplate = jdbcTemplate;
        this.yelpFusion = yelpFusion;
        
    }

    @Override
    public List<Invitee> getInviteeById(Long inviteId) {
        List<Invitee> invitees = new ArrayList<>();
        String sql = "SELECT * FROM invitee_details WHERE invite_id = ?";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, inviteId);

        while (results.next()) {
            Invitee invitee = mapRowToInvitee(results);
            invitees.add(invitee);
        }
        return invitees;
    }

    @Override
    public Invitee getInviteeByUniqueId(String uniqueId) {
        Invitee invitee = new Invitee();
        String sql = "SELECT id.invitee_user_id AS \"invitee_user_id\",id.invite_id AS \"invite_id\", id.unique_id AS \"unique_id\", id.name AS \"name\", " +
                "id.email AS \"email\", id.has_voted AS \"has_voted\", id.is_attending AS \"is_attending\", inv.deadline AS \"deadline\", inv.reservation_date_time AS \"reservation_date_time\" " +
                "FROM invitee_details AS id  JOIN invitation AS inv ON id.invite_id = inv.invite_id WHERE id.unique_id = ?";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, uniqueId);

        while (results.next()) {
            invitee = mapRowToInvitee(results);
            Timestamp deadline = results.getTimestamp("deadline");
            if (deadline != null) {
                invitee.setDeadline(deadline);
            }
            invitee.setReservationDate(results.getString("reservation_date_time"));
        }

        String sql2 = "SELECT yelp_id FROM invitation_restaurant WHERE invite_id = ?";

        SqlRowSet results2 = jdbcTemplate.queryForRowSet(sql2, invitee.getInviteId());
        List<String> yelpId = new ArrayList<>();
        while (results2.next()) {
            yelpId.add(results2.getString("yelp_id"));
        }
        invitee.setBusinessDetails(yelpFusion.getBusinessDetailsById(yelpId, uniqueId));

        return invitee;
    }

    @Override
    public Invitee createInvitee(Invitee invitee) {
        // Generates the unique ID we will use on the front end to track the users.
        String uniqueId = generateUniqueId();
        invitee.setUniqueId(uniqueId);

        String sql = "INSERT INTO invitee_details (invite_id,unique_id, invitee_user_id, name, email) VALUES (?,?,?,?,?) RETURNING has_voted, is_attending";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, invitee.getInviteId(), invitee.getUniqueId(), invitee.getUserId(),
                invitee.getName(), invitee.getEmail());

        while (results.next()) {
            invitee.setHasVoted(results.getBoolean("has_voted"));
            invitee.setIsAttending(results.getString("is_attending"));
        }

        return invitee;
    }

    @Override
    public Invitee updateInviteeStatus(Invitee invitee) {
       /*
       String sqlVoteStatus = "SELECT has_voted FROM invitee_details WHERE unique_id = ?";
        SqlRowSet votestat = jdbcTemplate.queryForRowSet(sqlVoteStatus, invitee.getUniqueId());
        boolean hasVoted = votestat.getBoolean("has_voted");

        if (hasVoted) {
            String sqlVotes = "SELECT ";
        }
        */

        String sql = "UPDATE invitee_details SET has_voted = ?, is_attending = ? WHERE unique_id = ?";

        jdbcTemplate.update(sql, invitee.getHasVoted(), invitee.getIsAttending(), invitee.getUniqueId());
		SqlRowSet results = jdbcTemplate.queryForRowSet("SELECT * FROM invitee_details WHERE unique_id = ?", invitee.getUniqueId());
		while (results.next()) {
			invitee = mapRowToInvitee(results);
		}
        return invitee;
    }

    private List<InviteeVotes> getVotesByUniqueId(String uniqueId) {
        List<InviteeVotes> inviteeVotes = new ArrayList<InviteeVotes>();

        String sql = "SELECT yelp_id, thumbs_up, thumbs_down FROM public.invitee_vote WHERE unique_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, uniqueId);

        while (results.next()) {

        }

        return inviteeVotes;

    }

    private Invitee mapRowToInvitee(SqlRowSet rs) {
        Invitee invitee = new Invitee();

        invitee.setInviteId(rs.getInt("invite_id"));
        invitee.setUniqueId(rs.getString("unique_id"));
        invitee.setUserId(rs.getInt("invitee_user_id"));
        invitee.setName(rs.getString("name"));
        invitee.setEmail(rs.getString("email"));
        invitee.setHasVoted(rs.getBoolean("has_voted"));
        invitee.setIsAttending(rs.getString("is_attending"));

        return invitee;
    }


    private String generateUniqueId() {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 15;
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

}
