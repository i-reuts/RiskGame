package ca.concordia.risk.io.commands;

import java.util.Collections;
import java.util.List;

import ca.concordia.risk.GameEngine;
import ca.concordia.risk.game.Country;
import ca.concordia.risk.game.Player;
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

		assignCountries();

		GameEngine.SwitchToNextPhase();
		l_view.display("Countries assigned. The game begins.");
	}

	/** Assigns countries randomly to active players. */
	private static void assignCountries() {

		// Get all countries and shuffle them randomly
		List<Country> l_countryList = GameEngine.GetMap().getCountries();
		Collections.shuffle(l_countryList);

		// While there are countries remaining, assign shuffled countries one by one to
		// players in a round-robin fashion
		while (!l_countryList.isEmpty()) {
			for (Player l_player : GameEngine.GetPlayers()) {
				if (l_countryList.isEmpty()) {
					break;
				}

				Country l_country = l_countryList.remove(l_countryList.size() - 1);
				l_player.addCountry(l_country);
				l_country.setOwner(l_player);
			}
		}
	}

}
