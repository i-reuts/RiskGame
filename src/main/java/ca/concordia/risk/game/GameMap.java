package ca.concordia.risk.game;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

/**
 * This class is the representation of the game map.
 * <p>
 * It is responsible for managing countries and continents belonging to this
 * map.
 * 
 * @author Enrique
 *
 */
public class GameMap {
	private Map<String, Country> d_countries;
	private Map<String, Continent> d_continents;

	/**
	 * Constructor for the GameMap entity
	 */
	public GameMap() {
		d_countries = new TreeMap<String, Country>();
		d_continents = new TreeMap<String, Continent>();
	}

	/**
	 * Get the number of countries the GameMap has 
	 * 
	 * @return <code>Number of Countries</code>
	 */
	public int numberOfCountries() {
		return d_countries.size();
	}
	
	/**
	 * Get a list with the name of all continents
	 * 
	 * @return <code>List</code> with the name of all <code>Continents</code>
	 */
	public String[] getArrayOfContinents() {
		return d_continents.keySet().toArray(new String[0]);
	}
	
	/**
	 * Get a list with the name of all countries
	 * 
	 * @return <code>List</code> with the name of all <code>Countries</code>
	 */
	public String[] getArrayOfCountries() {
		return d_countries.keySet().toArray(new String[0]);
	}
	
	/**
	 * Gets a country with the corresponding name if it exists.
	 * 
	 * @param p_countryName name of the country to return.
	 * @return <code>Country</code> with the the <code>p_countryName</code> if it
	 *         exists in the map.<br>
	 *         <code>null</code> if the country with <code>p_countryName</code> does
	 *         not exist in the map.
	 */
	public Country getCountry(String p_countryName) {
		return d_countries.get(p_countryName);
	}

	/**
	 * Gets a continent with the corresponding name if it exists.
	 * 
	 * @param p_continentName name of the country to return.
	 * @return <code>Continent</code> with the the <code>continentName</code> if it
	 *         exists in the map.<br>
	 *         <code>null</code> if the continent with <code>continentName</code>
	 *         does not exist in the map.
	 */
	public Continent getContinent(String p_continentName) {
		return d_continents.get(p_continentName);
	}

	/**
	 * Adds a country to the map if a country with the same name does not already
	 * exist.
	 * 
	 * @param p_country country to add.
	 * @return <code>true</code> if country was successfully added.<br>
	 *         <code>false</code> if the country already existed.
	 */
	public boolean addCountry(Country p_country) {
		if (d_countries.containsKey(p_country.getName())) {
			return false;
		}

		d_countries.put(p_country.getName(), p_country);
		p_country.getContinent().addCountry(p_country);

		return true;
	}

	/**
	 * Adds a continent to the map if a continent with the same name does not
	 * already exist.
	 * 
	 * @param p_continent Continent object
	 * @return <code>true</code> if continent was successfully added.<br>
	 *         <code>false</code> if the continent already existed.
	 */
	public boolean addContinent(Continent p_continent) {
		if (d_continents.containsKey(p_continent.getName())) {
			return false;
		}

		d_continents.put(p_continent.getName(), p_continent);
		return true;
	}

	/**
	 * Removes a country from the map.
	 * 
	 * @param p_countryName name of the country to remove.
	 * @return <code>true</code> if the country was removed.<br>
	 *         <code>false</code> if the country was not found.
	 */
	public boolean removeCountry(String p_countryName) {
		Country l_country = d_countries.get(p_countryName);
		if (l_country == null) {
			return false;
		}

		// Remove country from the continent it belongs to
		Continent l_continent = l_country.getContinent();
		l_continent.removeCountry(l_country);

		// Remove country from neighbors of all other countries
		for (Country l_c : d_countries.values()) {
			l_c.removeNeighbor(l_country);
		}

		// Remove country from the map
		d_countries.remove(p_countryName);

		return true;
	}

	/**
	 * Removes a continent from the map.
	 * 
	 * @param p_continentName name of the continent to remove.
	 * @return <code>true</code> if the continent was removed.<br>
	 *         <code>false</code> if the continent was not found.
	 */
	public boolean removeContinent(String p_continentName) {
		Continent l_continent = d_continents.get(p_continentName);
		if (l_continent == null) {
			return false;
		}

		// Remove all of the continent countries
		for (Country l_country : l_continent.getCountries()) {
			// Remove country from neighbors of all other countries
			for (Country l_c : d_countries.values()) {
				l_c.removeNeighbor(l_country);
			}

			// Remove country from the map
			d_countries.remove(l_country.getName());
		}

		// Remove continent
		d_continents.remove(p_continentName);

		return true;
	}

	/**
	 * Builds a string representation of the map.
	 * 
	 * @return string representing the map.
	 */
	@Override
	public String toString() {
		StringBuilder l_builder = new StringBuilder();

		// Build the continent table
		l_builder.append(String.format("\n%-20s %s\n", "Continent", "Countries"));
		for (Continent l_c : d_continents.values()) {
			l_builder.append(String.format("%-20s ", l_c.getName()));
			Iterator<Country> l_i = l_c.getCountries().iterator();

			while (l_i.hasNext()) {
				l_builder.append(l_i.next().getName());
				if (l_i.hasNext()) {
					l_builder.append(", ");
				}
			}
			l_builder.append("\n");
		}

		// Build the country neighbors table
		l_builder.append(String.format("\n%-20s %s\n", "Country", "Neighbors"));
		for (Country l_c : d_countries.values()) {
			l_builder.append(String.format("%-20s ", l_c.getName()));

			Iterator<Country> l_i = l_c.getNeighbors().iterator();
			while (l_i.hasNext()) {
				l_builder.append(l_i.next().getName());
				if (l_i.hasNext()) {
					l_builder.append(", ");
				}
			}
			l_builder.append("\n");
		}

		return l_builder.toString();
	}

	/**
	 * Show map in play mode
	 */
	public void showMap() {
		StringBuilder sb = new StringBuilder();
		for (Country l_c : d_countries.values()) {
			Iterator<Country> l_i = l_c.getNeighbors().iterator();
			while (l_i.hasNext()) {
				sb.append(l_i.next().getName());
				if (l_i.hasNext()) {
					sb.append(", ");
				}
			}
		}

		StringBuilder l_builder = new StringBuilder(
				String.format("%-20s %-20s %-20s %-20s %s\n", "Country", "Owner", "Armies", "Continent", "Neighbors"));
		for (Entry<String, Country> entry : d_countries.entrySet()) {
			l_builder.append(String.format("%-20s %-20s %-20d %-20s %s\n", entry.getValue().getName(),
					entry.getValue().getOwner().getName(), entry.getValue().getArmies(),
					entry.getValue().getContinent().getName(), sb.toString()));
		}
		System.out.println(l_builder.toString());
	}
}

