package ca.concordia.risk.io.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import ca.concordia.risk.game.GameEngine;
import ca.concordia.risk.game.GameMap;
import ca.concordia.risk.io.views.ConsoleView;

/**
 * @author ishika
 *
 */
class Graph {
	private List<List<Integer>> graph;
	private boolean visited[];
	private int nodes;

	/**
	 * Constructor of class Graph
	 * 
	 * @param nodes
	 */
	public Graph(int nodes) {
		graph = new ArrayList<>();
		visited = new boolean[nodes];
		this.nodes = nodes;

		for (int i = 0; i < nodes; i++) {
			graph.add(i, new ArrayList<>());
		}
	}
	
	/**
	 * @param a
	 * @param b
	 */
	public void addEdge(int a, int b) {
		graph.get(a).add(b);
	}


	/**
	 * @return
	 */
	public boolean isConnected() {

		for (int i = 0; i < nodes; i++) {
			dfs(i);

			for (int j = 0; j < nodes; j++) {
				if (!visited[j]) {
					return false;
				}
			}

			Arrays.fill(visited, false);
		}

		return true;
	}


	public void dfs(int start) {
	}
}

/** Command representing <i>"validatemap"</i> operation. */
public class ValidateMapCommand implements Command {

	/** Validates the active Map. */
	@Override
	public void execute() {
		// TODO Replace with actual implementation
		ConsoleView l_view = GameEngine.GetView();
		l_view.display("\nValidating the active map\n");

		GameMap l_gameMap = GameEngine.GetMap();
		int nodes = l_gameMap.numberOfCountries();

		Graph a = new Graph(nodes);

	}
}