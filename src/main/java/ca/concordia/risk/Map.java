package ca.concordia.risk;


import java.util.LinkedList;

/**
 * This class is the representation of the game entity Map. It has a linked
 * list to manage its countries and continents belonging to this map.
 * 
 * @author Enrique
 *
 */
public class Map {
	private LinkedList<Country> d_countries;
	private LinkedList<Continent> d_continents;
	
	/**
	 * Add a country to the map.
	 * @param p_country Country object
	 */
	public void addCountry(Country p_country) {
		this.d_countries.add(p_country);
	}
	/**
	 * Add a continent to the map.
	 * @param p_continent Continent object
	 */
	public void addContinent(Continent p_continent) {
		this.d_continents.add(p_continent);
	}
	
	/**
	 * Remove a country from the map.
	 * @param p_country Country object.
	 * @return True if the Country existed. False other way.
	 */
	public boolean removeCountry(Country p_country) {
		return this.d_countries.remove(p_country);
	}
	/**
	 * Remove a continent from the map.
	 * @param p_continent Continent object.
	 * @return True if the continent existed. False other way.
	 */
	public boolean removeContinent(Continent p_continent) {
		return this.d_continents.remove(p_continent);
	}
}
