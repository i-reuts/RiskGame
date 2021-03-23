package ca.concordia.risk.game.orders;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import ca.concordia.risk.game.Continent;
import ca.concordia.risk.game.Country;
import ca.concordia.risk.game.GameMap;
import ca.concordia.risk.game.Player;

/** Unit test class for the <code>Airlift Order</code>. */
public class AirliftOrderTest {
	private GameMap d_defaultMap;
	private Player d_player1;
	private Player d_player2;
	private int d_noOfCountries = 3;

	/**
	 * Initializes the context with an empty map, creates 2 player and adds a
	 * country to the player before each tests.
	 */
	@BeforeEach
	public void SetUp() {
		d_defaultMap = new GameMap();
		d_player1 = new Player("Player1");
		d_player2 = new Player("Player2");

		// Create a continent
		Continent l_continent = new Continent("Continent", 5);
		d_defaultMap.addContinent(l_continent);

		// Generate all the countries
		for (int l_i = 0; l_i < d_noOfCountries; l_i++) {
			Country l_tmp_country = new Country("Country_" + l_i, l_continent);
			d_defaultMap.addCountry(l_tmp_country);

			/*
			 * Country_0 will be owned by player2 and the rest by player1. All countries
			 * will have 4 armies.
			 */
			if (l_i == 0) {
				d_player2.addCountry(l_tmp_country);
				l_tmp_country.setOwner(d_player2);
				l_tmp_country.addArmies(4);
			} else {
				d_player1.addCountry(l_tmp_country);
				l_tmp_country.setOwner(d_player1);
				l_tmp_country.addArmies(4);
			}
		}
	}

	/**
	 * Tests validating an Airlift order.
	 * <p>
	 * Player should be able to airlift between owned countries.
	 */
	@Test
	@Order(1)
	public void testAirlift() {
		// Get an array of armies
		Country[] l_countries_1 = (Country[]) d_player1.getCountries().toArray(Country[]::new);

		// Create the airlift order to an enemy country
		AirliftOrder d_dairliftorder = new AirliftOrder(d_player1, l_countries_1[0], l_countries_1[1], 4);
		d_dairliftorder.execute();

		assertTrue(d_dairliftorder.getStatus().startsWith("Player1 airlift 4 armies"));
	}

	/**
	 * Tests validating an Airlift order.
	 * <p>
	 * Players shouldn't be able to airlift armies to an enemy countries.
	 */
	@Test
	@Order(2)
	public void testAirliftToEnemyCountry() {
		// Get an array of armies
		Country[] l_countries_1 = (Country[]) d_player1.getCountries().toArray(Country[]::new);
		Country[] l_countries_2 = (Country[]) d_player2.getCountries().toArray(Country[]::new);

		// Create the airlift order to an enemy country
		AirliftOrder d_dairliftorder = new AirliftOrder(d_player1, l_countries_1[0], l_countries_2[0], 4);
		d_dairliftorder.execute();

		assertTrue(d_dairliftorder.getStatus().startsWith("Airlift failed: "));
	}

	/**
	 * Tests validating an Airlift order.
	 * <p>
	 * Players shouldn't be able to airlift armies from an enemy country.
	 */
	@Test
	@Order(3)
	public void testAirliftFromEnemyCountry() {
		// Get an array of armies
		Country[] l_countries_1 = (Country[]) d_player1.getCountries().toArray(Country[]::new);
		Country[] l_countries_2 = (Country[]) d_player2.getCountries().toArray(Country[]::new);

		// Create the airlift order to an enemy country
		AirliftOrder d_dairliftorder = new AirliftOrder(d_player1, l_countries_2[0], l_countries_1[0], 4);
		d_dairliftorder.execute();

		assertTrue(d_dairliftorder.getStatus().startsWith("Airlift failed: "));
	}

	/**
	 * Tests validating an Airlift order.
	 * <p>
	 * Players should be able to airlift the armies that still remain in the country
	 * even if their number is less that the initially requested amount.
	 */
	@Test
	@Order(4)
	public void testAirliftMoreArmies() {
		// Get an array of armies
		Country[] l_countries_1 = (Country[]) d_player1.getCountries().toArray(Country[]::new);

		// Create the airlift order to an enemy country
		AirliftOrder d_dairliftorder = new AirliftOrder(d_player1, l_countries_1[0], l_countries_1[1], 5);
		d_dairliftorder.execute();

		assertTrue(d_dairliftorder.getStatus().startsWith("Player1 airlift 4 armies"));
	}
}
