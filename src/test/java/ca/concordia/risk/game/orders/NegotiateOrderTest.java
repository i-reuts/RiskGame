package ca.concordia.risk.game.orders;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import ca.concordia.risk.game.Continent;
import ca.concordia.risk.game.Country;
import ca.concordia.risk.game.GameMap;
import ca.concordia.risk.game.Player;

/**
 * Unit test class for the <code>NegotiateOrder</code>.
 */
class NegotiateOrderTest {

	private static GameMap d_Map;
	private static Country d_Country1;
	private static Country d_Country2;

	/**
	 * Initializes the context with a starting map with two countries.
	 */
	@BeforeAll
	static void SetUp() throws Exception {
		// Create a map
		d_Map = new GameMap();

		// Create a continent and with two countries in it
		Continent l_continent = new Continent("Continent 1", 5);
		d_Country1 = new Country("Country 1", l_continent);
		d_Country2 = new Country("Country 2", l_continent);
		l_continent.addCountry(d_Country1);
		l_continent.addCountry(d_Country2);

		// Connect the two countries
		d_Country1.addNeighbor(d_Country2);
		d_Country2.addNeighbor(d_Country1);

		// Add continent and countries to the map
		d_Map.addContinent(l_continent);
		d_Map.addCountry(d_Country1);
		d_Map.addCountry(d_Country2);
	}

	/**
	 * Tests that players are unable to attack each other when negotiation between
	 * them is in place.
	 */
	@Test
	void testNegotiate() {
		// Create two players, each owning 1 country
		Player l_player1 = new Player("Player 1");
		Player l_player2 = new Player("Player 2");
		l_player1.addCountry(d_Country1);
		d_Country1.setOwner(l_player1);
		l_player2.addCountry(d_Country2);
		d_Country2.setOwner(l_player2);

		// Deploy 10 armies to each country
		d_Country1.addArmies(10);
		d_Country2.addArmies(10);

		// Ensure players can attack and bomb each other before negotiate is in place
		Order l_order1 = new AdvanceOrder(l_player1, d_Country1, d_Country2, 1);
		Order l_order2 = new AdvanceOrder(l_player2, d_Country2, d_Country1, 1);
		l_order1.execute();
		l_order2.execute();

		assertTrue(l_order1.getStatus().startsWith("Player 1 attacked"));
		assertTrue(l_order2.getStatus().startsWith("Player 2 attacked"));

		l_order1 = new BombOrder(l_player1, d_Country2);
		l_order2 = new BombOrder(l_player2, d_Country1);
		l_order1.execute();
		l_order2.execute();

		assertTrue(l_order1.getStatus().startsWith("Player 1 bombed"));
		assertTrue(l_order2.getStatus().startsWith("Player 2 bombed"));

		// Start negotiating
		Order l_negotiateOrder = new NegotiateOrder(l_player1, l_player2);
		l_negotiateOrder.execute();

		// Repeat the advance and bomb orders, ensure they now fail
		l_order1 = new AdvanceOrder(l_player1, d_Country1, d_Country2, 1);
		l_order2 = new AdvanceOrder(l_player2, d_Country2, d_Country1, 1);
		l_order1.execute();
		l_order2.execute();

		assertTrue(l_order1.getStatus().startsWith("Advance failed"));
		assertTrue(l_order2.getStatus().startsWith("Advance failed"));

		l_order1 = new BombOrder(l_player1, d_Country2);
		l_order2 = new BombOrder(l_player2, d_Country1);
		l_order1.execute();
		l_order2.execute();

		assertTrue(l_order1.getStatus().startsWith("Bombing failed"));
		assertTrue(l_order2.getStatus().startsWith("Bombing failed"));
	}
}
