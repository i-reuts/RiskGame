package ca.concordia.risk.phases;

import ca.concordia.risk.io.parsers.CommandParser;

public abstract class Phase {
	CommandParser d_parser;
	
	Phase(CommandParser p_parser){
		this.d_parser = p_parser;
	}
	
	abstract void nextPhase();
}
