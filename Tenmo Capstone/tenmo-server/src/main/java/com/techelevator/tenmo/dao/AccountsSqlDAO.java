package com.techelevator.tenmo.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import com.techelevator.tenmo.exception.AccountNotFoundException;
import com.techelevator.tenmo.exception.UserNotFoundException;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;

@Component
public class AccountsSqlDAO implements AccountsDAO {

	private JdbcTemplate jdbcTemplate;

	public AccountsSqlDAO(DataSource datasource) {
		this.jdbcTemplate = new JdbcTemplate(datasource);
	}

	// TODO add try/catch blocks and create custom exceptions.

	@Override
	public List<Account> viewAllAccounts(User user) throws AccountNotFoundException {

		List<Account> accounts = new ArrayList<>();
		String query = "SELECT account_id, user_id, balance " + "FROM accounts " + "WHERE user_id = ?";

		SqlRowSet results = jdbcTemplate.queryForRowSet(query, user.getId());
		
		if(results.wasNull()) {
			throw new AccountNotFoundException();
		}
		
		while (results.next()) {
			Account account = mapRowToAccount(results);
			accounts.add(account);
		}

		return accounts;
	}

	@Override
	public Account viewAccount(long id, long userId) throws AccountNotFoundException {
		if (!checkAccountOwner(userId, id)) {
		    return null;
		}
		String query = "SELECT account_id, user_id, balance FROM accounts WHERE account_id = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(query, id);
		
		if(results.wasNull()) {
			throw new AccountNotFoundException();
		}
		
		while (results.next()) {
			Account account = mapRowToAccount(results);
			return account;
		}
		return null;

	}

	@Override
	public Account accountDeposit(Account account, BigDecimal depositAmount, long userId) {
		if (!checkAccountOwner(userId, account.getAccountId())) {
			return null;
		}
		
		account.setBalance(this.viewBalance(account));
		BigDecimal newBalance = account.getBalance().add(depositAmount);
		String sql = "UPDATE accounts SET balance = ? WHERE account_id = ?";
		jdbcTemplate.update(sql, newBalance, account.getAccountId());
		
		account.setBalance(newBalance);
		return account;
	}

	@Override
	public Account accountWithdraw(Account account, BigDecimal withdrawAmount, long userId) {
		if (!checkAccountOwner(userId, account.getAccountId())) {
			return null;
		}
		account.setBalance(this.viewBalance(account));

		if (account.getBalance().subtract(withdrawAmount).signum() >= 0) {
			BigDecimal newBalance = account.getBalance().subtract(withdrawAmount);
			String sql = "UPDATE accounts SET balance = ? WHERE account_id = ?";
			jdbcTemplate.update(sql, newBalance, account.getAccountId());
			account.setBalance(newBalance);
			return account;
		}
		return null;
	}
	
	@Override
	public String matchAccountOwner(long accountId) {
	    String query = "SELECT username FROM users JOIN accounts ON users.user_id = accounts.user_id WHERE accounts.account_id = ?";
	    
	    SqlRowSet results = jdbcTemplate.queryForRowSet(query, accountId);

        while (results.next()) {
            return results.getString("username");

        }

        return null;
	}
	
	@Override
    public long matchOwnerAccount(long userId) {
        String query = "SELECT account_id FROM accounts WHERE user_id = ?";
        
        SqlRowSet results = jdbcTemplate.queryForRowSet(query, userId);
        
        while (results.next()) {
            return results.getLong("account_id");
        }

        return 0L;
    }

	// helper
	private Account mapRowToAccount(SqlRowSet account) {
		Account ac = new Account();
		ac.setAccountId(account.getLong("account_id"));
		ac.setUserId(account.getLong("user_id"));
		ac.setBalance(account.getBigDecimal("balance"));

		return ac;

	}

	// helper
	private boolean checkAccountOwner(long userId, long accountId) {
		String query = "SELECT user_id FROM accounts WHERE account_id = ?";
		SqlRowSet result = jdbcTemplate.queryForRowSet(query, accountId);
		while (result.next()) {
			long accountOwner = result.getLong("user_id");
			if (accountOwner == userId) {
				return true;
			}
		}
		return false;
	}
	
	private BigDecimal viewBalance(Account account){

		String query = "SELECT balance FROM accounts WHERE account_id = ?";
		
		BigDecimal balanceResult = jdbcTemplate.queryForObject(query, BigDecimal.class, account.getAccountId());

		return balanceResult;
	}

}
