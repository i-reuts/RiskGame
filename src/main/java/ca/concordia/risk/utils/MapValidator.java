package ca.concordia.risk.utils;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import ca.concordia.risk.game.Continent;
import ca.concordia.risk.game.Country;
import ca.concordia.risk.game.GameMap;

/** This class validates the game maps. */
public class MapValidator {

	private static String d_Status = "";

	/**
	 * Checks the validity of a map as a whole and each continent in the map.
	 * 
	 * @param p_map map to validate.
	 * @return <code>true</code> if map is a valid map.<br>
	 *         <code>false</code> if map is not a valid map.
	 */
	public static boolean Validate(GameMap p_map) {
		Set<Country> l_countries = new HashSet<Country>(p_map.getCountries());
		if (l_countries.size() < 2) {
			d_Status = "map has less than two countries";
			return false;
		}
		// Validate the graph as a whole
		for (Country l_country : l_countries) {
			Set<Country> l_visited = new HashSet<Country>();
			Dfs(l_country, l_visited, l_countries);
			if (l_visited.size() != l_countries.size()) {
				l_countries.removeAll(l_visited);
				d_Status = "map validation - countries " + BuildCountryListString(l_countries)
						+ " are unreachable from country " + l_country.getName();
				return false;
			}
		}
		// Validate each continent
		List<Continent> l_continents = p_map.getContinents();
		for (Continent l_continent : l_continents) {
			Set<Country> l_continentCountries = l_continent.getCountries();
			if (l_continentCountries.isEmpty()) {
				d_Status = "continent " + l_continent.getName() + " has no countries";
				return false;
			}
			for (Country l_country : l_continentCountries) {
				Set<Country> l_visited = new HashSet<Country>();
				Dfs(l_country, l_visited, l_continentCountries);
				if (l_visited.size() != l_continentCountries.size()) {
					Set<Country> l_unreachableCountries = new HashSet<Country>(l_continentCountries);
					l_unreachableCountries.removeAll(l_visited);
					d_Status = "continent validation - countries " + BuildCountryListString(l_unreachableCountries)
							+ " are unreachable from country " + l_country.getName() + " in continent "
							+ l_continent.getName();
					return false;
				}
			}
		}

		d_Status = "map is valid";
		return true;
	}

	/**
	 * Gets the validation status.
	 * 
	 * @return validation status message.
	 */
	public static String getStatus() {
		return d_Status;
	}

	/**
	 * 
	 * Implements the depth first search algorithm using stacks for map validation.
	 * 
	 * @param p_startNode The starting node.
	 * @param p_visited   A set of visited nodes.
	 * @param p_included  A set of nodes included in the search.
	 */
	private static void Dfs(Country p_startNode, Set<Country> p_visited, Set<Country> p_included) {
		Stack<Country> l_stack = new Stack<Country>();
		l_stack.push(p_startNode);
		p_visited.add(p_startNode);
		while (!l_stack.isEmpty()) {
			Country l_node = l_stack.pop();
			Set<Country> l_neighbourSet = l_node.getNeighbors();
			for (Country l_neighbour : l_neighbourSet) {
				if (!p_visited.contains(l_neighbour) && p_included.contains(l_neighbour)) {
					l_stack.push(l_neighbour);
					p_visited.add(l_neighbour);
				}
			}
		}
	}

	/**
	 * Builds and get a list of country names.
	 * 
	 * @param p_countries list of countries to build the list from.
	 * @return string list of country names.
	 */
	private static String BuildCountryListString(Set<Country> p_countries) {
		StringBuilder l_sb = new StringBuilder();

		l_sb.append("[");
		Iterator<Country> l_it = p_countries.iterator();
		while (l_it.hasNext()) {
			l_sb.append(l_it.next().getName());
			if (l_it.hasNext()) {
				l_sb.append(", ");
			}
		}
		l_sb.append("]");

		return l_sb.toString();
	}
}
