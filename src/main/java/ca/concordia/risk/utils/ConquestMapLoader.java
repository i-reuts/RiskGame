package ca.concordia.risk.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import ca.concordia.risk.game.Continent;
import ca.concordia.risk.game.Country;
import ca.concordia.risk.game.GameMap;
import ca.concordia.risk.utils.MapLoader.FileParsingException;

/**
 * This class implements Conquest Map Loader
 * 
 * @author Sindu
 */
public class ConquestMapLoader {

	/**
	 * Loads a map file.
	 * <p>
	 * Reads a (.map) file by calling appropriate functions for reading the
	 * continents, countries and borders.
	 * 
	 * @param p_fileName filename of the (.map) file
	 * @return game map loaded from the file.
	 * @throws FileParsingException  thrown if the parsing error occurs when parsing
	 *                               the map file.
	 * @throws FileNotFoundException thrown if the map file with the requested file
	 *                               name does not exist.
	 */
	public GameMap loadMap(String p_fileName) throws FileParsingException, FileNotFoundException {
		File l_file = new File(MapLoader.d_MapFolder + p_fileName);
		if (!l_file.exists()) {
			throw new FileNotFoundException(p_fileName + " not found in the maps folder");
		}

		// Use ISO-8859-1 encoding, since most map files are encoded with it.
		// UTF encoded map files are theoretically possible, however we haven't found
		// any in practice.
		// If required, encoding detection can be added later on.
		Scanner l_sc = new Scanner(l_file, MapLoader.d_Encoding);

		Map<Integer, Continent> l_continentMap;
		Map<String, Country> l_countryMap;

		seekToTag("[Continents]", l_sc);
		l_continentMap = readContinents(l_sc);

		seekToTag("[Territories]", l_sc);
		l_countryMap = readCountries(l_sc, l_continentMap);

		l_sc.close();
		l_sc = new Scanner(l_file, MapLoader.d_Encoding);

		seekToTag("[Territories]", l_sc);
		readBorders(l_sc, l_countryMap);
		l_sc.close();

		return createMap(l_continentMap, l_countryMap);
	}

