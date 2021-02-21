package com.techelevator;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.Map;
import com.techelevator.io.*;
import com.techelevator.products.*;
import com.techelevator.view.Menu;
import com.techelevator.transactions.*;

public class VendingMachineCLI {

	private static final String MAIN_MENU_OPTION_DISPLAY_ITEMS = "Display Vending Machine Items";
	private static final String MAIN_MENU_OPTION_PURCHASE = "Purchase";
	private static final String EXIT = "Exit";
	private static final String SUPER_SECRET_MENU_OPTION = "Super Secret Menu";
	private static final String[] MAIN_MENU_OPTIONS = { MAIN_MENU_OPTION_DISPLAY_ITEMS, MAIN_MENU_OPTION_PURCHASE, EXIT,
			SUPER_SECRET_MENU_OPTION};

	private static final String PURCHASE_MENU_FEED_MONEY = "Feed Money";
	private static final String PURCHASE_MENU_SELECT_PRODUCT = "Select Product";
	private static final String PURCHASE_MENU_FINISH_TRANSACTION = "Finish Transaction";
	private static final String[] PURCHASE_MENU_OPTIONS = { PURCHASE_MENU_FEED_MONEY, PURCHASE_MENU_SELECT_PRODUCT,
			PURCHASE_MENU_FINISH_TRANSACTION };

	private static final String PRINT_SALES_REPORT = "Sales Report";
	private static final String RESTOCK_CURRENT_PRODUCTS = "Restock Existing Selection";
	private static final String LOAD_NEW_PRODUCTS = "Refill with new products from file";
	private static final String RESTOCK_AND_RESET = "Refill the machine and reset all counters";
	private static final String MAIN_MENU = "Return to Main Menu";
	private static final String[] SECRET_MENU_OPTIONS = { PRINT_SALES_REPORT, RESTOCK_CURRENT_PRODUCTS,
			RESTOCK_AND_RESET, LOAD_NEW_PRODUCTS, MAIN_MENU };

	private static Map<String, Product> inventory;
	private Menu menu;

	private static CsvLoader csv = new CsvLoader();

	public VendingMachineCLI(Menu menu) {
		this.menu = menu;
	}

	public void run() throws FileNotFoundException {
		while (true) {
			String choice = (String) menu.getChoiceFromOptions(MAIN_MENU_OPTIONS, "Main");
			switch (choice) {
				case MAIN_MENU_OPTION_DISPLAY_ITEMS:
					menu.displayProductsLikeMachine(inventory);
					break;
				case MAIN_MENU_OPTION_PURCHASE:
					runPurchaseMenu();
					break;
				case EXIT:
					menu.displayMessage("Thank you for your business!");
					System.exit(0);
				case SUPER_SECRET_MENU_OPTION:
					// print the sales report
					menu.displayMessage("\nWELCOME TO THE SUPER SECRET ADMIN MENU");
					runSecretMenu();
					break;
			}
		}
	}

	public void runPurchaseMenu() throws FileNotFoundException {
		while (true) {
			String choice = (String) menu.getChoiceFromOptions(PURCHASE_MENU_OPTIONS, "Purchase");
			switch (choice) {
				case PURCHASE_MENU_FEED_MONEY:
					int depositAmount = 0;
					while (depositAmount <= 0) {
						try {
							String input = (String) menu.getUserInput("How much money would you like to deposit?");
							depositAmount = Integer.parseInt(input);
							if (depositAmount == 1 || depositAmount == 2 || depositAmount == 5 || depositAmount == 10 ||
									depositAmount == 20 || depositAmount == 50 || depositAmount == 100) {
								BigDecimal amountToDeposit = new BigDecimal(input);
								Transaction.loadMoney(amountToDeposit);
							} else {
								depositAmount = 0;
								throw new NumberFormatException();
							}
						} catch (NumberFormatException e) {
							menu.displayMessage("Please enter a valid whole dollar amount to deposit. 1, 2, 5, 10 etc.");
						}
					}
					System.out.println();
					break;
				case PURCHASE_MENU_SELECT_PRODUCT:
					menu.displayProductsLikeMachine(inventory);
					String input = "";
					while (!inventory.containsKey(input)) {
						input = (String) menu.getUserInput("What item would you like to purchase?");
						input = input.toUpperCase();
						if (!inventory.containsKey(input)) {
							menu.displayMessage("Please enter a valid selection");
						}
					}
					if (inventory.get(input).getRemainingInventory() == 0) {
						menu.displayMessage("Sorry this item is sold out please try again");

					} else {
						boolean isSuccess = Transaction.purchase(inventory.get(input).getProductName(),
								inventory.get(input).getPrice());
						if (isSuccess) {
							inventory.get(input).sellProduct();
							String purchaseDetails = "You purchased: " + inventory.get(input).getProductName() + " for $"
									+ inventory.get(input).getPrice() + " you have $" + Transaction.getBalance()
									+ " Remaining\n" + inventory.get(input).getMessage();
							menu.displayMessage(purchaseDetails);
						} else {
							menu.displayMessage("You do not have enough money for this purchase, please add money first!");
						}
					}
					break;
				case PURCHASE_MENU_FINISH_TRANSACTION:
					String returnString = Transaction.giveChange();
					menu.displayMessage("\n" + returnString + "\n");
					run();
					break;
			}
		}
	}

	public void runSecretMenu() throws FileNotFoundException {
		while (true) {
			String choice = (String) menu.getChoiceFromOptions(SECRET_MENU_OPTIONS, "Secret");
			switch (choice) {
				case PRINT_SALES_REPORT:
					SalesReport.createSalesReport(inventory);
					break;
				case RESTOCK_CURRENT_PRODUCTS:
					Inventory.restockExistingInventory(inventory);
					break;
				case RESTOCK_AND_RESET:
					Inventory.restockAndResetExistingInventory(inventory);
					break;
				case LOAD_NEW_PRODUCTS:
					File inputFile = menu.getFileFromUser();
					inventory = csv.loadProductFromFile(inputFile);
					Inventory.restockAndResetExistingInventory(inventory);
					int productsLoaded = inventory.size();
					menu.displayMessage("Successfully Loaded: " + productsLoaded + " new products");
					break;
				case MAIN_MENU:
					run();
					break;
			}
		}
	}

	public static void main(String[] args) throws FileNotFoundException {
		File staticFile = new File("vendingmachine.csv");
		Log.createLogFile();
		try {
			inventory = csv.loadProductFromFile(staticFile);
		} catch (FileNotFoundException e) {
			System.out.println("Unable to load CSV file, please try again.");
			System.exit(1);
		}
		Menu menu = new Menu(System.in, System.out);
		VendingMachineCLI cli = new VendingMachineCLI(menu);
		cli.run();
	}

}