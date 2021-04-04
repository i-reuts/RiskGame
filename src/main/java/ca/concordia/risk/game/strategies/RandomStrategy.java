package ca.concordia.risk.game.strategies;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Set;

import ca.concordia.risk.GameEngine;
import ca.concordia.risk.game.Country;
import ca.concordia.risk.game.Player;
import ca.concordia.risk.game.orders.AdvanceOrder;
import ca.concordia.risk.game.orders.DeployOrder;
import ca.concordia.risk.game.orders.Order;

public class RandomStrategy extends PlayerStrategy {

	ArrayList<Country> d_countryList;
	Set<Country> d_countriesToAdvance;
	int d_ = 0;

	/**
	 * {@inheritDoc}
	 */
	public RandomStrategy(Player p_player) {
		super(p_player);
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
			d_countriesToAdvance.add(d_countryList.get(0));
			return new DeployOrder(d_player, d_countryList.get(0), rand.nextInt(d_player.getRemainingReinforcements() + 1));
		}
		return null;
	}
}
