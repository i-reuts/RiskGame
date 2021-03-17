package ca.concordia.risk.phases;

import ca.concordia.risk.io.commands.Command;
import ca.concordia.risk.io.parsers.CommandParser;

/**
 * Abstract class from which all Phases must extend.
 * @author Enrique
 *
 */
public abstract class Phase {
	CommandParser d_parser;
	
	/**
	 * Constructor to assign the proper parser for the current phase
	 * @param p_parser parser to be used by the current phase
	 */
	Phase(CommandParser p_parser){
		this.d_parser = p_parser;
	}
	
	/**
	 * Method to execute user inputs with the help of its own parser.
	 * @param p_userInput User command string
	 */
	public void executeCommand(String p_userInput) {
		Command l_command = d_parser.parse(p_userInput);
		l_command.execute();
	}
}
