package ca.concordia.risk.game.orders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import ca.concordia.risk.game.Continent;
import ca.concordia.risk.game.Country;
import ca.concordia.risk.game.GameMap;
import ca.concordia.risk.game.Player;

/**
 * Unit test class for the <code>AdvanceOrder</code>.
 */
public class AdvanceOrderTest {
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
	 * Test advance functionality when armies from the source country are reduced after the order was created.
	 */
	@Test
	@Order(1)
	public void testAdvanceWithReducedArmies() {
		// Amount to advance originally
		int l_originalAmount = 4;
		
		// Amount to reduce from the source country
		int l_reducedAmount = 2;
		
		// Get an array of armies
		Country[] l_countries_1 = (Country[]) d_player1.getCountries().toArray(Country[]::new);
		Country[] l_countries_2 = (Country[]) d_player2.getCountries().toArray(Country[]::new);

		// Create the advance order to an enemy country
		AdvanceOrder d_advanceorder = new AdvanceOrder(d_player1, l_countries_1[0], l_countries_2[0], l_originalAmount);
		
		// Reduce source country armies
		l_countries_1[0].removeArmies(l_reducedAmount);
		
		// Execute advance order
		d_advanceorder.execute();

		assertTrue(d_advanceorder.getStatus().contains("Attacker armies: " + (l_originalAmount - l_reducedAmount) + " (out of "+ l_originalAmount +" requested)"));
	}
	
	/**
	 * Test advance functionality with armies from an enemy source country.
	 */
	@Test
	@Order(2)
	public void testAdvanceWithSourceCountryNotOwned() {
		// Amount to advance
		int l_amount = 4;
		
		// Get an array of enemy countries
		Country[] l_countries_2 = (Country[]) d_player2.getCountries().toArray(Country[]::new);

		// Create the advance order from an enemy country to an enemy country
		AdvanceOrder d_advanceorder = new AdvanceOrder(d_player1, l_countries_2[0], l_countries_2[0], l_amount);
		
		// Execute advance order
		d_advanceorder.execute();

		//System.out.println(d_advanceorder.getStatus());
		assertTrue(d_advanceorder.getStatus().contains("Advance failed: " + l_countries_2[0].getName() + " not owned by " + d_player1.getName()));
	}
	
	/**
	 * Test advance functionality to an already owned country.
	 */
	@Test
	@Order(3)
	public void testAdvanceToFriendlyCountry() {
		// Amount to advance
		int l_amount = 4;
		int l_originalArmySize;
		
		// Get an array of owned countries
		Country[] l_countries_1 = (Country[]) d_player1.getCountries().toArray(Country[]::new);

		// Create the advance order from an owned country to another owned country
		AdvanceOrder d_advanceorder = new AdvanceOrder(d_player1, l_countries_1[0], l_countries_1[1], l_amount);
		
		// Get original Army size
		l_originalArmySize = l_countries_1[1].getArmies();
		
		// Execute advance order
		d_advanceorder.execute();

		// Assert target Country has the new amount of armies
		assertEquals(l_countries_1[1].getArmies(), l_originalArmySize+l_amount);
	}
	
	/**
	 * Test advance functionality between negotiating players.
	 */
	@Test
	@Order(4)
	public void testAdvanceToNegotiatingCountry() {
		// Amount to advance
		int l_amount = 4;
		
		// Get an array of enemy countries
		Country[] l_countries_2 = (Country[]) d_player2.getCountries().toArray(Country[]::new);
		
		// Get an array of owned countries
		Country[] l_countries_1 = (Country[]) d_player1.getCountries().toArray(Country[]::new);
		
		// Create a negotiation between the players
		NegotiateOrder d_negotiateOrder = new NegotiateOrder(d_player1, d_player2);
		d_negotiateOrder.execute();

		// Create the advance order from an owned country to an "friendly" enemy country
		AdvanceOrder d_advanceorder = new AdvanceOrder(d_player1, l_countries_1[0], l_countries_2[0], l_amount);
		
		// Execute advance order
		d_advanceorder.execute();

		// Assert there is no advance between negotiating players
		assertTrue(d_advanceorder.getStatus().contains("are currently negotiating"));
	}
	
	/**
	 * Test advance functionality when no armies.
	 */
	@Test
	@Order(5)
	public void testAdvanceWithNoArmies() {
		// Amount to advance originally
		int l_originalAmount = 4;

		
		// Get an array of countries
		Country[] l_countries_1 = (Country[]) d_player1.getCountries().toArray(Country[]::new);
		Country[] l_countries_2 = (Country[]) d_player2.getCountries().toArray(Country[]::new);

		// Create the advance order to an enemy country
		AdvanceOrder d_advanceorder = new AdvanceOrder(d_player1, l_countries_1[0], l_countries_2[0], l_originalAmount);
		
		// Reduce source country armies
		l_countries_1[0].removeArmies(l_countries_1[0].getArmies());
		
		// Execute advance order
		d_advanceorder.execute();

		// Assert nothing happens if source country is empty
		assertTrue(d_advanceorder.getStatus().contains("Advance failed: " + l_countries_1[0].getName() + " has no armies"));
	}
}
