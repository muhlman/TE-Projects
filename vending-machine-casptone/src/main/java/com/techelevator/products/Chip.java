package com.techelevator.products;

import java.math.BigDecimal;

public class Chip extends Product {

	public Chip(String slot, String productName, BigDecimal price, String category) {
		super(slot, productName, price, category);
	}

	@Override
	public String getMessage() {
		return "Crunch Crunch, Yum!";
	}
	
}
