package ca.concordia.risk.game;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * This class reads the existing maps.
 * 
 * @author Shubham Vashisth
 */
public class MapLoader {
	
	/**
	 * This method loads the (.map) file by calling appropriate functions for reading the continents, countries and borders.
	 * 
	 * @param p_fileName location of the (.map) file
	 * @return game map
	 * @throws Exception
	 */
	public ca.concordia.risk.game.Map loadMap(String p_fileName) throws Exception {
		File l_mapFile = new File(p_fileName);
		Scanner l_sc = new Scanner(l_mapFile);

		seekToTag("[continents]", l_sc);
		Map<Integer, Continent> l_continentMap = readContinents(l_sc);
			
		seekToTag("[countries]", l_sc);
		Map<Integer, Country> l_countryMap = readCountries(l_sc, l_continentMap);
		
		seekToTag("[borders]", l_sc);
		readBorders(l_sc, l_countryMap);
			
		// TODO Delete the check after parser is completed
		displayDebugOutput(l_continentMap, l_countryMap);
	
		return createMap(l_continentMap, l_countryMap);
	}
	
	
	/**
	 * Skips through the file until it finds a line that starts with <code>p_tag</code>.
	 * <p>
	 * Scanner <code>p_sc</code> will point to the line after the line with the tag
	 * after the method returns.
	 * 
	 * @param p_tag tag to look for.
	 * @param p_sc  scanner to use.
	 * @throws Exception thrown if end of file is reached before encountering the tag.	
	 */
	private void seekToTag(String p_tag, Scanner p_sc) throws Exception {
		while(p_sc.hasNextLine()) {
			String l_line = p_sc.nextLine().trim();
			if(l_line.startsWith(p_tag)) {
				return;
			}
		}
		
		throw new FileParsingException(p_tag + " tag not found"); 
	}
	
	/**
	 * This method parses Continent names and their corresponding reinforcement value from the .map file.
	 * 
	 * @param p_sc Scanner object.
	 * @return HashMap with Continent ID mapped to its corresponding Continent object.
	 */
	private Map<Integer, Continent> readContinents(Scanner p_sc) {
		Map<Integer, Continent> l_continentMap = new HashMap<Integer, Continent>();
		
		int l_runningID = 1;
		while(p_sc.hasNextLine()) {
			String l_line = p_sc.nextLine();
			
			// Stop reading when we come across an empty line
			if(l_line.isBlank()) {
				break;
			}
			
			String[] l_tokens = l_line.split("\\s+");
			String l_continentName = l_tokens[0].replace('_', ' ');
			int l_continentValue = Integer.parseInt(l_tokens[1]);
			
			l_continentMap.put(l_runningID, new Continent(l_continentName, l_continentValue));
			l_runningID++;
		}
		
		return l_continentMap;
	}
	
	/**
	 * This method parses Country names & IDs and maps them to their respective Continent. 
	 * 
	 * @param p_sc Scanner object.
	 * @param p_continentMap HashMap with Continent ID mapped to its corresponding Continent object.
	 * @return HashMap with Country ID mapped to its corresponding Country object. 
	 */
	private Map<Integer, Country> readCountries(Scanner p_sc, Map<Integer, Continent> p_continentMap) {
		Map<Integer, Country> l_countryMap = new HashMap<Integer, Country>();
		
		while(p_sc.hasNextLine()) {
			String l_line = p_sc.nextLine();
			
			// Stop reading when we come across an empty line
			if(l_line.isBlank()) {
				break;
			}
			
			// Parse country data
			String[] l_tokens = l_line.split("\\s+");
			int l_countryID = Integer.parseInt(l_tokens[0]);
			String l_countryName = l_tokens[1].replace('_', ' ');
			int l_continentID = Integer.parseInt(l_tokens[2]);
			
			// Create country
			Country l_country = new Country(l_countryName);
			
			// Add continent to country and country to continent
			Continent l_continent = p_continentMap.get(l_continentID);
			l_country.setParent(l_continent);
			l_continent.addCountry(l_country);
			
			// Add country to country map
			l_countryMap.put(l_countryID, l_country);
		}
		
		return l_countryMap;
	}

	/**
	 * This method parses the borders and adds the neighbors for each country.
	 * 
	 * @param p_sc Scanner object.
	 * @param p_countryMap HashMap with Country ID mapped to its corresponding Country object. 
	 */
	private void readBorders(Scanner p_sc, Map<Integer, Country> p_countryMap) {
		while(p_sc.hasNextLine()) {
			String l_line = p_sc.nextLine();
			
			// Stop reading when we come across an empty line
			if(l_line.isBlank()) {
				break;
			}
			
			// Parse country data
			String[] l_tokens = l_line.split("\\s+");
			int l_countryID = Integer.parseInt(l_tokens[0]);
			for(int i = 1; i < l_tokens.length; i++) {
				// Add neighbors to country
				int l_neighborID = Integer.parseInt(l_tokens[i]);
				p_countryMap.get(l_countryID).addNeighbor(p_countryMap.get(l_neighborID));
			}
		}
	}
	
	/**
	 * This method creates the Risk game map. 
	 * 
	 * @param p_continentMap HashMap with Continent ID mapped to its corresponding Continent object. 
	 * @param p_countryMap HashMap with Country ID mapped to its corresponding Country object.
	 * @return game map.
	 */
	private ca.concordia.risk.game.Map createMap(Map<Integer, Continent> p_continentMap, Map<Integer, Country> p_countryMap) {
		ca.concordia.risk.game.Map l_gameMap = new ca.concordia.risk.game.Map();
		
		for(Continent c : p_continentMap.values()) {
			l_gameMap.addContinent(c);
		}
		for(Country c : p_countryMap.values()) {
			l_gameMap.addCountry(c);
		}
		
		return l_gameMap;
	}
	
	/**
	 * This methods prints the game map on the console.
	 * 
	 * @param p_continentMap HashMap with Continent ID mapped to its corresponding Continent object.
	 * @param p_countryMap HashMap with Country ID mapped to its corresponding Country object.
	 */
	private void displayDebugOutput(Map<Integer, Continent> p_continentMap, Map<Integer, Country> p_countryMap) {
		System.out.printf("\n%-15s %s\n" , "Continent", "Countries");
		for(Continent l_c : p_continentMap.values()) {
			System.out.printf("%-15s ", l_c.getName());
			List<Country> l_countries = l_c.getCountries();
			for(int l_i = 0; l_i < l_countries.size(); l_i++) {
				System.out.print(l_countries.get(l_i).getName());
				if(l_i < l_countries.size() - 1) {
					System.out.print(", ");
				}
			}
			System.out.println();
		}
		System.out.printf("\n%-15s %s\n", "Country", "Neighbors");
		for(Country c : p_countryMap.values()) {
			System.out.printf("%-15s ", c.getName());
			for(Country n : c.getNeighbors()) {
				System.out.print(n.getName() + " ");
			}
			System.out.println();
		}	
	}
	
	/**
	 * This class handles exception while parsing the .map file.
	 * 
	 * @author Shubham Vashisth
	 */
	public static class FileParsingException extends Exception {
		
		/**
		 * This constructor calls the constructor of the Exception class and sets a custom file exception message.
		 * 
		 * @param p_message contains the custom file exception message.
		 */
		public FileParsingException(String p_message) {
			super("Invalid map file: " + p_message);
		}
	}
	
	public static void main(String[] args) {
		MapLoader obj = new MapLoader();
		
		try {
			obj.loadMap("testMapFile.map");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}