package ca.concordia.risk.io.commands;

import java.io.IOException;

import ca.concordia.risk.GameEngine;
import ca.concordia.risk.game.GameMap;
import ca.concordia.risk.io.views.ConsoleView;
import ca.concordia.risk.utils.MapLoader;

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
		ConsoleView l_view = GameEngine.GetView();
		GameMap l_gameMap = GameEngine.GetMap();

		if (l_gameMap != null) {
			try {
				MapLoader.SaveMap(d_filename, l_gameMap);
				l_view.display("Map saved");
			} catch (IOException l_e) {
				l_view.display("Error when saving the map: " + l_e.getMessage());
			}
		} else {
			l_view.display("No map to save - please load a map first");
		}
	}

}