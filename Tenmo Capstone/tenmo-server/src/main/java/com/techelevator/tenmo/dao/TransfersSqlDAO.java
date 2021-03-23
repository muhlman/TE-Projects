package com.techelevator.tenmo.dao;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import com.techelevator.tenmo.exception.TransferNotFoundException;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferStatus;
import com.techelevator.tenmo.model.TransferType;
import com.techelevator.tenmo.model.User;

@Component
public class TransfersSqlDAO implements TransfersDAO {

    private JdbcTemplate jdbcTemplate;

    public TransfersSqlDAO(DataSource datasource) {
        this.jdbcTemplate = new JdbcTemplate(datasource);
    }

    // TODO add security around making sure the proper user is doing transactions
    // for accounts they own.
    // TODO add try/catch blocks and create custom exceptions.

    @Override
    public List<Transfer> listAllTransfers(User user) throws TransferNotFoundException {
        List<Transfer> transfers = new ArrayList<Transfer>();
        String query = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount "
                + "FROM transfers "
                + "INNER JOIN accounts ON account_from = accounts.account_id OR account_to = accounts.account_id "
                + "WHERE accounts.user_id = ? AND transfer_status_id <> 1 ORDER BY transfer_id";

        SqlRowSet results = jdbcTemplate.queryForRowSet(query, user.getId());
        
        if(results.wasNull()) {
        	throw new TransferNotFoundException();
        }
        
        while (results.next()) {
            Transfer transfer = mapRowToTransfer(results);
            transfers.add(transfer);
        }
        return transfers;
    }

    
    
    
    @Override
    public List<Transfer> listPendingTransfers(User user) throws TransferNotFoundException {
        List<Transfer> transfers = new ArrayList<Transfer>();
        String query = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount "
                + "FROM transfers "
                + "INNER JOIN accounts ON account_from = accounts.account_id "
                + "WHERE transfer_status_id = 1 AND accounts.user_id = ? ORDER BY transfer_id";

        SqlRowSet results = jdbcTemplate.queryForRowSet(query, user.getId());
        
        if(results.wasNull()) {
        	throw new TransferNotFoundException();
        }

        while (results.next()) {
            Transfer transfer = mapRowToTransfer(results);
            transfers.add(transfer);
        }
        if (transfers.size() == 0) {
            //do something there.
        }
        return transfers;
    }

    @Override
    public Transfer findByTransferId(int id) throws TransferNotFoundException {
        String query = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount "
                + "FROM transfers WHERE transfer_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(query, id);
        
        if(results.wasNull()) {
        	throw new TransferNotFoundException();
        }

        while (results.next()) {
            return mapRowToTransfer(results);
        }

        return null;
    }

    @Override
    public Transfer initiateTransfer(Transfer transfer) {
        String query = "INSERT INTO transfers (transfer_type_id, transfer_status_id, account_from, account_to, amount) "
                + "VALUES (?,?,?,?,?) RETURNING transfer_id";

        SqlRowSet result = jdbcTemplate.queryForRowSet(query, transfer.getTransferTypeId(),
                transfer.getTransferStatusId(), transfer.getAccountFrom(), transfer.getAccountTo(),
                transfer.getAmount());

        while (result.next()) {
            transfer.setTransferId(result.getInt("transfer_id"));
            return transfer;
        }
        return null;
    }

    @Override
    public Transfer updateTransferStatus(int transferId, int transferStatus) throws TransferNotFoundException {
        String query = "UPDATE transfers SET transfer_status_id = ? WHERE transfer_id = ? RETURNING *";
        SqlRowSet result = jdbcTemplate.queryForRowSet(query, transferStatus, transferId);
        
        if(result.wasNull()) {
        	throw new TransferNotFoundException();
        }
        
        while (result.next()) {
            return mapRowToTransfer(result);
        }

        return null;
    }
    
    
	@Override
	public List<TransferStatus> getTransferStatusList() throws TransferNotFoundException {
		List<TransferStatus> transferStatus = new ArrayList<TransferStatus>();
        String query = "SELECT transfer_status_id, transfer_status_desc "
                + "FROM transfer_statuses ";
             
        SqlRowSet results = jdbcTemplate.queryForRowSet(query);
        
        if(results.wasNull()) {
        	throw new TransferNotFoundException();
        }

        while (results.next()) {
            TransferStatus status = mapRowToStatus(results);
            transferStatus.add(status);
        }
        return transferStatus;
	
	}

	@Override
	public List<TransferType> getTransferTypeList() throws TransferNotFoundException {
		List<TransferType> transfers = new ArrayList<TransferType>();
		  String query = "SELECT transfer_type_id, transfer_type_desc "
	                + "FROM transfer_types ";

        SqlRowSet results = jdbcTemplate.queryForRowSet(query);
        
        if(results.wasNull()) {
        	throw new TransferNotFoundException();
        }

        while (results.next()) {
            TransferType type = mapRowToType(results);
            transfers.add(type);
        }
        return transfers;
	
	}

    // helper
    private Transfer mapRowToTransfer(SqlRowSet result) {
        Transfer transfer = new Transfer();
        transfer.setTransferId(result.getInt("transfer_id"));
        transfer.setTransferTypeId(result.getInt("transfer_type_id"));
        transfer.setTransferStatusId(result.getInt("transfer_status_id"));
        transfer.setAccountFrom(result.getInt("account_from"));
        transfer.setAccountTo(result.getInt("account_to"));
        transfer.setAmount(result.getBigDecimal("amount"));
        return transfer;
    }

    private TransferStatus mapRowToStatus(SqlRowSet result) {
    	TransferStatus status = new TransferStatus();
    	status.setStatusId(result.getInt("transfer_status_id"));
    	status.setStatusDescription(result.getString("transfer_status_desc"));
		return status;
    }

    
    private TransferType mapRowToType(SqlRowSet result) {
    	TransferType type = new TransferType();
    	type.setTypeId(result.getInt("transfer_type_id"));
    	type.setTypeDescription(result.getString("transfer_type_desc"));
		return type;
    }

    /*
     * Merged and replace with initiateTransfer method.
     * 
     * @Override public Transfer sendTransfer(Transfer transfer) { String query =
     * "INSERT INTO transfers (transfer_type_id, transfer_status_id, account_from, account_to, amount) "
     * + "VALUES (?,?,?,?,?) RETURNING transfer_id";
     * 
     * SqlRowSet result = jdbcTemplate.queryForRowSet(query,
     * transfer.getTransferTypeId(), transfer.getTransferStatusId(),
     * transfer.getAccountFrom(), transfer.getAccountTo(), transfer.getAmount());
     * 
     * while (result.next()) { transfer.setTransferId(result.getInt("transfer_id"));
     * return transfer; } return null; }
     */

}
