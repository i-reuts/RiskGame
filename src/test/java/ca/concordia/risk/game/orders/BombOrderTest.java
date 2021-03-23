package ca.concordia.risk.game.orders;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ca.concordia.risk.game.Continent;
import ca.concordia.risk.game.Country;
import ca.concordia.risk.game.GameMap;
import ca.concordia.risk.game.Player;

/**
 * Unit test class for the <code>BombOrder</code>.
 * 
 * @author ishika
 * 
 */
public class BombOrderTest {

	private static GameMap d_map;
	private static Player d_player1;
	private static Country d_country1;
	private static Country d_country2;

	/**
	 * Initializes the context with an empty map, creates a player and adds a
	 * country to the player before all tests.
	 *
	 */
	@BeforeAll
	static void SetUp() {
		d_map = new GameMap();

		// Create sample map with a continent and countries
		Continent l_continent = new Continent("Test Continent", 10);
		d_map.addContinent(l_continent);
		for (int l_i = 0; l_i < 5; l_i++) {
			d_map.addCountry(new Country("Country " + l_i, l_continent));
		}
		List<Country> l_countries = d_map.getCountries();
		d_country1 = l_countries.get(0);
		d_country2 = l_countries.get(1);
		
		// Deploy some armies to countries
		d_country1.addArmies(8);
		d_country2.addArmies(8);

		// Create a player: Player A and add country1 to countries owned by it
		d_player1 = new Player("Player A");
		d_player1.addCountry(d_country1);
	}

	/**
	 * Tests validating a Bomb order.
	 * <p>
	 * Country to be bombed should not be one of player's owned countries.
	 */
	@Test
	public void bombOrderFailOwnCountryTest() {
		// Get the number of armies before bombing
		int l_armiesBefore = d_country1.getArmies();
		
		// Give an order to bomb the country that is owned by the player itself
		BombOrder l_bombOrder = new BombOrder(d_player1, d_country1);
		l_bombOrder.execute();

		// Ensure bombing fails and armies were not affected
		assertTrue(l_bombOrder.getStatus().startsWith("Bombing failed:"));
		assertEquals(l_armiesBefore, d_country1.getArmies());
	}

	/**
	 * Tests validating a Bomb order.
	 * <p>
	 * Country to be bombed should be adjacent to one of the current player’s
	 * territories.
	 */
	@Test
	public void bombOrderFailNotAdjacentTest() {
		// Get the number of armies before bombing
		int l_armiesBefore = d_country2.getArmies();
		
		// Give an order to bomb the country that is not adjacent to the player
		BombOrder l_bombOrder = new BombOrder(d_player1, d_country2);
		l_bombOrder.execute();

		// Ensure bombing fails and armies were not affected
		assertTrue(l_bombOrder.getStatus().startsWith("Bombing failed: country"));
		assertEquals(l_armiesBefore, d_country2.getArmies());
	}

	/**
	 * Tests validating a Bomb order.
	 * <p>
	 * If both the conditions mentioned above are satisfied then player should be
	 * able to bomb the mentioned country.
	 */
	@Test
	public void bombOrderPassTest() {
		// Make country two adjacent to country 1
		d_country1.addNeighbor(d_country2);
		
		// Get the number of armies before bombing
		int l_armiesBefore = d_country1.getArmies();

		// Give an order to bomb the country that is not owned by the player and is
		// adjacent to the its territory
		BombOrder l_bomborder = new BombOrder(d_player1, d_country2);
		l_bomborder.execute();

		// Ensure bombing succeeds and armies are halfed
		assertEquals(l_bomborder.getStatus(), d_player1.getName() + " bombed the country " + d_country2.getName());
		assertEquals(l_armiesBefore/2, d_country2.getArmies());
	}
}
