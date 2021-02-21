package ca.concordia.risk.io.commands;

import java.util.ArrayList;
import java.util.List;

import ca.concordia.risk.game.Continent;
import ca.concordia.risk.game.Country;
import ca.concordia.risk.game.GameEngine;
import ca.concordia.risk.game.GameMap;
import ca.concordia.risk.io.views.ConsoleView;

/** Command representing <i>"editcountry"</i> operation. */
public class EditCountryCommand implements Command {

	private List<NewCountryData> d_countriesToAdd = new ArrayList<NewCountryData>();
	private List<String> d_countriesToRemove = new ArrayList<String>();

	/**
	 * Performs requested country add and remove operations on the active
	 * <code>GameMap</code>.
	 * <p>
	 * New country is added only if a country with the specified name does not
	 * already exist.<br>
	 * Country is removed only if a country with the specified name exists.
	 */
	@Override
	public void execute() {
		ConsoleView l_view = GameEngine.GetView();
		GameMap l_gameMap = GameEngine.GetMap();

		if (l_gameMap != null) {
			for (NewCountryData l_countryData : d_countriesToAdd) {
				executeAddCountry(l_view, l_gameMap, l_countryData);
			}
			for (String l_countryName : d_countriesToRemove) {
				executeRemoveCountry(l_view, l_gameMap, l_countryName);
			}
		} else {
			l_view.display("No map to edit - please load a map first");
		}
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
	 * Executes a single add country command.
	 * 
	 * @param p_view        view to display feedback to the user.
	 * @param p_gameMap     active game map to add the country to.
	 * @param p_countryData data of the country to add.
	 */
	private void executeAddCountry(ConsoleView p_view, GameMap p_gameMap, NewCountryData p_countryData) {
		// Check if the specified continent exists
		Continent l_countryContinent = p_gameMap.getContinent(p_countryData.d_continentName);
		if (l_countryContinent == null) {
			p_view.display(
					"Failed to add country - continent with name " + p_countryData.d_continentName + " does not exist");
			return;
		}

		// Try adding the country
		Country l_newCountry = new Country(p_countryData.d_countryName, l_countryContinent);
		if (p_gameMap.addCountry(l_newCountry)) {
			p_view.display("Country " + p_countryData.d_countryName + " added");
		} else {
			p_view.display(
					"Failed to add country - country with name " + p_countryData.d_countryName + " already exists");
		}
	}

	/**
	 * Executes a single remove country command.
	 * 
	 * @param p_view        view to display feedback to the user.
	 * @param p_gameMap     active game map to remove the country from.
	 * @param p_countryName name of the country to remove.
	 */
	private void executeRemoveCountry(ConsoleView p_view, GameMap p_gameMap, String p_countryName) {
		if (p_gameMap.removeCountry(p_countryName)) {
			p_view.display("Country " + p_countryName + " removed");
		} else {
			p_view.display("Failed to remove country - country with name " + p_countryName + " does not exist");
		}
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
