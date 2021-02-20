package ca.concordia.risk.game;

import java.util.*;

/**
 * This class is the representation of the game entity Continent. It contains a
 * hash map to keep track of the countries it owns. This class generates its own
 * id as it is not specific to the map file.
 * 
 * @author Enrique
 * 
 */
public class Continent {
	private static int d_counter = 1;
	private int d_value;
	private Integer d_id;
	private String d_name;
	private HashMap<String, Country> d_countries;

	/**
	 * Constructor for the Continent class.
	 * 
	 * @param p_name  Name of the continent.
	 * @param p_value Value of the continent for mustering when fully controlled.
	 */
	public Continent(String p_name, int p_value) {
		d_value = p_value;
		d_id = Continent.d_counter++;
		d_name = p_name;
		d_countries = new HashMap<String, Country>();
	}

	/**
	 * @return The continent value for mustering when fully controlled.
	 */
	public int getValue() {
		return d_value;
	}

	/**
	 * @return The continent id.
	 */
	public Integer getId() {
		return d_id;
	}

	/**
	 * @return The continent name.
	 */
	public String getName() {
		return d_name;
	}

	/**
	 * Method to add a country to the hash map of countries own by the continent,
	 * and set its parent to be this continent.
	 * 
	 * @param p_country The country to be added to this continent.
	 * @return True if added. False if the country id already existed.
	 */
	public boolean addCountry(Country p_country) {
		if (d_countries.containsKey(p_country.getName())) {
			return false;
		}

		d_countries.put(p_country.getName(), p_country);
		p_country.setParent(this);
		return true;
	}

	/**
	 * Method to remove a country from the hash map of the continent.
	 * 
	 * @param p_name The name of the country to be remove.
	 * @return True if the continent existed. False other way.
	 */
	public boolean removeCountry(String p_name) {
		return (d_countries.remove(p_name) != null);
	}
	
	/**
	 * This method returns list of countries.
	 * 
	 * @return List of type Country.
	 */
	public List<Country> getCountries() {
		return new ArrayList<Country>(d_countries.values());
	}
}
