package ca.concordia.risk.io.commands;

import java.util.ArrayList;
import java.util.List;

import ca.concordia.risk.game.GameEngine;
import ca.concordia.risk.io.views.ConsoleView;

/** Command representing <i>"editcontinent"</i> operation. */
public class EditContinentCommand implements Command {

	private List<NewContinentData> m_continentsToAdd = new ArrayList<NewContinentData>();
	private List<String> m_continentsToRemove = new ArrayList<String>();
	
	
	/** Performs requested continent add and remove operations on the active Map. */
	@Override
	public void execute() {
		ConsoleView l_view = GameEngine.getView();
		
		// TODO: Replace by the actual implementation
		l_view.display("\nExecuting editcontinent command");
		l_view.display("Continents to add: " + m_continentsToAdd);
		l_view.display("Continents to remove: " + m_continentsToRemove + "\n");	
	}
	
	/** Adds a continent to the list of continents to be added.
	 * @param p_continentName continent name that the country belongs to.
	 * @param p_continentValue continent bonus reinforcements value.
	 */
	public void addContinent(String p_continentName, int p_continentValue) {
		m_continentsToAdd.add(new NewContinentData(p_continentName, p_continentValue));
	}
	
	/**
	 * Adds a country to the list of countries to be removed.
	 * @param p_continentName name of the country to be removed.
	 */
	public void removeContinent(String p_continentName) {
		m_continentsToRemove.add(p_continentName);
	}
	
	/**
	 * Helper class representing {ContinentName, ContinentValue} tuple.
	 * <p> Used for storing continents to be added.
	 */
	private static class NewContinentData {
		
		public String m_continentName;
		public int m_continentValue;
		
		/**	
		 * Creates a new <code>NewContinentData</code> tuple.
		 * @param p_continentName continent that the country belongs to.
		 * @param p_continentValue continent bonus reinforcements value.
		 */
		public NewContinentData(String p_continentName, int p_continentValue) {
			m_continentName = p_continentName;
			m_continentValue = p_continentValue;
		}
				
		/**
		 * Returns a string representation of the {ContinentName, ContinentValue} tuple.
		 * @return string representing the {ContinentName, ContinentValue} tuple.
		 */
		@Override
		public String toString() {
			return "{" + m_continentName + ", " + m_continentValue + "}";
		}
	}
}
