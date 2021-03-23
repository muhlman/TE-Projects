package com.techelevator.dao.invitation;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import com.techelevator.model.invitation.InviteeVotes;
import org.springframework.jdbc.core.JdbcTemplate;

import com.techelevator.model.invitation.InviteRestaurants;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

@Component
public class InviteRestaurantsSqlDAO implements InviteRestaurantsDAO {

	private JdbcTemplate jdbcTemplate;

	public InviteRestaurantsSqlDAO(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public List<InviteRestaurants> getInviteRestaurantById(Long inviteId) {
		List<InviteRestaurants> inviterestaurants = new ArrayList<>();
		String sql = "SELECT * FROM invitation_restaurant WHERE invite_id = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql, inviteId);

		while (results.next()) {
			InviteRestaurants invrest = mapRowToInviteRestaurant(results);
			inviterestaurants.add(invrest);
		}
		return inviterestaurants;
	}

	@Override
	public InviteRestaurants create(InviteRestaurants listOfChoices) {
		String sql = "INSERT INTO invitation_restaurant (invite_id, yelp_id) VALUES (?,?) RETURNING invite_id, yelp_id, total_thumbs_up, total_thumbs_down";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql, listOfChoices.getInviteId(), listOfChoices.getYelpId());

		while (results.next()) {
			listOfChoices.setInviteId(results.getLong("invite_id"));
			listOfChoices.setYelpId(results.getString("yelp_id"));
			listOfChoices.setThumbsUp(results.getInt("total_thumbs_up"));
			listOfChoices.setThumbsDown(results.getInt("total_thumbs_down"));
		}

		return listOfChoices;
	}

	private InviteRestaurants mapRowToInviteRestaurant(SqlRowSet rs) {
		InviteRestaurants ir = new InviteRestaurants();

		ir.setInviteId(rs.getInt("invite_id"));
		ir.setYelpId(rs.getString("yelp_id"));
		ir.setThumbsUp(rs.getInt("total_thumbs_up"));
		ir.setThumbsDown(rs.getInt("total_thumbs_down"));

		return ir;

	}

	@Override
	public boolean voteThumbsUp(InviteeVotes inviteeVote) {

		String sqlRemove = "DELETE FROM invitee_vote WHERE invitee_unique_id = ? AND yelp_id = ?";
		jdbcTemplate.update(sqlRemove, inviteeVote.getUniqueId(), inviteeVote.getYelpId());

		String sql = "UPDATE invitation_restaurant SET total_thumbs_up = total_thumbs_up + 1 "
				+ "WHERE yelp_id = ? AND invite_id = ?";
		jdbcTemplate.update(sql, inviteeVote.getYelpId(), inviteeVote.getInviteId());

		String sql3 = "INSERT INTO invitee_vote (invite_id, invitee_unique_id, yelp_id, thumbs_up) VALUES (?,?,?,true)";
		jdbcTemplate.update(sql3, inviteeVote.getInviteId(), inviteeVote.getUniqueId(), inviteeVote.getYelpId());

		return true;

	}

	@Override
	public boolean voteThumbsDown(InviteeVotes inviteeVote) {

		String sqlRemove = "DELETE FROM invitee_vote WHERE invitee_unique_id = ? AND yelp_id = ?";
		jdbcTemplate.update(sqlRemove, inviteeVote.getUniqueId(), inviteeVote.getYelpId());

		String sql = "UPDATE invitation_restaurant SET total_thumbs_down = total_thumbs_down + 1 "
				+ "WHERE yelp_id = ? AND invite_id = ?";
		jdbcTemplate.update(sql, inviteeVote.getYelpId(), inviteeVote.getInviteId());

		String sql3 = "INSERT INTO invitee_vote (invite_id, invitee_unique_id, yelp_id, thumbs_down) VALUES (?,?,?,true)";
		jdbcTemplate.update(sql3, inviteeVote.getInviteId(), inviteeVote.getUniqueId(), inviteeVote.getYelpId());

		return true;

	}

	@Override
	public boolean removeVote(InviteeVotes inviteeVote) {
		boolean thumbsUp = false;
		boolean thumbsDown = false;
		String sql = "SELECT thumbs_down, thumbs_up FROM invitee_vote WHERE invitee_unique_id = ? AND yelp_id = ? AND invite_id = ?";

		SqlRowSet result = jdbcTemplate.queryForRowSet(sql, inviteeVote.getUniqueId(), inviteeVote.getYelpId(), inviteeVote.getInviteId());
		while (result.next()) {
			thumbsDown = result.getBoolean("thumbs_down");
			thumbsUp = result.getBoolean("thumbs_up");
			if (thumbsDown) {
				this.undoThumbsDown(inviteeVote);
			} else if (thumbsUp) {
				this.undoThumbsUp(inviteeVote);
			} else {
				return false;
			}

			String sql3 = "DELETE FROM invitee_vote WHERE invitee_unique_id = ? AND yelp_id = ? AND invite_id = ?";
			jdbcTemplate.update(sql3, inviteeVote.getUniqueId(), inviteeVote.getYelpId(), inviteeVote.getInviteId());
		}

		return true;

	}

	@Override
	public void removeAllVotesForUser(InviteeVotes inviteeVote) {
		int inviteId = inviteeVote.getInviteId();
		String uniqueID = inviteeVote.getUniqueId();

		String sql = "SELECT ir.yelp_id AS \"yelp_id\", iv.invitee_unique_id AS \"invitee_unique_id\", iv.thumbs_down " +
				"AS \"thumbs_down\", iv.thumbs_up AS \"thumbs_up\" " +
				"FROM  invitation_restaurant AS ir " +
				"RIGHT JOIN invitee_vote AS iv ON ir.yelp_id = iv.yelp_id " +
				"WHERE iv.invitee_unique_id = ? AND ir.invite_id = ?";

		SqlRowSet selection = jdbcTemplate.queryForRowSet(sql, uniqueID, inviteId);
		while (selection.next()) {
			String yelpId = selection.getString("yelp_id");
			boolean thumbsDown = selection.getBoolean("thumbs_down");
			boolean thumbsUp = selection.getBoolean("thumbs_up");
			if (thumbsDown) {
				String sqlDown = "UPDATE invitation_restaurant SET total_thumbs_down = total_thumbs_down - 1 "
						+ "WHERE yelp_id = ? AND invite_id = ?";
				jdbcTemplate.update(sqlDown, yelpId, inviteId);
			} else if (thumbsUp) {
				String sqlUp = "UPDATE invitation_restaurant SET total_thumbs_up = total_thumbs_up - 1 "
						+ "WHERE yelp_id = ? AND invite_id = ?";
				jdbcTemplate.update(sqlUp, yelpId, inviteId);
			}
		}

		String deleteSql = "DELETE FROM invitee_vote WHERE invitee_unique_id = ?";
		jdbcTemplate.update(deleteSql, uniqueID);

	}

	private boolean undoThumbsUp(InviteeVotes inviteeVote) {

		String sql = "UPDATE invitation_restaurant SET total_thumbs_up = total_thumbs_up - 1 "
				+ "WHERE yelp_id = ? AND invite_id = ?";
		jdbcTemplate.update(sql, inviteeVote.getYelpId(), inviteeVote.getInviteId());

		return true;

	}


	private boolean undoThumbsDown(InviteeVotes inviteeVote) {

		String sql = "UPDATE invitation_restaurant SET total_thumbs_down = total_thumbs_down - 1 "
				+ "WHERE yelp_id = ? AND invite_id = ?";
		jdbcTemplate.update(sql, inviteeVote.getYelpId(), inviteeVote.getInviteId());

		return true;

	}

}
