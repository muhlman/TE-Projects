package com.techelevator.transactions;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.techelevator.io.Log;

public class Transaction {

	private static BigDecimal totalSales = new BigDecimal("0.00");
	private static BigDecimal balance = new BigDecimal("0.00");

	public static void loadMoney(BigDecimal moneyIn) {
		Log.logTransaction("FEED MONEY:", balance, balance.add(moneyIn));
		balance = balance.add(moneyIn);
	}

	public static boolean purchase(String productName, BigDecimal moneySpent) {
		if (balance.subtract(moneySpent).signum() >= 0) {
			Log.logTransaction(productName, balance, balance.subtract(moneySpent));
			totalSales = totalSales.add(moneySpent);
			balance = balance.subtract(moneySpent);
			return true;
		} else {
			return false;
		}
	}

	public static String giveChange() {
		BigDecimal startingBalance = balance;
		String returnString = "Change Due: ";
		
		if (balance.signum() > 0) {
			int[] change = new int[4];
			int tempBalance = (int)(balance.doubleValue() * 100);
			String[] coins = { "Quarter", "Dime", "Nickel", "Penny", "Quarters", "Dimes", "Nickels", "Pennies" };
			int[] coinValue = { 25, 10, 5, 1 };
			
			for (int i = 0; i < 4; i++) {
				change[i] = tempBalance / coinValue[i];
				tempBalance -= change[i] * coinValue[i];
				if (change[i] > 1) {
					returnString += change[i] + " " + coins[i + 4] + " ";
				} else if (change[i] == 1) {
					returnString += change[i] + " " + coins[i] + " ";
				}
			}  
		} else {
			return "No change is due.";
		}

		balance = BigDecimal.valueOf(0.00);
		Log.logTransaction("GIVE CHANGE:", startingBalance, balance);
		return returnString;
	}

	public static BigDecimal getBalance() {
		return balance.setScale(2, RoundingMode.HALF_UP);
	}

	public static BigDecimal getTotalSales() {
		return totalSales.setScale(2, RoundingMode.HALF_UP);
	}

	// Called from the proper super secret menu option via the inventory class file.
	protected static void resetSales() {
		totalSales = BigDecimal.valueOf(0.00);
	}
	
	// used only for j-unit tests, is this ok to use or should we find another way?
	protected static void resetBalance() {
		balance = BigDecimal.valueOf(0.00);
	}

}
