package ca.concordia.risk.io.commands;

/** An interface implemented by all executable commands.
 * 
 *  <p>A <code>Command</code> object represents and executable
 *  command produces by parsing a string command provided by user.
 */
public interface Command {
	/**
	 * Executes the command.
	 */
	public void execute();
}
