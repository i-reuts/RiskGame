package ca.concordia.risk.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import ca.concordia.risk.game.GameMap;

/**
 * This class provides functionality to load the map from and save the map to
 * <i>.map</i> files of various types.
 */
public class MapLoader {

	/** Map file root directory. */
	protected static final String d_MapFolder = "./maps/";
	/** Encoding to use when reading and writing map files. */
	protected static final String d_Encoding = "ISO-8859-1";

	/** Represents the map file type. */
	private enum MapType {
		DOMINATION, CONQUEST
	};

	/**
	 * Loads a map file.
	 * <p>
	 * Reads a (.map) file by finding its file type and delegating the file parsing
	 * to the appropriate map loader for the given type.
	 * 
	 * @param p_fileName filename of the (.map) file
	 * @return game map loaded from the file.
	 * @throws FileParsingException  thrown if the parsing error occurs when parsing
	 *                               the map file.
	 * @throws FileNotFoundException thrown if the map file with the requested file
	 *                               name does not exist.
	 */
	public static GameMap LoadMap(String p_fileName) throws FileParsingException, FileNotFoundException {
		// Check if the map file exists
		File l_file = new File(d_MapFolder + p_fileName);
		if (!l_file.exists()) {
			throw new FileNotFoundException(p_fileName + " not found in the maps folder");
		}

		// Determine the map file type
		MapType l_mapType = GetMapFileType(l_file);

		// Instantiate the appropriate map loader depending on the map file type
		DominationMapLoader l_mapLoader;
		if (l_mapType == MapType.DOMINATION) {
			l_mapLoader = new DominationMapLoader();
		} else {
			l_mapLoader = new ConquestMapLoaderAdapter(new ConquestMapLoader());
		}

		// Load and return the map
		return l_mapLoader.LoadMap(p_fileName);
	}

	/**
	 * Saves the map to a file of the specified type.
	 * <p>
	 * Delegates the saving to the to the appropriate map loader for the given map
	 * file type.
	 * 
	 * @param p_fileName name of the file to save the map into.
	 * @param p_map      map object to save.
	 * @param p_fileType map file type to use for saving.
	 * @throws IOException exception thrown if an error occurs while writing the map
	 *                     to file.
	 */
	public static void SaveMap(String p_fileName, GameMap p_map, String p_fileType) throws IOException {
		// Instantiate the appropriate map loader depending on the desired save file
		// type
		DominationMapLoader l_mapLoader;
		switch (p_fileType) {
		case "domination":
			l_mapLoader = new DominationMapLoader();
			break;
		case "conquest":
			l_mapLoader = new ConquestMapLoaderAdapter(new ConquestMapLoader());
			break;
		default:
			throw new IllegalArgumentException("unknown map file type " + p_fileType);
		}

		// Save the map
		l_mapLoader.SaveMap(p_fileName, p_map);
	}

	/**
	 * Gets the map folder location.
	 * 
	 * @return map folder path.
	 */
	public static String GetMapFolderPath() {
		return d_MapFolder;
	}

	/**
	 * Returns the types of the map file being read.
	 * 
	 * @param p_file the map file to determine the type of.
	 * @return the type of the map file.
	 * @throws FileParsingException  thrown if the map file is empty or blank.
	 * @throws FileNotFoundException thrown if map file was not found.
	 */
	private static MapType GetMapFileType(File p_file) throws FileParsingException, FileNotFoundException {
		try (Scanner l_sc = new Scanner(p_file, d_Encoding)) {
			// Check if the first non-blank line has a "[Map]" tag
			while (l_sc.hasNextLine()) {
				// Read the line
				String l_line = l_sc.nextLine().trim();

				// Skip the line if its blank
				if (l_line.isBlank()) {
					continue;
				}

				// For the first non-blank line, check if it is a "[Map] tag
				// If so the map is a conquest map, otherwise it is a domination map
				if (l_line.startsWith("[Map]")) {
					return MapType.CONQUEST;
				} else {
					return MapType.DOMINATION;
				}
			}

			// Error: map file was empty or blank
			throw new FileParsingException("map file is empty or blank");
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