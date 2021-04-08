package ca.concordia.risk.io.commands;

import ca.concordia.risk.GameEngine;

/** Command representing <i>"loadgame"</i> operation. */
public class LoadGameCommand implements Command {

	private String d_filename;

	/**
	 * Creates a new <code>LoadGameCommand</code> object.
	 * 
	 * @param p_filename filename of the save file to load the game from.
	 */
	public LoadGameCommand(String p_filename) {
		d_filename = p_filename;
	}

	/** Loads the game from the requested map file. */
	@Override
	public void execute() {
		// TODO: Replace with the actual implementation
		GameEngine.GetView().display("Loading the game from save file " + d_filename);
	}

}
