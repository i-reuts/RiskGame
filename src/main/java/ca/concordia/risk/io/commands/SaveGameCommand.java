package ca.concordia.risk.io.commands;

import ca.concordia.risk.GameEngine;
import ca.concordia.risk.io.views.ConsoleView;
import ca.concordia.risk.utils.GameLoader;
import ca.concordia.risk.utils.GameLoader.GameLoaderException;

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
		ConsoleView l_view = GameEngine.GetView();
		l_view.display("Saving the game into save file " + d_filename + "...");
		try {
			GameLoader.SaveGame(d_filename);
			l_view.display("Game saved successfully");
		} catch (GameLoaderException l_e) {
			l_view.display(l_e.getMessage());
			l_view.display("Saving failed");
		}
	}

}
