package ca.concordia.risk.io.commands;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca.concordia.risk.GameEngine;
import ca.concordia.risk.game.Continent;
import ca.concordia.risk.game.Country;
import ca.concordia.risk.game.GameMap;
import ca.concordia.risk.utils.MapLoader;
import ca.concordia.risk.utils.MapLoader.FileParsingException;
import ca.concordia.risk.utils.MapValidator;

/**
 * Unit test class for the MapValidator class
 * 
 * @author ishika
 *
 */
class ValidateMapCommandTest {

	private static GameMap d_Map;

	/**
	 * Initializes the context with an empty map before each test.
	 */
	@BeforeEach
	void SetUp() {
		d_Map = new GameMap();
	}

	/**
	 * Tests the map with no countries
	 */
	@Test
	void mapWithNoCountries() {
		Continent l_continent1 = new Continent("Test Continent 1", 15);
		d_Map.addContinent(l_continent1);

		// Should fail due to invalid number of countries
		assertFalse(MapValidator.Validate(d_Map));
		assertTrue(MapValidator.getStatus().startsWith("map has less than two countries"));
	}

	/**
	 * Tests whether the map is strongly connected or not
	 */
	@Test
	void notStronglyConnectedMap() {
		Continent l_continent2 = new Continent("Test Continent 2", 16);
		d_Map.addContinent(l_continent2);

		for (int l_i = 0; l_i < 10; l_i++) {
			d_Map.addCountry(new Country("Country " + l_i, l_continent2));
		}

		assertFalse(MapValidator.Validate(d_Map));
		assertTrue(MapValidator.getStatus().startsWith("map validation - countries "));
	}

}
