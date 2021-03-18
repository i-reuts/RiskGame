package ca.concordia.risk.game.orders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import ca.concordia.risk.game.Continent;
import ca.concordia.risk.game.Country;
import ca.concordia.risk.game.GameMap;
import ca.concordia.risk.game.Player;


/**
 * Unit test class for the <code>DeployOrder</code>.
 */
public class DeployOrderTest {

	@Test
	public void deploymentFailtest() {
		DeployOrder d_deployorder;
		// Create sample map with a continent and countries
				GameMap l_map = new GameMap();
				Continent l_continent = new Continent("Test Continent", 10);
				l_map.addContinent(l_continent);
				for (int l_i = 0; l_i < 5; l_i++) {
					l_map.addCountry(new Country("Country " + l_i, l_continent));
				}
				List<Country> l_countries = l_map.getCountries();
				Country l_country1 = l_countries.get(0);
				//Country l_country2 = l_countries.get(1);

				// Create a player
				Player l_player = new Player("Player A");
				Player l_player2 = new Player("Player B");

				// Add 1st country to countries owned by the player
				l_player.addCountry(l_country1);
				d_deployorder = new DeployOrder(l_player2, l_country1, 4);
				d_deployorder.execute();
				
				//assertEquals(d_deployorder.getStatus(),"Deployment failed: " + l_country1 + " no longer owned by Player B");
				assertTrue(d_deployorder.getStatus().startsWith("Deployment failed: "));				
	}
}
