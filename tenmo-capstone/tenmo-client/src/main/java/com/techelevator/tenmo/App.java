package com.techelevator.tenmo;

import java.math.BigDecimal;

import com.techelevator.tenmo.models.*;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.AuthenticationServiceException;
import com.techelevator.tenmo.services.ServiceException;
import com.techelevator.tenmo.services.TransferService;
import com.techelevator.tenmo.services.UserService;
import com.techelevator.view.ConsoleService;

public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";

    private static final String MENU_OPTION_EXIT = "Exit";
    private static final String LOGIN_MENU_OPTION_REGISTER = "Register";
    private static final String LOGIN_MENU_OPTION_LOGIN = "Login";
    private static final String[] LOGIN_MENU_OPTIONS = { LOGIN_MENU_OPTION_REGISTER, LOGIN_MENU_OPTION_LOGIN,
            MENU_OPTION_EXIT };
    private static final String MAIN_MENU_OPTION_VIEW_BALANCE = "View your current balance";
    private static final String MAIN_MENU_OPTION_SEND_BUCKS = "Send TE bucks";
    private static final String MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS = "View your past transfers";
    private static final String MAIN_MENU_OPTION_REQUEST_BUCKS = "Request TE bucks";
    private static final String MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS = "View your pending requests";
    private static final String MAIN_MENU_OPTION_LOGIN = "Login as different user";
    private static final String[] MAIN_MENU_OPTIONS = { MAIN_MENU_OPTION_VIEW_BALANCE, MAIN_MENU_OPTION_SEND_BUCKS,
            MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS, MAIN_MENU_OPTION_REQUEST_BUCKS,
            MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS, MAIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT };

    private AuthenticatedUser currentUser;
    private ConsoleService console;
    private AuthenticationService authenticationService;

    // instantiate the two services.
    private TransferService transferService = new TransferService(API_BASE_URL);
    private UserService userService = new UserService(API_BASE_URL);
    // Get the list of transfer status names and transfer type names.
    TransferStatus[] transferStatuses = null;
    TransferType[] transferTypes = null;
    // Create a blank account object type that will load the account details when
    // the user logs in.
    Account userAccount = null;

    public static void main(String[] args) throws ServiceException {
        App app = new App(new ConsoleService(System.in, System.out), new AuthenticationService(API_BASE_URL));
        app.run();
    }

    public App(ConsoleService console, AuthenticationService authenticationService) {
        this.console = console;
        this.authenticationService = authenticationService;
    }

    public void run() throws ServiceException {
        System.out.println("*********************");
        System.out.println("* Welcome to TEnmo! *");
        System.out.println("*********************");

        registerAndLogin();
        mainMenu();
    }

    private void mainMenu() throws ServiceException {
        while (true) {
            String choice = (String) console.getChoiceFromOptions(MAIN_MENU_OPTIONS);
            if (MAIN_MENU_OPTION_VIEW_BALANCE.equals(choice)) {
                viewCurrentBalance();
            } else if (MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS.equals(choice)) {
                viewTransferHistory();
            } else if (MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS.equals(choice)) {
                viewPendingRequests();
            } else if (MAIN_MENU_OPTION_SEND_BUCKS.equals(choice)) {
                sendBucks();
            } else if (MAIN_MENU_OPTION_REQUEST_BUCKS.equals(choice)) {
                requestBucks();
            } else if (MAIN_MENU_OPTION_LOGIN.equals(choice)) {
                login();
            } else {
                // the only other option on the main menu is to exit
                exitProgram();
            }
        }
    }

    private void viewCurrentBalance() throws ServiceException {
        if (userAccount == null) {
            System.out.println("Unable to retrieve account balance");
        } else {
            System.out.println("Your current account balance is: $" + userAccount.getBalance());
        }

    }

    private void viewTransferHistory() throws ServiceException {
        Transfer[] transferList = transferService.viewAll();
        if (transferList.length != 0) {
            this.printTransferList(transferList);
            int viewHistory = console.getUserInputInteger("Please enter transfer ID to view details (0 to cancel)");
            boolean validTransfer = false;
            while (!validTransfer) {
                for (int i = 0; i < transferList.length; i++) {
                    if (transferList[i].getTransferId() == viewHistory || viewHistory == 0) {
                        validTransfer = true;
                        break;
                    }
                }
                if (!validTransfer) {
                    viewHistory = console.getUserInputInteger(
                            "\nYou have entered an invalid transaction to view, please enter a valid transfer ID (0 to cancel)s");
                }
            }
            if (viewHistory == 0) {
                mainMenu();
            }
            this.printTransferDetail(transferService.findTransferById(viewHistory));

        } else {
            System.out.println("\nThere are no transactions to display\n");
            mainMenu();
        }

    }

    private void viewPendingRequests() throws ServiceException {
        // Retrieve the list of transfers for the user
        Transfer[] transferList = transferService.viewPendingTransfer();
        // Check if there are any transfers. If the length is 0 we bail out of the
        // method because there are no actions to take.
        if (transferList.length == 0) {
            System.out.println("\nThere are no pending transactions to display\n");
            mainMenu();
        }
        // if there are transfers we call the method to print them to the screen here
        this.printPendingTransfers(transferList);
        // Ask the user for input to display the full details of a specific transaction
        // or cancel back to the main menu.
        int transferId = console.getUserInputInteger("Please enter transfer ID to approve/reject (0 to cancel)");
        // Here was create a loop that will keep prompting the user for a valid
        // selection. They either need to choose a transfer that
        // is in their list of transfers of 0 to cancel out.
        boolean validTransfer = false;
        while (!validTransfer) {
            // This loop is what does the actual checking to see if they made a valid
            // selection or not, if they do we set our boolean
            // to true and bail on the loop since we don't need to check against the rest of
            // the transactions.
            for (int i = 0; i < transferList.length; i++) {
                if (transferId == transferList[i].getTransferId() || transferId == 0) {
                    validTransfer = true;
                    break;
                }
            }
            // If we don't have a valid selection after scanning the transactions list we
            // prompt the user to enter their selection again
            if (!validTransfer) {
                transferId = console.getUserInputInteger(
                        "\nYou have entered an invalid transaction to approve, please enter a valid transfer ID (0 to cancel)");
            }
        }
        // Check if the user chose to cancel back to main menu, if so we bail on the
        // method.
        if (transferId == 0) {
            mainMenu();
        }
        // Now that we have a valid non 0 selection we print the menu of approval
        // options and get the choice from the user.
        int choice = printPendingApprovalMenu();
        // If the user chose to cancel the approval process we bail on the method.
        if (choice == 0) {
            mainMenu();
        }
        // if the user chose to approve or reject the transfer we start the process to
        // update
        // Create a transfer object to be able to update on the server.
        Transfer transfer = transferService.findTransferById(transferId);
        // Check if the user enough money, if they don't print an error and bail on the
        // method otherwise proceed with transfer.
        BigDecimal balance = userAccount.getBalance();
        if (balance.subtract(transfer.getAmount()).signum() < 0) {
            // signum returns -1, 0 or 1 not an actual value from the math, since we just
            // care if they have enough money and not the new balancea 0 or 1 valie is what
            // we care about.
            System.out.println(transfer.getAmount() + " is  more than the " + userAccount.getBalance()
                    + " you have available to send.");
            System.out.println("Please add funds and try your transfer again.");
            mainMenu();
        } else {
            // update the transfer object with the new transfer status
            transfer.setTransferStatusId(choice);
            // transmit that update to the server to save in the database and perform the
            // account balance updates.
            transferService.updateTransferStatus(transfer);
        }

        userAccount = transferService.viewBalance(currentUser.getUser().getId());

    }

    private void sendBucks() throws ServiceException {
        User[] users = userService.userList();
        this.printUserList(users);
        int receivingUser = console.getUserInputInteger("Enter ID of user you are sending to (0 to cancel)");

        boolean validUser = false;
        while (!validUser) {
            for (int i = 0; i < users.length; i++) {
                if ((users[i].getId() == receivingUser && receivingUser != currentUser.getUser().getId())
                        || receivingUser == 0) {
                    validUser = true;
                    break;
                }
            }
            if (!validUser) {
                receivingUser = console
                        .getUserInputInteger("\nPlease enter a valid user that is not yourself. (0 to cancel)");
            }
        }

        if (receivingUser == 0) {
            mainMenu();
        }

        String amount = console.getUserInput("Enter amount");
        BigDecimal balance = userAccount.getBalance();
        if (balance.subtract(new BigDecimal(amount)).signum() < 0) {
            System.out.println(
                    amount + " is  more than the " + userAccount.getBalance() + " you have available to send.");
            System.out.println("Please try your transfer again.");
            mainMenu();
        } else {
            Transfer transfer = new Transfer();
            transfer.setAmount(new BigDecimal(amount));
            transfer.setAccountTo(userService.matchOwnerAccount(receivingUser));
            transfer.setAccountFrom(userService.matchOwnerAccount(currentUser.getUser().getId()));
            transfer.setTransferStatusId(2);
            transfer.setTransferTypeId(2);

            transferService.initiateTransfer(transfer);
        }
        userAccount = transferService.viewBalance(currentUser.getUser().getId());

    }

    private void requestBucks() throws ServiceException {
        User[] users = userService.userList();
        this.printUserList(users);
        int userInput = console.getUserInputInteger("Enter ID of user you are requesting from (0 to cancel)");

        boolean validUser = false;
        while (!validUser) {
            for (int i = 0; i < users.length; i++) {
                if ((users[i].getId() == userInput && userInput != currentUser.getUser().getId()) || userInput == 0) {
                    validUser = true;
                    break;
                }
            }
            if (!validUser) {
                userInput = console
                        .getUserInputInteger("\nPlease enter a valid user that is not yourself. (0 to cancel)");
            }
        }

        if (userInput == 0) {
            mainMenu();
        }

        String amount = console.getUserInput("Enter amount");
        Transfer transfer = new Transfer();
        transfer.setAmount(new BigDecimal(amount));
        transfer.setAccountTo(userService.matchOwnerAccount(currentUser.getUser().getId()));
        transfer.setAccountFrom(userService.matchOwnerAccount(userInput));
        transfer.setTransferStatusId(1);
        transfer.setTransferTypeId(1);

        transferService.initiateTransfer(transfer);

    }

    private void exitProgram() {
        System.exit(0);
    }

    private void registerAndLogin() throws ServiceException {
        while (!isAuthenticated()) {
            String choice = (String) console.getChoiceFromOptions(LOGIN_MENU_OPTIONS);
            if (LOGIN_MENU_OPTION_LOGIN.equals(choice)) {
                login();
            } else if (LOGIN_MENU_OPTION_REGISTER.equals(choice)) {
                register();
            } else {
                // the only other option on the login menu is to exit
                exitProgram();
            }
        }
    }

    private boolean isAuthenticated() {
        return currentUser != null;
    }

    private void register() {
        System.out.println("Please register a new user account");
        boolean isRegistered = false;
        while (!isRegistered) // will keep looping until user is registered
        {
            UserCredentials credentials = collectUserCredentials();
            try {
                authenticationService.register(credentials);
                isRegistered = true;
                System.out.println("Registration successful. You can now login.");
            } catch (AuthenticationServiceException e) {
                System.out.println("REGISTRATION ERROR: " + e.getMessage());
                System.out.println("Please attempt to register again.");
            }
        }
    }

    private void login() throws ServiceException {
        System.out.println("Please log in");
        currentUser = null;
        while (currentUser == null) // will keep looping until user is logged in
        {
            UserCredentials credentials = collectUserCredentials();
            try {
                currentUser = authenticationService.login(credentials);

                // TODO add current user token to any services
                this.transferService.setAuthToken(currentUser.getToken());
                this.userService.setAuthToken(currentUser.getToken());
                // load the transfer statuses and types into the arrays created at load.
                transferStatuses = transferService.viewAllStatuses();
                transferTypes = transferService.viewAllTypes();
                // populate account details for account object created at load.
                userAccount = transferService.viewBalance(currentUser.getUser().getId());
            } catch (AuthenticationServiceException e) {
                System.out.println("LOGIN ERROR: " + e.getMessage());
                System.out.println("Please attempt to login again.");
            }
        }
    }

    private UserCredentials collectUserCredentials() {
        String username = console.getUserInput("Username");
        String password = console.getUserInput("Password");
        return new UserCredentials(username, password);
    }

    // helper method
    private void printTransferList(Transfer[] transferList) {
        System.out.println("------------------------------------------------");
        System.out.println("Transfers");
        System.out.println("ID\tStatus\t\tFrom\tTo\tAmount");
        System.out.println("------------------------------------------------");
        for (Transfer transfer : transferList) {
            System.out.print(transfer.getTransferId() + "\t");
            System.out.print(transferStatuses[transfer.getTransferStatusId() - 1].getStatusDescription() + "\t");
            System.out.print(userService.matchAccountOwner(transfer.getAccountFrom()) + "\t");
            System.out.print(userService.matchAccountOwner(transfer.getAccountTo()) + "\t");
            System.out.println(transfer.getAmount());
        }
        System.out.println("--------\n");
    }

    private void printTransferDetail(Transfer transfer) {
        System.out.println("------------------------------------------------");
        System.out.println("Transfer Details");
        System.out.println("------------------------------------------------");
        System.out.println("id: " + transfer.getTransferId());
        System.out.println("From: " + userService.matchAccountOwner(transfer.getAccountFrom()));
        System.out.println("To: " + userService.matchAccountOwner(transfer.getAccountTo()));
        System.out.println("Type: " + transferTypes[transfer.getTransferTypeId() - 1].getTypeDescription());
        System.out.println("Status: " + transferStatuses[transfer.getTransferStatusId() - 1].getStatusDescription());
        System.out.println("Amount: $" + transfer.getAmount() + "\n");
    }

    private void printUserList(User[] userList) {
        System.out.println("---------------------------------------------");
        System.out.println("Users");
        System.out.println("ID\tName");
        System.out.println("---------------------------------------------");
        for (User user : userList) {
            if (currentUser.getUser().getId() != user.getId()) {
                System.out.println(user.getId() + "\t" + user.getUsername());
            }
        }
        System.out.println("--------\n");
    }

    private void printPendingTransfers(Transfer[] transferList) {
        System.out.println("-------------------------------------------");
        System.out.println("Pending Transfers");
        System.out.println("ID\tTo\tAmount");
        System.out.println("-------------------------------------------");
        for (Transfer transfer : transferList) {
            System.out.print(transfer.getTransferId() + "\t");
            System.out.print(userService.matchAccountOwner(transfer.getAccountTo()) + "\t");
            System.out.println("$" + transfer.getAmount());
        }
        System.out.println("--------\n");
    }

    private int printPendingApprovalMenu() {
        String menu = "\n1: Approve\n2: Reject\n0: Don't approve or reject\n---------\nPlease choose an option: ";
        int choice = console.getUserInputInteger(menu);
        boolean validChoice = false;
        while (!validChoice) {
            if (choice >= 0 && choice <= 2) {
                validChoice = true;
                break;
            }
            if (!validChoice) {
                choice = console.getUserInputInteger(menu);
            }
        }
        switch (choice) {
        case 1:
            return 2;
        case 2:
            return 3;
        default:
            return 0;

        }
    }
}