package ca.concordia.risk.io.commands;

import ca.concordia.risk.GameEngine;

/**
 * Command representing an invalid user command.
 * <p>
 * When executed, displays a relevant error message.
 */
public class InvalidCommand implements Command {

	private String d_message;

	/**
	 * Creates a new <code>InvalidCommand</code> object with a given error message.
	 * 
	 * @param p_message error message to display when the command is executed.
	 */
	public InvalidCommand(String p_message) {
		d_message = p_message;
	}

	/** Displays the error message to the user. */
	@Override
	public void execute() {
		GameEngine.GetView().display("Invalid command received: " + d_message);
	}

}
