package ca.concordia.risk.io.commands;

import ca.concordia.risk.game.GameEngine;
import ca.concordia.risk.io.views.ConsoleView;

/** Command representing <i>"savemap"</i> operation. */
public class SaveMapCommand implements Command {

	private String m_filename;
	
	/**
	 * Creates a new <code>LoadMapCommand</code> object.
	 * 
	 * @param p_filename filename of the map file to save active Map into.
	 */
	public SaveMapCommand(String p_filename) {
		m_filename = p_filename;
	}

	/** Saves the active Map into the requested map file. */
	@Override
	public void execute() {
		// TODO Replace with actual implementation
		ConsoleView l_view = GameEngine.getView();
		l_view.display("\nExecuting savemap command with filename: " + m_filename +"\n");
	}

}
