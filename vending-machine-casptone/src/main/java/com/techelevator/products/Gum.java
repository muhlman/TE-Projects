package com.techelevator.products;

import java.math.BigDecimal;

public class Gum extends Product {

	public Gum(String slot, String productName, BigDecimal price, String category) {
		super(slot, productName, price, category);
	}

	@Override
	public String getMessage() {
		return "Chew Chew, Yum!";
	}
	
}
