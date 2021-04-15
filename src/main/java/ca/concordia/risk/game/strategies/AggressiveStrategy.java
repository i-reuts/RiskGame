package ca.concordia.risk.game.strategies;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Stack;

import ca.concordia.risk.game.Country;
import ca.concordia.risk.game.Player;
import ca.concordia.risk.game.orders.AdvanceOrder;
import ca.concordia.risk.game.orders.DeployOrder;
import ca.concordia.risk.game.orders.Order;

/**
 * This class represents the aggressive player strategy.
 * <p>
 * A <code>Player</code> that concentrates all his armies into one country and
 * attack!
 */
public class AggressiveStrategy extends PlayerStrategy {
	// Key = node , Value = backtrack node
	private HashMap<Country, Country> d_backtrack = new HashMap<Country, Country>();
	private Stack<Country> d_path = new Stack<Country>();
	private ArrayList<Country> d_countryList;
	private Country d_countryToDeploy = null;
	private int d_amountToDeploy = 0;
	private boolean d_hasAdvance = false;

	/**
	 * Creates a new aggressive strategy.
	 * 
	 * @param p_player player using this strategy to set as context.
	 */
	public AggressiveStrategy(Player p_player) {
		super(p_player);
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Aggressive behavior for issuing orders. - Find the country with your armies
	 * and deploy all armies in it. - Find the nearest enemy with a BFS approach. -
	 * Either attack an enemy or start advancing towards one.
	 */
	@Override
	public Order issueOrder() {
		d_countryList = new ArrayList<Country>(d_player.getCountries());
		if (d_countryList.size() > 0 && d_player.getRemainingReinforcements() > 0) {
			d_amountToDeploy = d_player.getRemainingReinforcements();
			if (d_amountToDeploy > 0) {
				// We will deploy all armies
				d_player.retrieveReinforcements(d_amountToDeploy);

				// Look up for a country that already has armies
				for (Country l_c : d_countryList) {
					if (l_c.getArmies() > 0) {
						d_countryToDeploy = l_c;
						return new DeployOrder(d_player, d_countryToDeploy, d_amountToDeploy);
					}
				}

				// If no country has armies, look up for a country with enemies
				if (d_countryToDeploy == null) {
					for (Country l_c : d_countryList) {
						for (Country l_n : l_c.getNeighbors()) {
							if (!l_n.getOwner().getName().equals(d_player.getName())) {
								d_countryToDeploy = l_c;
								return new DeployOrder(d_player, d_countryToDeploy, d_amountToDeploy);
							}
						}
					}
				}
			}
		}

		// Advance once per turn
		if (!d_hasAdvance) {
			Country l_enemy;
			l_enemy = findEnemy(d_countryToDeploy);
			buildPath(l_enemy);
			if (!d_path.isEmpty()) {
				d_hasAdvance = true;
				return new AdvanceOrder(d_player, d_countryToDeploy, d_path.pop(),
						d_countryToDeploy.getArmies() + d_amountToDeploy);
			}
		}

		// Finish issuing orders
		d_hasAdvance = false;
		d_countryToDeploy = null;
		d_amountToDeploy = 0;
		d_player.setFinishedIssuingOrder(true);
		return null;
	}

	/**
	 * Populate d_backtrack with a BFS approach, until finding an enemy.
	 * 
	 * @param p_fromCountry Current country to trace a path to an enemy country.
	 * @return Enemy Country to advance towards
	 */
	public Country findEnemy(Country p_fromCountry) {
		LinkedList<Country> l_queue = new LinkedList<Country>();
		HashSet<Country> l_visited = new HashSet<Country>();
		Country l_tmp;

		l_queue.add(p_fromCountry);
		l_visited.add(p_fromCountry);
		d_backtrack.put(p_fromCountry, null);

		while (l_queue.size() > 0) {
			l_tmp = l_queue.poll();

			for (Country l_c : l_tmp.getNeighbors()) {
				if (l_c.getOwner().getName().equals(d_player.getName())) {
					if (!l_visited.contains(l_c)) {
						l_queue.add(l_c);
						l_visited.add(l_c);
						d_backtrack.put(l_c, l_tmp);
					}
					continue;
				}
				d_backtrack.put(l_c, l_tmp);
				return l_c;
			}
		}

		return null;
	}

	/**
	 * Populate d_path walking back in d_backtrack from the target enemy until the
	 * current source country.
	 * 
	 * @param p_enemy Enemy Country to trace a path to.
	 */
	public void buildPath(Country p_enemy) {
		Country l_tmp = p_enemy;
		while (d_backtrack.get(l_tmp) != null) {
			d_path.push(l_tmp);
			l_tmp = d_backtrack.get(l_tmp);
		}
	}

}
