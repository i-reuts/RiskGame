package ca.concordia.risk.io.commands;

import ca.concordia.risk.GameEngine;

/** Command representing <i>"assigncountries"</i> operation. */
public class AssignCountriesCommand implements Command {

	/** Assigns countries to Players. */
	@Override
	public void execute() {
		if (GameEngine.getNumberOfPlayers() > GameEngine.GetMap().getNumberOfCountries()){
			GameEngine.GetView().display("There can not be more players than countries.");
			return;
		}
		else if (GameEngine.getNumberOfPlayers() < 2) {
			GameEngine.GetView().display("You need at least two players to play.");
			return;
		}
		GameEngine.AssignCountries();
		GameEngine.SwitchToGameplayMode();
		
		GameEngine.GetView().display("Countries assigned. The game begins.");
	}

}
