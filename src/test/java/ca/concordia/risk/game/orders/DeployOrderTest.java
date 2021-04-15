package ca.concordia.risk.game.orders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import ca.concordia.risk.game.Continent;
import ca.concordia.risk.game.Country;
import ca.concordia.risk.game.GameMap;
import ca.concordia.risk.game.Player;

/**
 * Unit test class for the <code>DeployOrder</code>.
 * 
 * @author ishika
 */
public class DeployOrderTest {

	private static GameMap d_Map;
	private static Player d_Player;
	private static Country d_Country1;

	/**
	 * Initializes the context with an empty map, creates a player and adds a
	 * country to the player before all tests.
	 */
	@BeforeAll
	public static void SetUp() {
		d_Map = new GameMap();

		// Create sample map with a continent and countries
		Continent l_continent = new Continent("Test Continent", 10);
		d_Map.addContinent(l_continent);
		for (int l_i = 0; l_i < 5; l_i++) {
			d_Map.addCountry(new Country("Country " + l_i, l_continent));
		}
		List<Country> l_countries = d_Map.getCountries();
		d_Country1 = l_countries.get(0);

		// Create a player
		d_Player = new Player("Player A");

		// Add 1st country to countries owned by the player A
		d_Player.addCountry(d_Country1);

	}

	/**
	 * Tests validating a Deployment order.
	 * <p>
	 * Player must own the country to add the requested number of armies to the
	 * deploy country.
	 */
	@Test
	public void deploymentOrderPasstest() {
		// give an order to deploy any random number of armies: 4
		DeployOrder l_deployorder = new DeployOrder(d_Player, d_Country1, 4);
		l_deployorder.execute();
		assertEquals(l_deployorder.getStatus(), "Player A deployed 4 armies to Country 0");
	}

	/**
	 * Tests validating a Deployment order.
	 * <p>
	 * If player no longer owns the country then the deployment should fail.
	 */
	@Test
	public void deploymentOrderFailtest() {
		// give an order deploy any random number of armies: 6
		DeployOrder l_deployorder = new DeployOrder(d_Player, d_Country1, 6);
		l_deployorder.execute();
		assertEquals(l_deployorder.getStatus(), "Player A deployed 6 armies to Country 0");

		// Remove the added country from the countries owned by the player
		d_Player.removeCountry(d_Country1);

		// give an order to deploy any random number of armies: 6
		l_deployorder = new DeployOrder(d_Player, d_Country1, 6);
		l_deployorder.execute();
		assertTrue(l_deployorder.getStatus().startsWith("Deployment failed: "));
	}
}
