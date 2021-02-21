package ca.concordia.risk.io.commands;

import java.io.FileNotFoundException;

import ca.concordia.risk.game.GameEngine;
import ca.concordia.risk.game.GameMap;
import ca.concordia.risk.io.views.ConsoleView;
import ca.concordia.risk.services.MapLoader;
import ca.concordia.risk.services.MapLoader.FileParsingException;

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
		l_view.display("\nExecuting loadmap command with filename: " + d_filename + "\n");

		try {
			GameMap gameMap=MapLoader.LoadMap(d_filename);
			GameEngine.SetMap(gameMap);
			GameEngine.SwitchToStartupMode();
		} catch (FileNotFoundException | FileParsingException e) {
			e.printStackTrace();
		}
		
	}

}
