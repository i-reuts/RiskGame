package ca.concordia.risk.io.commands;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca.concordia.risk.game.Continent;
import ca.concordia.risk.game.Country;
import ca.concordia.risk.game.GameMap;
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

	/**
	 * Tests for all continents with no country
	 */
	@Test
	void continentWithNoCountries() {

		Continent l_continent3 = new Continent("Test Continent 3", 17);
		d_Map.addContinent(l_continent3);

		Country l_country1 = new Country("Test country 1 of Continent 3", l_continent3);
		d_Map.addCountry(l_country1);

		Country l_country2 = new Country("Test country 2 of Continent 3", l_continent3);
		d_Map.addCountry(l_country2);

		Country l_country3 = new Country("Test country 3 of Continent 3", l_continent3);
		d_Map.addCountry(l_country3);

		l_country1.addNeighbor(l_country2);
		l_country2.addNeighbor(l_country3);
		l_country3.addNeighbor(l_country1);

		Continent l_continent4 = new Continent("Test Continent 4", 18);
		d_Map.addContinent(l_continent4);

		assertFalse(MapValidator.Validate(d_Map));
		assertTrue(MapValidator.getStatus().startsWith("continent "));

	}

	/**
	 * Tests for Strongly connected Continents
	 */
	@Test
	void notStronglyConnectedContinents() {

		// continent 3
		Continent l_continent3 = new Continent("Test Continent 3", 17);
		d_Map.addContinent(l_continent3);

		Country l_country1 = new Country("Test country 1 of Continent 3", l_continent3);
		d_Map.addCountry(l_country1);

		Country l_country2 = new Country("Test country 2 of Continent 3", l_continent3);
		d_Map.addCountry(l_country2);

		// Continent 4
		Continent l_continent4 = new Continent("Test Continent 4", 18);
		d_Map.addContinent(l_continent4);

		Country l_country3 = new Country("Test country 1 of Continent 4", l_continent4);
		d_Map.addCountry(l_country3);

		l_country1.addNeighbor(l_country3);
		l_country2.addNeighbor(l_country3);
		l_country3.addNeighbor(l_country2);
		l_country3.addNeighbor(l_country1);

		assertFalse(MapValidator.Validate(d_Map));
		assertTrue(MapValidator.getStatus().startsWith("continent validation - countries "));
	}

	/**
	 * Tests for the overall map to be valid
	 */
	@Test
	void validMap() {

		// Continent 5
		Continent l_continent5 = new Continent("Test Continent 5", 19);
		d_Map.addContinent(l_continent5);

		Country l_country4 = new Country("Test country 1 of Continent 5", l_continent5);
		d_Map.addCountry(l_country4);

		Country l_country5 = new Country("Test country 2 of Continent 5", l_continent5);
		d_Map.addCountry(l_country5);

		Country l_country6 = new Country("Test country 3 of Continent 5", l_continent5);
		d_Map.addCountry(l_country6);

		l_country4.addNeighbor(l_country5);
		l_country5.addNeighbor(l_country6);
		l_country6.addNeighbor(l_country4);

		// Continent 6

		Continent l_continent6 = new Continent("Test Continent 6", 20);
		d_Map.addContinent(l_continent6);

		Country l_country7 = new Country("Test country 1 of Continent 6", l_continent6);
		d_Map.addCountry(l_country7);

		Country l_country8 = new Country("Test country 2 of Continent 6", l_continent6);
		d_Map.addCountry(l_country8);

		Country l_country9 = new Country("Test country 3 of Continent 6", l_continent6);
		d_Map.addCountry(l_country9);

		l_country7.addNeighbor(l_country8);
		l_country8.addNeighbor(l_country9);
		l_country9.addNeighbor(l_country7);

		l_country5.addNeighbor(l_country7);
		l_country8.addNeighbor(l_country6);

		assertTrue(MapValidator.Validate(d_Map));
		assertTrue(MapValidator.getStatus().startsWith("map is valid"));
	}

}
