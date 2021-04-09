package ca.concordia.risk.game.strategies;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

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
	int d_minArmies;
	int d_maxArmies;
	Country d_weakestCountry;
	Country d_strongestCountry;

	/**
	 * {@inheritDoc}
	 */
	public BenevolentStrategy(Player p_player) {
		super(p_player);
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

		if (d_countryList.size() > 0) {
			if (d_player.getRemainingReinforcements() > 0) {

				d_weakestCountry = weakestCountry();

				// Get the number of armies to be deployed
				int l_amountToDeploy = d_player.getRemainingReinforcements();

				// Retrieve reinforcements from the player
				d_player.retrieveReinforcements(l_amountToDeploy);

				// Add them to the local array of countries (to keep track of armies deploy in
				// this turn)
				d_weakestCountry.addArmies(l_amountToDeploy);

				return new DeployOrder(d_player, d_weakestCountry, l_amountToDeploy);
			}
		}

		// Play cards if available
		// Moves its armies in order to reinforce its weaker country
		// Can only advance armies to its own weaker country
		if (!d_player.getCards().isEmpty()) {
			d_weakestCountry = weakestCountry();
			d_strongestCountry = strongestCountry();

			// Airlift
			if (d_player.useCard(Card.getAirliftCard())) {
				return new AirliftOrder(d_player, d_strongestCountry, d_weakestCountry,
						(d_strongestCountry.getArmies() - d_weakestCountry.getArmies()) / 2);
			}

			// Diplomacy
			if (d_player.useCard(Card.getDiplomacyCard())) {

				for (Country l_c : d_countryList) {
					for (Country l_n : l_c.getNeighbors()) {
						if (l_n.getArmies() > strongestCountry().getArmies()) {
							return new NegotiateOrder(d_player, l_n.getOwner());
						}
					}
				}

			}
		}

		// Advance
		if (strongestCountry().getArmies() != 0) {
			d_strongestCountry = strongestCountry();
			d_weakestCountry = weakestCountry();
			return new AdvanceOrder(d_player, d_strongestCountry, d_weakestCountry,
					(d_strongestCountry.getArmies() - d_weakestCountry.getArmies()) / 2);
		}
		// Finish issuing orders
		d_player.setFinishedIssuingOrder(true);

		return null;
	}

	// returns the strongest country
	private Country strongestCountry() {
		d_countryList = new ArrayList<Country>(d_player.getCountries());
		Country l_strongest = d_countryList.get(0);
		for (Country c : d_countryList) {
			if (c.getArmies() > l_strongest.getArmies()) {
				l_strongest = c;
			}
		}
		return l_strongest;
	}

	// returns the weakest country
	private Country weakestCountry() {
		d_countryList = new ArrayList<Country>(d_player.getCountries());
		Country l_weakest = d_countryList.get(0);
		for (Country c : d_countryList) {
			for (Country n : c.getNeighbors()) {
				if (c.getArmies() < l_weakest.getArmies() && !n.getOwner().equals(d_player)) {
					l_weakest = c;
				}
			}
		}
		return l_weakest;
	}
}
