package ca.concordia.risk.phases;

import ca.concordia.risk.io.parsers.CommandParser;

/**
 * Class representing the Startup Phase
 * @author Enrique
 *
 */
public class StartupPhase extends Phase{
	
	/**
	 * Constructor for Startup Phase
	 * @param p_parser parser used by this phase
	 */
	public StartupPhase(CommandParser p_parser) {
		super(p_parser);
	}
}
