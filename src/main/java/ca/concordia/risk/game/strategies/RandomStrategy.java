package ca.concordia.risk.game.strategies;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import ca.concordia.risk.GameEngine;
import ca.concordia.risk.game.Card;
import ca.concordia.risk.game.Country;
import ca.concordia.risk.game.Player;
import ca.concordia.risk.game.orders.AdvanceOrder;
import ca.concordia.risk.game.orders.AirliftOrder;
import ca.concordia.risk.game.orders.BlockadeOrder;
import ca.concordia.risk.game.orders.BombOrder;
import ca.concordia.risk.game.orders.DeployOrder;
import ca.concordia.risk.game.orders.NegotiateOrder;
import ca.concordia.risk.game.orders.Order;

/**
 * This class represents the random player strategy.
 * <p>
 * A <code>Player</code> that takes random decisions.
 */
public class RandomStrategy extends PlayerStrategy {

	private ArrayList<Country> d_countryList;
	private ArrayList<Country> d_countryToAdvance;
	private Set<Country> d_countrySet;
	private Random d_rand;
	private int d_randomCount = 1;
	private int d_advanceIndex = 0;
	private boolean d_hasThisRoundRand = false;

	/**
	 * Creates a new random strategy.
	 * 
	 * @param p_player player using this strategy to set as context.
	 */
	public RandomStrategy(Player p_player) {
		super(p_player);
		d_countrySet = new HashSet<Country>();
		d_rand = new Random();
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Random behavior for issuing orders.
	 */
	@Override
	public Order issueOrder() {
		d_countryList = new ArrayList<Country>(d_player.getCountries());
		if (d_countryList.size() > 0) {
			if (d_player.getRemainingReinforcements() > 0) {
				// We will deploy to a random country
				Collections.shuffle(d_countryList);

				// Random amount to deploy
				int l_amountToDeploy = d_rand.nextInt(d_player.getRemainingReinforcements()) + 1;

				// Retrieve reinforcements from the player
				d_player.retrieveReinforcements(l_amountToDeploy);

				return new DeployOrder(d_player, d_countryList.get(0), l_amountToDeploy);
			}

			// Randomize the countries that can advance armies once per round
			if (!d_hasThisRoundRand) {
				d_hasThisRoundRand = true;

				// Add all countries that already had armies
				for (Country l_c : d_countryList) {
					if (l_c.getArmies() > 0) {
						d_countrySet.add(l_c);
					}
				}

				// Randomize the countries to advance armies
				d_countryToAdvance = new ArrayList<Country>(d_countrySet);
				Collections.shuffle(d_countryToAdvance);
			}

			// Play cards if available
			if (!d_player.getCards().isEmpty()) {
				// Blockade
				if (d_player.useCard(Card.getBlockadeCard())) {
					if (!d_countryToAdvance.isEmpty()) {
						Country l_c = d_countryToAdvance.get(0);
						d_countryToAdvance.remove(0);
						return new BlockadeOrder(d_player, l_c);
					}
				}
				// Airlift
				if (d_player.useCard(Card.getAirliftCard())) {
					if (!d_countryToAdvance.isEmpty()) {
						Country l_c = d_countryToAdvance.get(0);
						d_countryToAdvance.remove(0);
						return new AirliftOrder(d_player, l_c, d_countryList.get(0), l_c.getArmies());
					}
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
				// Bomb
				if (d_player.useCard(Card.getBombCard())) {
					for (Country l_c : d_countryList) {
						for (Country l_n : l_c.getNeighbors()) {
							if ((!l_n.getOwner().getName().equals(d_player.getName())) && l_n.getArmies() > 0) {
								return new BombOrder(d_player, l_n);
							}
						}
					}
				}
			}

			// At least advance armies from 1 country per round
			if (d_advanceIndex < d_countryToAdvance.size() && d_rand.nextInt(d_randomCount) < 1) {

				// After each advance, the probabilities to advance again are reduced
				d_randomCount++;

				// Get the next country
				Country l_c = d_countryToAdvance.get(d_advanceIndex);
				d_advanceIndex++;

				// Get a Random neighbor
				ArrayList<Country> l_neighborList = new ArrayList<Country>(l_c.getNeighbors());
				Collections.shuffle(l_neighborList);

				return new AdvanceOrder(d_player, l_c, l_neighborList.get(0), d_rand.nextInt(l_c.getArmies()) + 1);
			}
		}

		// Reset all values for the next round
		d_advanceIndex = 0;
		d_randomCount = 1;
		d_hasThisRoundRand = false;
		d_countrySet = new HashSet<Country>();

		// Finish issuing orders
		d_player.setFinishedIssuingOrder(true);
		return null;
	}
}
