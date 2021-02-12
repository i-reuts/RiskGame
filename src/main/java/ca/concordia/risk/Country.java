package ca.concordia.risk;

import java.util.LinkedList;

/**
 * This class is the representation of the game entity Country. It has a linked
 * list to manage its neighbors. Currently there is no detection for duplicate
 * neighbors.
 * 
 * @author Enrique
 *
 */
public class Country {
	private int d_armies;
	private Integer d_id;
	private String d_name;
	private Continent d_parent;
	private Player d_owner;
	private LinkedList<Country> d_neighbors;

	/**
	 Constructor that doesn't ask for ID to construct the Country class.
	 * 
	 * @param p_name Name of the country.
	 */
	Country(String p_name) {
		d_armies = 0;
		d_id = 0;
		d_name = p_name;
		d_neighbors = new LinkedList<Country>();
	}
	
	/**
	 * Constructor for the Country class.
	 * 
	 * @param p_id   ID of the country.
	 * @param p_name Name of the country.
	 */
	Country(Integer p_id, String p_name) {
		d_armies = 0;
		d_id = p_id;
		d_name = p_name;
		d_neighbors = new LinkedList<Country>();
	}

	/**
	 * @return number of current amount of armies on this country.
	 */
	public int getArmies() {
		return d_armies;
	}

	/**
	 * @return ID of the country.
	 */
	public Integer getId() {
		return d_id;
	}

	/**
	 * @return Name of the country.
	 */
	public String getName() {
		return d_name;
	}

	/**
	 * @return Continent it belongs to.
	 */
	public Continent getParent() {
		return d_parent;
	}

	/**
	 * @return Player owning this country.
	 */
	public Player getOwner() {
		return d_owner;
	}

	/**
	 * @param p_id Id for the county
	 */
	public void setId(Integer p_id) {
		this.d_id = p_id;
	}
	
	/**
	 * @param p_parent Continent owning this country.
	 */
	public void setParent(Continent p_parent) {
		this.d_parent = p_parent;
	}

	/**
	 * @param p_owner Player owning this country.
	 */
	public void setOwner(Player p_owner) {
		this.d_owner = p_owner;
	}

	/**
	 * Add an amount of armies.
	 * 
	 * @param p_armies Positive integer of armies to add.
	 */
	public void addArmies(int p_armies) {
		if (p_armies > 0) {
			d_armies += p_armies;
		}
	}

	/**
	 * Remove an amount of armies.
	 * 
	 * @param p_armies Positive integer of armies to remove.
	 */
	public void removeArmies(int p_armies) {
		if (p_armies > 0 && p_armies <= d_armies) {
			d_armies -= p_armies;
		}
	}

	/**
	 * Add a neighbor. Currently it doesn't detects duplicates.
	 * 
	 * @param p_country Country to be added as a neighbor.
	 */
	public void addNeighbor(Country p_country) {
		d_neighbors.add(p_country);
	}

	/**
	 * Remove a neighbor.
	 * 
	 * @param p_country Country to be removed from the list of neighbors.
	 * @return True if removed from the list. False if it was not on the list.
	 */
	public boolean removeNeighbor(Country p_country) {
		return d_neighbors.remove(p_country);
	}
}
