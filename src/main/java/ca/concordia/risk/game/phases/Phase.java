package ca.concordia.risk.game.phases;

import ca.concordia.risk.GameEngine;
import ca.concordia.risk.io.commands.Command;
import ca.concordia.risk.io.parsers.CommandParser;

/**
 * Abstract class from which all Phases must extend.
 * <p>
 * Represent a game phase.
 * 
 * @author Enrique
 *
 */
public abstract class Phase {

	/** Phase specific command parser. */
	protected CommandParser d_commandParser;
	private Phase d_nextPhase;

	/**
	 * Executes the phase once.
	 * <p>
	 * Default implementation waits for and processes one user command using the
	 * appropriate phase-specific command-parser.
	 */
	public void execute() {
		GameEngine.ProcessUserCommand();
	};

	/**
	 * Parses a user command using the appropriate phase-specific command-parser.
	 * 
	 * @param p_rawInput string representing raw command input.
	 * @return executable <code>Command</code> constructed from the given command
	 *         string.
	 */
	public Command parseCommand(String p_rawInput) {
		return d_commandParser.parse(p_rawInput);
	}

	/**
	 * Gets the phase following the current phase.
	 * 
	 * @return phase object representing the next phase.
	 */
	public Phase getNextPhase() {
		return d_nextPhase;
	}

	/**
	 * Sets the phase following the current phase.
	 * 
	 * @param p_nextPhase phase object representing the next phase.
	 */
	public void setNextPhase(Phase p_nextPhase) {
		d_nextPhase = p_nextPhase;
	}
}
