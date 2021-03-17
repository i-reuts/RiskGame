package ca.concordia.risk.phases;

import ca.concordia.risk.io.commands.Command;
import ca.concordia.risk.io.parsers.CommandParser;

public abstract class Phase {
	CommandParser d_parser;
	
	Phase(CommandParser p_parser){
		this.d_parser = p_parser;
	}
	
	public void executeCommand(String p_userInput) {
		Command l_command = d_parser.parse(p_userInput);
		l_command.execute();
	}
}
