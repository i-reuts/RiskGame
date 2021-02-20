package ca.concordia.risk.game;

import java.util.LinkedList;
import java.util.List;

/**
 * This class is the representation of the game map. It has a linked list
 * to manage its countries and continents belonging to this map.
 * 
 * @author Enrique
 *
 */
public class GameMap {
	private LinkedList<Country> d_countries;
	private LinkedList<Continent> d_continents;

	/**
	 * Constructor for the GameMap entity
	 */
	public GameMap() {
		d_countries = new LinkedList<Country>();
		d_continents = new LinkedList<Continent>();
	}

	/**
	 * Add a country to the map.
	 * 
	 * @param p_country Country object
	 */
	public void addCountry(Country p_country) {
		this.d_countries.add(p_country);
	}

	/**
	 * Add a continent to the map.
	 * 
	 * @param p_continent Continent object
	 */
	public void addContinent(Continent p_continent) {
		this.d_continents.add(p_continent);
	}

	/**
	 * Remove a country from the map.
	 * 
	 * @param p_country Country object.
	 * @return True if the Country existed. False other way.
	 */
	public boolean removeCountry(Country p_country) {
		return this.d_countries.remove(p_country);
	}

	/**
	 * Remove a continent from the map.
	 * 
	 * @param p_continent Continent object.
	 * @return True if the continent existed. False other way.
	 */
	public boolean removeContinent(Continent p_continent) {
		return this.d_continents.remove(p_continent);
	}
	
	/**
	 * This method returns the StringBuilder object for showing the map
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();	
		
		builder.append(String.format("\n%-15s %s\n", "Continent", "Countries"));
		for (Continent l_c : d_continents) {	
			builder.append(String.format("%-15s ", l_c.getName()));
			List<Country> l_countries = l_c.getCountries();

			for (int l_i = 0; l_i < l_countries.size(); l_i++) {
				builder.append(l_countries.get(l_i).getName());
				if (l_i < l_countries.size() - 1) {
					builder.append(" ");
				}
			}
			builder.append("\n");
	    }
		
		builder.append(String.format("\n%-15s %s\n", "Country", "Neighbors"));
		for (Country c : d_countries) {
			builder.append(String.format("%-15s ", c.getName()));
			for (Country n : c.getNeighbors()) {
				builder.append(n.getName() + " ");
			}
			builder.append("\n");
		}
		
		return builder.toString();
    }
}
	