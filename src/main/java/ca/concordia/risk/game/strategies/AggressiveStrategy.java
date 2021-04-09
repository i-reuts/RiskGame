package ca.concordia.risk.game.strategies;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Stack;

import ca.concordia.risk.game.Country;
import ca.concordia.risk.game.Player;
import ca.concordia.risk.game.orders.AdvanceOrder;
import ca.concordia.risk.game.orders.DeployOrder;
import ca.concordia.risk.game.orders.Order;



public class AggressiveStrategy extends PlayerStrategy {
	// Key = node , Value = backtrack node
	HashMap<Country, Country> d_backtrack = new HashMap<Country, Country>();
	Stack<Country> d_path = new Stack<Country>();
	ArrayList<Country> d_countryList;
	Country d_countryToDeploy = null;
	int d_amountToDeploy = 0;
	boolean d_hasAdvance = false;
	
	public AggressiveStrategy(Player p_player) {
		super(p_player);
	}

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
		
		if (!d_hasAdvance) {
			Country l_enemy;
			l_enemy = findEnemy(d_countryToDeploy);
			buildPath(l_enemy);
			if (!d_path.isEmpty()) {
				d_hasAdvance = true;
				System.out.println("Advance " + (d_countryToDeploy.getArmies() + d_amountToDeploy));
				return new AdvanceOrder(d_player, d_countryToDeploy, d_path.pop(), d_countryToDeploy.getArmies() + d_amountToDeploy);
			}
		}
		
		// Finish issuing orders
		d_hasAdvance = false;
		d_countryToDeploy = null;
		d_amountToDeploy = 0;
		d_player.setFinishedIssuingOrder(true);
		return null;
	}
	
	public Country findEnemy (Country p_fromCountry) {
		// Key = Country, Value = Backtrack node

		LinkedList< Country> l_queue = new LinkedList<Country>();
		HashSet<Country> l_visited = new HashSet<Country>();
		Country l_tmp;
		
		// clean the stack if peek returns an enemy
		
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
	
	public void buildPath(Country p_enemy) {
		Country l_tmp = p_enemy;
		while (d_backtrack.get(l_tmp) != null) {
			d_path.push(l_tmp);
			l_tmp = d_backtrack.get(l_tmp);
		}
	}

}
