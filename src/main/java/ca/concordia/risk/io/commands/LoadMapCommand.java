package ca.concordia.risk.io.commands;

import java.io.FileNotFoundException;

import ca.concordia.risk.GameEngine;
import ca.concordia.risk.game.GameMap;
import ca.concordia.risk.io.views.ConsoleView;
import ca.concordia.risk.utils.MapLoader;

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
			GameEngine.SetMap(l_gameMap);
			GameEngine.SwitchToStartupMode();
		} catch (FileNotFoundException e) {
			l_view.display("Map file with filename " + d_filename + " does not exist");
		} catch (MapLoader.FileParsingException e) {
			l_view.display("Error while loading the map file");
			l_view.display(e.getMessage());
		}
	}

}
