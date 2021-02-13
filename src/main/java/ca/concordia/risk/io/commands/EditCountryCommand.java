package ca.concordia.risk.io.commands;

import java.util.ArrayList;
import java.util.List;

import ca.concordia.risk.game.GameEngine;
import ca.concordia.risk.io.views.ConsoleView;

/** Command representing <i>"editcountry"</i> operation. */
public class EditCountryCommand implements Command {

	private List<NewCountryData> d_countriesToAdd = new ArrayList<NewCountryData>();
	private List<String> d_countriesToRemove = new ArrayList<String>();

	/** Performs requested country add and remove operations on the active Map. */
	@Override
	public void execute() {
		ConsoleView l_view = GameEngine.GetView();

		// TODO: Replace by the actual implementation
		l_view.display("\nExecuting editcountry command");
		l_view.display("Countries to add: " + d_countriesToAdd);
		l_view.display("Countries to remove: " + d_countriesToRemove + "\n");
	}

	/**
	 * Adds a country to the list of countries to be added.
	 * 
	 * @param p_countryName   name of the country to add.
	 * @param p_continentName continent name that the country belongs to.
	 */
	public void addCountry(String p_countryName, String p_continentName) {
		d_countriesToAdd.add(new NewCountryData(p_countryName, p_continentName));
	}

	/**
	 * Adds a country to the list of countries to be removed.
	 * 
	 * @param p_countryName name of the country to be removed.
	 */
	public void removeCountry(String p_countryName) {
		d_countriesToRemove.add(p_countryName);
	}

	/**
	 * Helper class representing {CountryName, ContinentName} tuple.
	 * <p>
	 * Used for storing countries to be added.
	 */
	private static class NewCountryData {

		public String d_countryName;
		public String d_continentName;

		/**
		 * Creates a new <code>NewCountryData</code> tuple.
		 * 
		 * @param p_countryName   name of the country
		 * @param p_continentName continent that the country belongs to
		 */
		public NewCountryData(String p_countryName, String p_continentName) {
			d_countryName = p_countryName;
			d_continentName = p_continentName;
		}

		/**
		 * Returns a string representation of the {CountryName, ContinentName} tuple.
		 * 
		 * @return string representing the {CountryName, ContinentName} tuple.
		 */
		@Override
		public String toString() {
			return "{" + d_countryName + ", " + d_continentName + "}";
		}
	}
}
