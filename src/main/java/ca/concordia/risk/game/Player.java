package ca.concordia.risk.game;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import ca.concordia.risk.game.orders.Order;
import ca.concordia.risk.io.views.ConsoleView;

/**
 * This class is the representation of the game entity Player, which handles its
 * own orders.
 * 
 * @author Enrique
 *
 */
public class Player {
	private int d_reinforcements;
	private String d_name;
	private Queue<Order> d_orders;
	private Set<Country> d_countries;

	public Player(String p_name) {
		d_name = p_name;
		d_reinforcements = 0;
		d_orders = new LinkedList<Order>();
		d_countries = new HashSet<Country>();
	}

	public String getName() {
		return d_name;
	}

	public Order nextOrder() {
		return d_orders.poll();
	}
	
	public void issueOrder() {
		Order l_order = GameEngine.ProcessOrderCommand(this);
		d_orders.add(l_order);
	}
	
	public boolean finishedIssuingOrders() {
		return d_reinforcements == 0;
	}

	public void addCountry(Country p_country) {
		p_country.setOwner(this);
		d_countries.add(p_country);
	}

	public void removeCountry(Country p_country) {
		p_country.setOwner(null);
		d_countries.remove(p_country);
	}
	
	public boolean ownsCountry(Country p_country) {
		return d_countries.contains(p_country);
	}

	/**
	 * Assigns reinforcements to the player.
	 * <p>
	 * Reinforcements are assigned based on the minimum reinforcement number, number
	 * of countries owned and bonus value of wholly owned continents.
	 */
	public void assignReinfocements() {
		// Assign base reinforcements based on number of countries owned
		d_reinforcements = Math.max(3, d_countries.size() / 3);

		// 1. Get all continents that the player has countries in
		// 2. Find which continents are fully owned by the player (continent countries
		// must form a subset of the countries owned by the player)
		// 3. Add fully owned continent bonus values to reinforcements
		Set<Continent> l_processedContinents = new HashSet<Continent>();
		for (Country country : d_countries) {
			Continent l_continent = country.getContinent();
			if (!l_processedContinents.contains(l_continent)) {
				if (d_countries.containsAll(l_continent.getCountries())) {
					d_reinforcements += l_continent.getValue();
				}
				l_processedContinents.add(l_continent);
			}
		}
	}

	public int numberOfReinforcementsLeft() {
		return d_reinforcements;
	}

	public boolean retrieveReinforcements(int p_amount) {
		if (p_amount <= 0) {
			return false;
		} else if (p_amount > d_reinforcements) {
			return false;
		}

		d_reinforcements -= p_amount;

		return true;
	}

}
