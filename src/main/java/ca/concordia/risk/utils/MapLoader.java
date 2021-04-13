package ca.concordia.risk.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import ca.concordia.risk.game.GameMap;

public class MapLoader {
	public static DominationMapLoader d_MapLoader;

	private static String d_MapType;
	private static final String d_MapFolder = "./maps/";
	private static final String d_Encoding = "ISO-8859-1";

	public static GameMap LoadMap(String p_fileName) throws FileParsingException, FileNotFoundException {
		File l_file = new File(d_MapFolder + p_fileName);
		if (!l_file.exists()) {
			throw new FileNotFoundException(p_fileName + " not found in the maps folder");
		}

		Scanner l_sc = new Scanner(l_file, d_Encoding);
		d_MapType = getMapType(l_sc);

		if(d_MapType.equalsIgnoreCase("Conquest")) {
			d_MapLoader = new ConquestMapLoaderAdapter(new ConquestMapLoader());
		} else {
			d_MapLoader = new DominationMapLoader();
		}

		try {
			return d_MapLoader.LoadMap(p_fileName);			
		} catch (Exception e) {
			throw new FileParsingException(e.getMessage().substring(18));
		}				
	}

	public static void SaveMap(String p_fileName, GameMap p_map) throws IOException {
		d_MapLoader.SaveMap(p_fileName, p_map);
	}
	
	/**
	 * Gets the map folder location.
	 * 
	 * @return map folder path.
	 */
	public static String getMapFolderPath() {
		return d_MapFolder;
	}

	private static String getMapType(Scanner p_sc) {
		String d_mapType = "Domination";
		String p_tag = "[Map]";
		while (p_sc.hasNextLine()) {
			String l_line = p_sc.nextLine().trim();
			if (l_line.startsWith(p_tag)) {
				d_mapType = "Conquest";
				return d_mapType;
			}
		}

		return d_mapType;
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