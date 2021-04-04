package ca.concordia.risk.game.strategies;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import ca.concordia.risk.game.Country;
import ca.concordia.risk.game.Player;
import ca.concordia.risk.game.orders.AdvanceOrder;
import ca.concordia.risk.game.orders.DeployOrder;
import ca.concordia.risk.game.orders.Order;

public class RandomStrategy extends PlayerStrategy {

	ArrayList<Country> d_countryList;
	ArrayList<Country> d_countryToAdvance;
	Set<Country> d_countrySet;
	int d_randomCount = 1;
	int d_advanceIndex = 0;
	boolean d_hasThisRoundRand = false;

	/**
	 * {@inheritDoc}
	 */
	public RandomStrategy(Player p_player) {
		super(p_player);
		d_countrySet = new HashSet<Country>();
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Random behavior for issuing orders.
	 */
	@Override
	public Order issueOrder() {
		Random rand = new Random();
		d_countryList = new ArrayList<Country>(d_player.getCountries());
		if (d_player.getRemainingReinforcements() > 0) {
			Collections.shuffle(d_countryList);
			
			// Add this country to the list of countries that can advance armies
			d_countrySet.add(d_countryList.get(0));
			
			return new DeployOrder(d_player, d_countryList.get(0), rand.nextInt(d_player.getRemainingReinforcements() + 1));
		}
		
		// Randomize the countries that can advance armies once per round
		if (!d_hasThisRoundRand) {
			d_hasThisRoundRand = true;
			
			// Add all countries that already had armies
			for (Country c : d_countryList) {
				if (c.getArmies() > 0) {
					d_countrySet.add(c);
				}
			}
			d_countryToAdvance = new ArrayList<Country>(d_countrySet);
			Collections.shuffle(d_countryToAdvance);
		}

		// At least advance armies from 1 country per round
		if (d_advanceIndex < d_countryToAdvance.size() && rand.nextInt(d_randomCount) < 1) {
			// After each advance, the probabilities are reduced
			d_randomCount ++;
			
			// Get the next country
			Country l_c = d_countryToAdvance.get(d_advanceIndex);
			d_advanceIndex ++;
			
			// Get a random neighbor
			ArrayList<Country> l_neighborList = new ArrayList<Country>(l_c.getNeighbors());
			Collections.shuffle(l_neighborList);

			return new AdvanceOrder(d_player, l_c, l_neighborList.get(0), rand.nextInt(l_c.getArmies() + 1));
		}
		
		// Reset all values for the next round
		d_advanceIndex = 0;
		d_randomCount = 1;
		d_hasThisRoundRand = false;
		d_countrySet = new HashSet<Country>();
		d_player.getFinishedIssuingOrders();
		return null;
	}
}
