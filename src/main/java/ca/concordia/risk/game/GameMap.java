package ca.concordia.risk.game;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;


/**
 * This class is the representation of the game map. It has a linked list to
 * manage its countries and continents belonging to this map.
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
	 * @param p_country Country object
	 * @return <code>true</code> if country was successfully added.<br>
	 *         <code>false</code> if the country already existed.
	 */
	public boolean addCountry(Country p_country) {
		if (d_countries.containsKey(p_country.getName())) {
			return false;
		}

		d_countries.put(p_country.getName(), p_country);
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
	 * @param p_countryName name of the Country to remove.
	 * @return Country that was removed if the Country existed. Null other way.
	 */
	public Country removeCountry(String p_countryName) {
		return d_countries.remove(p_countryName);
	}

	/**
	 * Removes a continent from the map.
	 * 
	 * @param p_continentName name of the Continent to remove.
	 * @return Continent that was removed if the continent existed. Null other way.
	 */
	public Continent removeContinent(String p_continentName) {
		return d_continents.remove(p_continentName);
	}
	
	/**
	 * Returns a string representation of the map.
	 * 
	 * @return string representing the map. 
	 */
	@Override
	public String toString() {
		StringBuilder l_builder = new StringBuilder();	
		
		l_builder.append(String.format("\n%-15s %s\n", "Continent", "Countries"));
		for (Continent l_c : d_continents.values()) {	
			l_builder.append(String.format("%-15s ", l_c.getName()));
			List<Country> l_countries = l_c.getCountries();

			for (int l_i = 0; l_i < l_countries.size(); l_i++) {
				l_builder.append(l_countries.get(l_i).getName());
				if (l_i < l_countries.size() - 1) {
					l_builder.append(", ");
				}
			}
			l_builder.append("\n");
	    }
		
		l_builder.append(String.format("\n%-15s %s\n", "Country", "Neighbors"));
		for (Country l_c : d_countries.values()) {
			l_builder.append(String.format("%-15s ", l_c.getName()));
			List<Country> l_neighbors = l_c.getNeighbors();
			
			for (int l_i = 0; l_i < l_neighbors.size(); l_i++) {
				l_builder.append(l_neighbors.get(l_i).getName());
				if (l_i < l_neighbors.size() - 1) {
					l_builder.append(", ");
				}
			}
			l_builder.append("\n");
		}
		
		return l_builder.toString();
    }
}
	