package ca.concordia.risk.io.commands;

import java.util.ArrayList;
import java.util.List;

import ca.concordia.risk.GameEngine;
import ca.concordia.risk.game.Continent;
import ca.concordia.risk.game.GameMap;
import ca.concordia.risk.io.views.ConsoleView;

/** Command representing <i>"editcontinent"</i> operation. */
public class EditContinentCommand implements Command {

	private List<NewContinentData> d_continentsToAdd = new ArrayList<NewContinentData>();
	private List<String> d_continentsToRemove = new ArrayList<String>();

	/**
	 * Performs requested continent add and remove operations on the active
	 * <code>GameMap</code>.
	 * <p>
	 * New continent is added only if a continent with the specified name does not
	 * already exist.<br>
	 * Continent is removed only if a continent with the specified name exists.
	 */
	@Override
	public void execute() {
		ConsoleView l_view = GameEngine.GetView();
		GameMap l_gameMap = GameEngine.GetMap();

		if (l_gameMap != null) {
			for (NewContinentData l_continentData : d_continentsToAdd) {
				executeAddContinent(l_view, l_gameMap, l_continentData);
			}
			for (String l_continentName : d_continentsToRemove) {
				executeRemoveContinent(l_view, l_gameMap, l_continentName);
			}
		} else {
			l_view.display("No map to edit - please load a map first");
		}
	}

	/**
	 * Adds a continent to the list of continents to be added.
	 * 
	 * @param p_continentName  name of the continent to be added.
	 * @param p_continentValue continent bonus reinforcements value.
	 */
	public void addContinent(String p_continentName, int p_continentValue) {
		d_continentsToAdd.add(new NewContinentData(p_continentName, p_continentValue));
	}

	/**
	 * Adds a continent to the list of continents to be removed.
	 * 
	 * @param p_continentName name of the continent to be removed.
	 */
	public void removeContinent(String p_continentName) {
		d_continentsToRemove.add(p_continentName);
	}

	/**
	 * Executes a single add continent command.
	 * 
	 * @param p_view          view to display feedback to the user.
	 * @param p_gameMap       active game map to add the continent to.
	 * @param p_continentData data of the continent to add.
	 */
	private void executeAddContinent(ConsoleView p_view, GameMap p_gameMap, NewContinentData p_continentData) {
		Continent l_newContinent = new Continent(p_continentData.d_continentName, p_continentData.d_continentValue);
		if (p_gameMap.addContinent(l_newContinent)) {
			p_view.display("Continent " + p_continentData.d_continentName + " added");
		} else {
			p_view.display("Failed to add continent - continent with name " + p_continentData.d_continentName
					+ " already exists");
		}
	}

	/**
	 * Executes a single remove continent command.
	 * 
	 * @param p_view          view to display feedback to the user.
	 * @param p_gameMap       active game map to remove the continent from.
	 * @param p_continentName name of the continent to remove.
	 */
	private void executeRemoveContinent(ConsoleView p_view, GameMap p_gameMap, String p_continentName) {
		if (p_gameMap.removeContinent(p_continentName)) {
			p_view.display("Continent " + p_continentName + " removed");
		} else {
			p_view.display("Failed to remove continent - continent with name " + p_continentName + " does not exist");
		}
	}

	/**
	 * Helper class representing {ContinentName, ContinentValue} tuple.
	 * <p>
	 * Used for storing continents to be added.
	 */
	private static class NewContinentData {

		public String d_continentName;
		public int d_continentValue;

		/**
		 * Creates a new <code>NewContinentData</code> tuple.
		 * 
		 * @param p_continentName  continent that the country belongs to.
		 * @param p_continentValue continent bonus reinforcements value.
		 */
		public NewContinentData(String p_continentName, int p_continentValue) {
			d_continentName = p_continentName;
			d_continentValue = p_continentValue;
		}

		/**
		 * Returns a string representation of the {ContinentName, ContinentValue} tuple.
		 * 
		 * @return string representing the {ContinentName, ContinentValue} tuple.
		 */
		@Override
		public String toString() {
			return "{" + d_continentName + ", " + d_continentValue + "}";
		}
	}
}
