package com.techelevator.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class User {

	private Long id;
	private String username;
	@JsonIgnore
	private String password;
	@JsonIgnore
	private boolean activated;
	@JsonIgnore
	private Set<Authority> authorities = new HashSet<>();

	private String firstName;

	private String lastName;

	private String email;

	private UserDetails userDetails;

	public User() {
	}

	public User(Long id, String username, String password, String authorities, String firstName, String lastName, String email, UserDetails userDetails) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.activated = true;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.userDetails = userDetails;
	}
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isActivated() {
		return activated;
	}

	public void setActivated(boolean activated) {
		this.activated = activated;
	}

	public Set<Authority> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(Set<Authority> authorities) {
		this.authorities = authorities;
	}

	public void setAuthorities(String authorities) {
		String[] roles = authorities.split(",");
		for (String role : roles) {
			String authority = role.contains("ROLE_") ? role : "ROLE_" + role;
			this.authorities.add(new Authority(authority));
		}
	}

	public UserDetails getUserDetails() {
		return userDetails;
	}

	public void setUserDetails(UserDetails userDetails) {
		this.userDetails = userDetails;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		User user = (User) o;
		return id == user.id && activated == user.activated && Objects.equals(username, user.username)
				&& Objects.equals(password, user.password) && Objects.equals(authorities, user.authorities) 
				&& Objects.equals(firstName, user.firstName) && Objects.equals(lastName, user.lastName) 
				&& Objects.equals(email, user.email) && Objects.equals(userDetails, user.userDetails);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, username, password, activated, authorities, firstName, lastName, email, userDetails);
	}

	@Override
	public String toString() {
		return "User{" + "id=" + id + ", username='" + username + '\'' + ", activated=" + activated + ", authorities="
				+ authorities + ", firstname=" + firstName + ", lastname=" + lastName + ", email=" + email +", userdetails=" + '}';
	}


}
