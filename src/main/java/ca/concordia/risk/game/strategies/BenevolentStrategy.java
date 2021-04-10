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

	ArrayList<Country> d_countryList;
	ArrayList<Country> d_hasAdvanced;

	/**
	 * {@inheritDoc}
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
			l_weakestCountry.addArmies(l_amountToDeploy);

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

		Country l_advanceFrom = strongestCountry();
		ArrayList<Country> l_neighborsOfStrongest = new ArrayList<Country>(l_advanceFrom.getNeighbors());
		Country l_advanceTo = l_neighborsOfStrongest.get(0);

		for (Country l_c : l_neighborsOfStrongest) {
			if ((l_c.getArmies() < l_advanceTo.getArmies()) && (l_c.getOwner().getName().equals(d_player.getName()))) {
				l_advanceTo = l_c;
			}
		}

		// Advance
		if (!d_hasAdvanced.contains(l_advanceFrom)) {
			if ((l_advanceTo.getOwner().getName().equals(d_player.getName())) && l_advanceFrom.getArmies() != 0) {
				d_hasAdvanced.add(l_advanceFrom);
				return new AdvanceOrder(d_player, l_advanceFrom, l_advanceTo,
						((l_advanceFrom.getArmies() - l_advanceTo.getArmies()) / 2));
			}
		}
		// Finish issuing orders
		d_hasAdvanced = new ArrayList<Country>();
		d_player.setFinishedIssuingOrder(true);

		return null;
	}

	/**
	 * The strongest country of the player having maximum number of armies is
	 * decided
	 * 
	 * @return the country with Maximum number of armies
	 */
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

	/**
	 * The weakest country of the player having minimum number of armies is decided
	 * 
	 * @return the country with Minimum number of armies
	 */
	private Country weakestCountry() {
		d_countryList = new ArrayList<Country>(d_player.getCountries());
		Country l_weakest = d_countryList.get(0);
		for (Country c : d_countryList) {
			for (Country n : c.getNeighbors()) {
				if (c.getArmies() < l_weakest.getArmies() && !n.getOwner().equals(d_player)) {
					l_weakest = c;
					break;
				}
			}
		}
		return l_weakest;
	}
}
