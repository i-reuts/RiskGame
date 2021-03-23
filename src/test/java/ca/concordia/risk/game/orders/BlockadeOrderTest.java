package ca.concordia.risk.game.orders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca.concordia.risk.game.Continent;
import ca.concordia.risk.game.Country;
import ca.concordia.risk.game.GameMap;
import ca.concordia.risk.game.Player;

/**
 * Unit test class for the <code>BlockadeOrder</code>.
 *
 */
public class BlockadeOrderTest {

	private GameMap d_map;
	private Player d_player1;
	private Country d_country1;
	private Country d_country2;

	/**
	 * Initializes the context with an empty map, creates a player and adds a
	 * country to the player before each tests.
	 */
	@BeforeEach
	public void SetUp() {
		// Create sample map with a continent and countries
		d_map = new GameMap();
		Continent l_continent = new Continent("Test Continent", 10);
		d_map.addContinent(l_continent);
		for (int l_i = 0; l_i < 5; l_i++) {
			d_map.addCountry(new Country("Country " + l_i, l_continent));
		}
		List<Country> l_countries = d_map.getCountries();
		d_country1 = l_countries.get(0);
		d_country2 = l_countries.get(1);

		// Deploy some armies to countries
		d_country1.addArmies(5);
		d_country2.addArmies(10);

		// Create a player: Player A and add country1 to countries owned by it
		d_player1 = new Player("Player A");
		d_player1.addCountry(d_country1);
	}

	/**
	 * Tests validating a Blockade order.
	 * <p>
	 * Player must own the country that has to be blockade.
	 */
	@Test
	public void blockadeOrderCountryNotOwnTest() {
		int l_armiesBefore = d_country2.getArmies();
		BlockadeOrder l_blockadeOrder = new BlockadeOrder(d_player1, d_country2);
		l_blockadeOrder.execute();

		assertTrue(l_blockadeOrder.getStatus().startsWith("Blockade failed:"));
		assertEquals(l_armiesBefore, d_country2.getArmies());
	}

	/**
	 * Tests validating a Blockade order.
	 * <p>
	 * This method tests tripling of armies on the blockade country and tests that
	 * blockade country must become a neutral territory.
	 */
	@Test
	public void blockadeOrderPassTest() {
		int l_armiesBefore = d_country1.getArmies();
		BlockadeOrder l_blockadeOrder = new BlockadeOrder(d_player1, d_country1);
		l_blockadeOrder.execute();

		// Ensure number of armies tripled and player no longer owns the country
		assertTrue(l_blockadeOrder.getStatus().equals("Player A blockaded Country 0"));
		assertEquals((l_armiesBefore * 3), d_country1.getArmies());
		assertFalse(d_player1.ownsCountry(d_country1));

		// Ensure country is neutral (has no owner)
		assertNull(d_country1.getOwner());
	}
}
