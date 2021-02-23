package ca.concordia.risk.io.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import ca.concordia.risk.GameEngine;
import ca.concordia.risk.game.Country;
import ca.concordia.risk.game.GameMap;
import ca.concordia.risk.io.views.ConsoleView;

/**
 * @author ishika
 *
 */
class Graph {
	private List<List<Integer>> d_graph;
	private boolean d_visited[];
	private int d_nodes;

	/**
	 * Constructor of class Graph
	 * 
	 * @param p_nodes Total number of Countries/Vertices in the given map
	 */
	public Graph(int p_nodes) {
		d_graph = new ArrayList<>();
		d_visited = new boolean[p_nodes];
		this.d_nodes = p_nodes;

		for (int i = 0; i < p_nodes; i++) {
			d_graph.add(i, new ArrayList<>());
		}
	}

	/**
	 * Adds edges to the adjacency list
	 * 
	 * @param a Vertex Country
	 * @param b Neighboring country
	 */
	public void addEdge(int a, int b) {
		d_graph.get(a).add(b);
	}

	/**
	 * Checks whether the given directed graph is fully connected or not.
	 * 
	 * @return<code>true</code> if the given directed graph is fully connected.<br>
	 * <code>false</code> if the given directed graph not is fully connected.
	 */
	public boolean isConnected() {

		for (int i = 0; i < d_nodes; i++) {
			dfs(i);

			for (int j = 0; j < d_nodes; j++) {
				if (!d_visited[j]) {
					return false;
				}
			}
			Arrays.fill(d_visited, false);
		}
		return true;
	}

	/**
	 * Uses depth first search for checking whether the given directed graph is
	 * fully connected or not
	 * 
	 * @param p_start
	 */
	public void dfs(int p_start) {
		Stack<Integer> stack = new Stack<>();

		stack.push(p_start);
		d_visited[p_start] = true;

		while (!stack.isEmpty()) {
			Integer node = stack.pop();

			List<Integer> neighboursList = d_graph.get(node);

			for (Integer neighbour : neighboursList) {
				if (!d_visited[neighbour]) {
					stack.push(neighbour);
					d_visited[neighbour] = true;
				}
			}
		}
	}
}

/** Command representing <i>"validatemap"</i> operation. */
public class ValidateMapCommand implements Command {

	/** Validates the active Map. */
	@Override
	public void execute() {
		// TODO Replace with actual implementation
		/* ConsoleView l_view = GameEngine.GetView();
		l_view.display("\nValidating the active map\n");

		try {
			GameMap l_gameMap = GameEngine.GetMap();
			String[] d_nameCountries = l_gameMap.getArrayOfCountries();
			int[] d_countryId = IntStream.range(1, d_nameCountries.length + 1).toArray();
			Country c;
			Map<Country, Integer> d_m = new HashMap<Country, Integer>();
			Set<Country> d_neighborCountry;
			int d_nodes = l_gameMap.numberOfCountries();

			Graph obj = new Graph(d_nodes);

			for (int i = 0; i < l_gameMap.numberOfCountries(); i++) {
				c = l_gameMap.getCountry(d_nameCountries[i]);
				d_m.put(c, d_countryId[i]);
			}

			d_m = d_m.entrySet().stream().sorted(Entry.comparingByValue())
					.collect(Collectors.toMap(Entry::getKey, Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

			for (Entry<Country, Integer> e : d_m.entrySet()) {
				c = e.getKey();
				int d_homeId = d_m.get(c);
				// System.out.println(c.getName() + " : " + d_homeId + "\n");
				d_neighborCountry = c.getNeighbors();
				for (Country c1 : d_neighborCountry) {
					int d_neighborId = d_m.get(c1);
					// System.out.println(d_neighborId);
					obj.addEdge(d_homeId - 1, d_neighborId - 1);
				}
				System.out.println();
			}

			System.out.println(obj.isConnected());

		} catch (Exception e) {
			e.printStackTrace();
		}*/
	}
}