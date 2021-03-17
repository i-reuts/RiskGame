package ca.concordia.risk.io.commands;

import ca.concordia.risk.GameEngine;

/** Command representing <i>"assigncountries"</i> operation. */
public class AssignCountriesCommand implements Command {

	/** Assigns countries to Players. */
	@Override
	public void execute() {
		if (GameEngine.GetNumberOfPlayers() > GameEngine.GetMap().getCountries().size()) {
			GameEngine.GetView().display("There can not be more players than countries on the map.");
			return;
		} else if (GameEngine.GetNumberOfPlayers() < 2) {
			GameEngine.GetView().display("At least two players are required to play.");
			return;
		}

		GameEngine.AssignCountries();
		GameEngine.isCountryAssignationDone = true;

		GameEngine.GetView().display("Countries assigned. The game begins.");
	}

}
