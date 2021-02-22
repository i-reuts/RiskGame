package ca.concordia.risk.game;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

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
	
	public Player(String p_name){
		d_name = p_name;
		d_reinforcements = 0;
		d_orders = new LinkedList<Order>();
		d_countries = new HashSet<Country>();
	}
	
	public String getName() {
		return d_name;
	}
	
	public Order next_order() {
		return d_orders.poll();
	}
	
	public void addCountry(Country p_country) {
		p_country.setOwner(this);
		d_countries.add(p_country);
	}
	
	public void removeCountry(Country p_country) {
		p_country.setOwner(null);
		d_countries.remove(p_country);
	}
	
	
	/** Assigning Reinforcement to players.
	 * 
	 */
	public void assignReinfocements() {
		
		Set<String> continents = new HashSet<String>();
		for (Country country : d_countries) {
			continents.add(country.getContinent().getName());
		}
		d_reinforcements = Math.max(3, d_countries.size() / 3) + continents.size();
	
	}

	public int numberOfReinforcementsLeft() {
		ConsoleView l_view = GameEngine.GetView();
		l_view.display("\nPlayer \"" + d_name + " has " + d_reinforcements + " left.\n");
		return d_reinforcements;
	}
	
	public boolean retrieveReinforcements(int p_amount) {
		ConsoleView l_view = GameEngine.GetView();
		if (p_amount <= 0) {
			l_view.display("\nCan't retrieve a negative amout of armies");
			return false;
		}
		else if (p_amount > d_reinforcements) {
			l_view.display("\nCan't retrieve more armies than you have. Armies you have: " + d_reinforcements + "\n");
			return false;
		}
		
		d_reinforcements -= p_amount;
		
		return true;
	}
	
	
}
