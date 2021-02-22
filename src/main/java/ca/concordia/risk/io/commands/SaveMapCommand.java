package ca.concordia.risk.io.commands;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

import ca.concordia.risk.game.GameEngine;
import ca.concordia.risk.game.GameMap;
import ca.concordia.risk.game.*;
import ca.concordia.risk.io.views.ConsoleView;

/** Command representing <i>"savemap"</i> operation. */
public class SaveMapCommand implements Command {

	private String d_filename;

	/**
	 * Creates a new <code>SaveMapCommand</code> object.
	 * 
	 * @param p_filename filename of the map file to save the active Map into.
	 */
	public SaveMapCommand(String p_filename) {
		d_filename = p_filename;
	}

	/** Saves the active Map into the requested map file. */
	@Override
	public void execute() {
		ConsoleView l_view = GameEngine.GetView();
		l_view.display("\nExecuting savemap command with filename: " + d_filename + "\n");
		
		GameMap l_gameMap = GameEngine.GetMap();
	
		if(l_gameMap!= null) {
			String[] l_names_continents = l_gameMap.getArrayOfContinents();
			String[] l_names_countries = l_gameMap.getArrayOfCountries();
			Map<Continent, Integer> l_active_continents;
			Map<Country, Integer> l_active_countries;
		
			l_active_continents = fetchAllContinents(l_gameMap, l_names_continents);	
			l_active_countries = fetchAllCountries(l_gameMap, l_names_countries);
		}
		else {
			l_view.display("No map to be saved in the file named '" + d_filename + ".map'"); 
		}
	}

	private Map<Country, Integer> fetchAllCountries(GameMap l_gameMap, String[] l_names_countries) {
		// TODO Auto-generated method stub
		return null;
	}

	private Map<Continent, Integer> fetchAllContinents(GameMap p_gameMap, String[] p_names_continents) {
		
		Arrays.sort(p_names_continents); //sorts all the active Continents by ascending order (alphabetically) 
		Map<Continent, Integer> l_continent_map = new HashMap<>(); //Empty Map
		int[] l_id_continents = IntStream.range(1, p_names_continents.length+1).toArray(); //generates continent IDs for all continent names 	
		Continent l_continent;
		
		//this loops generates a HashMap which consist of <Continent, Integer> in alphabetical order.
		for(int i = 0; i < p_names_continents.length; i++) {
			l_continent = p_gameMap.getContinent(p_names_continents[i]);
			l_continent_map.put(l_continent, l_id_continents[i]);
		} 
		
		return l_continent_map;
		
	}

}
