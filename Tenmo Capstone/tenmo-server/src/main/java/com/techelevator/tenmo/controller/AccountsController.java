package com.techelevator.tenmo.controller;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.techelevator.tenmo.dao.AccountsDAO;
import com.techelevator.tenmo.dao.UserDAO;
import com.techelevator.tenmo.exception.AccountNotFoundException;
import com.techelevator.tenmo.exception.UserNotFoundException;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;

@RequestMapping ("/account")
@PreAuthorize("isAuthenticated()")
@RestController
public class AccountsController {

	private AccountsDAO dao;
	private UserDAO userDao;
	
	public AccountsController(AccountsDAO dao, UserDAO userDao) {
	    this.dao = dao;
	    this.userDao = userDao; 
	}
	
	//TODO Add responses statuses for account actions.
	
	
	@RequestMapping (method = RequestMethod.GET)
	public List<Account> viewAllAccounts(Principal principal) throws UserNotFoundException, AccountNotFoundException {
	        User user = userDao.findByUsername(principal.getName());
	        return dao.viewAllAccounts(user);
	}
	
	
	@RequestMapping (path = "/{id}", method = RequestMethod.GET)
    public Account viewAccount(@PathVariable long id, Principal principal) throws UserNotFoundException, AccountNotFoundException {
        long userId = userDao.findIdByUsername(principal.getName());
            return dao.viewAccount(id, userId);
    }
	
	@RequestMapping(path = "/deposit", method = RequestMethod.PUT)
	public Account accountDeposit(@RequestBody Account account, @RequestParam BigDecimal depositAmount, Principal principal) throws UserNotFoundException {
	    long userId = userDao.findIdByUsername(principal.getName());
		return dao.accountDeposit(account, depositAmount,userId);
	}
	
	@RequestMapping(path = "/withdraw", method = RequestMethod.PUT)
	public Account accountWithdraw(@RequestBody Account account, @RequestParam BigDecimal withdrawAmount, Principal principal) throws UserNotFoundException {
	    long userId = userDao.findIdByUsername(principal.getName());
		return dao.accountWithdraw(account, withdrawAmount, userId);
	}
	
	@RequestMapping(path = "/findusername/{id}", method = RequestMethod.GET)
	public String findUserByAccount(@PathVariable long id) {
	    return dao.matchAccountOwner(id);
	}
	
	@RequestMapping(path = "/findaccount/{id}", method = RequestMethod.GET)
    public long findAccountByUser(@PathVariable long id) {
        return dao.matchOwnerAccount(id);
    }
	
	/* This may not be needed if we just use the viewAccount call on our client and then on that side do account.getBalance()
	// since we returned a whole account object there. This reduces API complexity.
	@RequestMapping(path = "/{id}/balance", method = RequestMethod.GET)
	public BigDecimal viewBalance(@PathVariable long id, Principal principal) {
	    long userId = userDao.findIdByUsername(principal.getName());
	    Account account = dao.viewAccount(id, userId);
		return account.getBalance();
	}
	*/
		
	/* @RequestMapping( path = "/whoami")
	    public String whoAmI(Principal principal) {
	        return principal.getName();
	    }
	 */
	
}
