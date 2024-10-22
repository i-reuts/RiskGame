package ca.concordia.risk.io.commands;

/**
 * Interface implemented by all executable commands.
 * 
 * <p>
 * A <code>Command</code> object represents an executable command produced by
 * parsing a string command provided by user.
 */
public interface Command {
	/** Executes the command. */
	public void execute();
}
