package com.techelevator.dao;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.techelevator.model.User;
import com.techelevator.model.UserDetails;
import com.techelevator.dao.UserDetailsDAO;

@Service
public class UserSqlDAO implements UserDAO {

	private JdbcTemplate jdbcTemplate;
	private UserDetailsDAO userDetailsDAO;


	public UserSqlDAO(JdbcTemplate jdbcTemplate, UserDetailsDAO userDetailsDAO) {
		this.jdbcTemplate = jdbcTemplate;
		this.userDetailsDAO = userDetailsDAO;

	}

	@Override
	public int findIdByUsername(String username) {
		return jdbcTemplate.queryForObject("select user_id from users where username = ?", int.class, username);
	}

	@Override
	public User getUserById(Long userId) {
		String sql = "SELECT * FROM users WHERE user_id = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
		if (results.next()) {
			return mapRowToUser(results);
		} else {
			throw new RuntimeException("userId " + userId + " was not found.");
		}
	}

	@Override
	public List<User> findAll() {
		List<User> users = new ArrayList<>();
		String sql = "select * from users";

		SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
		while (results.next()) {
			User user = mapRowToUser(results);
			users.add(user);
		}

		return users;
	}

	@Override
	public User findByUsername(String username) throws UsernameNotFoundException {
		for (User user : this.findAll()) {
			if (user.getUsername().toLowerCase().equals(username.toLowerCase())) {
				return user;
			}
		}
		throw new UsernameNotFoundException("User " + username + " was not found.");
	}

	@Override
	public boolean create(String username, String password, String role, String firstName, String lastName, String email) {
		boolean userCreated = false;

		// Checks if the password contains at least 1 upper, 1 lower and 1 digit as well
		// as a
		// minimum length of 8 characters.
		// if (!password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d+)[a-zA-Z\\d+]{8,}$")) {
		//	return userCreated;
		// }

		// create user
		// check if user exists
		// add first/last name
		String insertUser = "insert into users (username,password_hash,role, first_name, last_name, email) values(?,?,?,?,?,?)";
		String password_hash = new BCryptPasswordEncoder().encode(password);
		String ssRole = "ROLE_" + role.toUpperCase();

		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		String id_column = "user_id";
		userCreated = jdbcTemplate.update(con -> {
			PreparedStatement ps = con.prepareStatement(insertUser, new String[] { id_column });
			ps.setString(1, username);
			ps.setString(2, password_hash);
			ps.setString(3, ssRole);
			ps.setString(4, firstName);
			ps.setString(5, lastName);
			ps.setString(6, email);
			return ps;
		}, keyHolder) == 1;
		int newUserId = (int) keyHolder.getKeys().get(id_column);

		userDetailsDAO.create(newUserId);
		return userCreated;
	}

	public User updateUser(User user) {
		String sql = "UPDATE users SET username = ?, first_name = ?, last_name = ?, email = ? WHERE user_id = ?";
		jdbcTemplate.update(sql, user.getUsername(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getId());

		userDetailsDAO.updateDetails(user.getUserDetails());

		user = findByUsername(user.getUsername());

		return user;
	}

	private User mapRowToUser(SqlRowSet rs) {
		long userId = rs.getLong("user_id");
		User user = new User();
		user.setId(rs.getLong("user_id"));
		user.setUsername(rs.getString("username"));
		user.setPassword(rs.getString("password_hash"));
		user.setAuthorities(rs.getString("role"));
		user.setActivated(true);
		user.setFirstName(rs.getString("first_name"));
		user.setLastName(rs.getString("last_name"));
		user.setEmail(rs.getString("email"));
		UserDetails userDetails = new UserDetails();
		userDetails = userDetailsDAO.getDetails(userId);
		if (userDetails != null) {
			user.setUserDetails(userDetails);
		}
		return user;
	}
}