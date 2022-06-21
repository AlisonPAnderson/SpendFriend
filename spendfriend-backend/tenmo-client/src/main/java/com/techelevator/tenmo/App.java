package com.techelevator.tenmo;

import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.services.AccountService;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.ConsoleService;
import com.techelevator.tenmo.services.TransferService;

import java.math.BigDecimal;
import java.util.List;


public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";

    private final ConsoleService consoleService = new ConsoleService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);
    private final AccountService accountService = new AccountService(API_BASE_URL);
    private final TransferService transferService = new TransferService(API_BASE_URL);

    private final User user = new User();

    private AuthenticatedUser currentUser;

    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    private void run() {
        consoleService.printGreeting();
        loginMenu();
        if (currentUser != null) {

            mainMenu();
        }
    }
    private void loginMenu() {
        int menuSelection = -1;
        while (menuSelection != 0 && currentUser == null) {
            consoleService.printLoginMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                handleRegister();
            } else if (menuSelection == 2) {
                handleLogin();
            } else if (menuSelection != 0) {
                System.out.println("Invalid Selection");
                consoleService.pause();
            }
        }
    }

    private void handleRegister() {
        System.out.println("Please register a new user account");
        UserCredentials credentials = consoleService.promptForCredentials();
        if (authenticationService.register(credentials)) {
            System.out.println("Registration successful. You can now login.");
        } else {
            consoleService.printErrorMessage();
        }
    }

    private void handleLogin() {
        UserCredentials credentials = consoleService.promptForCredentials();
        currentUser = authenticationService.login(credentials);
        try {
            transferService.setAuthToken(currentUser.getToken());
            accountService.setAuthToken(currentUser.getToken());
        } catch (Exception e) {
            consoleService.printErrorMessage();
        }
        if (currentUser == null) {
            consoleService.printErrorMessage();
        }
    }

    private void mainMenu() {
        int menuSelection = -1;
        while (menuSelection != 0) {
            consoleService.printMainMenu();

            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                viewCurrentBalance();
            } else if (menuSelection == 2) {
                viewTransferHistory();
            } else if (menuSelection == 3) {
                viewPendingRequests();
            } else if (menuSelection == 4) {
                sendBucks();
            } else if (menuSelection == 5) {
                requestBucks();
            } else if (menuSelection == 0) {
                continue;
            } else {
                System.out.println("Invalid Selection");
            }
            consoleService.pause();
        }
    }

	private void viewCurrentBalance() {
        if (accountService.getBalance(currentUser.getUser().getAccount().getId()) == null) {
            System.out.println("You cannot access this balance. ");
        } else {
            System.out.println("Your current balance is:  $" + accountService.getBalance(currentUser.getUser().getAccount().getId()));
        }
    }

	private void viewTransferHistory() {

        System.out.println("--------------------------------------------------------");
        System.out.println("------------------Transfer History----------------------");
        System.out.printf("%-10s %-2s %-10s %-2s %-10s %-2s %-10s\n", "Transfer ID", " | ", "From", " | ", "To", " | ", "Amount");
        //System.out.println("Transfer ID                 |  Username                                       Amount     ");
        System.out.println("--------------------------------------------------------");

    List<Transfer> transferHistory = transferService.getAllTransfersByAccountId(currentUser.getUser().getAccount().getId());
        for (Transfer t : transferHistory) {
            if (t.getTransferStatus() == TransferStatus.approved || t.getTransferStatus() == TransferStatus.rejected) {
                User toUser = accountService.getUserByUserId(t.getToAccount().getUserId());
                User fromUser = accountService.getUserByUserId(t.getFromAccount().getUserId());
                System.out.printf("%-11s %-2s %-10s %-2s %-10s %-2s %-10s\n", t.getTransferId(), " | ", fromUser.getUsername(), " | ", toUser.getUsername(), " | ", "$" + t.getAmount());
                //System.out.println("Transfer ID: " + t.getTransferId() + "  |  From User: " + fromUser.getUsername() + "  |  To User: " + toUser.getUsername() + "  |  $" + t.getAmount());
            }
        }
        System.out.println("\n");

        consoleService.printGetTransferDetailsOption();
        int selection = consoleService.promptForMenuSelection("Please choose an option: ");
        if (selection == 1) {
            long transferID = consoleService.promptForInt("Enter a transfer ID to view transfer details: ");
            showTransferDetails(transferID);
        }

	}

	private void viewPendingRequests() {

        List<Transfer> pendingRequests = transferService.getAllSentTransfersByAccountId(currentUser.getUser().getAccount().getId());

        System.out.println("---------------------------------");
        System.out.println("        Pending Requests         ");
        System.out.printf("%-10s %-2s %-10s %-2s %-10s\n", "ID", " | ", "To", " | ", "Amount");
        System.out.println("---------------------------------");

        for (Transfer t : pendingRequests) {
            if (t.getTransferStatus() == TransferStatus.pending) {
                User toUser = accountService.getUserByUserId(t.getToAccount().getUserId());
                System.out.printf("%-10s %-2s %-10s %-2s %-10s\n", t.getTransferId(), " | ", toUser.getUsername(), " | ", t.getAmount());
            }
        }
        System.out.println("\n");
        consoleService.printApproveOrRejectOptions();
        int selection = consoleService.promptForMenuSelection("Please choose an option:");
        boolean validOption = false;
        while (!validOption) {
            if (selection == 1) {
                // APPROVE TRANSFER
                long transferId = consoleService.promptForInt("Please enter the ID of the transfer you would like to approve: ");
                Transfer transfer = transferService.approveTransfer(transferId);
                if (transfer.getTransferStatus() == TransferStatus.nsf) {
                    System.out.println("Non sufficient funds.");
                    consoleService.printMainMenu();
                } else if (transfer.getTransferStatus() == TransferStatus.unauthorized) {
                    System.out.println("You are not authorized to approve or decline this transaction.");
                }else if (transfer.getTransferStatus() == TransferStatus.approved) {
                    Transfer approvedTransfer = transferService.getTransferByTransferId(transfer.getTransferId());
                    System.out.println("Approved transfer ID: " + approvedTransfer.getTransferId() +
                            "\nSent: $" + approvedTransfer.getAmount() + " to: " + accountService.getUserByUserId(approvedTransfer.getToAccount().getUserId()).getUsername() +
                            "\nYour updated balance: $" + accountService.getAccountByUserId(currentUser.getUser().getId()).getBalance());
                }

                validOption = true;
            } else if (selection == 2) {
                // REJECT TRANSFER
                long transferId = consoleService.promptForInt("Please enter the ID of the transfer you would like to reject: ");
                Transfer rejectedTransfer = transferService.rejectTransfer(transferId);
                if (rejectedTransfer.getTransferStatus() == TransferStatus.unauthorized) {
                    System.out.println("You are not authorized to approve or decline this transaction.");
                }else if (rejectedTransfer.getTransferStatus() == TransferStatus.rejected) {
                    System.out.println("Rejected transfer ID: " + rejectedTransfer.getTransferId() + " for: $" + rejectedTransfer.getAmount());
                }
                validOption = true;
            } else if (selection == 0) {
                validOption = true;
            } else {
                System.out.println("Invalid selection");
                break;
            }
        }

        }


	private void sendBucks() {

        consoleService.printAllUsers(accountService.getAllUsers());

        TransferCredentials td = consoleService.promptForTransferCredentials(currentUser.getUser().getId());

        Transfer transfer = transferService.sendTransfer(td);

        if (transfer.getTransferStatus() == TransferStatus.invalid_transfer) {
            System.out.println("Transfer invalid.");
            consoleService.printMainMenu();
        } else if (transfer.getTransferStatus() == TransferStatus.user_not_found) {
            System.out.println("User not found.");
            consoleService.printMainMenu();
        } else if (transfer.getTransferStatus() == TransferStatus.invalid_amount) {
            System.out.println("Invalid transfer amount.");
            consoleService.printMainMenu();
        } else if (transfer.getTransferStatus() == TransferStatus.nsf) {
            System.out.println("Non sufficient funds.");
            consoleService.printMainMenu();
        } else if (transfer.getTransferStatus() == TransferStatus.approved) {
            Transfer sentTransfer = transferService.getTransferByTransferId(transfer.getTransferId());
            System.out.println("Transferred $" + sentTransfer.getAmount() + " To: " + accountService.getUserByUserId(sentTransfer.getToAccount().getUserId()).getUsername() +
                    "\nYour updated balance: $" + accountService.getAccountByUserId(currentUser.getUser().getId()).getBalance());

        }
	}

	private void requestBucks() {

        consoleService.printAllUsers(accountService.getAllUsers());

        TransferCredentials td = consoleService.promptForRequestTransferCredentials(currentUser.getUser().getId());

        Transfer transfer = transferService.requestTransfer(td);

        if (transfer.getTransferStatus() == TransferStatus.invalid_transfer) {
            System.out.println("Transfer invalid.");
            consoleService.printMainMenu();
        } else if (transfer.getTransferStatus() == TransferStatus.user_not_found) {
            System.out.println("User not found.");
            consoleService.printMainMenu();
        } else if (transfer.getTransferStatus() == TransferStatus.invalid_amount) {
            System.out.println("Invalid transfer amount.");
            consoleService.printMainMenu();
        } else if (transfer.getTransferStatus() == TransferStatus.nsf) {
            System.out.println("Non sufficient funds.");
            consoleService.printMainMenu();
        } else if (transfer.getTransferStatus() == TransferStatus.pending) {
            System.out.println("Requested: $" + transfer.getAmount() + " From: " + accountService.getUserByUserId(td.getFromId()).getUsername());
        }

	}

    private void showTransferDetails (long transferId) {
        try {
            Transfer transfer = transferService.getTransferByTransferId(transferId);
            long id = transfer.getTransferId();
            String fromUsername = accountService.getUserByUserId(transfer.getFromAccount().getUserId()).getUsername();
            String toUsername = accountService.getUserByUserId(transfer.getToAccount().getUserId()).getUsername();
            String type = String.valueOf(transfer.getTransferType());
            String status = String.valueOf(transfer.getTransferStatus());
            BigDecimal amount = transfer.getAmount();

            consoleService.printTransferDetails(id, fromUsername, toUsername, type, status, amount);
        } catch (Exception e) {
            System.out.println("Invalid transfer ID.");
        }
    }

}
