package ca.concordia.risk.game.phases;

import ca.concordia.risk.io.parsers.StartupCommandParser;

/**
 * Class representing the Startup Phase.
 * 
 * <p>
 * Inherits the default implementation of the parent <code>Phase</code> class
 * for it's execution method. Executing this phase waits for and processes one
 * user command using the <code>StartupCommandParser</code>.
 * 
 * @author Enrique
 *
 */
public class StartupPhase extends Phase {
	/**
	 * Creates a new <code>StartupPhase</code> object.
	 */
	public StartupPhase() {
		d_commandParser = new StartupCommandParser();
	}
}
