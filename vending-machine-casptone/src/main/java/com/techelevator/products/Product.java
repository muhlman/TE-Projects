package com.techelevator.products;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Product {
	
	private final int MAX_INVENTORY = 5;
	private int remainingInventory = MAX_INVENTORY;
	private int productSold = 0;
	private BigDecimal price = new BigDecimal(0.00);
	private String productName;
	private String slot;
	private String category;

	public Product(String slot, String productName, BigDecimal price, String category) {
		this.productName = productName;
		this.price = price;
		this.slot = slot;
		this.category = category;
	}

	public int getTotalSold() {
		return productSold;
	}

	public BigDecimal getPrice() {
		return price.setScale(2, RoundingMode.HALF_UP);
	}

	public String getProductName() {
		return productName;
	}
	
	public String getSlot() {
		return slot;
	}
	
	public String getCategory() {
		return category;
	}

	public int getRemainingInventory() {
		return remainingInventory;
	}

	public void sellProduct() {
		this.remainingInventory -= 1;
		productSold++;
	}
	
	public void restockInventory() {
		this.remainingInventory = MAX_INVENTORY;
	}
	
	public void resetInventory() {
		restockInventory();
		this.productSold = 0;
	}
	
	public String getMessage() {
		return "Enjoy your snack!";
	}
}
