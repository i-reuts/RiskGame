package ca.concordia.risk.io.commands;

import java.io.FileNotFoundException;

import ca.concordia.risk.GameEngine;
import ca.concordia.risk.game.GameMap;
import ca.concordia.risk.io.views.ConsoleView;
import ca.concordia.risk.utils.MapLoader;

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
		} catch (FileNotFoundException e) {
			l_view.display("Map file with filename " + d_filename + " does not exist");
			l_view.display("Creating a new map...");
			l_gameMap = new GameMap();
		} catch (MapLoader.FileParsingException e) {
			l_view.display("Error while loading the map file");
			l_view.display(e.getMessage());
		} finally {
			GameEngine.SetMap(l_gameMap);
		}
	}

}
