package ca.concordia.risk.io.commands;

import java.io.FileNotFoundException;

import ca.concordia.risk.GameEngine;
import ca.concordia.risk.game.GameMap;
import ca.concordia.risk.io.views.ConsoleView;
import ca.concordia.risk.utils.MapLoader;
import ca.concordia.risk.utils.MapValidator;

/** Command representing <i>"loadmap"</i> operation. */
public class LoadMapCommand implements Command {

	private String d_filename;

	/**
	 * Creates a new <code>LoadMapCommand</code> object.
	 * 
	 * @param p_filename filename of the map file to load and start the game with.
	 */
	public LoadMapCommand(String p_filename) {
		d_filename = p_filename;
	}

	/** Loads the requested map file in play mode. */
	@Override
	public void execute() {
		ConsoleView l_view = GameEngine.GetView();
		GameMap l_gameMap = null;
		try {
			l_gameMap = MapLoader.LoadMap(d_filename);
			l_view.display("Map succesfully loaded from file");

			// Validate loaded map
			l_view.display("Validating the map...");
			if (MapValidator.Validate(l_gameMap)) {
				l_view.display("Map is valid");
				GameEngine.SetMap(l_gameMap);
				GameEngine.isMapLoaded = true;
			} else {
				// Invalid maps are not allowed in gameplay mode
				l_view.display("The map loaded is invalid - " + MapValidator.getStatus());
				l_view.display("Loadmap failed: map has to be valid to play");
			}
		} catch (FileNotFoundException l_e) {
			l_view.display("Map file with filename " + d_filename + " does not exist");
		} catch (MapLoader.FileParsingException l_e) {
			l_view.display("Error while loading the map file");
			l_view.display(l_e.getMessage());
		}
	}

}
