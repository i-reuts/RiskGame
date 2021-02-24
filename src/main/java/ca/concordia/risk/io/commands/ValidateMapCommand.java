package ca.concordia.risk.io.commands;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import ca.concordia.risk.GameEngine;
import ca.concordia.risk.game.Continent;
import ca.concordia.risk.game.Country;
import ca.concordia.risk.io.views.ConsoleView;

/**
 * Command representing <i>"validatemap"</i> operation.
 * 
 * @author ishika
 */
public class ValidateMapCommand implements Command {

	/** Validates the active Map. */
	@Override
	public void execute() {

		ConsoleView l_view = GameEngine.GetView();
		l_view.display("\nValidating the active map\n");

		boolean d_validate = validate();
		if (d_validate == true) {
			System.out.println("The given map is a valid map");
		} else {
			System.out.println("The given map is not a valid map");
		}
	}

	/**
	 * Checks the validity of a map as a whole and each continent in the map
	 * 
	 * @return <code>true</code> if map is a valid map.<br>
	 *         <code>false</code> if map is not a valid map.
	 */
	private boolean validate() {
		Set<Country> l_countries = new HashSet<Country>(GameEngine.GetMap().getCountries());
		if (l_countries.isEmpty()) {
			return false;
		}
		// Validate the graph as a whole
		for (Country l_country : l_countries) {
			Set<Country> l_visited = new HashSet<Country>();
			dfs(l_country, l_visited, l_countries);
			if (l_visited.size() != l_countries.size()) {
				return false;
			}
		}
		// Validate each continent
		List<Continent> l_continents = GameEngine.GetMap().getContinents();
		for (Continent l_continent : l_continents) {
			Set<Country> l_continentCountries = l_continent.getCountries();
			if (l_continentCountries.isEmpty()) {
				return false;
			}
			Set<Country> l_visited = new HashSet<Country>();
			dfs(l_continentCountries.iterator().next(), l_visited, l_continentCountries);
			if (l_visited.size() != l_continentCountries.size()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 
	 * Implements the depth first search algorithm using stacks for map validation
	 * 
	 * @param p_startNode The start node
	 * @param p_visited A set of visited nodes
	 * @param p_included Adjacency list
	 */
	private void dfs(Country p_startNode, Set<Country> p_visited, Set<Country> p_included) {
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

}