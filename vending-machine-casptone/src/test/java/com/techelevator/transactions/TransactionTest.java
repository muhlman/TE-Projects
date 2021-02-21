package com.techelevator.transactions;

import java.math.BigDecimal;
import java.math.RoundingMode;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.techelevator.products.Chip;
import com.techelevator.products.Product;

public class TransactionTest {
	
	@Before
	public void setup() {
		Transaction.resetBalance();
	}
	
	@Test
	public void loadMoneyCheckBalanceTest() {
		//Arrange
		Transaction.loadMoney(new BigDecimal(5.00));
		BigDecimal expectedResult = new BigDecimal(5.00).setScale(2, RoundingMode.HALF_UP);
		//Act
		BigDecimal actualReturn = Transaction.getBalance();
		//Assert
		Assert.assertEquals("Expected: 5.00", expectedResult, actualReturn);		
	}
	
	@Test
	public void makePurchaseTest() {
		//Arrange
		Transaction.loadMoney(new BigDecimal(5.00));
		//Act
		boolean actualReturn = Transaction.purchase("Chippy Chips", new BigDecimal(1.50));
		//Assert
		Assert.assertTrue("Expected: true", actualReturn);		
	}
	
	@Test
	public void makePurchaseNotEnoughMoneyTest() {
		//Arrange
		Transaction.loadMoney(new BigDecimal(1.00));
		//Act
		boolean actualReturn = Transaction.purchase("Chippy Chips", new BigDecimal(1.50));
		//Assert
		Assert.assertFalse("Expected: true", actualReturn);		
	}
	
	@Test
	public void makeChangeTest() {
		//Arrange
		Transaction.loadMoney(new BigDecimal(5.65));
		String expectedResult = "Change Due: 22 Quarters 1 Dime 1 Nickel ";
		//Act
		String actualReturn = Transaction.giveChange();
		//Assert
		Assert.assertEquals("Expected: 5.00", expectedResult, actualReturn);		
	}
	
	@Test
	public void checkTotalSalesTest() {
		//Arrange
		Transaction.loadMoney(new BigDecimal(5.00));
		boolean tempBool = Transaction.purchase("Chippy Chips", new BigDecimal(1.50));
		tempBool = Transaction.purchase("Chippy Chips", new BigDecimal(1.50));
		tempBool = Transaction.purchase("Chippy Chips", new BigDecimal(1.50));
		BigDecimal expectedResult = new BigDecimal(4.50).setScale(2, RoundingMode.HALF_UP);
		//Act
		BigDecimal actualReturn = Transaction.getTotalSales();
		//Assert
		Assert.assertEquals("Expected: 5.00", expectedResult, actualReturn);		
	}

}
