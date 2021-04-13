package ca.concordia.risk.utils;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.io.File;

import org.junit.jupiter.api.Test;

import ca.concordia.risk.game.Continent;
import ca.concordia.risk.game.Country;
import ca.concordia.risk.game.GameMap;
import ca.concordia.risk.utils.MapLoader.FileParsingException;

/**
 * Unit test class for the <code>MapLoader</code> class.
 *
 */
class MapLoaderTest {

	private static String d_ValidMapPath = "test/valid_map.map";
	private static String d_InvalidSectionMapPath = "test/invalid_section_map.map";
	private static String d_InvalidLineMapPath = "test/invalid_line_map.map";
	private static String d_ConquestMapPath = "test/conquest.map";
	private static String d_SavedMapPath = "test/saved.map";

	/**
	 * Tests loading a valid sample map.
	 */
	@Test
	void testValidMap() {
		// Assume test map file exists. Abort test if it doesn't
		assumeTrue(MapFileExists(d_ValidMapPath), "Aborting test: test map file does not exist");

		// Assert that no exception is thrown while loading and the resulting map is not
		// null
		GameMap l_map = assertDoesNotThrow(() -> MapLoader.LoadMap(d_ValidMapPath));
		assertNotNull(l_map);
	}

	/**
	 * Tests loading a sample map that has missing or incorrectly ordered sections.
	 */
	@Test
	void testMissingSectionMap() {
		// Assume test map file exists. Abort test if it doesn't
		assumeTrue(MapFileExists(d_InvalidSectionMapPath), "Aborting test: test map file does not exist");

		// Assert an exception is thrown when loading the sample test file
		FileParsingException l_e = assertThrows(FileParsingException.class,
				() -> MapLoader.LoadMap(d_InvalidSectionMapPath));

		// Check if exception thrown matches the expected exception type
		String l_exceptionMessage = l_e.getMessage();
		assertTrue(l_exceptionMessage.endsWith("tag not found"));
	}

	/**
	 * Tests loading a sample map that has lines with invalid format.
	 */
	@Test
	void testInvalidLineMap() {
		// Assume test map file exists. Abort test if it doesn't
		assumeTrue(MapFileExists(d_InvalidLineMapPath), "Aborting test: test map file does not exist");

		// Assert an exception is thrown when loading the sample test file
		FileParsingException l_e = assertThrows(FileParsingException.class,
				() -> MapLoader.LoadMap(d_InvalidLineMapPath));

		// Check if exception thrown matches the expected exception type
		String l_exceptionMessage = l_e.getMessage();
		assertTrue(l_exceptionMessage.startsWith("Invalid map file: error when parsing - invalid line format"));
	}

	/**
	 * Tests loading a valid conquest map file.
	 */
	@Test
	void testValidConquestMap() {
		// Assume test map file exists. Abort test if it doesn't
		assumeTrue(MapFileExists(d_ConquestMapPath), "Aborting test: test map file does not exist");

		// Assert that no exception is thrown while loading and the resulting map is not
		// null
		GameMap l_map = assertDoesNotThrow(() -> MapLoader.LoadMap(d_ConquestMapPath));
		assertNotNull(l_map);
	}

	/**
	 * Test saving and loading a domination map.
	 */
	@Test
	void testSavingDominationMap() {
		// Assume test map file exists. Abort test if it doesn't
		assumeTrue(MapFileExists(d_ValidMapPath), "Aborting test: test map file does not exist");
		// Load a domination test map
		GameMap l_map = assertDoesNotThrow(() -> MapLoader.LoadMap(d_ValidMapPath));
		// Save the map as the domination map
		assertDoesNotThrow(() -> MapLoader.SaveMap(d_SavedMapPath, l_map, "domination"));
		// Load the saved domination map
		GameMap l_loadedMap = assertDoesNotThrow(() -> MapLoader.LoadMap(d_SavedMapPath));
		// Compare the two maps
		EnsureMapsEqual(l_map, l_loadedMap);
		// Delete the save game file
		DeleteMapFile(d_SavedMapPath);
	}

