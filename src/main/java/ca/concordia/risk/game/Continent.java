package ca.concordia.risk.game;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

/**
 * This class is the representation of the game entity Continent.
 * <p>
 * It contains the set of countries it owns.
 * 
 * @author Enrique
 * 
 */
public class Continent {
	private int d_value;
	private String d_name;
	private Set<Country> d_countries = new TreeSet<Country>(Comparator.comparing(Country::getName));

	/**
	 * Constructor for the Continent class.
	 * 
	 * @param p_name  name of the continent.
	 * @param p_value value of the continent for mustering when fully controlled by
	 *                a player.
	 */
	public Continent(String p_name, int p_value) {
		d_value = p_value;
		d_name = p_name;
	}

	/**
	 * Gets continent value.
	 * 
	 * @return continent value for mustering when fully controlled.
	 */
	public int getValue() {
		return d_value;
	}

	/**
	 * Gets continent name.
	 * 
	 * @return The continent name.
	 */
	public String getName() {
		return d_name;
	}

	/**
	 * Gets the set of countries belonging to the continent.
	 * 
	 * @return set of countries belonging to the continent.
	 */
	public Set<Country> getCountries() {
		return d_countries;
	}

	/**
	 * Adds a country to the set of countries owned by the continent.
	 * 
	 * @param p_country country to be added to this continent.
	 * @return <code>true</code> the country was added.<br>
	 *         <code>false</code> if the country already belonged to this continent.
	 */
	public boolean addCountry(Country p_country) {
		return d_countries.add(p_country);
	}

	/**
	 * Removes a country from the continent.
	 * 
	 * @param p_country name of the country to remove.
	 * @return <code>true</code> if the country was removed.<br>
	 *         <code>false</code> if the country did not belong to the continent.
	 */
	public boolean removeCountry(Country p_country) {
		return d_countries.remove(p_country);
	}
}
