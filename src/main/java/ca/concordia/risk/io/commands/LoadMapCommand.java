package ca.concordia.risk.io.commands;

import ca.concordia.risk.game.GameEngine;
import ca.concordia.risk.io.views.ConsoleView;

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
		// TODO Replace with actual implementation
		ConsoleView l_view = GameEngine.GetView();
		l_view.display("\nExecuting loadmap command with filename: " + d_filename + "\n");

		GameEngine.SwitchToStartupMode();
	}

}
