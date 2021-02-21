package com.techelevator.io;

import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.Date;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class Log {

	private static String fileName = "Log.txt";
	
	// Appends the transaction to the existing log file.
	private static void logToFile(String logLine) {

		try {
			PrintWriter writer = new PrintWriter(new FileOutputStream(new File(fileName), true)); 
		    writer.append(logLine);
		    writer.close();

		} catch (FileNotFoundException e) {
			System.out.println("Unable to open file for writing. Please try again.");
			System.exit(1);
		}

	}

	// Constructs the string to write to the log file and then calls the method to output the string to the logfile.
	public static void logTransaction(String transactionType, BigDecimal beginningBalance, BigDecimal endingBalance) {

		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa");
		String dateString = dateFormat.format(new Date());
		beginningBalance = beginningBalance.setScale(2, RoundingMode.HALF_UP);
		endingBalance = endingBalance.setScale(2, RoundingMode.HALF_UP);
		String beginBalance = "$" + beginningBalance;
		String endBalance = "$" + endingBalance;
		String logString = String.format("%-24s %-23s %-13s %-6s\n", dateString, transactionType, beginBalance, endBalance);
		logToFile(logString);


	}

	
	// This method is called when the program is started, if the log file exists it will simply append to the existing log file with a time stamp of the start
	public static void createLogFile() {

			DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy-hh-mm-ss-aa");
			String dateString = dateFormat.format(new Date());
			String openingLine = String.format("| %-41s %-25s |", "Start of vending log for new session: ", dateString);
			String header = String.format("| %-21s | %-20s | %-10s | %-6s |", "Date", "Action", "Begin Bal", "End Bal");
			
			File logFile = new File(fileName);
			
			try (PrintWriter writer = new PrintWriter(new FileOutputStream(new File(fileName), true))) {
				logFile.createNewFile();
				writer.println("———————————————————————————————————————————————————————————————————————");
				writer.println(openingLine);
				writer.println("———————————————————————————————————————————————————————————————————————");
				writer.println(header);
				writer.println("———————————————————————————————————————————————————————————————————————");
				writer.close();

			} catch (FileNotFoundException e) {
				System.out.println("Unable to open file for writing. Please try again.");
				System.exit(1);
			} catch (IOException e) {
				System.out.println("Unable to create the log file, please fix and try again.");
				System.exit(1);
			}
		
		
		

	}

}
