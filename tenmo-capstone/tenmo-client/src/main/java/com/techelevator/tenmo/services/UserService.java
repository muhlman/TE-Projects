package com.techelevator.tenmo.services;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import com.techelevator.tenmo.models.Transfer;
import com.techelevator.tenmo.models.User;

public class UserService {

	private String authToken = "";

	private String BASE_URL;
	private RestTemplate restTemplate = new RestTemplate();

	public UserService(String url) {
		this.BASE_URL = url;
	}

	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}

	public User[] userList() throws ServiceException {

		User[] users = null;
		try {
			users = restTemplate.exchange(BASE_URL + "user", HttpMethod.GET, makeAuthEntity(), User[].class).getBody();
		} catch (RestClientResponseException ex) {
			throw new ServiceException(ex.getRawStatusCode() + " : " + ex.getResponseBodyAsString());
		}
		return users;

	}

	public User findByUsername(String username) throws ServiceException {
		User users = null;
		try {
			users = restTemplate.exchange(BASE_URL + "user/" + username, HttpMethod.GET, makeAuthEntity(), User.class)
					.getBody();
		} catch (RestClientResponseException ex) {
			throw new ServiceException(ex.getRawStatusCode() + " : " + ex.getResponseBodyAsString());
		}
		return users;
	}

	public int findIdByUsername(String username) throws ServiceException {
		int userId = 0;
		try {
			userId = restTemplate
					.exchange(BASE_URL + "user/" + username + "/findid", HttpMethod.GET, makeAuthEntity(), int.class)
					.getBody();
		} catch (RestClientResponseException ex) {
			throw new ServiceException(ex.getRawStatusCode() + " : " + ex.getResponseBodyAsString());
		}
		return userId;
	}

	public void create(String username, String password) {
		String requesturl = BASE_URL + "user?username=" + username + "&password=" + password;
		restTemplate.exchange(requesturl, HttpMethod.POST, makeAuthEntity(), User.class).getBody();
	}

	public String matchAccountOwner(int id) {
		return restTemplate
				.exchange(BASE_URL + "account/findusername/" + id, HttpMethod.GET, makeAuthEntity(), String.class)
				.getBody();
	}

	public int matchOwnerAccount(int id) {
		return restTemplate
				.exchange(BASE_URL + "account/findaccount/" + id, HttpMethod.GET, makeAuthEntity(), int.class)
				.getBody();
	}

	private HttpEntity makeAuthEntity() {
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(authToken);
		HttpEntity entity = new HttpEntity<>(headers);
		return entity;
	}

}
