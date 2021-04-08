package ca.concordia.risk.game.strategies;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import ca.concordia.risk.GameEngine;
import ca.concordia.risk.game.Card;
import ca.concordia.risk.game.Country;
import ca.concordia.risk.game.Player;
import ca.concordia.risk.game.orders.AdvanceOrder;
import ca.concordia.risk.game.orders.AirliftOrder;
import ca.concordia.risk.game.orders.DeployOrder;
import ca.concordia.risk.game.orders.NegotiateOrder;
import ca.concordia.risk.game.orders.Order;

/**
 * This class represents the Benevolent player strategy.
 * <p>
 * A <code>Computer Player</code> using this strategy focuses on protecting its
 * weak countries (deploys on its weakest country, never attacks, then moves its
 * armies in order to reinforce its weaker country)
 */
public class BenevolentStrategy extends PlayerStrategy {

	ArrayList<Country> d_countryList;
	ArrayList<Country> d_countryToAdvance;
	Set<Country> d_countrySet;
	int d_minArmies;
	int d_maxArmies;
	Country d_weakestCountry;
	Country d_strongestCountry;

	/**
	 * {@inheritDoc}
	 */
	public BenevolentStrategy(Player p_player) {
		super(p_player);
		d_countrySet = new HashSet<Country>();
		d_minArmies = p_player.getRemainingReinforcements();
		d_maxArmies = 0;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Computer player deploys on its weakest country, never attacks, then moves its
	 * armies in order to reinforce its weaker country.
	 */
	@Override
	public Order issueOrder() {
		d_countryList = new ArrayList<Country>(d_player.getCountries());

		if (d_player.getRemainingReinforcements() > 0) {

			// Get the number of armies to be deployed
			int l_amountToDeploy = d_player.getRemainingReinforcements();

			d_weakestCountry = weakestCountry();

			// Add them to the local array of countries (to keep track of armies deploy in
			// this turn)
			d_weakestCountry.addArmies(l_amountToDeploy);

			// Add this country to the list of countries that can advance armies
			d_countrySet.add(d_weakestCountry);

			return new DeployOrder(d_player, d_weakestCountry, l_amountToDeploy);
		}

		d_countryToAdvance = new ArrayList<Country>(d_countrySet);

		// Play cards if available
		// Moves its armies in order to reinforce its weaker country
		// Can only advance armies to its own weaker country
		if (!d_player.getCards().isEmpty()) {
			d_weakestCountry = weakestCountry();

			// Airlift
			if (d_player.useCard(Card.getAirliftCard())) {
				return new AirliftOrder(d_player, d_countryToAdvance.get(-1), d_weakestCountry,
						(d_countryToAdvance.get(0).getArmies() - d_weakestCountry.getArmies()) / 2);
			}

			// Diplomacy
			if (d_player.useCard(Card.getDiplomacyCard())) {
				ArrayList<Player> l_players = new ArrayList<Player>(GameEngine.GetPlayers());

				for (Player l_otherPlayer : l_players) {
					if (!l_otherPlayer.getName().equals(d_player.getName())) {
						return new NegotiateOrder(d_player, l_otherPlayer);
					}
				}
			}
		}

		// Advance
		if (!strongestCountry().equals(null)) {
			d_strongestCountry = strongestCountry();
			d_weakestCountry = weakestCountry();
			return new AdvanceOrder(d_player, d_strongestCountry, d_weakestCountry,
					(d_strongestCountry.getArmies() - d_weakestCountry.getArmies()) / 2);
		}
		// Reset all values for the next round
		d_countrySet = new HashSet<Country>();

		// Finish issuing orders
		d_player.setFinishedIssuingOrder(true);

		return null;
	}

	//returns the strongest country
	private Country strongestCountry() {
		for (Country l_country : d_countryList) {
			if (l_country.getArmies() == 0) {
				d_strongestCountry = null;
			} else
				d_maxArmies = Math.max(l_country.getArmies(), d_maxArmies);
		}

		for (Country l_country : d_countryList) {
			if (d_maxArmies == l_country.getArmies())
				d_strongestCountry = l_country;
		}
		return d_strongestCountry;
	}

	//returns the weakest country
	private Country weakestCountry() {
		for (Country l_country : d_countryList) {
			if (l_country.getArmies() == 0) {
				d_weakestCountry = l_country;
				break;
			} else {
				d_minArmies = Math.min(l_country.getArmies(), d_minArmies);
			}
		}

		for (Country l_country : d_countryList) {
			if (l_country.getArmies() != 0 && d_minArmies == l_country.getArmies())
				d_weakestCountry = l_country;
		}
		return d_weakestCountry;
	}
}