	/**
	 * Saves the map to a file.
	 * 
	 * @param p_fileName name of the file to save the map into.
	 * @param p_map      map object to save.
	 * @throws IOException exception thrown if an error occurs while writing the map
	 *                     to file.
	 */
	public void saveMap(String p_fileName, GameMap p_map) throws IOException {
		List<Continent> l_continentList = p_map.getContinents();
		List<Country> l_countryList = p_map.getCountries();

		Map<Continent, Integer> l_continentToIdMap = generateContinentIds(l_continentList);

		File l_file = new File(MapLoader.d_MapFolder + p_fileName);
		try (BufferedWriter l_writer = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(l_file), MapLoader.d_Encoding))) {
			l_writer.write("[Map]\n\n");
			writeContinents(l_writer, l_continentList);
			writeCountries(l_writer, l_countryList, l_continentToIdMap);
		} catch (IOException l_e) {
			// In case if we have a partially written file, delete it
			if (l_file.exists()) {
				l_file.delete();
			}
			throw l_e;
		}
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
	private void seekToTag(String p_tag, Scanner p_sc) throws FileParsingException {
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
	 * @throws FileParsingException thrown if an invalid line is encountered.
	 */
	private Map<Integer, Continent> readContinents(Scanner p_sc) throws FileParsingException {
		Map<Integer, Continent> l_continentMap = new HashMap<Integer, Continent>();

		int l_runningId = 1;
		while (p_sc.hasNextLine()) {
			String l_line = p_sc.nextLine().trim();

			// Stop reading when we come across an empty line
			if (l_line.isBlank()) {
				break;
			}
			try {
				String l_continentName;
				int l_continentValue;
				String[] l_tokens = l_line.split("=");
				l_continentName = l_tokens[0];
				l_continentValue = Integer.parseInt(l_tokens[1]);

				l_continentMap.put(l_runningId, new Continent(l_continentName, l_continentValue));
				l_runningId++;
			} catch (Exception l_e) {
				throw new FileParsingException("error when parsing - invalid line format \"" + l_line + "\"");
			}
		}

		return l_continentMap;
	}

	/**
	 * This method parses Territory names and IDs and maps them to their respective
	 * Continent.
	 * 
	 * @param p_sc           Scanner object.
	 * @param p_continentMap HashMap with Continent ID mapped to its corresponding
	 *                       Continent object.
	 * @return HashMap with Country name mapped to its corresponding Country object.
	 * @throws FileParsingException thrown if an invalid line is encountered.
	 */
	private Map<String, Country> readCountries(Scanner p_sc, Map<Integer, Continent> p_continentMap)
			throws FileParsingException {
		Map<String, Country> l_countryMap = new HashMap<String, Country>();

		int l_continentId = 0;
		String l_continentName = "";
		while (p_sc.hasNextLine()) {
			String l_line = p_sc.nextLine().trim();
			// Go to next line when we come across an empty line
			if (l_line.isBlank()) {
				continue;
			}
			try {
				String[] l_tokens = l_line.split(",");
				String l_countryName = l_tokens[0];
				String tmp_continentName = l_tokens[3];

				if (!l_continentName.equalsIgnoreCase(tmp_continentName)) {
					l_continentId++;
					l_continentName = tmp_continentName;
				}

				// Get country continent
				Continent l_continent = p_continentMap.get(l_continentId);

				// Create country
				Country l_country = new Country(l_countryName, l_continent);
				// Add country to continent
				l_continent.addCountry(l_country);

				// Add country to country map
				l_countryMap.put(l_countryName, l_country);
			} catch (Exception l_e) {
				throw new FileParsingException("error when parsing - invalid line format \"" + l_line + "\"");
			}
		}

		return l_countryMap;
	}

	/**
	 * This method parses the borders and adds the neighbors for each country.
	 * 
	 * @param p_sc         Scanner object.
	 * @param p_countryMap HashMap with Country name mapped to its corresponding
	 *                     Country object.
	 * @throws FileParsingException thrown if an invalid line is encountered.
	 */

	private void readBorders(Scanner p_sc, Map<String, Country> p_countryMap) throws FileParsingException {
		while (p_sc.hasNextLine()) {
			String l_line = p_sc.nextLine().trim();
			if (l_line.isBlank()) {
				continue;
			}
			try {
				String[] l_tokens = l_line.split(",");
				String l_countryName = l_tokens[0];
				for (int l_i = 4; l_i < l_tokens.length; l_i++) {
					// Add neighbors to country
					String l_neighborName = l_tokens[l_i].trim();
					Country neighbor = p_countryMap.get(l_neighborName);
					p_countryMap.get(l_countryName).addNeighbor(neighbor);
				}
			} catch (Exception l_e) {
				throw new FileParsingException("error when parsing - invalid line format \"" + l_line + "\"");
			}
		}
	}

	/**
	 * This method creates the Risk game map.
	 * 
	 * @param p_continentMap HashMap with Continent ID mapped to its corresponding
	 *                       Continent object.
	 * @param p_countryMap   HashMap with Country name mapped to its corresponding
	 *                       Country object.
	 * @return game map.
	 */
	private GameMap createMap(Map<Integer, Continent> p_continentMap, Map<String, Country> p_countryMap) {
		GameMap l_gameMap = new GameMap();

		for (Continent l_c : p_continentMap.values()) {
			l_gameMap.addContinent(l_c);
		}
		for (Country l_c : p_countryMap.values()) {
			l_gameMap.addCountry(l_c);
		}

		return l_gameMap;
	}

	/**
	 * This method builds and returns a map of continents mapped to generated
	 * continent ID.
	 * 
	 * @param p_continentList list of all continents in the map.
	 * @return map of Continent to generated Continent ID.
	 */
	private Map<Continent, Integer> generateContinentIds(List<Continent> p_continentList) {
		Map<Continent, Integer> l_continentToIdMap = new HashMap<Continent, Integer>();

		int l_continentId = 1;
		for (Continent l_continet : p_continentList) {
			l_continentToIdMap.put(l_continet, l_continentId);
			l_continentId++;
		}

		return l_continentToIdMap;
	}

	/**
	 * This method writes the required continent details to the map file.
	 * 
	 * @param p_writer        buffered writer used to write to the file.
	 * @param p_continentList list of all continents.
	 * @throws IOException exception thrown if an error occurs while writing the map
	 *                     to file.
	 */
	private void writeContinents(BufferedWriter p_writer, List<Continent> p_continentList) throws IOException {
		p_writer.write("[Continents]");
		p_writer.newLine();

		for (Continent l_continent : p_continentList) {
			p_writer.write(l_continent.getName() + "=" + l_continent.getValue());
			p_writer.newLine();
		}
	}

	/**
	 * This method writes the required territory details to the map file.
	 * 
	 * @param p_writer           buffered writer used to write to the file.
	 * @param p_countryList      list of all countries.
	 * @param p_continentToIdMap map of continents mapped to generated continent
	 *                           IDs.
	 * @throws IOException exception thrown if an error occurs while writing the map
	 *                     to file.
	 */
	private void writeCountries(BufferedWriter p_writer, List<Country> p_countryList,
			Map<Continent, Integer> p_continentToIdMap) throws IOException {
		// Write the tag
		p_writer.newLine();
		p_writer.write("[Territories]");
		p_writer.newLine();

		// Sort the country list by continent before saving
		Collections.sort(p_countryList, (c1, c2) -> c1.getContinent().getName().compareTo(c2.getContinent().getName()));

		String l_prevContinentName = "";
		for (int l_i = 0; l_i < p_countryList.size(); l_i++) {
			Country l_country = p_countryList.get(l_i);
			Continent l_continent = l_country.getContinent();

			// If continent block is finished, add an extra new line
			if (!l_continent.getName().equals(l_prevContinentName)) {
				l_prevContinentName = l_continent.getName();
				p_writer.newLine();
			}

			// Write country and continent names
			p_writer.write(l_country.getName() + ",,," + l_continent.getName());

			// Write the neighbors
			for (Country l_c : l_country.getNeighbors()) {
				p_writer.write("," + l_c.getName());
			}
			p_writer.newLine();
		}
		p_writer.newLine();
	}
}
