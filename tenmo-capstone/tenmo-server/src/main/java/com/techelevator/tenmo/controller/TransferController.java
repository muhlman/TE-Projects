package com.techelevator.tenmo.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.techelevator.tenmo.dao.*;
import com.techelevator.tenmo.exception.AccountNotFoundException;
import com.techelevator.tenmo.exception.TransferNotFoundException;
import com.techelevator.tenmo.exception.UserNotFoundException;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferStatus;
import com.techelevator.tenmo.model.TransferType;
import com.techelevator.tenmo.model.User;

@RequestMapping("/transfer")
@RestController
public class TransferController {

    private TransfersDAO dao;
    private UserDAO userDao;
    private AccountsDAO accountDao;

    public TransferController(TransfersDAO dao, UserDAO userDao, AccountsDAO accountDao) {
        this.dao = dao;
        this.userDao = userDao;
        this.accountDao = accountDao;
    }

    // TODO add account checks before performing any actions on account for security
    // TODO Add response statuses for various transactions
    // TODO test the updateTransferStatus controller method in postman

    @RequestMapping(method = RequestMethod.GET)
    public List<Transfer> viewAllTransfers(Principal principal) throws TransferNotFoundException, UserNotFoundException {
        return dao.listAllTransfers(getUser(principal));
    }

    @RequestMapping(path = "/pending", method = RequestMethod.GET)
    public List<Transfer> viewPendingTransfers(Principal principal) throws TransferNotFoundException, UserNotFoundException {
        return dao.listPendingTransfers(getUser(principal));
    }

    @RequestMapping(path = "/{transferId}", method = RequestMethod.GET)
    public Transfer findByTransferId(@PathVariable int transferId, Principal principal) throws TransferNotFoundException {
        return dao.findByTransferId(transferId);
    }

    @RequestMapping(path = "/{transferId}", method = RequestMethod.PUT)
    public Transfer updateTransferStatus(@PathVariable int transferId,
            @RequestParam(name = "status", defaultValue = "0") int statusId, Principal principal) throws TransferNotFoundException, AccountNotFoundException {
        if (statusId > 0 && statusId < 4) {
            Transfer transfer = dao.findByTransferId(transferId);
            transfer.setTransferStatusId(statusId);
            boolean transferSuccessful = false;
            if (transfer.getTransferStatusId() == 2) {
                transferSuccessful = accountTransfer(transfer);
            }
            if (transferSuccessful || transfer.getTransferStatusId() != 2) {
                return dao.updateTransferStatus(transferId, statusId);
            }
        }
        return null;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.POST)
    public Transfer initiateTransfer(@RequestBody Transfer transfer) throws AccountNotFoundException {
        if (transfer.getTransferStatusId() == 2) {
            boolean transferSuccessful = accountTransfer(transfer);
            if (!transferSuccessful) {
                return null;
            }
        }
        return dao.initiateTransfer(transfer);
    }

    @RequestMapping(path = "/statuslist", method = RequestMethod.GET)
    public List<TransferStatus> viewTransferStatus(Principal principal) throws TransferNotFoundException {
        return dao.getTransferStatusList();
    }

    @RequestMapping(path = "/typelist", method = RequestMethod.GET)
    public List<TransferType> viewTransferList(Principal principal) throws TransferNotFoundException {
        return dao.getTransferTypeList();
    }

    // helper method
    private User getUser(Principal principal) throws UserNotFoundException {
        User user = userDao.findByUsername(principal.getName());
        return user;
    }

    private boolean accountTransfer(Transfer transfer) throws AccountNotFoundException {
        Account fromAccount = accountDao.viewAccount(transfer.getAccountFrom(), transfer.getAccountFrom());
        Account toAccount = accountDao.viewAccount(transfer.getAccountTo(), transfer.getAccountTo());

        Account updatedAccount = accountDao.accountWithdraw(fromAccount, transfer.getAmount(), fromAccount.getUserId());
        accountDao.accountDeposit(toAccount, transfer.getAmount(), toAccount.getUserId());
        if (updatedAccount != null) {
            return true;
        }

        return false;
    }

}
