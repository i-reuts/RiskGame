package ca.concordia.risk.io.commands;

import ca.concordia.risk.game.GameEngine;

/** Command representing <i>"assigncountries"</i> operation. */
public class AssignCountriesCommand implements Command {

	/** Assigns countries to Players. */
	@Override
	public void execute() {
		GameEngine.AssignCountries();
		GameEngine.SwitchToGameplayMode();
		
		GameEngine.GetView().display("Countries assigned. The game begins.");
	}

}
