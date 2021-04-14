package ca.concordia.risk.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import ca.concordia.risk.GameEngine;
import ca.concordia.risk.game.Card;
import ca.concordia.risk.game.Country;
import ca.concordia.risk.game.GameMap;
import ca.concordia.risk.game.Player;
import ca.concordia.risk.game.phases.GameplayPhase;
import ca.concordia.risk.io.commands.AssignCountriesCommand;
import ca.concordia.risk.io.commands.GamePlayerCommand;
import ca.concordia.risk.io.commands.LoadMapCommand;

/**
 * Unit test class for <code>GameLoader</code> class.
 */
class GameLoaderTest {

	private static final String d_TestMapPath = "test/testmap.map";
	private static final String d_TestSavePath = "test/testsave";
	private static PrintStream d_DefaultOutputStream;

	/**
	 * Sets up the test context by loading a map, adding players and starting the
	 * game.
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		// Assume test map file exists
		File l_mapFile = new File(MapLoader.GetMapFolderPath() + d_TestMapPath);
		assumeTrue(l_mapFile.exists());

		// Redirect standard output to a null stream
		d_DefaultOutputStream = System.out;
		System.setOut(new PrintStream(PrintStream.nullOutputStream()));

		// Initialize game engine
		GameEngine.Initialize();

		// Load the map
		LoadMapCommand l_command = new LoadMapCommand(d_TestMapPath);
		l_command.execute();

		// Create and add players
		GamePlayerCommand l_addPlayersCommand = new GamePlayerCommand();
		l_addPlayersCommand.addPlayer("Random", "random");
		l_addPlayersCommand.addPlayer("Benevolent", "benevolent");
		l_addPlayersCommand.addPlayer("Aggressive", "aggressive");
		l_addPlayersCommand.execute();

		// Assign countries
		AssignCountriesCommand l_assignCountriesCommand = new AssignCountriesCommand();
		l_assignCountriesCommand.execute();
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
	 * Tests saving and loading the game.
	 * 
	 * @throws Exception thrown if any unexpected exception occurs during the test.
	 */
	@Test
	void testGameSaveAndLoad() throws Exception {
		// Play the game for a few turns
		GameplayPhase l_phase = (GameplayPhase) GameEngine.GetActivePhase();
		int l_numTurns = 5;
		while (l_numTurns > 0) {
			l_phase.execute();
			l_numTurns--;
		}

		// Get the map and the players before saving and loading
		GameMap l_mapBeforeLoad = GameEngine.GetMap();
		List<Player> l_playersBeforeLoad = new ArrayList<Player>(GameEngine.GetPlayers());

		// Save the game
		GameLoader.SaveGame(d_TestSavePath);
		// Reset the Game Engine
		GameEngine.Initialize();
		// Load the game
		GameLoader.LoadGame(d_TestSavePath);

		// Get the map after saving and loading
		GameMap l_mapAfterLoad = GameEngine.GetMap();

		// Compare the map countries before and after
		for (Country l_beforeCountry : l_mapBeforeLoad.getCountries()) {
			Country l_afterCountry = l_mapAfterLoad.getCountry(l_beforeCountry.getName());

			// Check if the loaded country with the same name exists, owned by the same
			// player and has the same amount of armies deployed on it
			assertNotNull(l_afterCountry);
			assertEquals(l_beforeCountry.getOwner().getName(), l_afterCountry.getOwner().getName());
			assertEquals(l_beforeCountry.getArmies(), l_afterCountry.getArmies());
		}

		// Compare players before and after
		for (Player l_beforePlayer : l_playersBeforeLoad) {
			Player l_afterPlayer = GameEngine.GetPlayer(l_beforePlayer.getName());

			// Check if the loaded player with the same name exists and is using the same
			// strategy
			assertNotNull(l_afterPlayer);
			assertEquals(l_beforePlayer.GetStrategy().getClass(), l_afterPlayer.GetStrategy().getClass());

			// Compare cards owned by the player before and after loading
			for (Card l_card : l_beforePlayer.getCards()) {
				assertTrue(l_afterPlayer.getCards().contains(l_card));
			}
		}
	}

}
