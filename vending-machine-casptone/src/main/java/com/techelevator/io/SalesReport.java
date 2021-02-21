package com.techelevator.io;

import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.Date;
import java.util.Map;

import com.techelevator.products.Product;
import com.techelevator.transactions.Transaction;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;

public class SalesReport {
	
		private static String fileName;
		public static void createSalesReport(Map<String,Product> inventory) {

				DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy hh-mm-ss aa");
				String dateString = dateFormat.format(new Date());
				fileName = "Sales Report " + dateString + ".txt";
				
				File logFile = new File(fileName);
				
				try (PrintWriter writer = new PrintWriter(new FileOutputStream(new File(fileName), true))) {
					logFile.createNewFile();
					for(Map.Entry<String, Product> items : inventory.entrySet()) {
						String productName = items.getValue().getProductName(); 
						int totalSales = items.getValue().getTotalSold();
						String outputString = productName + "|" + totalSales;
						writer.println(outputString);
					}
					writer.println("\n$" + Transaction.getTotalSales());
					writer.close();
					displaySales(inventory);

				} catch (FileNotFoundException e) {
					System.out.println("Unable to open file for writing. Please try again.");
					System.exit(1);
				} catch (IOException e) {
					System.out.println("Unable to create the log file, please fix and try again.");
					System.exit(1);
				}

		}
		
		public static void displaySales(Map<String, Product> inventory) {
			System.out.println("————————————————————————————————————————————————————");
			System.out.printf("| %-17s %-30s |\n", " ", "Sales Report");
			System.out.println("————————————————————————————————————————————————————");
			System.out.printf("| %-5s | %-18s | %-6s | %-10s |\n", "Slot", "Product", "Sold", "Tot Sales");
			System.out.println("————————————————————————————————————————————————————");
			for (Map.Entry<String, Product> item : inventory.entrySet()) {
				String slot = item.getKey();
				String productName = item.getValue().getProductName();
				int sales = item.getValue().getTotalSold();
				BigDecimal totalSales = item.getValue().getPrice();
				totalSales = totalSales.multiply(BigDecimal.valueOf(sales));
				System.out.printf("| %-5s | %-18s | %-6s | %-10s |\n", slot, productName, sales, totalSales);
			}
			System.out.println("————————————————————————————————————————————————————\n");
			System.out.println("TOTAL SALES: " + Transaction.getTotalSales());
			System.out.println("\nYour sales report has been sucessfully created with the filename: " + fileName + "\n\n");
		}

	}
