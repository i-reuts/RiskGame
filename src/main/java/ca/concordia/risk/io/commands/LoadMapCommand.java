package ca.concordia.risk.io.commands;

import ca.concordia.risk.game.GameEngine;
import ca.concordia.risk.io.views.ConsoleView;

/** Command representing <i>"loadmap"</i> operation. */
public class LoadMapCommand implements Command {

	private String m_filename;
	
	/**
	 * Creates a new <code>LoadMapCommand</code> object.
	 * 
	 * @param p_filename filename of the map file to load and edit.
	 */
	public LoadMapCommand(String p_filename) {
		m_filename = p_filename;
	}

	/** Loads the requested map file in play mode. */
	@Override
	public void execute() {
		// TODO Replace with actual implementation
		ConsoleView l_view = GameEngine.getView();
		l_view.display("\nExecuting loadmap command with filename: " + m_filename +"\n");
		
		GameEngine.SwitchToStartupMode();
	}

}
