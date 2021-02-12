package ca.concordia.risk.io.views;

import java.util.Scanner;

/**
 * A simple view that displays messages and reads user input from the Console.
 */
public class ConsoleView {

	private Scanner d_scanner = new Scanner(System.in);

	/**
	 * Prints the given message to the Console.
	 * 
	 * @param p_message message to be printed.
	 */
	public void display(String p_message) {
		System.out.println(p_message);
	}

	/**
	 * Reads a line of user input from the Console.
	 * 
	 * @return user input string.
	 */
	public String getInput() {
		return d_scanner.nextLine();
	}
}
