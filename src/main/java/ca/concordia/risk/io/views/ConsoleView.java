package ca.concordia.risk.io.views;

import java.util.Scanner;

/**
 * A simple view that prints messages to and reads user input from the Console.
 */
public class ConsoleView {
	
	private Scanner m_scanner = new Scanner(System.in);
	
	/** 
	 * Prints the given message to the Console.
	 * @param p_message message to be printed.
	 */
	public void display(String p_message) {
		System.out.println(p_message);
	}
	
	/** 
	 * Reads a line of user input from the Console.
	 * @return a string containing user input.
	 */
	public String getInput() {
		return m_scanner.nextLine();
	}
}
