package ca.concordia.risk.game.strategies;

import java.util.ArrayList;
import java.util.Collections;
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

	private ArrayList<Country> d_countryList;
	private ArrayList<Country> d_hasAdvanced;

	/**
	 * Creates a new benevolent strategy.
	 * 
	 * @param p_player player using this strategy to set as context.
	 */
	public BenevolentStrategy(Player p_player) {
		super(p_player);
		d_hasAdvanced = new ArrayList<Country>();
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
		if (d_countryList.size() > 0 && d_player.getRemainingReinforcements() > 0) {

			Country l_weakestCountry = weakestCountry();

			// Get the number of armies to be deployed
			int l_amountToDeploy = d_player.getRemainingReinforcements();

			d_player.retrieveReinforcements(l_amountToDeploy);

			return new DeployOrder(d_player, l_weakestCountry, l_amountToDeploy);
		}

		// Play cards if available
		if (!d_player.getCards().isEmpty()) {
			Country l_weakestCountry = weakestCountry();
			Country l_strongestCountry = strongestCountry();

			// Airlift
			if (d_player.useCard(Card.getAirliftCard())) {
				return new AirliftOrder(d_player, l_strongestCountry, l_weakestCountry,
						(l_strongestCountry.getArmies() - l_weakestCountry.getArmies()) / 2);
			}

			// Diplomacy
			if (d_player.useCard(Card.getDiplomacyCard())) {
				ArrayList<Player> l_players = new ArrayList<Player>(GameEngine.GetPlayers());
				Collections.shuffle(l_players);

				for (Player l_otherPlayer : l_players) {
					if (!l_otherPlayer.getName().equals(d_player.getName())) {
						return new NegotiateOrder(d_player, l_otherPlayer);
					}
				}
			}
		}

		// Issue advance orders from stronger to weaker countries
		// For each country owned by the player
		for (Country l_c : d_player.getCountries()) {
			// If country already advanced or has less than two armies, skip it
			if (d_hasAdvanced.contains(l_c) || l_c.getArmies() < 2) {
				continue;
			}
			// Otherwise, check all neighbors and look for the weakest ally neighbor
			Country l_weakestNeighbor = null;
			for (Country l_neighbor : l_c.getNeighbors()) {
				boolean l_playerOwnsNeighbour = l_neighbor.getOwner().equals(d_player);
				boolean l_armyDifferenceMoreThanOne = (l_c.getArmies() - l_neighbor.getArmies() > 1);
				// If player owns the neighbor and difference of armies between the country and
				// the neighbor is more than 1, the neighbor is considered a weaker neighbor and
				// we can advance to it
				if (l_playerOwnsNeighbour && l_armyDifferenceMoreThanOne) {
					// Check if this weaker neighbor is the weakest out of the ones we've seen so
					// far
					if (l_weakestNeighbor == null || l_neighbor.getArmies() < l_weakestNeighbor.getArmies()) {
						// If so make it the weakest neighbor
						l_weakestNeighbor = l_neighbor;
					}
				}
			}
			// If the weakest neighbor was found, issue an Advance order to it
			if (l_weakestNeighbor != null) {
				d_hasAdvanced.add(l_c);
				return new AdvanceOrder(d_player, l_c, l_weakestNeighbor,
						((l_c.getArmies() - l_weakestNeighbor.getArmies()) / 2));
			}
		}
		// Finish issuing orders
		d_hasAdvanced = new ArrayList<Country>();
		d_player.setFinishedIssuingOrder(true);

		return null;
	}

	/**
	 * The strongest country of the player having maximum number of armies is
	 * decided.
	 * 
	 * @return the country with Maximum number of armies
	 */
	private Country strongestCountry() {
		d_countryList = new ArrayList<Country>(d_player.getCountries());
		Country l_strongest = d_countryList.get(0);
		for (Country l_c : d_countryList) {
			if (l_c.getArmies() > l_strongest.getArmies()) {
				l_strongest = l_c;
			}
		}
		return l_strongest;
	}

	/**
	 * The weakest country of the player having minimum number of armies is decided.
	 * 
	 * @return the country with Minimum number of armies
	 */
	private Country weakestCountry() {
		d_countryList = new ArrayList<Country>(d_player.getCountries());
		Country l_weakest = d_countryList.get(0);
		for (Country l_c : d_countryList) {
			for (Country l_n : l_c.getNeighbors()) {
				if (l_c.getArmies() < l_weakest.getArmies() && !l_n.getOwner().equals(d_player)) {
					l_weakest = l_c;
					break;
				}
			}
		}
		return l_weakest;
	}
}
