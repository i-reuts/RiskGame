package ca.concordia.risk.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * Unit test class for the <code>Player</code>.
 */
class PlayerTest {

	/**
	 * Tests assignment of reinforcements to players depending on on the minimum
	 * reinforcement number, number of countries owned and bonus value of wholly
	 * owned continents.
	 */
	@Test
	void testReinforcementAssignment() {
		// Create sample map with a continent and countries
		GameMap l_map = new GameMap();
		Continent l_continent = new Continent("Test Continent", 10);
		l_map.addContinent(l_continent);
		for (int l_i = 0; l_i < 13; l_i++) {
			l_map.addCountry(new Country("Country " + l_i, l_continent));
		}
		List<Country> l_countries = l_map.getCountries();

		// Create a player
		Player l_player = new Player("Player 1");

		// Assign reinforcements to a player with no countries
		// Since the player owns no countries, they should get the minimum reinforcement
		// amount of 3
		l_player.assignReinfocements();
		assertEquals(l_player.getRemainingReinforcements(), 3);

		// Add 11 countries to countries owned by the player and assign again
		// The number of reinforcements should still be 3
		for (int l_i = 0; l_i < 11; l_i++) {
			l_player.addCountry(l_countries.remove(0));
		}
		l_player.assignReinfocements();
		assertEquals(l_player.getRemainingReinforcements(), 3);

		// Add a twelfth country
		// Number of reinforcements should now be 4
		l_player.addCountry(l_countries.remove(0));
		l_player.assignReinfocements();
		assertEquals(l_player.getRemainingReinforcements(), 4);

		// Add a thirteenth country
		// Player now owns the whole test continent
		// Number of reinforcements should now be 14: 4 + continent bonus value of 10
		l_player.addCountry(l_countries.remove(0));
		l_player.assignReinfocements();
		assertEquals(l_player.getRemainingReinforcements(), 14);
	}

	/**
	 * Tests that the player cannot deploy more armies that there is in their
	 * reinforcement pool or deploy an invalid number of armies.
	 */
	@Test
	void testReinforcementPool() {
		// Create a player and assign reinforcements
		Player l_player = new Player("Player 1");
		l_player.assignReinfocements();

		// Try taking more than 3 reinforcements from the player
		assertFalse(l_player.retrieveReinforcements(4));
		assertEquals(l_player.getRemainingReinforcements(), 3);

		// Try taking a negative number of reinforcements
		assertFalse(l_player.retrieveReinforcements(-1));
		assertEquals(l_player.getRemainingReinforcements(), 3);
	}

	/** Tests the functionality of giving player and picking a card. */
	@Test
	void testCards() {
		// Create a player and give them a blockade card
		Player l_player = new Player("Player 1");
		l_player.addCard(Card.getBlockadeCard());

		// Try using the Blockade card
		assertTrue(l_player.useCard(Card.getBlockadeCard()));

		// Try using the Blockage card again to ensure the player no longer has it
		assertFalse(l_player.useCard(Card.getBlockadeCard()));

		// Try using a Bomb card which a player does not have
		assertFalse(l_player.useCard(Card.getBombCard()));

		// Give the player a Bomb card and try using it again
		l_player.addCard(Card.getBombCard());
		assertTrue(l_player.useCard(Card.getBombCard()));

		// Give the player two Airlift cards and ensure they can use them exactly twice
		l_player.addCard(Card.getAirliftCard());
		l_player.addCard(Card.getAirliftCard());

		assertTrue(l_player.useCard(Card.getAirliftCard()));
		assertTrue(l_player.useCard(Card.getAirliftCard()));
		assertFalse(l_player.useCard(Card.getAirliftCard()));
	}
}
