package ca.concordia.risk.io.commands;

import ca.concordia.risk.GameEngine;

/** Command representing <i>"savegame"</i> operation. */
public class SaveGameCommand implements Command {

	private String d_filename;

	/**
	 * Creates a new <code>SaveGameCommand</code> object.
	 * 
	 * @param p_filename filename of the save file to save the active game into.
	 */
	public SaveGameCommand(String p_filename) {
		d_filename = p_filename;
	}

	/** Saves the active game into the requested game file. */
	@Override
	public void execute() {
		// TODO: Replace with the actual implementation
		GameEngine.GetView().display("Saving the game into save file " + d_filename);
	}

}
