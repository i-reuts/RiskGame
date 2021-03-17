package ca.concordia.risk.phases;

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
	
	public abstract void execute();
	protected abstract void nextPhase();
}
