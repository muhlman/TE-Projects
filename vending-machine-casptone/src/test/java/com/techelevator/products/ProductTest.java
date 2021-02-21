package com.techelevator.products;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.junit.Assert;
import org.junit.Test;

public class ProductTest {
	
	@Test
	public void getProductSlotTest() {
		//Arrange
		Product product = new Chip("A4", "Chippity Chips", new BigDecimal(5.50), "chip");
		String expectedResult = "A4";
		//Act
		String actualReturn = product.getSlot();
		//Assert
		Assert.assertEquals("Expected: A4", expectedResult, actualReturn);		
	}
	
	@Test
	public void getProductNameTest() {
		//Arrange
		Product product = new Chip("A4", "Chippity Chips", new BigDecimal(5.50), "chip");
		String expectedResult = "Chippity Chips";
		//Act
		String actualReturn = product.getProductName();
		//Assert
		Assert.assertEquals("Expected: Chippity Chios", expectedResult, actualReturn);		
	}
	
	@Test
	public void getProductPriceTest() {
		//Arrange
		Product product = new Chip("A4", "Chippity Chips", new BigDecimal(5.50), "chip");
		BigDecimal expectedResult = new BigDecimal(5.50).setScale(2, RoundingMode.HALF_UP);
		//Act
		BigDecimal actualReturn = product.getPrice();
		//Assert
		Assert.assertEquals("Expected: 5.50", expectedResult, actualReturn);		
	}
	
	@Test
	public void getProductPriceWithExtraDigitsTest() {
		//Arrange
		Product product = new Chip("A4", "Chippity Chips", new BigDecimal(5.5004214421), "chip");
		BigDecimal expectedResult = new BigDecimal(5.50).setScale(2, RoundingMode.HALF_UP);
		//Act
		BigDecimal actualReturn = product.getPrice();
		//Assert
		Assert.assertEquals("Expected: 5.50", expectedResult, actualReturn);		
	}
	
	@Test
	public void getProductCategoryTest() {
		//Arrange
		Product product = new Chip("A4", "Chippity Chips", new BigDecimal(5.50), "chip");
		String expectedResult = "chip";
		//Act
		String actualReturn = product.getCategory();
		//Assert
		Assert.assertEquals("Expected: chip", expectedResult, actualReturn);		
	}
	
	@Test
	public void getTotalSoldTallyTest() {
		//Arrange
		Product product = new Chip("A4", "Chippity Chips", new BigDecimal(5.50), "chip");
		int expectedResult = 2;
		//Act
		product.sellProduct();
		product.sellProduct();
		int actualReturn = product.getTotalSold();
		//Assert
		Assert.assertEquals("Expected: 2", expectedResult, actualReturn);		
	}
	
	@Test
	public void getTotalRemainingInMachineTest() {
		//Arrange
		Product product = new Chip("A4", "Chippity Chips", new BigDecimal(5.50), "chip");
		int expectedResult = 3;
		//Act
		product.sellProduct();
		product.sellProduct();
		int actualReturn = product.getRemainingInventory();
		//Assert
		Assert.assertEquals("Expected: 3", expectedResult, actualReturn);		
	}
	
	@Test
	public void getProductMessageChipTest() {
		//Arrange
		Product product = new Chip("A4", "Chippity Chips", new BigDecimal(5.50), "chip");
		String expectedResult = "Crunch Crunch, Yum!";
		//Act
		String actualReturn = product.getMessage();
		//Assert
		Assert.assertEquals("Expected: Crunch Crunch, Yum!", expectedResult, actualReturn);		
	}
	
	@Test
	public void getProductMessageGumTest() {
		//Arrange
		Product product = new Gum("A4", "Wrigley", new BigDecimal(0.75), "gum");
		String expectedResult = "Chew Chew, Yum!";
		//Act
		String actualReturn = product.getMessage();
		//Assert
		Assert.assertEquals("Expected: Chew Chew, Yum!", expectedResult, actualReturn);		
	}
	
	@Test
	public void getProductMessageDrinkTest() {
		//Arrange
		Product product = new Drink("C3", "Dr. Pibb", new BigDecimal(1.75), "drink");
		String expectedResult = "Glug Glug, Yum!";
		//Act
		String actualReturn = product.getMessage();
		//Assert
		Assert.assertEquals("Expected: Chew Chew, Yum!", expectedResult, actualReturn);		
	}
	
	@Test
	public void getProductMessageCandyTest() {
		//Arrange
		Product product = new Candy("D3", "Snackers", new BigDecimal(1.25), "candy");
		String expectedResult = "Munch Munch, Yum!";
		//Act
		String actualReturn = product.getMessage();
		//Assert
		Assert.assertEquals("Expected: Chew Chew, Yum!", expectedResult, actualReturn);		
	}
	
	
}
