package ca.concordia.risk.io.commands;

import ca.concordia.risk.game.GameEngine;
import ca.concordia.risk.io.views.ConsoleView;

/** Command representing <i>"showmap"</i> operation. */
public class ShowMapCommand implements Command {

	/** Displays active Map. */
	@Override
	public void execute() {
		// TODO Replace with actual implementation
		ConsoleView l_view = GameEngine.getView();
		l_view.display("\nDisplaying the active map\n");
	}

}
