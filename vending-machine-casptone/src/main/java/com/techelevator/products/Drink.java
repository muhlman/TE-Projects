package com.techelevator.products;

import java.math.BigDecimal;

public class Drink extends Product {

	public Drink(String slot, String productName, BigDecimal price, String category) {
		super(slot, productName, price, category);
	}

	@Override
	public String getMessage() {
		return "Glug Glug, Yum!";
	}
	
}