	/**
	 * Test saving and loading a conquest map.
	 */
	@Test
	void testSavingConquestMap() {
		// Assume test map file exists. Abort test if it doesn't
		assumeTrue(MapFileExists(d_ConquestMapPath), "Aborting test: test map file does not exist");
		// Load a domination test map
		GameMap l_map = assertDoesNotThrow(() -> MapLoader.LoadMap(d_ConquestMapPath));
		// Save the map as the domination map
		assertDoesNotThrow(() -> MapLoader.SaveMap(d_SavedMapPath, l_map, "domination"));
		// Load the saved domination map
		GameMap l_loadedMap = assertDoesNotThrow(() -> MapLoader.LoadMap(d_SavedMapPath));
		// Compare the two maps
		EnsureMapsEqual(l_map, l_loadedMap);
		// Delete the save game file
		DeleteMapFile(d_SavedMapPath);
	}

	/**
	 * Test saving and loading maps of different types.
	 */
	@Test
	void testMapFileCompatibility() {
		// Assume test map file exists. Abort test if it doesn't
		assumeTrue(MapFileExists(d_ValidMapPath), "Aborting test: test map file does not exist");
		// Load a domination test map
		GameMap l_map = assertDoesNotThrow(() -> MapLoader.LoadMap(d_ValidMapPath));
		// Save the map as the conquest map
		assertDoesNotThrow(() -> MapLoader.SaveMap(d_SavedMapPath, l_map, "conquest"));
		// Load the saved conquest map
		GameMap l_loadedMap = assertDoesNotThrow(() -> MapLoader.LoadMap(d_SavedMapPath));
		// Compare the two maps
		EnsureMapsEqual(l_map, l_loadedMap);
		// Delete the save game file
		DeleteMapFile(d_SavedMapPath);

		// Assume test map file exists. Abort test if it doesn't
		assumeTrue(MapFileExists(d_ConquestMapPath), "Aborting test: test map file does not exist");
		// Load a conquest test map
		GameMap l_map2 = assertDoesNotThrow(() -> MapLoader.LoadMap(d_ConquestMapPath));
		// Save the map as the domination map
		assertDoesNotThrow(() -> MapLoader.SaveMap(d_SavedMapPath, l_map2, "domination"));
		// Load the saved domination map
		l_loadedMap = assertDoesNotThrow(() -> MapLoader.LoadMap(d_SavedMapPath));
		// Compare the two maps
		EnsureMapsEqual(l_map2, l_loadedMap);
		// Clean up
		DeleteMapFile(d_SavedMapPath);
	}

	/**
	 * Checks if two game maps are equal.
	 * 
	 * @param p_map1 first map to compare.
	 * @param p_map2 second map to compare.
	 */
	private static void EnsureMapsEqual(GameMap p_map1, GameMap p_map2) {
		// Ensure maps have the same number of countries and continents
		assertEquals(p_map1.getContinents().size(), p_map2.getContinents().size());
		assertEquals(p_map1.getCountries().size(), p_map2.getCountries().size());

		// Ensure maps have the same continents
		for (Continent l_c : p_map1.getContinents()) {
			assertNotNull(p_map2.getContinent(l_c.getName()));
		}

		// Ensure maps have the same countries
		for (Country l_c1 : p_map1.getCountries()) {
			Country l_c2 = p_map2.getCountry(l_c1.getName());
			assertNotNull(l_c2);

			// Check if countries belong to the same continent in both maps
			assertEquals(l_c1.getContinent().getName(), l_c2.getContinent().getName());
			// Ensure countries have the same neighbors
			assertEquals(l_c1.getNeighbors().size(), l_c2.getNeighbors().size());
			for (Country l_n1 : l_c1.getNeighbors()) {
				Country l_n2 = p_map2.getCountry(l_n1.getName());
				assertNotNull(l_n2);
				assertTrue(l_c2.hasNeighbor(l_n2));
			}
		}
	}

	/**
	 * Checks if a map file exists inside the map directory.
	 * 
	 * @param p_fileName filename of the map file to look for.
	 * @return <code>true</code> is the requested map file exists.<br>
	 *         <code>false</code> otherwise.
	 */
	private static boolean MapFileExists(String p_fileName) {
		File l_mapFile = new File(MapLoader.GetMapFolderPath() + p_fileName);
		return l_mapFile.exists();
	}

	/**
	 * Deletes a map file with the give path.
	 * 
	 * @param p_filename filename of the map file to delete.
	 * @return <code>true</code> if the file was deleted.<br>
	 *         <code>false</code> otherwise.
	 */
	private static boolean DeleteMapFile(String p_filename) {
		File l_tempFile = new File(MapLoader.GetMapFolderPath() + p_filename);
		return l_tempFile.delete();
	}
}
