package ca.concordia.risk.game.orders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
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

	private GameMap d_map;
	private Player l_player;
	private Country l_country1; 
	
	/**
	 * Initializes the context with an empty map before each test.
	 */
	@BeforeEach
	void SetUp() {
		d_map = new GameMap();

		// Create sample map with a continent and countries
		Continent l_continent = new Continent("Test Continent", 10);
		d_map.addContinent(l_continent);
		for (int l_i = 0; l_i < 5; l_i++) {
			d_map.addCountry(new Country("Country " + l_i, l_continent));
		}
		List<Country> l_countries = d_map.getCountries();
		l_country1 = l_countries.get(0);

		// Create a player
		l_player = new Player("Player A");

		// Add 1st country to countries owned by the player A  
		l_player.addCountry(l_country1);

	}

	/**
	 * Tests validating a Deployment order
	 * <p>
	 * Player must own the country to add the requested number of armies to the
	 * deploy country.
	 */
	@Test
	public void deploymentOrderPasstest() {
		DeployOrder d_deployorder;
		
		// give an order to deploy any random number of armies: 4
		d_deployorder = new DeployOrder(l_player, l_country1, 4);
		d_deployorder.execute();
		assertEquals(d_deployorder.getStatus(), "Player A deployed 4 armies to Country 0");
	}

	/**
	 * Tests validating a Deployment order
	 * <p>
	 * If player no longer owns the country then the deployment should fail
	 */
	@Test
	public void deploymentOrderFailtest() {
		DeployOrder d_deployorder;
		
		//give an order deploy any random number of armies: 6
		d_deployorder = new DeployOrder(l_player, l_country1, 6);
		d_deployorder.execute();
		assertEquals(d_deployorder.getStatus(), "Player A deployed 6 armies to Country 0");

		// Remove the added country from the countries owned by the player
		l_player.removeCountry(l_country1);
		
		// give an order to deploy any random number of armies: 6
		d_deployorder = new DeployOrder(l_player, l_country1, 6);
		d_deployorder.execute();
		assertTrue(d_deployorder.getStatus().startsWith("Deployment failed: "));
	}

}
