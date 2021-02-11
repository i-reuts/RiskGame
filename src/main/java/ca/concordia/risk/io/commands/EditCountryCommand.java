package ca.concordia.risk.io.commands;

import java.util.ArrayList;
import java.util.List;

import ca.concordia.risk.game.GameEngine;
import ca.concordia.risk.io.views.ConsoleView;

/**
 * Command representing <i>"editcountry"</i> operation.
 */
public class EditCountryCommand implements Command {
	
	private List<NewCountryData> m_countriesToAdd = new ArrayList<NewCountryData>();
	private List<String> m_countriesToRemove = new ArrayList<String>();

	/**
	 * Performs requested add and remove operations on the active Map.
	 */
	@Override
	public void execute() {
		ConsoleView l_view = GameEngine.getView();
		
		// TODO: Replace by the actual implementation
		l_view.display("\nExecuting edit map command");
		l_view.display("Countries to add: " + m_countriesToAdd);
		l_view.display("Countries to remove: " + m_countriesToRemove + "\n");
	}
	
	/**
	 * Adds a country to the list of countries to be added.
	 * @param p_countryName name of the country to add.
	 * @param p_continentName continent name that the country belongs to.
	 */
	public void addCountry(String p_countryName, String p_continentName) {
		m_countriesToAdd.add(new NewCountryData(p_countryName, p_continentName));
	}
	
	/**
	 * Adds a country to the list of countries to be removed.
	 * @param p_countryName name of the country to be removed.
	 */
	public void removeCountry(String p_countryName) {
		m_countriesToRemove.add(p_countryName);
	}

	/**
	 * Helper class representing {CountryName, ContinentName} tuple.
	 * <p> Used for storing countries to be added.
	 */
	private static class NewCountryData {
		
		public String m_countryName;
		public String m_continentName;
		
		/**	
		 * Creates a new <code>NewCountryData</code> tuple.
		 * @param p_countryName name of the country
		 * @param p_continentName continent that the country belongs to
		 */
		public NewCountryData(String p_countryName, String p_continentName) {
			m_countryName = p_countryName;
			m_continentName = p_continentName;
		}
				
		/**
		 * Returns a string representation of the {CountryName, ContinentName} tuple.
		 * @return string representing the {CountryName, ContinentName} tuple.
		 */
		@Override
		public String toString() {
			return "{" + m_countryName + ", " + m_continentName + "}";
		}
	}
}
