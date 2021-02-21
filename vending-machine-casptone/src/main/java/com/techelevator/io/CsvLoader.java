package com.techelevator.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

import com.techelevator.products.*;

public class CsvLoader {
	private static int loadedProducts = 0;
	
	public Map<String, Product> loadProductFromFile(File fileName) throws FileNotFoundException {
		Map<String, Product> inventory = new TreeMap<String, Product>();
		loadedProducts = 0;
		try (Scanner fileScanner = new Scanner(fileName)) {
			while (fileScanner.hasNextLine()) {
				String line = fileScanner.nextLine();
				if (line != null) {
					Product p = splitProduct(line);
					if (p != null) {
						inventory.put(p.getSlot(), p);
					}
				}
			}
		} catch (FileNotFoundException fnf) {
			System.out.println("Sorry, I can't read the file, please try again");
			System.exit(1);
		}
		
		return inventory;
	}

	private Product splitProduct(String line) {
		String[] components = line.split("\\|");
		if (components.length != 4) {
			System.out.println("Unable to load products. Try again later.");
			System.exit(1);
		}
		String slot = components[0].trim().toUpperCase();
		String productName = components[1].trim();
		BigDecimal price = new BigDecimal(components[2].trim());
		String productType = components[3].trim().toLowerCase();

		switch (productType) {
			case "chip":
				loadedProducts++;
				return new Chip(slot, productName, price, productType);
			case "drink":
				loadedProducts++;
				return new Drink(slot, productName, price, productType);
			case "candy":
				loadedProducts++;
				return new Candy(slot, productName, price, productType);
			case "gum":
				loadedProducts++;
				return new Gum(slot, productName, price, productType);
			default:
				// TODO: Possibly add a generic product if type is missing instead of throwing error message and returning null
				Log.logTransaction("Malformed Product " + slot, BigDecimal.valueOf(0.00), BigDecimal.valueOf(0.00));
				return null;
		}
	}

}
