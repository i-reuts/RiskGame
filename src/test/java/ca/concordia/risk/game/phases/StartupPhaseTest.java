package ca.concordia.risk.game.phases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.PrintStream;
import java.util.Random;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import ca.concordia.risk.GameEngine;
import ca.concordia.risk.game.Continent;
import ca.concordia.risk.game.Country;
import ca.concordia.risk.game.GameMap;
import ca.concordia.risk.game.Player;
import ca.concordia.risk.io.commands.AssignCountriesCommand;
import ca.concordia.risk.io.commands.GamePlayerCommand;

/**
 * Unit test class for the <code>StartupPhase</code> class.
 * <p>
 * Tests the correct parse and execution of the commands allowed during startup
 * phase.
 */
public class StartupPhaseTest {

	private static PrintStream d_DefaultOutputStream;

	private GameMap d_defaultMap;

	/**
	 * Sets up the shared context for all test cases.
	 * 
	 * @throws Exception any unexpected exception occurring during set up.
	 */
	@BeforeAll
	static void SetUpBeforeClass() throws Exception {
		// Redirect standard output to a null stream
		d_DefaultOutputStream = System.out;
		System.setOut(new PrintStream(PrintStream.nullOutputStream()));
	}

	/**
	 * Cleans up the context after the tests complete.
	 */
	@AfterAll
	static void CleanUpAfterClass() {
		// Restore output stream
		System.setOut(d_DefaultOutputStream);
	}

	/**
	 * Setup the default map and cleanup the GameEngine and set it up to be in the
	 * Startup Phase
	 */
	@BeforeEach
	void SetUp() {
		d_defaultMap = new GameMap();
		Random l_rand = new Random();

		// Random number of countries
		int l_noOfCountries = l_rand.nextInt(17) + 3;

		// Create a continent
		Continent l_continent = new Continent("Continent", 5);
		d_defaultMap.addContinent(l_continent);

		// Generate all the random countries
		for (int l_i = 0; l_i < l_noOfCountries; l_i++) {
			d_defaultMap.addCountry(new Country("Country_" + l_i, l_continent));
		}

		// Initialize the GameEngine and set it up to startup phase
		GameEngine.Initialize();
		GameEngine.SetMap(d_defaultMap);
		GameEngine.SwitchToNextPhase();
	}

	/** Tests adding and removing players to the game */
	@Test
	@Order(1)
	void testGameplayerCommand() {
		// Try to add the same player twice
		GamePlayerCommand l_gameplayerCommand = new GamePlayerCommand();
		l_gameplayerCommand.addPlayer("Player_1");
		l_gameplayerCommand.addPlayer("Player_1");
		l_gameplayerCommand.addPlayer("Player_2");
		l_gameplayerCommand.execute();

		// Player with the same name cannot be added twice
		assertEquals(GameEngine.GetNumberOfPlayers(), 2);

		// Players were added to the GameEngine successfully
		assertEquals(GameEngine.GetPlayer("Player_1").getName(), "Player_1");
		assertEquals(GameEngine.GetPlayer("Player_2").getName(), "Player_2");

		// Remove player
		l_gameplayerCommand.removePlayer("Player_1");
		l_gameplayerCommand.removePlayer("Player_2");
		l_gameplayerCommand.execute();

		// Players removed successfully
		assertEquals(GameEngine.GetNumberOfPlayers(), 0);
	}

	/** Tests condition to not start the game with just one player */
	@Test
	void testStartGameOnePlayer() {
		// Add just one player
		GamePlayerCommand l_gameplayerCommand = new GamePlayerCommand();
		l_gameplayerCommand.addPlayer("Player_0");
		l_gameplayerCommand.execute();

		// Try to start the game with one player
		AssignCountriesCommand l_assignCountriesCommand = new AssignCountriesCommand();
		l_assignCountriesCommand.execute();

		// Not possible to start a game with just one player
		assertTrue(GameEngine.GetActivePhase() instanceof StartupPhase);
	}

	/** Tests condition to not start the game with more players than countries */
	@Test
	@Order(3)
	void testStartGameMorePlayersThanCountries() {
		// Add more players than countries
		GamePlayerCommand l_gameplayerCommand = new GamePlayerCommand();
		for (int l_i = 0; l_i < d_defaultMap.getCountries().size() + 1; l_i++) {
			l_gameplayerCommand.addPlayer("Player_" + l_i);
		}
		l_gameplayerCommand.execute();

		// Try to start the game with more players than countries
		AssignCountriesCommand l_assignCountriesCommand = new AssignCountriesCommand();
		l_assignCountriesCommand.execute();

		// Not possible to start a game with more players than countries
		assertTrue(GameEngine.GetActivePhase() instanceof StartupPhase);
	}

	/** Tests all countries are assigned to a player */
	@Test
	@Order(4)
	void testAssignCountries() {
		// Add random number of players
		Random l_rand = new Random();
		int l_noOfPlayers = l_rand.nextInt(3) + 2;
		GamePlayerCommand l_gameplayerCommand = new GamePlayerCommand();
		for (int l_i = 0; l_i < l_noOfPlayers; l_i++) {
			l_gameplayerCommand.addPlayer("Player_" + l_i);
		}
		l_gameplayerCommand.execute();

		// Assign countries
		AssignCountriesCommand l_assignCountriesCommand = new AssignCountriesCommand();
		l_assignCountriesCommand.execute();

		// Assert that every country has a player as an owner
		GameEngine.GetMap().getCountries().forEach((l_country) -> {
			if (!GameEngine.GetPlayers().contains(l_country.getOwner())) {
				assertTrue(false);
			}
		});

		// Assert that number of countries each player own differs at most by one
		Player l_firstPlayer = GameEngine.GetPlayers().iterator().next();
		int l_firstPlayerCountriesOwned = l_firstPlayer.getCountries().size();
		for (Player l_p : GameEngine.GetPlayers()) {
			int l_countriesOwned = l_p.getCountries().size();
			assertTrue(Math.abs(l_firstPlayerCountriesOwned - l_countriesOwned) <= 1);
		}
	}
}
