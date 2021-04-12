package ca.concordia.risk.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
	 * Constructor for the <code>GameMap</code> entity.
	 */
	public GameMap() {
		d_countries = new TreeMap<String, Country>();
		d_continents = new TreeMap<String, Continent>();
	}

	/**
	 * Gets a list of all countries in the map.
	 * 
	 * @return list of map countries.
	 */
	public List<Country> getCountries() {
		return new ArrayList<Country>(d_countries.values());
	}

	/**
	 * Gets a list of all continents in the map.
	 * 
	 * @return list of map continents.
	 */
	public List<Continent> getContinents() {
		return new ArrayList<Continent>(d_continents.values());
	}

	/**
	 * Gets a country with the corresponding name if it exists.
	 * 
	 * @param p_countryName name of the country to return.
	 * @return <code>Country</code> with the name <code>p_countryName</code> if it
	 *         exists in the map.<br>
	 *         <code>null</code> if the country with name <code>p_countryName</code>
	 *         does not exist in the map.
	 */
	public Country getCountry(String p_countryName) {
		return d_countries.get(p_countryName);
	}

	/**
	 * Gets a continent with the corresponding name if it exists.
	 * 
	 * @param p_continentName name of the continent to return.
	 * @return <code>Continent</code> with the name <code>p_continentName</code> if
	 *         it exists in the map.<br>
	 *         <code>null</code> if the continent with name
	 *         <code>p_continentName</code> does not exist in the map.
	 */
	public Continent getContinent(String p_continentName) {
		return d_continents.get(p_continentName);
	}

	/**
	 * Adds a country to the map if a country with the same name does not already
	 * exist.
	 * 
	 * @param p_country country to add.
	 * @return <code>true</code> if the country was successfully added.<br>
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
	 * @param p_continent continent to add.
	 * @return <code>true</code> if the continent was successfully added.<br>
	 *         <code>false</code> if the continent already existed.
	 */
	public boolean addContinent(Continent p_continent) {
		if (d_continents.containsKey(p_continent.getName())) {
			return false;
		}

		d_continents.put(p_continent.getName(), p_continent);
		return true;
	}
	
	
	public void removeNeighbors(Country p_neighborToRemove) {
		for (Country l_c : d_countries.values()) {
			l_c.removeNeighbor(p_neighborToRemove);
		}
	}

	/**
	 * Removes a country with the specified name from the map.
	 * 
	 * @param p_countryName name of the country to remove.
	 * @return <code>true</code> if the country was removed.<br>
	 *         <code>false</code> if the country with name
	 *         <code>p_countryName</code> was not found.
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
	 * Removes a continent with the specified name from the map.
	 * 
	 * @param p_continentName name of the continent to remove.
	 * @return <code>true</code> if the continent was removed.<br>
	 *         <code>false</code> if the continent with name
	 *         <code>p_continentName</code> was not found.
	 */
	public boolean removeContinent(String p_continentName) {
		Continent l_continent = d_continents.get(p_continentName);
		if (l_continent == null) {
			return false;
		}

		// Remove all of the continent countries
		for (Country l_country : l_continent.getCountries()) {
			// Remove country from neighbors of all other countries
			/*for (Country l_c : d_countries.values()) {
				l_c.removeNeighbor(l_country);
			}*/
			removeNeighbors(l_country);
			// Remove country from the map
			removeCountry(l_country.getName());
		}

		// Remove continent
		d_continents.remove(p_continentName);

		return true;
	}

	/**
	 * Builds a string representation of the map.
	 * <p>
	 * This representation includes only the map topology, i.e. countries,
	 * continents and their relationships.
	 * 
	 * @return string representing the map.
	 */
	public String buildMapString() {
		StringBuilder l_builder = new StringBuilder();

		// Build the continent table
		buildContinentString(l_builder);

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
	 * Builds a string representation of the gameplay map.
	 * <p>
	 * This representation includes both the map topology and gameplay information
	 * such as which country is owned by which player and how many armies are
	 * deployed in each country.
	 * 
	 * @return string representing the gameplay map.
	 */
	public String buildGameplayMapString() {
		// Create a country list grouped and sorted in 3 levels: by player name, then by
		// continent name and finally by country name
		List<Country> l_countryList = new ArrayList<Country>(d_countries.values());
		Collections.sort(l_countryList, (l_c1, l_c2) -> {
			if (l_c1.getOwner().getName().compareTo(l_c2.getOwner().getName()) == 0) {
				return l_c1.getContinent().getName().compareTo(l_c2.getContinent().getName());
			} else {
				return l_c1.getOwner().getName().compareTo(l_c2.getOwner().getName());
			}
		});

		StringBuilder l_builder = new StringBuilder();
		// Build the continent table
		buildContinentString(l_builder);

		// Build a table of countries from the sorted list
		// Build the table header
		l_builder.append(
				String.format("\n%-20s %-15s %-8s %-15s %s\n", "Country", "Owner", "Armies", "Continent", "Neighbors"));
		for (Country l_country : l_countryList) {
			// Build a single row of the table
			l_builder.append(String.format("%-20s %-15s %-8s %-15s ", l_country.getName(),
					l_country.getOwner().getName(), l_country.getArmies(), l_country.getContinent().getName()));

			Iterator<Country> l_i = l_country.getNeighbors().iterator();
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
	 * Builds the continent table and appends it to the given string builder.
	 * 
	 * @param p_builder string builder to append the continent table to.
	 */
	private void buildContinentString(StringBuilder p_builder) {
		p_builder.append(String.format("\n%-20s %-6s %s\n", "Continent", "Value", "Countries"));
		for (Continent l_c : d_continents.values()) {
			p_builder.append(String.format("%-20s %-6s ", l_c.getName(), l_c.getValue()));
			Iterator<Country> l_i = l_c.getCountries().iterator();

			while (l_i.hasNext()) {
				p_builder.append(l_i.next().getName());
				if (l_i.hasNext()) {
					p_builder.append(", ");
				}
			}
			p_builder.append("\n");
		}
	}
}
