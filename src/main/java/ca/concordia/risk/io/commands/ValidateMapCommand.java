package ca.concordia.risk.io.commands;

import ca.concordia.risk.game.GameEngine;
import ca.concordia.risk.io.views.ConsoleView;

/** Command representing <i>"validatemap"</i> operation. */
public class ValidateMapCommand implements Command {

	/** Validates active Map. */
	@Override
	public void execute() {
		// TODO Replace with actual implementation
		ConsoleView l_view = GameEngine.getView();
		l_view.display("\nValidating the active map\n");
	}

}
