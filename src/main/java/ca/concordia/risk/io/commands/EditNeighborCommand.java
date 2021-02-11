package ca.concordia.risk.io.commands;

import java.util.ArrayList;
import java.util.List;

import ca.concordia.risk.game.GameEngine;
import ca.concordia.risk.io.views.ConsoleView;

/** Command representing <i>"editneighbor"</i> operation. */
public class EditNeighborCommand implements Command {

	private List<NeighborData> m_neighborsToAdd = new ArrayList<NeighborData>();
	private List<NeighborData> m_neighborsToRemove = new ArrayList<NeighborData>();
	
	/** Performs requested country neighbor add and remove operations on the active Map. */
	@Override
	public void execute() {
		ConsoleView l_view = GameEngine.getView();
		
		// TODO: Replace by the actual implementation
		l_view.display("\nExecuting editneighbor command");
		l_view.display("Neighbors to add: " + m_neighborsToAdd);
		l_view.display("Neighbors to remove: " + m_neighborsToRemove + "\n");	
	}
	
	/** Adds a {CountryName, NeighborCountryName} to the list of neighbors to be added.
		 * @param m_countryName country a neighbor of which is to be added.
		 * @param m_neighborCountryName neighbor country to added.
	 */
	public void addNeighbor(String p_countryName, String p_neighborCountryName) {
		m_neighborsToAdd.add(new NeighborData(p_countryName, p_neighborCountryName));
	}
	
	/** Adds a {CountryName, NeighborCountryName} to the list of neighbors to be removed.
	 * @param m_countryName country a neighbor of which is to be removed.
	 * @param m_neighborCountryName neighbor country to remove.
	 */
	public void removeNeighbor(String p_countryName, String p_neighborCountryName) {
		m_neighborsToRemove.add(new NeighborData(p_countryName, p_neighborCountryName));
	}

	/**
	 * Helper class representing {CountryName, NeighborCountryName} tuple.
	 * <p> Used for storing countries and their neighbors to be added or removed.
	 */
	private static class NeighborData {
		
		public String m_countryName;
		public String m_neighborCountryName;

		/**	
		 * Creates a new <code>NeighborData</code> tuple.
		 * @param m_countryName country a neighbor of which is to be modified.
		 * @param m_neighborCountryName neighbor country to modify.
		 */
		public NeighborData(String p_countryName, String p_neighborCountryName) {
			m_countryName = p_countryName;
			m_neighborCountryName = p_neighborCountryName;
		}
				
		/**
		 * Returns a string representation of the {CountryName, NeighborCountryName} tuple.
		 * @return string representing the {CountryName, NeighborCountryName} tuple.
		 */
		@Override
		public String toString() {
			return "{" + m_countryName + ", " + m_neighborCountryName + "}";
		}
	}
}
