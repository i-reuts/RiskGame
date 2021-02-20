package ca.concordia.risk.io.commands;

import ca.concordia.risk.game.GameEngine;
import ca.concordia.risk.game.GameMap;
import ca.concordia.risk.io.views.ConsoleView;

/** Command representing <i>"showmap"</i> operation. */
public class ShowMapCommand implements Command {

	/** Displays the active <code>GameMap</code>. */
	@Override
	public void execute() {
		ConsoleView l_view = GameEngine.GetView();
		GameMap l_gameMap = GameEngine.GetMap();
		if(l_gameMap != null) {
			l_view.display(l_gameMap.toString());
		} 
		else {
			l_view.display("No map to display - please load a map first");
		}	
	}

}
