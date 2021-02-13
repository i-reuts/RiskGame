package ca.concordia.risk.io.commands;

import ca.concordia.risk.game.GameEngine;
import ca.concordia.risk.io.views.ConsoleView;

/** Command representing <i>"savemap"</i> operation. */
public class SaveMapCommand implements Command {

	private String d_filename;

	/**
	 * Creates a new <code>SaveMapCommand</code> object.
	 * 
	 * @param p_filename filename of the map file to save the active Map into.
	 */
	public SaveMapCommand(String p_filename) {
		d_filename = p_filename;
	}

	/** Saves the active Map into the requested map file. */
	@Override
	public void execute() {
		// TODO Replace with actual implementation
		ConsoleView l_view = GameEngine.GetView();
		l_view.display("\nExecuting savemap command with filename: " + d_filename + "\n");
	}

}
