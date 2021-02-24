package ca.concordia.risk.io.commands;

import java.io.FileNotFoundException;

import ca.concordia.risk.GameEngine;
import ca.concordia.risk.game.GameMap;
import ca.concordia.risk.io.views.ConsoleView;
import ca.concordia.risk.utils.MapLoader;
import ca.concordia.risk.utils.MapValidator;

/** Command representing <i>"editmap"</i> operation. */
public class EditMapCommand implements Command {

	private String d_filename;

	/**
	 * Creates a new <code>EditMapCommand</code> object.
	 * 
	 * @param p_filename filename of the map file to load and edit.
	 */
	public EditMapCommand(String p_filename) {
		d_filename = p_filename;
	}

	/** Loads the requested map file in edit mode. */
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
			} else {
				// Invalid maps are allowed in editor mode to let users fix them
				l_view.display("Warning - the map loaded is invalid: " + MapValidator.getStatus());
			}
		} catch (FileNotFoundException l_e) {
			l_view.display("Map file with filename " + d_filename + " does not exist");
			l_view.display("Creating a new map...");
			l_gameMap = new GameMap();
		} catch (MapLoader.FileParsingException l_e) {
			l_view.display("Error while loading the map file");
			l_view.display(l_e.getMessage());
		} finally {
			GameEngine.SetMap(l_gameMap);
		}
	}

}
