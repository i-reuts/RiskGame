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

	// Validation status message patterns
	private static final String d_ValidMapStatus = "map is valid";
	private static final String d_LessThanTwoCountriesStatus = "map has less than two countries";
	private static final String d_EmptyContinentStatus = "continent %s has no countries";
	private static final String d_MapGraphErrorStatus = "map validation - countries %s are unreachable from country %s";
	private static final String d_ContinentGraphErrorStatus = "continent validation - countries %s are unreachable from country %s in continent %s";

	private static String d_Status = "";

	/**
	 * Checks the validity of a map as a whole and of each continent in the map.
	 * 
	 * @param p_map map to validate.
	 * @return <code>true</code> if map is a valid map.<br>
	 *         <code>false</code> if map is not a valid map.
	 */
	public static boolean Validate(GameMap p_map) {
		// Validate the map graph as a whole
		boolean l_mapGraphValid = ValidateMapGraph(p_map);
		if (!l_mapGraphValid) {
			return false;
		}

		// Validate the subgraphs of each continent
		boolean l_continentGraphsValid = ValidateContinentGraphs(p_map);
		if (!l_continentGraphsValid) {
			return false;
		}

		// Map is valid, set status and return
		SetStatus(d_ValidMapStatus);
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
	 * Validates the map graph as a whole.
	 * 
	 * @param p_map map to validate.
	 * @return <code>true</code> if map graph is valid.<br>
	 *         <code>false</code> if map graph is not valid.
	 */
	private static boolean ValidateMapGraph(GameMap p_map) {
		// Ensure map has more than two countries
		Set<Country> l_countries = new HashSet<Country>(p_map.getCountries());
		if (l_countries.size() < 2) {
			SetStatus(d_LessThanTwoCountriesStatus);
			return false;
		}

		// Validate the map graph to be connected
		for (Country l_country : l_countries) {
			Set<Country> l_visited = new HashSet<Country>();
			Dfs(l_country, l_visited, l_countries);

			if (l_visited.size() != l_countries.size()) {
				l_countries.removeAll(l_visited);
				SetStatus(d_MapGraphErrorStatus, BuildCountryListString(l_countries), l_country.getName());
				return false;
			}
		}

		return true;
	}

	/**
	 * Validates the continent subgraphs.
	 * 
	 * @param p_map map to validate.
	 * @return <code>true</code> if all continent subgraphs are valid.<br>
	 *         <code>false</code> if there are continents with invalid subgraphs.
	 */
	private static boolean ValidateContinentGraphs(GameMap p_map) {
		List<Continent> l_continents = p_map.getContinents();

		// Validate each continent subgraph
		for (Continent l_continent : l_continents) {
			// Ensure continent is not empty
			Set<Country> l_continentCountries = l_continent.getCountries();
			if (l_continentCountries.isEmpty()) {
				SetStatus(d_EmptyContinentStatus, l_continent.getName());
				return false;
			}

			// Validate the continent subgraph to be connected
			for (Country l_country : l_continentCountries) {
				Set<Country> l_visited = new HashSet<Country>();
				Dfs(l_country, l_visited, l_continentCountries);

				if (l_visited.size() != l_continentCountries.size()) {
					Set<Country> l_unreachableCountries = new HashSet<Country>(l_continentCountries);
					l_unreachableCountries.removeAll(l_visited);

					SetStatus(d_ContinentGraphErrorStatus, BuildCountryListString(l_unreachableCountries),
							l_country.getName(), l_continent.getName());
					return false;
				}
			}
		}

		return true;
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
	 * Sets the validation status using the provided status message pattern.
	 * 
	 * @param p_messagePattern message pattern.
	 * @param p_arguments      arguments to insert in the message pattern.
	 */
	private static void SetStatus(String p_messagePattern, Object... p_arguments) {
		d_Status = String.format(p_messagePattern, p_arguments);
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
