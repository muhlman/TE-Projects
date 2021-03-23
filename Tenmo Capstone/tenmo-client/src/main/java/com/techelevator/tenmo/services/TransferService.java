package com.techelevator.tenmo.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import com.techelevator.tenmo.models.Account;
import com.techelevator.tenmo.models.AuthenticatedUser;
import com.techelevator.tenmo.models.Transfer;
import com.techelevator.tenmo.models.TransferStatus;
import com.techelevator.tenmo.models.TransferType;
import com.techelevator.tenmo.models.UserCredentials;

public class TransferService {

	private String authToken = "";

	private String BASE_URL;
	private RestTemplate restTemplate = new RestTemplate();

	public TransferService(String url) {
		this.BASE_URL = url;
	}

	public Account viewBalance(int userId) throws ServiceException {
		Account account = null;
		try {
			account = restTemplate
					.exchange(BASE_URL + "account/" + userId, HttpMethod.GET, makeAuthEntity(), Account.class)
					.getBody();
		} catch (RestClientResponseException ex) {
			throw new ServiceException(ex.getRawStatusCode() + " : " + ex.getResponseBodyAsString());
		}
		return account;
	}

	public Transfer[] viewAll() throws ServiceException {
		Transfer[] transfers = null;
		try {
			transfers = restTemplate.exchange(BASE_URL + "transfer", HttpMethod.GET, makeAuthEntity(), Transfer[].class)
					.getBody();
		} catch (RestClientResponseException ex) {
			throw new ServiceException(ex.getRawStatusCode() + " : " + ex.getResponseBodyAsString());
		}
		if (transfers == null) {
			// do something here.
		}
		return transfers;

	}

	public Transfer findTransferById(int transferId) throws ServiceException {
		Transfer transfers = null;
		try {
			transfers = restTemplate
					.exchange(BASE_URL + "transfer/" + transferId, HttpMethod.GET, makeAuthEntity(), Transfer.class)
					.getBody();
		} catch (RestClientResponseException ex) {
			throw new ServiceException(ex.getRawStatusCode() + " : " + ex.getResponseBodyAsString());
		}
		return transfers;

	}

	public Transfer initiateTransfer(Transfer transfer) {
		String requestUrl = BASE_URL + "transfer/";
		HttpEntity<Transfer> entity = makeTransferEntity(transfer);
		return restTemplate.exchange(requestUrl, HttpMethod.POST, entity, Transfer.class).getBody();

	}

	public Transfer updateTransferStatus(Transfer transfer) {
		String requestUrl = BASE_URL + "transfer/" + transfer.getTransferId() + "?status="
				+ transfer.getTransferStatusId();

		restTemplate.exchange(requestUrl, HttpMethod.PUT, makeTransferEntity(transfer), Transfer.class);

		return transfer;
	}

	public Transfer[] viewPendingTransfer() throws ServiceException {

		Transfer[] transfers = null;
		try {
			transfers = restTemplate
					.exchange(BASE_URL + "transfer/pending", HttpMethod.GET, makeAuthEntity(), Transfer[].class)
					.getBody();
		} catch (RestClientResponseException ex) {
			throw new ServiceException(ex.getRawStatusCode() + " : " + ex.getResponseBodyAsString());
		}

		return transfers;
	}

	public TransferStatus[] viewAllStatuses() throws ServiceException {

		TransferStatus[] transfersStatus = null;
		try {
			transfersStatus = restTemplate.exchange(BASE_URL + "transfer/statuslist", HttpMethod.GET, makeAuthEntity(),
					TransferStatus[].class).getBody();
		} catch (RestClientResponseException ex) {
			throw new ServiceException(ex.getRawStatusCode() + " : " + ex.getResponseBodyAsString());
		}
		return transfersStatus;
	}

	public TransferType[] viewAllTypes() throws ServiceException {
		TransferType[] transferTypes = null;
		try {
			transferTypes = restTemplate
					.exchange(BASE_URL + "transfer/typelist", HttpMethod.GET, makeAuthEntity(), TransferType[].class)
					.getBody();
		} catch (RestClientResponseException ex) {
			throw new ServiceException(ex.getRawStatusCode() + " : " + ex.getResponseBodyAsString());

		}
		return transferTypes;

	}

	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}

	private HttpEntity makeAuthEntity() {
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(authToken);
		HttpEntity entity = new HttpEntity<>(headers);
		return entity;
	}

	private HttpEntity<Transfer> makeTransferEntity(Transfer transfer) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBearerAuth(authToken);
		HttpEntity<Transfer> entity = new HttpEntity<>(transfer, headers);
		return entity;
	}

}
