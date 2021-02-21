package com.techelevator.view;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.File;
import java.util.Map;
import java.util.Scanner;
import org.apache.commons.lang3.StringUtils;

import com.techelevator.transactions.Inventory;
import com.techelevator.products.Product;
import com.techelevator.transactions.Transaction;

public class Menu {

    private PrintWriter out;
    private Scanner in;

    public Menu(InputStream input, OutputStream output) {
        this.out = new PrintWriter(output);
        this.in = new Scanner(input);
    }

    public Object getChoiceFromOptions(Object[] options, String menu) {
        Object choice = null;
        while (choice == null) {
            displayMenuOptions(options, menu);
            choice = getChoiceFromUserInput(options);
        }
        return choice;
    }

    private Object getChoiceFromUserInput(Object[] options) {
        Object choice = null;
        String userInput = in.nextLine();
        try {
            int selectedOption = Integer.parseInt(userInput);
            if (selectedOption > 0 && selectedOption <= options.length) {
                choice = options[selectedOption - 1];
            }
        } catch (NumberFormatException e) {
            // eat the exception, an error message will be displayed below since choice will
            // be null
        }
        if (choice == null) {
            out.println("\n*** " + userInput + " is not a valid option ***\n");
        }
        return choice;
    }

    private void displayMenuOptions(Object[] options, String menuType) {
        out.println();
        if (menuType.equals("Main")) {
            for (int i = 0; i < 3; i++) {
                int optionNum = i + 1;
                out.println(optionNum + ") " + options[i]);
            }
        } else {
            for (int i = 0; i < options.length; i++) {
                int optionNum = i + 1;
                out.println(optionNum + ") " + options[i]);
            }
            if (menuType.equals("Purchase")) {
                out.println("\nCurrent Money Provided: $" + String.format("%.2f", Transaction.getBalance()));
            }
        }

        out.println("\nPlease choose an option >>> ");
        out.flush();
    }

    public Object getUserInput(String prompt) {
        if (!prompt.equals("")) {
            out.println(prompt);
            out.flush();
        }
        return in.nextLine();
    }

    public File getFileFromUser() {
        System.out.println("Please enter the path to the new product file: ");
        String filePath = in.nextLine();

        File input = new File(filePath);

        while (!input.exists() && !input.isFile()) {
            if (!input.exists()) {
                System.out.println(filePath + " does not exist at this path");
            } else if (!input.isFile()) {
                System.out.println(filePath + " is not a proper file");
            }
            System.out.println("Please enter a valid file path: ");
            filePath = in.nextLine();
            input = new File(filePath);
        }

        return input;
    }

    // Method to display an error message if something went wrong.
    public void displayMessage(String message) {
        System.out.println(message);

    }

    // Display the products in a grid instead of a list.
    public void displayProductsLikeMachine(Map<String, Product> inventory) {
        String[][] productGrid = Inventory.buildProductArray(inventory);

        for (int i = 0; i < productGrid.length; i += 4) {
                System.out.println("—————————————————————————————————————————————————————————————————————————————————————————————");
                System.out.printf("| %-20s | %-20s | %-20s | %-20s |\n", StringUtils.center(productGrid[i][0], 20),
                        StringUtils.center(productGrid[i + 1][0], 20), StringUtils.center(productGrid[i + 2][0], 20),
                        StringUtils.center(productGrid[i + 3][0], 20));
                System.out.printf("| %-20s | %-20s | %-20s | %-20s |\n", StringUtils.center(productGrid[i][1], 20),
                        StringUtils.center(productGrid[i + 1][1], 20), StringUtils.center(productGrid[i + 2][1], 20),
                        StringUtils.center(productGrid[i + 3][1], 20));
                System.out.printf("| %-20s | %-20s | %-20s | %-20s |\n", StringUtils.center(productGrid[i][2], 20),
                        StringUtils.center(productGrid[i + 1][2], 20), StringUtils.center(productGrid[i + 2][2], 20),
                        StringUtils.center(productGrid[i + 3][2], 20));
                System.out.printf("| %-20s | %-20s | %-20s | %-20s |\n", StringUtils.center(productGrid[i][3], 20),
                        StringUtils.center(productGrid[i + 1][3], 20), StringUtils.center(productGrid[i + 2][3], 20),
                        StringUtils.center(productGrid[i + 3][3], 20));
            }
        System.out.println("—————————————————————————————————————————————————————————————————————————————————————————————");


    }

}
