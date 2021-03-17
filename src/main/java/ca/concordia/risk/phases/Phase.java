package ca.concordia.risk.phases;

import ca.concordia.risk.GameEngine;
import ca.concordia.risk.io.commands.Command;
import ca.concordia.risk.io.parsers.CommandParser;
import ca.concordia.risk.io.views.ConsoleView;

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
	public void execute() {
		ConsoleView l_view = GameEngine.GetView();
		l_view.display("\nPlease enter your command:");
		Command l_command = d_parser.parse(l_view.getInput());
		l_command.execute();
	}
}
