package com.techelevator.tenmo.dao;

import java.math.BigDecimal;
import java.util.List;

import com.techelevator.tenmo.exception.AccountNotFoundException;
import com.techelevator.tenmo.exception.UserNotFoundException;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;

public interface AccountsDAO {

    // TODO be able to see ALL account balance(s) by the user id
    List<Account> viewAllAccounts(User user) throws AccountNotFoundException;

    Account viewAccount(long id, long userId) throws AccountNotFoundException;

    // We dont need this to be a public method, should probably become a helper
    // method.
    // BigDecimal viewBalance(Account account);

    // TODO be able to increase balance
    Account accountDeposit(Account account, BigDecimal depositAmount, long userId);

    // TODO decrease balance || disable the user to be able to send more money than
    // what is in their account(s)
    Account accountWithdraw(Account account, BigDecimal withdrawAmount, long userId);
    
    public String matchAccountOwner(long accountId);
    
    public long matchOwnerAccount(long userId);

}
