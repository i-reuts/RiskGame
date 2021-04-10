package ca.concordia.risk.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import ca.concordia.risk.game.Continent;
import ca.concordia.risk.game.Country;
import ca.concordia.risk.game.GameMap;

/**
 * This class provides functionality to load the map from and save the map to
 * <i>.map</i> files.
 * 
 * @author Sindu
 */

public class DominationMapLoader {
	
	private static final String d_MapFolder = "./maps/";
	private static final String d_Encoding = "ISO-8859-1";

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
	public static GameMap LoadMap(String p_fileName) throws FileParsingException, FileNotFoundException {
		File l_file = new File(d_MapFolder + p_fileName);
		if (!l_file.exists()) {
			throw new FileNotFoundException(p_fileName + " not found in the maps folder");
		}

		// Use ISO-8859-1 encoding, since most map files are encoded with it.
		// UTF encoded map files are theoretically possible, however we haven't found
		// any in practice.
		// If required, encoding detection can be added later on.
		Scanner l_sc = new Scanner(l_file, d_Encoding);

		Map<Integer, Continent> l_continentMap;
		Map<Integer, Country> l_countryMap;

        SeekToTag("[continents]", l_sc);
        l_continentMap = ReadContinents(l_sc);

        SeekToTag("[countries]", l_sc);
        l_countryMap = ReadCountries(l_sc, l_continentMap);

        SeekToTag("[borders]", l_sc);
        ReadBorders(l_sc, l_countryMap);

		return CreateMap(l_continentMap, l_countryMap);
	}

