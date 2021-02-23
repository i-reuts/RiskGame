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
		try {

			if (l_gameMap != null) {
				String[] l_namesContinents = l_gameMap.getArrayOfContinents();
				String[] l_namesCountries = l_gameMap.getArrayOfCountries();
				Map<Continent, Integer> l_activeContinents;
				Map<Country, Integer> l_activeCountries;

				l_activeContinents = fetchActiveContinents(l_gameMap, l_namesContinents);
				l_activeCountries = fetchActiveCountries(l_gameMap, l_namesCountries);

				writeContients(d_filename, l_activeContinents);
				writeCountriesAndBorders(l_gameMap, d_filename, l_activeCountries, l_namesContinents, l_namesCountries);
			} else {
				l_view.display("No map to be saved in the file named '" + d_filename + ".map'");

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method returns a sorted HashMap of type <Continent, Integer>.
	 * 
	 * @param p_gameMap active game map.
	 * @param p_namesContinents array consisting of all continent names.
	 * @return sorted HashMap of type <Continent, Integer>.
	 */
	public Map<Continent, Integer> fetchActiveContinents(GameMap p_gameMap, String[] p_namesContinents) {

		Arrays.sort(p_namesContinents);
		Map<Continent, Integer> l_activeContinents = new HashMap<Continent, Integer>();
		int[] l_idContinents = IntStream.range(1, p_namesContinents.length + 1).toArray();
		Continent l_continent;

		for (int i = 0; i < p_namesContinents.length; i++) {
			l_continent = p_gameMap.getContinent(p_namesContinents[i]);
			l_activeContinents.put(l_continent, l_idContinents[i]);
		}

		l_activeContinents = l_activeContinents.entrySet().stream().sorted(Entry.comparingByValue())
				.collect(Collectors.toMap(Entry::getKey, Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
		return l_activeContinents;
	}

	/**
	 * This method returns a sorted HashMap of type <Country, Integer>.
	 * 
	 * @param p_gameMap active game map.
	 * @param p_namesCountries array consisting of all country names.
	 * @return sorted HashMap of type <Country, Integer>.
	 */
	public Map<Country, Integer> fetchActiveCountries(GameMap p_gameMap, String[] p_namesCountries) {
		Arrays.sort(p_namesCountries);
		Map<Country, Integer> l_activeCountries = new HashMap<>();
		int[] l_idCountries = IntStream.range(1, p_namesCountries.length + 1).toArray();
		Country l_country;

		for (int i = 0; i < p_namesCountries.length; i++) {
			l_country = p_gameMap.getCountry(p_namesCountries[i]);
			l_activeCountries.put(l_country, l_idCountries[i]);
		}

		l_activeCountries = l_activeCountries.entrySet().stream().sorted(Entry.comparingByValue())
				.collect(Collectors.toMap(Entry::getKey, Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
		return l_activeCountries;
	}

	/**
	 * This method writes the required continent details in the request map file.
	 * 
	 * @param p_filename filename of the map file to save the active Map into.
	 * @param l_activeContinents HashMap of type <Continent, Integer>.
	 */
	public void writeContients(String p_filename, Map<Continent, Integer> l_activeContinents) {

		File l_file = new File(p_filename);
		BufferedWriter l_bf;
		Continent l_continent;

		try {
			l_bf = new BufferedWriter(new FileWriter(l_file));
			l_bf.write("[continents]");
			l_bf.newLine();

			for (Entry<Continent, Integer> entry : l_activeContinents.entrySet()) {
				l_continent = entry.getKey();
				l_bf.write(l_continent.getName() + " " + l_continent.getValue());
				l_bf.newLine();
			}
			l_bf.flush();
			l_bf.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method writes the required country details and borders in the request map file.
	 * 
	 * @param p_gameMap active game map.
	 * @param p_filename filename of the map file to save the active Map into.
	 * @param p_activeCountries HashMap of type <Country, Integer>.
	 * @param p_namesContinent array consisting of all continent names.
	 * @param p_namesCountries array consisting of all country names.
	 */
	public void writeCountriesAndBorders(GameMap p_gameMap, String p_filename, Map<Country, Integer> p_activeCountries,
			String[] p_namesContinent, String[] p_namesCountries) {

		Arrays.sort(p_namesContinent);
		Arrays.sort(p_namesCountries);
		int[] l_idParent = IntStream.range(1, p_namesContinent.length + 1).toArray();
		int[] l_idCountries = IntStream.range(1, p_namesCountries.length + 1).toArray();
		Map<String, Integer> l_continentMap = new HashMap<>();
		Map<String, Integer> l_countryMap = new HashMap<>();
		Set<Country> l_neighbors;

		for (int i = 0; i < p_namesContinent.length; i++)
			l_continentMap.put(p_namesContinent[i], l_idParent[i]);

		for (int i = 0; i < p_namesCountries.length; i++)
			l_countryMap.put(p_namesCountries[i], l_idCountries[i]);

		l_continentMap = l_continentMap.entrySet().stream().sorted(Entry.comparingByValue())
				.collect(Collectors.toMap(Entry::getKey, Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

		l_countryMap = l_countryMap.entrySet().stream().sorted(Entry.comparingByValue())
				.collect(Collectors.toMap(Entry::getKey, Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

		File l_file = new File(p_filename);
		BufferedWriter l_bf;
		Country l_country;
		Continent l_continent;

		try {
			l_bf = new BufferedWriter(new FileWriter(l_file, true));
			l_bf.newLine();
			l_bf.write("[countries]");
			l_bf.newLine();

			for (Entry<Country, Integer> entry : p_activeCountries.entrySet()) {
				l_country = entry.getKey();
				l_continent = l_country.getContinent();
				l_bf.write(
						entry.getValue() + " " + l_country.getName() + " " + l_continentMap.get(l_continent.getName()));
				l_bf.newLine();
			}
			l_bf.flush();
			l_bf.newLine();
			l_bf.write("[borders]");
			l_bf.newLine();

			for (Entry<Country, Integer> entry : p_activeCountries.entrySet()) {
				l_country = entry.getKey();
				l_neighbors = l_country.getNeighbors();
				l_bf.write(entry.getValue() + " ");

				for (Country c : l_neighbors)
					l_bf.write(l_countryMap.get(c.getName()) + " ");

				l_bf.newLine();
			}
			l_bf.flush();
			l_bf.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}