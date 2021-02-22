package ca.concordia.risk.io.commands;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import ca.concordia.risk.game.*;
import ca.concordia.risk.services.MapLoader;
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
		//ConsoleView l_view = GameEngine.GetView();
		//l_view.display("\nExecuting savemap command with filename: " + d_filename + "\n");
		
		//GameMap l_gameMap = GameEngine.GetMap();
		try {
			GameMap l_gameMap = MapLoader.LoadMap("testMap.map");
	
			if(l_gameMap!= null) {
				String[] l_names_continents = l_gameMap.getArrayOfContinents();
				String[] l_names_countries = l_gameMap.getArrayOfCountries();
				Map<Continent, Integer> l_active_continents;
				Map<Country, Integer> l_active_countries;
		
				l_active_continents = fetchActiveContinents(l_gameMap, l_names_continents);	
				l_active_countries = fetchActiveCountries(l_gameMap, l_names_countries);
			
				writeContients(d_filename, l_active_continents);
				writeCountriesAndBorders(l_gameMap, d_filename , l_active_countries, l_names_continents, l_names_countries);
			}
			else {
				//l_view.display("No map to be saved in the file named '" + d_filename + ".map'");
				System.out.println("No map to be saved in the file named '" + d_filename + "'");
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}



	public Map<Continent, Integer> fetchActiveContinents(GameMap p_gameMap, String[] p_names_continents) {
		
		Arrays.sort(p_names_continents); //sorts all the active Continents by ascending order (alphabetically) 
		Map<Continent, Integer> l_active_continents = new HashMap<Continent, Integer>(); //Empty Map
		int[] l_id_continents = IntStream.range(1, p_names_continents.length+1).toArray(); //generates continent IDs for all continent names 	
		Continent l_continent;
				
		//this loops generates a HashMap which consist of <Continent, Integer> in alphabetical order.
		for(int i = 0; i < p_names_continents.length; i++) {
			l_continent = p_gameMap.getContinent(p_names_continents[i]);
			l_active_continents.put(l_continent, l_id_continents[i]);
		} 
		
		l_active_continents = l_active_continents.entrySet().stream()
			    .sorted(Entry.comparingByValue())
			    .collect(Collectors.toMap(Entry::getKey, Entry::getValue,
			                              (e1, e2) -> e1, LinkedHashMap::new));
		return l_active_continents;
	}
	
	public Map<Country, Integer> fetchActiveCountries(GameMap p_gameMap, String[] p_names_countries) {
		Arrays.sort(p_names_countries);
		Map<Country, Integer> l_active_countries = new HashMap<>();
		int[] l_id_countries = IntStream.range(1, p_names_countries.length+1).toArray();
		Country l_country;
			
		for(int i = 0; i < p_names_countries.length; i++) {
			l_country = p_gameMap.getCountry(p_names_countries[i]);
			l_active_countries.put(l_country, l_id_countries[i]);
		}

		l_active_countries = l_active_countries.entrySet().stream()
			    .sorted(Entry.comparingByValue())
			    .collect(Collectors.toMap(Entry::getKey, Entry::getValue,
			                              (e1, e2) -> e1, LinkedHashMap::new));
		return l_active_countries;
	}
	
	public void writeContients(String p_fileName, Map<Continent, Integer> l_active_continents) {
		
		File file = new File(p_fileName);
		BufferedWriter bf;
		Continent l_continent;
		
		try {
			bf = new BufferedWriter(new FileWriter(file));
			bf.write("[continents]");		
			bf.newLine();
			
			for(Entry<Continent, Integer> entry : l_active_continents.entrySet()) {
				l_continent = entry.getKey();
				bf.write(l_continent.getName() + " " + l_continent.getValue());
				bf.newLine();	
			}
			 bf.flush();
			 bf.close();
        } 
        catch (IOException e) { 
            e.printStackTrace(); 
        } 
	}
	
	public void writeCountriesAndBorders(GameMap p_gameMap, String p_fileName, 
			Map<Country, Integer> p_active_countries, String[] p_names_continent, String[] p_names_countries) {
		
		Arrays.sort(p_names_continent);
		Arrays.sort(p_names_countries);
		int[] l_id_parent = IntStream.range(1, p_names_continent.length+1).toArray();
		int[] l_id_countries = IntStream.range(1, p_names_countries.length+1).toArray();
		Map<String, Integer> l_continent_map = new HashMap<>();
		Map<String, Integer> l_country_map = new HashMap<>();
		Set<Country> l_neighbors;
		
		for(int i = 0; i < p_names_continent.length; i++)
			l_continent_map.put(p_names_continent[i], l_id_parent[i]);
		
		for(int i = 0; i < p_names_countries.length; i++)
			l_country_map.put(p_names_countries[i], l_id_countries[i]);
		
		l_continent_map = l_continent_map.entrySet().stream()
			    .sorted(Entry.comparingByValue())
			    .collect(Collectors.toMap(Entry::getKey, Entry::getValue,
			                              (e1, e2) -> e1, LinkedHashMap::new));
		
		l_country_map = l_country_map.entrySet().stream()
			    .sorted(Entry.comparingByValue())
			    .collect(Collectors.toMap(Entry::getKey, Entry::getValue,
			                              (e1, e2) -> e1, LinkedHashMap::new));
	
		File file = new File(p_fileName);
		BufferedWriter bf;
		Country l_country;
		Continent l_continent;
		
		try {
			bf = new BufferedWriter(new FileWriter(file, true));
			bf.newLine();
			bf.write("[countries]");		
			bf.newLine();
			
			for(Entry<Country, Integer> entry : p_active_countries.entrySet()) {
				l_country= entry.getKey();
				l_continent = l_country.getContinent();
				bf.write(entry.getValue() + " " + l_country.getName() + " " + l_continent_map.get(l_continent.getName()) );
				bf.newLine();	
			}
			 bf.flush();
			 
			 bf.newLine();
			 bf.write("[borders]");
			 bf.newLine();
			 
			for(Entry<Country, Integer> entry : p_active_countries.entrySet()) {
				l_country = entry.getKey();
				l_neighbors = l_country.getNeighbors();
				
				bf.write(entry.getValue() + " ");
				
				for (Country c : l_neighbors) 
					bf.write(l_country_map.get(c.getName()) + " ");
				
				bf.newLine();
			}
			bf.flush();
			bf.close();
        } 
        catch (IOException e) { 
            e.printStackTrace(); 
        } 
		
	}
	
	
	public static void main(String[] args) {
		SaveMapCommand obj = new SaveMapCommand("checkMap.map");
		obj.execute();
	}
}
