package ca.concordia.risk.io.commands;

import ca.concordia.risk.GameEngine;
import ca.concordia.risk.game.GameMap;
import ca.concordia.risk.io.views.ConsoleView;

/** Command representing <i>"showmap"</i> operation. */
public class ShowMapCommand implements Command {

	private boolean d_showGameplayInfo;

	/**
	 * Creates a new <code>ShowMapCommand</code> object.
	 * 
	 * @param p_showGameplayInfo flag specifying if gameplay info such as players
	 *                           and armies should be displayed.
	 */
	public ShowMapCommand(boolean p_showGameplayInfo) {
		d_showGameplayInfo = p_showGameplayInfo;
	}

	/** Displays the active <code>GameMap</code>. */
	@Override
	public void execute() {
		ConsoleView l_view = GameEngine.GetView();
		GameMap l_gameMap = GameEngine.GetMap();
		if (l_gameMap != null) {
			if (d_showGameplayInfo) {
				l_view.display(l_gameMap.buildGameplayMapString());
			} else {
				l_view.display(l_gameMap.buildMapString());
			}
		} else {
			l_view.display("No map to display - please load a map first");
		}
	}

}
