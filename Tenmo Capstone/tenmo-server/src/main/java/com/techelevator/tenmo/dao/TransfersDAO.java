package com.techelevator.tenmo.dao;

import java.util.List;

import com.techelevator.tenmo.exception.TransferNotFoundException;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferStatus;
import com.techelevator.tenmo.model.TransferType;
import com.techelevator.tenmo.model.User;

public interface TransfersDAO {

    // TODO see completed transfers
    // Return a list of transfers
    List<Transfer> listAllTransfers(User user) throws TransferNotFoundException;

    // TODO (optional) see pending transfers
    // Return a list with status pending for logged in user.
    List<Transfer> listPendingTransfers(User user) throws TransferNotFoundException;

    // TODO look up transfer by id
    // Return single transfer
    Transfer findByTransferId(int id) throws TransferNotFoundException;

    // TODO initiate a transfer
    // TODO (optional) method for requesting a transfer
    // return request id
    Transfer initiateTransfer(Transfer transfer);

    // TODO (optional) approve or reject a request transfer
    // Returns true or false, if true, we will need to call method to update balance
    // of both accounts.
    // We should also check for available funds before approving to make sure there
    // is enough money in account.
    Transfer updateTransferStatus(int transferId, int transferStatus) throws TransferNotFoundException;

    List<TransferStatus> getTransferStatusList() throws TransferNotFoundException;
    
    List<TransferType> getTransferTypeList() throws TransferNotFoundException;
    // TODO (optional) be able to approve or deny a REQUEST for TE bucks
    // PENDING NOT SURE IF NEEDED

    /*
     * Merged and replaced with request transfer into single "initiate transfer"
     * option. TODO initiate a transfer return transfer id, Transfer sendTransfer
     * (Transfer transfer);
     */

}
