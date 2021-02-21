package com.techelevator.products;

import java.math.BigDecimal;

public class Candy extends Product {

	public Candy(String slot, String productName, BigDecimal price, String category) {
		super(slot, productName, price, category);
		
	}

	@Override
	public String getMessage() {
		return "Munch Munch, Yum!";
	}
	
}
