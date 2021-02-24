package ca.concordia.risk.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit test class for the <code>GameMap</code>.
 */
class GameMapTest {

	private static GameMap d_Map;

	/**
	 * Initializes the context with an empty map before each test.
	 */
	@BeforeEach
	void setUp() {
		d_Map = new GameMap();
	}

	/**
	 * Tests the addition of new continents.
	 */
	@Test
	void testAddContinent() {
		// Test adding valid new continent
		Continent l_continent = new Continent("Test Continent", 5);
		d_Map.addContinent(l_continent);
		assertEquals(d_Map.getContinent(l_continent.getName()), l_continent);

		// Test adding continent that already exists
		assertFalse(d_Map.addContinent(l_continent));
	}

	/**
	 * Tests the addition of new countries.
	 */
	@Test
	void testAddCountry() {
		// Setup
		Continent l_continent = new Continent("Test Continent", 5);
		d_Map.addContinent(l_continent);

		// Test adding valid new country
		Country l_country = new Country("Test Country", l_continent);
		assertTrue(d_Map.addCountry(l_country));
		assertEquals(d_Map.getCountry(l_country.getName()), l_country);
		assertTrue(l_continent.getCountries().contains(l_country));

		// Test adding country that already exists
		assertFalse(d_Map.addCountry(l_country));
	}

	/**
	 * Tests the addition of new countries.
	 */
	@Test
	void testAddNeighbor() {
		// Setup
		Continent l_continent = new Continent("Test Continent", 5);
		d_Map.addContinent(l_continent);

		Country l_country1 = new Country("Test Country 1", l_continent);
		Country l_country2 = new Country("Test Country 2", l_continent);
		d_Map.addCountry(l_country1);
		d_Map.addCountry(l_country2);

		// Test adding valid new neighbor
		assertTrue(l_country1.addNeighbor(l_country2));
		assertTrue(l_country1.getNeighbors().contains(l_country2));
		assertFalse(l_country2.getNeighbors().contains(l_country1));

		// Test adding a duplicated neighbor
		assertFalse(l_country1.addNeighbor(l_country2));
	}

	/**
	 * Tests the removal of continents from the map.
	 */
	@Test
	void testRemoveContinent() {
		// Test removing a continent that does not exist
		assertFalse(d_Map.removeContinent("Test Continent"));

		// Test removing an existing continent
		Continent l_continent = new Continent("Test Continent", 5);
		d_Map.addContinent(l_continent);
		d_Map.addCountry(new Country("Test Country", l_continent));

		assertTrue(d_Map.removeContinent(l_continent.getName()));
		assertNull(d_Map.getContinent(l_continent.getName()));
		assertNull(d_Map.getCountry("Test Country"));
	}

	/**
	 * Tests the removal of countries.
	 */
	@Test
	void testRemoveCountry() {
		// Setup
		Continent l_continent = new Continent("Test Continent", 5);
		d_Map.addContinent(l_continent);

		// Test removing a country that does not exist
		Country l_country1 = new Country("Test Country 1", l_continent);
		assertFalse(d_Map.removeCountry(l_country1.getName()));

		// Test removing an existing country
		Country l_country2 = new Country("Test Country 1", l_continent);
		d_Map.addCountry(l_country1);
		d_Map.addCountry(l_country2);
		l_country1.addNeighbor(l_country2);

		assertTrue(d_Map.removeCountry(l_country2.getName()));
		assertFalse(l_continent.getCountries().contains(l_country2));
		assertFalse(l_country1.getNeighbors().contains(l_country2));
	}

	/**
	 * Tests the removal of countries.
	 */
	@Test
	void testRemoveNeighbor() {
		// Setup
		Continent l_continent = new Continent("Test Continent", 5);
		d_Map.addContinent(l_continent);
		Country l_country1 = new Country("Test Country 1", l_continent);
		Country l_country2 = new Country("Test Country 2", l_continent);
		d_Map.addCountry(l_country1);
		d_Map.addCountry(l_country2);

		// Test removing a country that is not a neighbor
		assertFalse(l_country1.removeNeighbor(l_country2));

		// Test removing a valid neighbor
		l_country1.addNeighbor(l_country2);
		assertTrue(l_country1.removeNeighbor(l_country2));
		assertFalse(l_country1.getNeighbors().contains(l_country2));
	}
}
