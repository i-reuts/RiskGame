package ca.concordia.risk.game;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

/**
 * This class is the representation of the game country.
 * 
 * @author Enrique
 *
 */
public class Country {
	private int d_numArmies;
	private String d_name;
	private Continent d_continent;
	private Player d_owner;
	private Set<Country> d_neighbors = new TreeSet<Country>(Comparator.comparing(Country::getName));

	/**
	 * Creates a new <code>Country</code>.
	 * 
	 * @param p_countryName name of the country to create.
	 * @param p_continent   continent the country belongs to.
	 */
	public Country(String p_countryName, Continent p_continent) {
		d_name = p_countryName;
		d_continent = p_continent;
	}

	/**
	 * Gets the name of the country.
	 * 
	 * @return country name.
	 */
	public String getName() {
		return d_name;
	}

	/**
	 * Gets the continent that this country belongs to.
	 * 
	 * @return continent the country belongs to.
	 */
	public Continent getContinent() {
		return d_continent;
	}

	/**
	 * Gets the neighboring countries.
	 * 
	 * @return set of neighboring countries.
	 */
	public Set<Country> getNeighbors() {
		return d_neighbors;
	}

	/**
	 * Gets the player owning this country.
	 * 
	 * @return player owning the country.
	 */
	public Player getOwner() {
		return d_owner;
	}

	/**
	 * Gets the number of deployed armies.
	 * 
	 * @return number of of armies currently deployed in this country.
	 */
	public int getArmies() {
		return d_numArmies;
	}

	/**
	 * Adds a neighbor to the country, representing a border.
	 * 
	 * @param p_country country to be added as a neighbor.
	 * @return <code>true</code> if the neighbor was added.<br>
	 *         <code>false</code> if the neighbor already existed.
	 */
	public boolean addNeighbor(Country p_country) {
		return d_neighbors.add(p_country);
	}

	/**
	 * Removes a neighbor from the country.
	 * 
	 * @param p_country country to be removed from the set of neighbors.
	 * @return <code>true</code> if the neighbor was removed.<br>
	 *         <code>false</code> if <code>p_country</code> was not a neighbor.
	 */
	public boolean removeNeighbor(Country p_country) {
		return d_neighbors.remove(p_country);
	}

	/**
	 * Sets the player owning this country.
	 * 
	 * @param p_owner player owning the country.
	 */
	public void setOwner(Player p_owner) {
		this.d_owner = p_owner;
	}

	/**
	 * Adds armies to the country.
	 * 
	 * @param p_armies positive integer representing the number of armies to add.
	 */
	public void addArmies(int p_armies) {
		if (p_armies > 0) {
			d_numArmies += p_armies;
		}
	}

	/**
	 * Removes armies from the country.
	 * 
	 * @param p_armies positive integer representing the number of armies to remove.
	 */
	public void removeArmies(int p_armies) {
		if (p_armies > 0 && p_armies <= d_numArmies) {
			d_numArmies -= p_armies;
		}
	}
}
