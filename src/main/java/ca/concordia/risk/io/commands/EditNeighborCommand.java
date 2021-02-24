package ca.concordia.risk.io.commands;

import java.util.ArrayList;
import java.util.List;

import ca.concordia.risk.GameEngine;
import ca.concordia.risk.game.Country;
import ca.concordia.risk.game.GameMap;
import ca.concordia.risk.io.views.ConsoleView;

/** Command representing <i>"editneighbor"</i> operation. */
public class EditNeighborCommand implements Command {

	private List<NeighborData> d_neighborsToAdd = new ArrayList<NeighborData>();
	private List<NeighborData> d_neighborsToRemove = new ArrayList<NeighborData>();

	/**
	 * Performs requested country neighbor add and remove operations on the active
	 * <code>GameMap</code>.
	 * <p>
	 * New neighbor is added only if both the specified target and neighbor
	 * countries exist, and neighbor is not already a neighbor of the target
	 * country.<br>
	 * Neighbor is removed only if both the specified target and neighbor countries
	 * exist and the specified neighbor is a neighbor of the target country.
	 */
	@Override
	public void execute() {
		ConsoleView l_view = GameEngine.GetView();
		GameMap l_gameMap = GameEngine.GetMap();

		if (l_gameMap != null) {
			for (NeighborData l_neighborData : d_neighborsToAdd) {
				executeAddNeighbor(l_view, l_gameMap, l_neighborData);
			}
			for (NeighborData l_neighborData : d_neighborsToRemove) {
				executeRemoveNeighbor(l_view, l_gameMap, l_neighborData);
			}
		} else {
			l_view.display("No map to edit - please load a map first");
		}
	}

	/**
	 * Adds a {CountryName, NeighborCountryName} pair to the list of neighbors to be
	 * added.
	 * 
	 * @param p_countryName         country a neighbor of which is to be added.
	 * @param p_neighborCountryName neighbor country to added.
	 */
	public void addNeighbor(String p_countryName, String p_neighborCountryName) {
		d_neighborsToAdd.add(new NeighborData(p_countryName, p_neighborCountryName));
	}

	/**
	 * Adds a {CountryName, NeighborCountryName} pair to the list of neighbors to be
	 * removed.
	 * 
	 * @param p_countryName         country a neighbor of which is to be removed.
	 * @param p_neighborCountryName neighbor country to remove.
	 */
	public void removeNeighbor(String p_countryName, String p_neighborCountryName) {
		d_neighborsToRemove.add(new NeighborData(p_countryName, p_neighborCountryName));
	}

	/**
	 * Executes a single add neighbor command.
	 * 
	 * @param p_view         view to display feedback to the user.
	 * @param p_gameMap      active game map to add the neighbor to.
	 * @param p_neighborData data of the neighbor to add.
	 */
	private void executeAddNeighbor(ConsoleView p_view, GameMap p_gameMap, NeighborData p_neighborData) {
		// Check if the target country exists
		Country l_country = p_gameMap.getCountry(p_neighborData.d_countryName);
		if (l_country == null) {
			p_view.display(
					"Failed to add neighbor - country with name " + p_neighborData.d_countryName + " does not exist");
			return;
		}

		// Try adding the neighbor
		Country l_neighbor = p_gameMap.getCountry(p_neighborData.d_neighborCountryName);
		if (l_neighbor != null) {
			if (l_country.addNeighbor(l_neighbor)) {
				p_view.display("Neighbor " + p_neighborData.d_neighborCountryName + " added to country "
						+ p_neighborData.d_countryName);
			} else {
				p_view.display("Failed to add neighbor - country with name " + p_neighborData.d_neighborCountryName
						+ " is already a neighbor of " + p_neighborData.d_countryName);
			}

		} else {
			p_view.display("Failed to add neighbor - country with name " + p_neighborData.d_neighborCountryName
					+ " does not exist");
		}
	}

	/**
	 * Executes a single remove neighbor command.
	 * 
	 * @param p_view         view to display feedback to the user.
	 * @param p_gameMap      active game map to remove the neighbor from.
	 * @param p_neighborData data of the neighbor to remove.
	 */
	private void executeRemoveNeighbor(ConsoleView p_view, GameMap p_gameMap, NeighborData p_neighborData) {
		// Check if the target country exists
		Country l_country = p_gameMap.getCountry(p_neighborData.d_countryName);
		if (l_country == null) {
			p_view.display("Failed to remove neighbor - country with name " + p_neighborData.d_countryName
					+ " does not exist");
			return;
		}

		// Try removing the neighbor
		Country l_neighbor = p_gameMap.getCountry(p_neighborData.d_neighborCountryName);
		if (l_neighbor != null) {
			if (l_country.removeNeighbor(l_neighbor)) {
				p_view.display("Neighbor " + p_neighborData.d_neighborCountryName + " removed from country "
						+ p_neighborData.d_countryName);
			} else {
				p_view.display("Failed to remove neighbor - country with name " + p_neighborData.d_neighborCountryName
						+ " is not a neighbor of " + p_neighborData.d_countryName);
			}
		} else {
			p_view.display("Failed to remove neighbor - country with name " + p_neighborData.d_neighborCountryName
					+ " does not exist");
		}
	}

	/**
	 * Helper class representing {CountryName, NeighborCountryName} tuple.
	 * <p>
	 * Used for storing countries and their neighbors to be added or removed.
	 */
	private static class NeighborData {

		public String d_countryName;
		public String d_neighborCountryName;

		/**
		 * Creates a new <code>NeighborData</code> tuple.
		 * 
		 * @param p_countryName         country a neighbor of which is to be modified.
		 * @param p_neighborCountryName neighbor country to modify.
		 */
		public NeighborData(String p_countryName, String p_neighborCountryName) {
			d_countryName = p_countryName;
			d_neighborCountryName = p_neighborCountryName;
		}

		/**
		 * Returns a string representation of the {CountryName, NeighborCountryName}
		 * tuple.
		 * 
		 * @return string representing the {CountryName, NeighborCountryName} tuple.
		 */
		@Override
		public String toString() {
			return "{" + d_countryName + ", " + d_neighborCountryName + "}";
		}
	}
}
