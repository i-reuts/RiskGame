package ca.concordia.risk.game.gamemodes;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.io.File;
import java.util.List;
import java.util.Scanner;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import ca.concordia.risk.utils.MapLoader;

/**
 * Unit test class for the <code>Tournament</code> game mode.
 */
class TournamentTest {

	private static final List<String> d_TestMapFiles = List.of("test/testmap.map", "test/conquest.map",
			"test/risk.map");

	/**
	 * Ensures that all test maps exist before running tests.
	 */
	@BeforeAll
	static void setUpBeforeClass() {
		for (String l_filename : d_TestMapFiles) {
			File l_file = new File(MapLoader.GetMapFolderPath() + l_filename);
			assumeTrue(l_file.exists());
		}
	}

	/**
	 * Tests configuring and running a tournament.
	 */
	@Test
	void testRunningTournament() {
		// Set the number of games to play on each map and the turn limit
		int l_numGames = 5;
		int l_numTurns = 50;

		// Create a tournament and add player strategies to it
		Tournament l_tournament = new Tournament(d_TestMapFiles, l_numGames, l_numTurns);
		l_tournament.addPlayerStrategy("aggressive");
		l_tournament.addPlayerStrategy("benevolent");
		l_tournament.addPlayerStrategy("random");

		// Ensure tournament runs successfully
		assertDoesNotThrow(() -> l_tournament.run());

		// Check if the results table has expected dimensions
		String l_resultsTable = l_tournament.buildResultsTable();
		try (Scanner l_sc = new Scanner(l_resultsTable)) {
			int l_numRows = 0;
			// Process the header row
			if (l_sc.hasNextLine()) {
				l_sc.nextLine();
				l_numRows++;
			}
			// Process map rows
			// Ensure each row has the appropriate amount of entries
			while (l_sc.hasNextLine()) {
				String[] l_lineTokens = l_sc.nextLine().split("\\s+");
				assertEquals(l_lineTokens.length, l_numGames + 1);
				l_numRows++;
			}
			// Ensure the number of rows is equal to the number of maps + header
			assertEquals(l_numRows, d_TestMapFiles.size() + 1);
		}
	}

	/**
	 * Tests that tournament victor is correctly reported in the results.
	 */
	@Test
	void testWinningTournament() {
		// Set the number of games to play on each map and the turn limit
		int l_numGames = 5;
		int l_numTurns = 50;

		// Create a tournament and add player strategies to it
		Tournament l_tournament = new Tournament(d_TestMapFiles, l_numGames, l_numTurns);
		l_tournament.addPlayerStrategy("aggressive");
		l_tournament.addPlayerStrategy("benevolent");
		l_tournament.addPlayerStrategy("random");
		// Adding a cheater player will guarantee a victor
		l_tournament.addPlayerStrategy("cheater");

		// Ensure tournament runs successfully
		assertDoesNotThrow(() -> l_tournament.run());

		// Check if cheater won all games
		String l_resultsTable = l_tournament.buildResultsTable();
		try (Scanner l_sc = new Scanner(l_resultsTable)) {
			// Skip header
			l_sc.nextLine();
			// Process games for each map
			while (l_sc.hasNextLine()) {
				String[] l_lineTokens = l_sc.nextLine().split("\\s+");
				// All tokens except the map title should be Cheater
				for (int l_i = 1; l_i < l_lineTokens.length; l_i++) {
					assertEquals(l_lineTokens[l_i], "Cheater");
				}
			}
		}
	}
}