	/**
	 * Saves the map to a file.
	 * 
	 * @param p_fileName name of the file to save the map into.
	 * @param p_map      map object to save.
	 * @throws IOException exception thrown if an error occurs while writing the map
	 *                     to file.
	 */
	public static void SaveMap(String p_fileName, GameMap p_map) throws IOException {
		List<Continent> l_continentList = p_map.getContinents();
		List<Country> l_countryList = p_map.getCountries();

		Map<Continent, Integer> l_continentToIdMap = GenerateContinentIds(l_continentList);
		Map<Country, Integer> l_countryToIdMap = GenerateCountryIds(l_countryList);

		File l_file = new File(d_MapFolder + p_fileName);
		try (BufferedWriter l_writer = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(l_file), d_Encoding))) {
			WriteContinents(l_writer, l_continentList);
            WriteCountries(l_writer, l_countryList, l_continentToIdMap);
            WriteBorders(l_writer, l_countryList, l_countryToIdMap);
		} catch (IOException l_e) {
			// In case if we have a partially written file, delete it
			if (l_file.exists()) {
				l_file.delete();
			}
			throw l_e;
		}
	}

	/**
	 * Gets the territory id.
	 * 
	 * @return id int.
	 */

	/**
	 * Gets the map folder location.
	 * 
	 * @return map folder path.
	 */
	public static String getMapFolderPath() {
		return d_MapFolder;
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
	 * @throws FileParsingException thrown if an invalid line is encountered.
	 */
	private static Map<Integer, Continent> ReadContinents(Scanner p_sc) throws FileParsingException {
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
                
                String[] l_tokens = l_line.split("\\s+");
                l_continentName = l_tokens[0].replace('_', ' ');
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
	 * This method parses Country names and IDs and maps them to their respective
	 * Continent.
	 * 
	 * @param p_sc           Scanner object.
	 * @param p_continentMap HashMap with Continent ID mapped to its corresponding
	 *                       Continent object.
	 * @return HashMap with Country ID mapped to its corresponding Country object.
	 * @throws FileParsingException thrown if an invalid line is encountered.
	 */
	private static Map<Integer, Country> ReadCountries(Scanner p_sc, Map<Integer, Continent> p_continentMap)
			throws FileParsingException {
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

			try {
				String[] l_tokens = l_line.split("\\s+");
				int l_countryId = Integer.parseInt(l_tokens[0]);
				String l_countryName = l_tokens[1].replace('_', ' ');
				int l_continentId = Integer.parseInt(l_tokens[2]);

				// Get country continent
				Continent l_continent = p_continentMap.get(l_continentId);

				// Create country
				Country l_country = new Country(l_countryName, l_continent);
				// Add country to continent
				l_continent.addCountry(l_country);

				// Add country to country map
				l_countryMap.put(l_countryId, l_country);
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
	 * @param p_countryMap HashMap with Country ID mapped to its corresponding
	 *                     Country object.
	 * @throws FileParsingException thrown if an invalid line is encountered.
	 */
	private static void ReadBorders(Scanner p_sc, Map<Integer, Country> p_countryMap) throws FileParsingException {
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

			try {				
				String[] l_tokens = l_line.split("\\s+");
				int l_countryId = Integer.parseInt(l_tokens[0]);
				for (int l_i = 1; l_i < l_tokens.length; l_i++) {
					// Add neighbors to country
					int l_neighborId = Integer.parseInt(l_tokens[l_i]);
					p_countryMap.get(l_countryId).addNeighbor(p_countryMap.get(l_neighborId));
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
	 * @param p_countryMap   HashMap with Country ID mapped to its corresponding
	 *                       Country object.
	 * @return game map.
	 */
	private static GameMap CreateMap(Map<Integer, Continent> p_continentMap, Map<Integer, Country> p_countryMap) {
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
	private static Map<Continent, Integer> GenerateContinentIds(List<Continent> p_continentList) {
		Map<Continent, Integer> l_continentToIdMap = new HashMap<Continent, Integer>();

		int l_continentId = 1;
		for (Continent l_continet : p_continentList) {
			l_continentToIdMap.put(l_continet, l_continentId);
			l_continentId++;
		}

		return l_continentToIdMap;
	}

	/**
	 * This method builds and returns a map of countries mapped to generated country
	 * ID.
	 * 
	 * @param p_countryList list of all countries in the map.
	 * @return map of Country to generated Country ID.
	 */
	private static Map<Country, Integer> GenerateCountryIds(List<Country> p_countryList) {
		Map<Country, Integer> l_countryToIdMap = new HashMap<>();

		int l_countryId = 1;
		for (Country l_country : p_countryList) {
			l_countryToIdMap.put(l_country, l_countryId);
			l_countryId++;
		}

		return l_countryToIdMap;
	}

	/**
	 * This method writes the required continent details to the map file.
	 * 
	 * @param p_writer        buffered writer used to write to the file.
	 * @param p_continentList list of all continents.
	 * @throws IOException exception thrown if an error occurs while writing the map
	 *                     to file.
	 */
	private static void WriteContinents(BufferedWriter p_writer, List<Continent> p_continentList) throws IOException {
		p_writer.write("[continents]");
		p_writer.newLine();

		for (Continent l_continent : p_continentList) {
			p_writer.write(l_continent.getName().replaceAll("\\s+", "_") + " " + l_continent.getValue());
			p_writer.newLine();
		}
	}

	/**
	 * This method writes the required country details to the map file.
	 * 
	 * @param p_writer           buffered writer used to write to the file.
	 * @param p_countryList      list of all countries.
	 * @param p_continentToIdMap map of continents mapped to generated continent
	 *                           IDs.
	 * @throws IOException exception thrown if an error occurs while writing the map
	 *                     to file.
	 */
	private static void WriteCountries(BufferedWriter p_writer, List<Country> p_countryList,
			Map<Continent, Integer> p_continentToIdMap) throws IOException {
		p_writer.newLine();
		p_writer.write("[countries]");
		p_writer.newLine();

		for (int l_i = 0; l_i < p_countryList.size(); l_i++) {
			Country l_country = p_countryList.get(l_i);
			Continent l_continent = l_country.getContinent();
			p_writer.write(l_i + 1 + " " + l_country.getName().replaceAll("\\s+", "_") + " "
					+ p_continentToIdMap.get(l_continent));
			p_writer.newLine();
		}
		p_writer.newLine();
	}

	/**
	 * This method writes the required country details to the map file.
	 * 
	 * @param p_writer         buffered writer used to write to the file.
	 * @param p_countryList    list of all countries.
	 * @param p_countryToIdMap map of countries mapped to generated country IDs.
	 * @throws IOException exception thrown if an error occurs while writing the map
	 *                     to file.
	 */
	private static void WriteBorders(BufferedWriter p_writer, List<Country> p_countryList,
			Map<Country, Integer> p_countryToIdMap) throws IOException {
		p_writer.write("[borders]");
		p_writer.newLine();

		for (int l_i = 0; l_i < p_countryList.size(); l_i++) {
			Country l_country = p_countryList.get(l_i);
			p_writer.write(l_i + 1 + " ");

			Set<Country> l_neighbors = l_country.getNeighbors();
			for (Country l_c : l_neighbors) {
				p_writer.write(p_countryToIdMap.get(l_c) + " ");
			}

			p_writer.newLine();
		}
	}

	/**
	 * A custom <code>Exception</code> class thrown when a parsing error occurs
	 * while parsing the .map file.
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
