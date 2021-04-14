package ca.concordia.risk.io.commands;

import ca.concordia.risk.GameEngine;
import ca.concordia.risk.io.views.ConsoleView;

/** Command representing <i>"assigncountries"</i> operation. */
public class AssignCountriesCommand implements Command {

	/** Assigns countries to Players. */
	@Override
	public void execute() {
		ConsoleView l_view = GameEngine.GetView();

		if (GameEngine.GetNumberOfPlayers() > GameEngine.GetMap().getCountries().size()) {
			l_view.display("There can not be more players than countries on the map.");
			return;
		} else if (GameEngine.GetNumberOfPlayers() < 2) {
			l_view.display("At least two players are required to play.");
			return;
		}

		GameEngine.AssignCountries();

		GameEngine.SwitchToNextPhase();
		l_view.display("Countries assigned. The game begins.");
	}

}
