package ca.concordia.risk.utils;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.io.File;

import org.junit.jupiter.api.Test;

import ca.concordia.risk.game.GameMap;
import ca.concordia.risk.utils.MapLoader.FileParsingException;

/**
 * Unit test class for the <code>MapLoader</code> class.
 *
 */
class MapLoaderTest {

	private static String d_validMapPath = "test/valid_map.map";
	private static String d_invalidSectionMapPath = "test/invalid_section_map.map";
	private static String d_invalidLineMapPath = "test/invalid_line_map.map";

	/**
	 * Tests loading a valid sample map.
	 */
	@Test
	void testValidMap() {
		// Assume test map file exists. Abort test if it doesn't
		assumeTrue(MapFileExists(d_validMapPath), "Aborting test: test map file does not exist");
		
		// Assert that no exception is thrown while loading and the resulting map is not null
		GameMap l_map = assertDoesNotThrow(() -> MapLoader.LoadMap(d_validMapPath));
		assertNotNull(l_map);
	}
	
	/**
	 * Tests loading a sample map that has missing or incorrectly ordered sections.
	 */
	@Test
	void testMissingSectionMap() {
		// Assume test map file exists. Abort test if it doesn't
		assumeTrue(MapFileExists(d_invalidSectionMapPath), "Aborting test: test map file does not exist");

		// Assert an exception is thrown when loading the sample test file
		FileParsingException l_e = assertThrows(FileParsingException.class,
				() -> MapLoader.LoadMap(d_invalidSectionMapPath));

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
		assumeTrue(MapFileExists(d_invalidLineMapPath), "Aborting test: test map file does not exist");

		// Assert an exception is thrown when loading the sample test file
		FileParsingException l_e = assertThrows(FileParsingException.class,
				() -> MapLoader.LoadMap(d_invalidLineMapPath));

		// Check if exception thrown matches the expected exception type
		String l_exceptionMessage = l_e.getMessage();
		assertTrue(l_exceptionMessage.startsWith("Invalid map file: error when parsing - invalid line format"));
	}

	/**
	 * Checks if a map file exists inside the map directory.
	 * 
	 * @param p_fileName filename of the map file to look for.
	 * @return <code>true</code> is the requested map file exists.<br>
	 *         <code>false</code> otherwise.
	 */
	private static boolean MapFileExists(String p_fileName) {
		File l_mapFile = new File(MapLoader.getMapFolderPath() + p_fileName);
		return l_mapFile.exists();
	}
}
