package ca.concordia.risk.io.commands;

import ca.concordia.risk.GameEngine;
import ca.concordia.risk.io.views.ConsoleView;
import ca.concordia.risk.utils.GameLoader;
import ca.concordia.risk.utils.GameLoader.GameLoaderException;

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
		ConsoleView l_view = GameEngine.GetView();
		l_view.display("Loading the save file " + d_filename + "...");
		try {
			GameLoader.LoadGame(d_filename);
			l_view.display("Save file loaded successfully");
		} catch (GameLoaderException l_e) {
			l_view.display(l_e.getMessage());
			l_view.display("Loading failed");
		}
	}
}
