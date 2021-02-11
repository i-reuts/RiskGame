package ca.concordia.risk.io.commands;

import ca.concordia.risk.game.GameEngine;
import ca.concordia.risk.io.views.ConsoleView;

/** Command representing <i>"editmap"</i> operation. */
public class EditMapCommand implements Command {
	
	private String m_filename;
	
	/**
	 * Creates a new <code>EditMapCommand</code> object.
	 * 
	 * @param p_filename filename of the map file to load and edit.
	 */
	public EditMapCommand(String p_filename) {
		m_filename = p_filename;
	}

	/** Loads the requested map file in edit mode. */
	@Override
	public void execute() {
		// TODO Replace with actual implementation
		ConsoleView l_view = GameEngine.getView();
		l_view.display("\nExecuting editmap command with filename: " + m_filename +"\n");
	}

}