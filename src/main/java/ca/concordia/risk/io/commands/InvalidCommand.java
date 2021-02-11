package ca.concordia.risk.io.commands;


/** The <code>InvalidCommand</code> class represents an invalid command given by user.
 * 
 *  <p>When executed, displays a relevant error message.
 */
public class InvalidCommand implements Command {

	private String m_message;
	
	/**
	 * Creates a new <code>InvalidCommand</code> object with 
	 * a given error message.
	 * @param p_message an error message to display when the command
	 * is executed.
	 */
	public InvalidCommand(String p_message) {
		m_message = p_message;
	}
	
	/**
	 * Displays the error message to the user.
	 */
	@Override
	public void execute() {	

	}

}
