package ca.concordia.risk.utils;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca.concordia.risk.game.Continent;
import ca.concordia.risk.game.Country;
import ca.concordia.risk.game.GameMap;

/**
 * Unit test class for the <code>MapValidator</code> class.
 * 
 * @author ishika
 *
 */
class MapValidatorTest {

	private GameMap d_map;

	/**
	 * Initializes the context with an empty map before each test.
	 */
	@BeforeEach
	void SetUp() {
		d_map = new GameMap();
	}

	/**
	 * Tests validating a map with less than two countries.
	 * <p>
	 * A map has to have more than two countries to be valid.
	 */
	@Test
	void testMapWithLessThanTwoCountries() {
		// Zero countries
		Continent l_continent = new Continent("Test Continent", 15);
		d_map.addContinent(l_continent);
		assertFalse(MapValidator.Validate(d_map));
		assertTrue(MapValidator.getStatus().startsWith("map has less than two countries"));

		// One country
		d_map.addCountry(new Country("Country 1", l_continent));
		assertFalse(MapValidator.Validate(d_map));
		assertTrue(MapValidator.getStatus().startsWith("map has less than two countries"));
	}

	/**
	 * Tests validating a map that is not a strongly connected graph.
	 */
	@Test
	void testNotStronglyConnectedMap() {
		// Test a non-connected map
		Continent l_continent = new Continent("Test Continent", 16);
		d_map.addContinent(l_continent);

		for (int l_i = 0; l_i < 3; l_i++) {
			d_map.addCountry(new Country("Country " + l_i, l_continent));
		}

		assertFalse(MapValidator.Validate(d_map));
		assertTrue(MapValidator.getStatus().startsWith("map validation"));

		// Test connected but not strongly connected map
		List<Country> l_countries = d_map.getCountries();
		l_countries.get(0).addNeighbor(l_countries.get(2));
		l_countries.get(1).addNeighbor(l_countries.get(2));
		l_countries.get(2).addNeighbor(l_countries.get(0));
		l_countries.get(1).addNeighbor(l_countries.get(0));

		assertFalse(MapValidator.Validate(d_map));
		assertTrue(MapValidator.getStatus().startsWith("map validation"));
	}

	/**
	 * Tests validating a map that has a continent with no countries.
	 */
	@Test
	void testEmptyContinentMap() {
		// Create and add a non-empty connected continent
		Continent l_continent1 = new Continent("Test Continent 1", 17);
		d_map.addContinent(l_continent1);

		Country l_country1 = new Country("Test country 1 of " + l_continent1.getName(), l_continent1);
		Country l_country2 = new Country("Test country 2 of " + l_continent1.getName(), l_continent1);
		Country l_country3 = new Country("Test country 3 of " + l_continent1.getName(), l_continent1);
		d_map.addCountry(l_country1);
		d_map.addCountry(l_country2);
		d_map.addCountry(l_country3);

		l_country1.addNeighbor(l_country2);
		l_country2.addNeighbor(l_country3);
		l_country3.addNeighbor(l_country1);

		// Create and add an empty continent
		Continent l_continent2 = new Continent("Test Continent 2", 18);
		d_map.addContinent(l_continent2);

		assertFalse(MapValidator.Validate(d_map));
		assertTrue(MapValidator.getStatus().startsWith("continent")
				&& MapValidator.getStatus().endsWith("has no countries"));
	}

	/**
	 * Tests validating a map that has a continent which is not strongly connected.
	 */
	@Test
	void testNotStronglyConnectedContinentMap() {
		// Add continent 1
		Continent l_continent1 = new Continent("Test Continent 1", 17);
		d_map.addContinent(l_continent1);

		Country l_country1 = new Country("Test country 1 of " + l_continent1.getName(), l_continent1);
		Country l_country2 = new Country("Test country 2 of " + l_continent1.getName(), l_continent1);
		d_map.addCountry(l_country1);
		d_map.addCountry(l_country2);

		// Add continent 2
		Continent l_continent2 = new Continent("Test Continent 2", 18);
		d_map.addContinent(l_continent2);

		Country l_country3 = new Country("Test country 1 of " + l_continent2.getName(), l_continent2);
		d_map.addCountry(l_country3);

		// Connect the countries to form a strongly connected map, but have continent 1
		// not strongly connected
		l_country1.addNeighbor(l_country3);
		l_country2.addNeighbor(l_country3);
		l_country3.addNeighbor(l_country2);
		l_country3.addNeighbor(l_country1);

		assertFalse(MapValidator.Validate(d_map));
		assertTrue(MapValidator.getStatus().startsWith("continent validation - countries "));
	}

	/**
	 * Tests validating a valid map.
	 */
	@Test
	void testValidMap() {
		// Add a strongly connected continent 1
		Continent l_continent1 = new Continent("Test Continent 1", 19);
		d_map.addContinent(l_continent1);

		Country l_country1 = new Country("Test country 1 of " + l_continent1.getName(), l_continent1);
		Country l_country2 = new Country("Test country 2 of " + l_continent1.getName(), l_continent1);
		Country l_country3 = new Country("Test country 3 of " + l_continent1.getName(), l_continent1);

		d_map.addCountry(l_country1);
		d_map.addCountry(l_country2);
		d_map.addCountry(l_country3);

		l_country1.addNeighbor(l_country2);
		l_country2.addNeighbor(l_country3);
		l_country3.addNeighbor(l_country1);

		// Add a strongly connected continent 2
		Continent l_continent2 = new Continent("Test Continent 2", 20);
		d_map.addContinent(l_continent2);

		Country l_country4 = new Country("Test country 1 of " + l_continent2.getName(), l_continent2);
		Country l_country5 = new Country("Test country 2 of " + l_continent2.getName(), l_continent2);
		Country l_country6 = new Country("Test country 3 of " + l_continent2.getName(), l_continent2);

		d_map.addCountry(l_country4);
		d_map.addCountry(l_country5);
		d_map.addCountry(l_country6);

		l_country4.addNeighbor(l_country5);
		l_country5.addNeighbor(l_country6);
		l_country6.addNeighbor(l_country4);

		// Connect the continents together to form a connected graph
		l_country2.addNeighbor(l_country4);
		l_country5.addNeighbor(l_country3);

		assertTrue(MapValidator.Validate(d_map));
		assertTrue(MapValidator.getStatus().startsWith("map is valid"));
	}
}
