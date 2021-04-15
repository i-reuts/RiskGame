package ca.concordia.risk.game.phases;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.io.File;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import ca.concordia.risk.GameEngine;
import ca.concordia.risk.game.Continent;
import ca.concordia.risk.game.Country;
import ca.concordia.risk.game.GameMap;
import ca.concordia.risk.game.Player;
import ca.concordia.risk.io.commands.GamePlayerCommand;
import ca.concordia.risk.utils.MapLoader;

/**
 * Unit test class for the <code>StartupPhase</code> class.
 * <p>
 * Tests player elimination and game ending conditions.
 */
@TestMethodOrder(OrderAnnotation.class)
class GameplayPhaseTest {

	private static final String d_TestMapPath = "test/testmap.map";
	private static PrintWriter d_MockInputStreamWriter;
	private static PrintStream d_DefaultOutputStream;
	private static InputStream d_DefaultInputStream;

	/**
	 * Sets up the test context by loading a map, adding players and starting the
	 * game.
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		// Assume test map file exists
		File l_mapFile = new File(MapLoader.GetMapFolderPath() + d_TestMapPath);
		assumeTrue(l_mapFile.exists());

		// Save the standard input and output streams
		d_DefaultOutputStream = System.out;
		d_DefaultInputStream = System.in;

		// Create a mock input stream and connect it to a writer
		// Any output written to the writer will be available in the mock input stream
		PipedInputStream l_mockInputStream = new PipedInputStream();
		d_MockInputStreamWriter = new PrintWriter(new PipedOutputStream(l_mockInputStream), true);
		System.setIn(l_mockInputStream);

		// Redirect standard output to a null stream
		System.setOut(new PrintStream(PrintStream.nullOutputStream()));

		// Initialize game engine
		GameEngine.Initialize();

		// Create and set the map
		Continent l_continent = new Continent("Continent 1", 3);
		Country l_country1 = new Country("Country 1", l_continent);
		Country l_country2 = new Country("Country 2", l_continent);
		Country l_country3 = new Country("Country 3", l_continent);

		l_country1.addNeighbor(l_country2);
		l_country1.addNeighbor(l_country3);
		l_country2.addNeighbor(l_country1);
		l_country2.addNeighbor(l_country3);
		l_country3.addNeighbor(l_country1);
		l_country3.addNeighbor(l_country2);

		GameMap l_map = new GameMap();
		l_map.addContinent(l_continent);
		l_map.addCountry(l_country1);
		l_map.addCountry(l_country2);
		l_map.addCountry(l_country3);

		GameEngine.SetMap(l_map);

		// Create and add players
		GamePlayerCommand l_addPlayersCommand = new GamePlayerCommand();
		l_addPlayersCommand.addPlayer("Player 1", "human");
		l_addPlayersCommand.addPlayer("Player 2", "human");
		l_addPlayersCommand.addPlayer("Player 3", "human");
		l_addPlayersCommand.execute();

		// Assign countries
		Player l_player1 = GameEngine.GetPlayer("Player 1");
		Player l_player2 = GameEngine.GetPlayer("Player 2");
		Player l_player3 = GameEngine.GetPlayer("Player 3");

		l_country1.setOwner(l_player1);
		l_player1.addCountry(l_country1);

		l_country2.setOwner(l_player2);
		l_player2.addCountry(l_country2);

		l_country3.setOwner(l_player3);
		l_player3.addCountry(l_country3);

		// Move to GamePlay Phase
		GameEngine.SwitchToNextPhase();
		GameEngine.SwitchToNextPhase();
	}

	/**
	 * Cleans up the context after the tests complete.
	 */
	@AfterAll
	static void CleanUpAfterClass() {
		// Restore default streams
		System.setOut(d_DefaultOutputStream);
		System.setIn(d_DefaultInputStream);
	}

	/**
	 * Test player elimination when they lose all countries.
	 */
	@Test
	@Order(1)
	void testPlayerElimination() {
		// Give commands for each player using the mock input stream
		d_MockInputStreamWriter.println("deploy Country_1 3");
		d_MockInputStreamWriter.println("pass");
		d_MockInputStreamWriter.println("pass");

		d_MockInputStreamWriter.println("advance Country_1 Country_2 3");

		d_MockInputStreamWriter.println("pass");

		// Execute first turn, player 2 should get eliminated
		GameplayPhase l_phase = (GameplayPhase) GameEngine.GetActivePhase();
		l_phase.execute();

		// Ensure country 2 owner was eliminated
		assertNull(GameEngine.GetPlayer("Player 2"));
	}

	/**
	 * Tests game ending when only one player remains.
	 */
	@Test
	@Order(2)
	void testGameEnding() {
		// Give commands for the next turn
		d_MockInputStreamWriter.println("advance Country_2 Country_3 3");
		d_MockInputStreamWriter.println("pass");

		d_MockInputStreamWriter.println("pass");

		// Execute one turn, player 3 should be eliminated
		GameplayPhase l_phase = (GameplayPhase) GameEngine.GetActivePhase();
		l_phase.execute();

		// Ensure country 3 owner was eliminated
		assertNull(GameEngine.GetPlayer("Player 3"));

		// Player 1 was the last remaining player, ensure game has ended and we're back
		// to the EditorPhase
		assertTrue(GameEngine.GetActivePhase() instanceof MapEditorPhase);
	}
}
