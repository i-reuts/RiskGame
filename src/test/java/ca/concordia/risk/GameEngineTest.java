package ca.concordia.risk;

import static org.junit.jupiter.api.Assertions.assertNotNull;
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

import ca.concordia.risk.game.Country;
import ca.concordia.risk.game.Player;
import ca.concordia.risk.utils.MapLoader;

/**
 * Unit test class for the <code>GameEngine</code> class.
 * <p>
 * Tests the correct flow of the game phases.
 */
@TestMethodOrder(OrderAnnotation.class)
class GameEngineTest {

	private static String d_TestMapPath = "test/testmap.map";
	private static PrintWriter d_MockInputStreamWriter;

	private static PrintStream d_DefaultOutputStream;
	private static InputStream d_DefaultInputStream;

	/**
	 * Sets up the shared context for all test cases.
	 * 
	 * @throws Exception any unexpected exception occurring during set up.
	 */
	@BeforeAll
	static void SetUpBeforeClass() throws Exception {
		assumeTrue(MapFileExists(d_TestMapPath), "Aborting test: test map file does not exist");

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
	 * Tests the editor phase by executing editor-specific commands and ensuring
	 * they pass, as well as executing unsupported commands and ensuring they fail.
	 * <p>
	 * After the test is over the phase should be changed to startup.
	 */
	@Test
	@Order(1)
	void testEditorPhase() {
		// Assert that there is no map loaded initially
		assertNull(GameEngine.GetMap());

		// Use editmap command to create a new map, check if a map is created
		executeCommand("editmap newmap.map");
		assertNotNull(GameEngine.GetMap());

		// Use an editcontinent command, exclusive to the Editor Phase and ensure it
		// runs
		executeCommand("editcontinent -add Test_Continent 5");
		assertNotNull(GameEngine.GetMap().getContinent("Test Continent"));

		// Use a gameplayer command, not supported in the Editor Phase and ensure that
		// it is rejected
		executeCommand("gameplayer -add Test_Player human");
		assertNull(GameEngine.GetPlayer("Test Player"));

		// Switch to the startup phase by executing the loadmap command
		executeCommand("loadmap " + d_TestMapPath);
	}

	/**
	 * Tests the startup phase by executing startup-specific commands and ensuring
	 * they pass, as well as executing unsupported commands and ensuring they fail.
	 * <p>
	 * After the test is over the phase should be changed to gameplay.
	 */
	@Test
	@Order(2)
	void testStartupPhase() {
		// Use a gameplayer command exclusive to the Startup Phase and ensure it
		// runs
		executeCommand("gameplayer -add Test_Player human");
		assertNotNull(GameEngine.GetPlayer("Test Player"));

		// Use a editmap command, not supported in the Startup Phase and ensure that
		// it is rejected
		executeCommand("editcontinent -add Test_Continent_2 5");
		assertNull(GameEngine.GetMap().getContinent("Test Continent 2"));

		// Add one more player and switch to the gameplay phase by executing the
		// assigncountries command
		executeCommand("gameplayer -add Test_Player_2 human");
		executeCommand("assigncountries");
	}

	/**
	 * Tests the gameplay phase by executing gameplay-specific commands and ensuring
	 * they pass, as well as executing unsupported commands and ensuring they fail.
	 */
	@Test
	@Order(3)
	void testGameplayPhase() {
		// Use a deploy command exclusive to the Gameplay Phase and ensure it
		// runs
		Country l_country = GameEngine.GetMap().getCountries().get(0);
		Player l_countryOwner = l_country.getOwner();
		l_countryOwner.assignReinfocements();

		executeOrderCommand("deploy " + l_country.getName() + " 3", l_countryOwner);
		assertTrue(l_country.getArmies() == 3);

		// Use an editmap command, not supported in the Gameplay Phase and ensure that
		// it is rejected
		executeCommand("editcontinent -add Test_Continent_3 5");
		assertNull(GameEngine.GetMap().getContinent("Test Continent 3"));

		// Use a gameplayer command, not supported in the Gameplay Phase and ensure that
		// it is rejected
		executeCommand("gameplayer -add Test_Player_3 human");
		assertNull(GameEngine.GetPlayer("Test Player 3"));
	}

	/**
	 * Writes a command to the mock input stream and asks the engine to process it.
	 * 
	 * @param p_commandInput raw command string to process.
	 */
	private void executeCommand(String p_commandInput) {
		d_MockInputStreamWriter.println(p_commandInput);
		GameEngine.ProcessUserCommand();
	}

	/**
	 * Writes an order command to the mock input stream, asks the engine to process
	 * it and executes the resulting order, if any.
	 * 
	 * @param p_commandInput raw command string to process.
	 * @param p_player       player to execute command for.
	 */
	private void executeOrderCommand(String p_commandInput, Player p_player) {
		d_MockInputStreamWriter.println(p_commandInput);
		ca.concordia.risk.game.orders.Order l_order = GameEngine.ProcessOrderCommand(p_player);
		if (l_order != null) {
			l_order.execute();
		}
	}

	/**
	 * Checks if a map file exists inside the map directory.
	 * 
	 * @param p_fileName filename of the map file to look for.
	 * @return <code>true</code> is the requested map file exists.<br>
	 *         <code>false</code> otherwise.
	 */
	private static boolean MapFileExists(String p_fileName) {
		File l_mapFile = new File(MapLoader.GetMapFolderPath() + p_fileName);
		return l_mapFile.exists();
	}
}
