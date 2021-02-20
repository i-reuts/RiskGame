package ca.concordia.risk.game;

import java.io.File;
import java.util.HashMap;

import java.util.Map;
import java.util.Scanner;

import ca.concordia.risk.game.Continent;
import ca.concordia.risk.game.Country;

/**
 * This class reads the existing maps.
 * 
 * @author Shubham Vashisth
 */
public class MapLoader {
	
	public static void main(String[] args) {
		MapLoader obj = new MapLoader();
		
		try {
			obj.loadMap("testMapFile.map");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static class FileParsingException extends Exception {
		public FileParsingException(String p_message) {
			super("Invalid map file: " + p_message);
		}
	}
	
	
	
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
	
	private void displayDebugOutput(Map<Integer, Continent> p_continentMap, Map<Integer, Country> p_countryMap) {
		System.out.println("\nContinents: ");
		for(Continent c : p_continentMap.values()) {
			System.out.print(c.getName() + " - countries: ");
			for(Country country : c.getCountries()) {
				System.out.print(country.getName() + " ");
			}
			System.out.println();
		}
		
		System.out.println("\nCountries: ");
		for(Country c : p_countryMap.values()) {
			System.out.print(c.getName() + " - neighbors: ");
			for(Country n : c.getNeighbors()) {
				System.out.print(n.getName() + " ");
			}
			System.out.println();
		}	
	}
}