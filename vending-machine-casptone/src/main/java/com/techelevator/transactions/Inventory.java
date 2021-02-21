package com.techelevator.transactions;

import java.util.Map;
import com.techelevator.products.*;

public class Inventory {
	
	public static void restockExistingInventory(Map<String, Product> inventory) {
		for (Map.Entry<String, Product> product : inventory.entrySet()) {
			product.getValue().restockInventory();
		}
	}

	public static void restockAndResetExistingInventory(Map<String, Product> inventory) {
		for (Map.Entry<String, Product> product : inventory.entrySet()) {
			product.getValue().resetInventory();
			Transaction.resetSales();
		}
	}

	public static String[][] buildProductArray (Map<String, Product> inventory) {
		int mapItem = 0;
		int numProducts = inventory.size();
		while (numProducts % 4 != 0) {
			numProducts++;
		}
		String[][] productGrid = new String[numProducts][4];
		for (int i = 0; i < numProducts; i++) {
			for (int j = 0; j < 4; j++) {
				productGrid[i][j] = "";
			}
		}
		for (Map.Entry<String, Product> product : inventory.entrySet()) {
			productGrid[mapItem][0] = product.getValue().getProductName();
			productGrid[mapItem][1] = "$" + product.getValue().getPrice();
			if (product.getValue().getRemainingInventory() > 0) {
				productGrid[mapItem][2] = product.getValue().getRemainingInventory() + " available";
			} else {
				productGrid[mapItem][2] = "SOLD OUT";
			}
			productGrid[mapItem][3] = product.getKey();
			mapItem++;
		}

		return productGrid;
	}
}
