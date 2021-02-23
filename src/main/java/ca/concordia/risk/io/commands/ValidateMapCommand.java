package ca.concordia.risk.io.commands;

import ca.concordia.risk.game.GameEngine;
import ca.concordia.risk.game.GameMap;
import ca.concordia.risk.io.views.ConsoleView;

/** Command representing <i>"validatemap"</i> operation. */
public class ValidateMapCommand implements Command {

	/** Validates the active Map. */
	@Override
	public void execute() {
		// TODO Replace with actual implementation
		ConsoleView l_view = GameEngine.GetView();
		l_view.display("\nValidating the active map\n");
		
		GameMap l_gameMap = GameEngine.GetMap();
		if (l_gameMap != null) {
			//l_gameMap.isConnected();
			
		} else {
			l_view.display("Please load a map to be validated first");
		}
	}

}
