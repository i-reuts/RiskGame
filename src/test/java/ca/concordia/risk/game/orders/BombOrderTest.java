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
	private static Country l_country1;
	private static Country l_country2;

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
		l_country1 = l_countries.get(0);
		l_country2 = l_countries.get(1);

		// Create a player: Player A and add country1 to countries owned by it
		d_player1 = new Player("Player A");
		d_player1.addCountry(l_country1);
	}

	/**
	 * Tests validating a Bomb order.
	 * <p>
	 * Country to be bombed should not be one of player's owned countries.
	 */
	@Test
	public void bombOrderFailOwnCountrytest() {
		BombOrder d_bomborder;

		// Give an order to bomb the country that is owned by the player itself
		d_bomborder = new BombOrder(d_player1, l_country1);
		d_bomborder.execute();

		assertTrue(d_bomborder.getStatus().startsWith("Bombing failed: Player: "));
	}

	/**
	 * Tests validating a Bomb order.
	 * <p>
	 * Country to be bombed should be adjacent to one of the current player’s
	 * territories.
	 */
	@Test
	public void bombOrderFailNotAdjacenttest() {
		BombOrder d_bomborder;

		// Give an order to bomb the country that is not adjacent to the player
		d_bomborder = new BombOrder(d_player1, l_country2);
		d_bomborder.execute();

		assertEquals(d_bomborder.getStatus(),
				"Bombing failed: None of the current player’s territories is adjacent to the opponent");
	}

	/**
	 * Tests validating a Bomb order.
	 * <p>
	 * If both the conditions mentioned above are satisfied then player should be
	 * able to bomb the mentioned country.
	 */
	@Test
	public void bombOrderPasstest() {
		BombOrder d_bomborder;

		l_country1.addNeighbor(l_country2);

		// Give an order to bomb the country that is not owned by the player and is
		// adjacent to the its territory
		d_bomborder = new BombOrder(d_player1, l_country2);
		d_bomborder.execute();

		assertEquals(d_bomborder.getStatus(), d_player1.getName() + " bombed the country " + l_country2.getName());
	}
}
