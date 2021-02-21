package ca.concordia.risk.services;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import ca.concordia.risk.game.Continent;
import ca.concordia.risk.game.Country;
import ca.concordia.risk.game.GameMap;

/**
 * This class reads the existing maps.
 * 
 * @author Shubham Vashisth
 */
public class MapLoader {

	/**
	 * This method loads the (.map) file by calling appropriate functions for
	 * reading the continents, countries and borders.
	 * 
	 * @param p_fileName location of the (.map) file
	 * @return game map
	 * @throws FileParsingException  thrown if the parsing error occurs when parsing
	 *                               the map file.
	 * @throws FileNotFoundException thrown if the map file with the requested file
	 *                               name does not exist.
	 */
	public static GameMap LoadMap(String p_fileName) throws FileParsingException, FileNotFoundException {
		InputStream l_fileStream = MapLoader.class.getClassLoader().getResourceAsStream("maps/" + p_fileName);
		if(l_fileStream == null) {
			throw new FileNotFoundException(p_fileName + " not found in the maps folder");
		}

		// Use ISO-8859-1 encoding, since most map files are encoded with it.
		// UTF encoded map files are theoretically possible, however we haven't found any in practice.
		// If required, encoding detection can be added later on.
		Scanner l_sc = new Scanner(l_fileStream, "ISO-8859-1");

		SeekToTag("[continents]", l_sc);
		Map<Integer, Continent> l_continentMap = ReadContinents(l_sc);

		SeekToTag("[countries]", l_sc);
		Map<Integer, Country> l_countryMap = ReadCountries(l_sc, l_continentMap);

		SeekToTag("[borders]", l_sc);
		ReadBorders(l_sc, l_countryMap);

		return CreateMap(l_continentMap, l_countryMap);
	}

	/**
	 * Skips through the file until it finds a line that starts with
	 * <code>p_tag</code>.
	 * <p>
	 * Scanner <code>p_sc</code> will point to the line after the line with the tag
	 * after the method returns.
	 * 
	 * @param p_tag tag to look for.
	 * @param p_sc  scanner to use.
	 * @throws FileParsingException thrown if end of file is reached before
	 *                              encountering the tag.
	 */
	private static void SeekToTag(String p_tag, Scanner p_sc) throws FileParsingException {
		while (p_sc.hasNextLine()) {
			String l_line = p_sc.nextLine().trim();
			if (l_line.startsWith(p_tag)) {
				return;
			}
		}

		throw new FileParsingException(p_tag + " tag not found");
	}

	/**
	 * This method parses Continent names and their corresponding reinforcement
	 * value from the .map file.
	 * 
	 * @param p_sc Scanner object.
	 * @return HashMap with Continent ID mapped to its corresponding Continent
	 *         object.
	 */
	private static Map<Integer, Continent> ReadContinents(Scanner p_sc) {
		Map<Integer, Continent> l_continentMap = new HashMap<Integer, Continent>();

		int l_runningID = 1;
		while (p_sc.hasNextLine()) {
			String l_line = p_sc.nextLine().trim();

			// Stop reading when we come across an empty line
			if (l_line.isBlank()) {
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
	 * This method parses Country names and IDs and maps them to their respective
	 * Continent.
	 * 
	 * @param p_sc           Scanner object.
	 * @param p_continentMap HashMap with Continent ID mapped to its corresponding
	 *                       Continent object.
	 * @return HashMap with Country ID mapped to its corresponding Country object.
	 */
	private static Map<Integer, Country> ReadCountries(Scanner p_sc, Map<Integer, Continent> p_continentMap) {
		Map<Integer, Country> l_countryMap = new HashMap<Integer, Country>();

		while (p_sc.hasNextLine()) {
			String l_line = p_sc.nextLine().trim();

			// Stop reading when we come across an empty line
			if (l_line.isBlank()) {
				break;
			}
			// Skip comments
			if (l_line.startsWith(";")) {
				continue;
			}

			// Parse country data
			String[] l_tokens = l_line.split("\\s+");
			int l_countryID = Integer.parseInt(l_tokens[0]);
			String l_countryName = l_tokens[1].replace('_', ' ');
			int l_continentID = Integer.parseInt(l_tokens[2]);

			// Get country continent
			Continent l_continent = p_continentMap.get(l_continentID);		
			// Create country
			Country l_country = new Country(l_countryName, l_continent);
			// Add country to continent
			l_continent.addCountry(l_country);

			// Add country to country map
			l_countryMap.put(l_countryID, l_country);
		}

		return l_countryMap;
	}

	/**
	 * This method parses the borders and adds the neighbors for each country.
	 * 
	 * @param p_sc         Scanner object.
	 * @param p_countryMap HashMap with Country ID mapped to its corresponding
	 *                     Country object.
	 */
	private static void ReadBorders(Scanner p_sc, Map<Integer, Country> p_countryMap) {
		while (p_sc.hasNextLine()) {
			String l_line = p_sc.nextLine().trim();

			// Stop reading when we come across an empty line
			if (l_line.isBlank()) {
				break;
			}
			// Skip comments
			if (l_line.startsWith(";")) {
				continue;
			}

			// Parse country data
			String[] l_tokens = l_line.split("\\s+");
			int l_countryID = Integer.parseInt(l_tokens[0]);
			for (int i = 1; i < l_tokens.length; i++) {
				// Add neighbors to country
				int l_neighborID = Integer.parseInt(l_tokens[i]);
				p_countryMap.get(l_countryID).addNeighbor(p_countryMap.get(l_neighborID));
			}
		}
	}

	/**
	 * This method creates the Risk game map.
	 * 
	 * @param p_continentMap HashMap with Continent ID mapped to its corresponding
	 *                       Continent object.
	 * @param p_countryMap   HashMap with Country ID mapped to its corresponding
	 *                       Country object.
	 * @return game map.
	 */
	private static GameMap CreateMap(Map<Integer, Continent> p_continentMap, Map<Integer, Country> p_countryMap) {
		GameMap l_gameMap = new GameMap();

		for (Continent c : p_continentMap.values()) {
			l_gameMap.addContinent(c);
		}
		for (Country c : p_countryMap.values()) {
			l_gameMap.addCountry(c);
		}

		return l_gameMap;
	}

	/**
	 * This class handles exceptions while parsing the .map file.
	 * 
	 * @author Shubham Vashisth
	 */
	@SuppressWarnings("serial")
	public static class FileParsingException extends Exception {

		/**
		 * This constructor calls the constructor of the Exception class and sets a
		 * custom file exception message.
		 * 
		 * @param p_message contains the custom file exception message.
		 */
		public FileParsingException(String p_message) {
			super("Invalid map file: " + p_message);
		}
	}

}