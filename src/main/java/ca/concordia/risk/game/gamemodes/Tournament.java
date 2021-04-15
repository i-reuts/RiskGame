package ca.concordia.risk.game.gamemodes;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ca.concordia.risk.GameEngine;
import ca.concordia.risk.game.GameMap;
import ca.concordia.risk.game.Player;
import ca.concordia.risk.game.phases.GameplayPhase;
import ca.concordia.risk.game.phases.Phase;
import ca.concordia.risk.game.strategies.AggressiveStrategy;
import ca.concordia.risk.game.strategies.BenevolentStrategy;
import ca.concordia.risk.game.strategies.CheaterStrategy;
import ca.concordia.risk.game.strategies.RandomStrategy;
import ca.concordia.risk.utils.MapLoader;

/**
 * This class provides the implementation for the Tournament mode.
 * <p>
 * In the tournament mode, multiple games between AI players can be run in batch
 * with the summary displayed at the end.
 */
public class Tournament {

	private int d_numGames;
	private int d_maxTurns;
	private List<String> d_mapFiles;
	private Set<String> d_playerStrategies = new HashSet<String>();
	private List<List<String>> d_tournamentResults = new ArrayList<List<String>>();

	/**
	 * Creates a new tournament.
	 * 
	 * @param p_mapFiles filenames of map files to use for the tournament.
	 * @param p_numGames number of games to play on each map.
	 * @param p_maxTurns maximum number of turns before a game is called a draw.
	 */
	public Tournament(List<String> p_mapFiles, int p_numGames, int p_maxTurns) {
		d_mapFiles = p_mapFiles;
		d_numGames = p_numGames;
		d_maxTurns = p_maxTurns;
	}

	/**
	 * Adds the name of a player strategy to the set of strategies to use for the
	 * tournament.
	 * 
	 * @param p_stategyName name of the strategy to add.
	 * @return <code>true</code> if the strategy was added.<br>
	 *         <code>false</code> if the strategy was already in the set.
	 */
	public boolean addPlayerStrategy(String p_stategyName) {
		return d_playerStrategies.add(p_stategyName);
	}

	/**
	 * Runs the tournament.
	 * <p>
	 * Plays the games automatically and records the winners of each game.
	 * 
	 * @throws Exception thrown if an unexpected exception occurs while playing.
	 */
	public void run() throws Exception {
		// Redirect the standard output stream to disable output, but still keep track
		// of it
		PrintStream l_defaultOutStream = System.out;
		ByteArrayOutputStream l_mockOutputStream = new ByteArrayOutputStream();
		System.setOut(new PrintStream(l_mockOutputStream));

		try {
			// For each map file
			for (String l_mapFilename : d_mapFiles) {
				List<String> l_gameWinners = new ArrayList<String>();
				// Play the set number of games
				for (int l_i = 0; l_i < d_numGames; l_i++) {
					// Initialize the game
					initializeGame(l_mapFilename);

					// Play the game for maximum number of turns or until there is a winner
					for (int l_turn = 0; l_turn < d_maxTurns; l_turn++) {
						Phase l_phase = GameEngine.GetActivePhase();
						// If active phase is still Gameplay, the game is still going
						if (l_phase instanceof GameplayPhase) {
							// Clear the output from the previous turn
							l_mockOutputStream.reset();
							// Play the next turn
							((GameplayPhase) l_phase).execute();
						} else {
							// If active phase is not Gameplay, the game ended with a winner
							break;
						}
					}

					// Get the winner and add it to the winners list
					String l_winnerName = getWinner(l_mockOutputStream.toString());
					l_gameWinners.add(l_winnerName);

					// Clear the output from the previous game
					l_mockOutputStream.reset();
				}

				// Add the results for this map to the tournament results
				d_tournamentResults.add(l_gameWinners);
			}

			// Reset the game engine
			GameEngine.Initialize();
		} finally {
			// Restore the default output stream
			System.setOut(l_defaultOutStream);
		}
	}

	/**
	 * Builds the table containing the tournament results.
	 * 
	 * @return string containing the tournament results table.
	 */
	public String buildResultsTable() {
		// Ensure tournament has ran
		if (d_tournamentResults.isEmpty()) {
			return "Tournament has not ran yet";
		}

		StringBuilder l_builder = new StringBuilder();

		// Build the table header
		l_builder.append(String.format("%-20s ", "Map"));
		for (int l_i = 1; l_i <= d_numGames; l_i++) {
			l_builder.append(String.format("%-15s ", "Game " + l_i));
		}
		l_builder.append("\n");

		// Builder the row with each game results
		for (int l_i = 0; l_i < d_mapFiles.size(); l_i++) {
			String l_mapFilename = d_mapFiles.get(l_i);
			l_builder.append(String.format("%-20s ", l_mapFilename));
			for (int l_j = 0; l_j < d_numGames; l_j++) {
				String l_winner = d_tournamentResults.get(l_i).get(l_j);
				l_builder.append(String.format("%-15s ", l_winner));
			}
			l_builder.append("\n");
		}

		// Return the built table
		return l_builder.toString();
	}

	/**
	 * Initializes the game.
	 * <p>
	 * Resets the state from the previous game and sets up the next game by loading
	 * the map, adding players and assigning countries.
	 * 
	 * @param p_mapFilename filename of the map file to use in the next game.
	 * @throws Exception thrown if an unexpected exception occurs while
	 *                   initializing.
	 */
	private void initializeGame(String p_mapFilename) throws Exception {
		// Re-initialize the game engine
		GameEngine.Initialize();

		// Load the map
		GameMap l_map = MapLoader.LoadMap(p_mapFilename);
		GameEngine.SetMap(l_map);

		// Create and add players
		for (String l_strategyName : d_playerStrategies) {
			Player l_player = createPlayer(l_strategyName);
			GameEngine.AddPlayer(l_player);
		}

		// Assign countries
		GameEngine.AssignCountries();

		// Switch to Startup phase
		GameEngine.SwitchToNextPhase();
		GameEngine.SwitchToNextPhase();
	}

	/**
	 * Creates a new player based on the given strategy.
	 * <p>
	 * The player name is the name of the strategy capitalized.
	 * 
	 * @param p_strategyName name of the strategy to use.
	 * @return player that was created using the given strategy.
	 */
	private Player createPlayer(String p_strategyName) {
		// Capitalize the player type to create their name
		String l_playerName = p_strategyName.substring(0, 1).toUpperCase() + p_strategyName.substring(1);

		// Create the player
		Player l_player = new Player(l_playerName);

		// Set the player strategy and return the player
		switch (p_strategyName) {
		case "aggressive":
			l_player.SetStrategy(new AggressiveStrategy(l_player));
			return l_player;
		case "benevolent":
			l_player.SetStrategy(new BenevolentStrategy(l_player));
			return l_player;
		case "random":
			l_player.SetStrategy(new RandomStrategy(l_player));
			return l_player;
		case "cheater":
			l_player.SetStrategy(new CheaterStrategy(l_player));
			return l_player;
		default:
			return null;
		}
	}

	/**
	 * Gets the winner of the last game from the output log.
	 * 
	 * @param p_logOutput log output from the last turn.
	 * @return name of the player who won the game if there was a winner.<br>
	 *         <i>Draw</i> otherwise.
	 */
	private String getWinner(String p_logOutput) {
		String[] l_lines = p_logOutput.split("\\r?\\n");
		String l_lastLine = l_lines[l_lines.length - 1];

		String l_winnerName = "Draw";
		if (l_lastLine.contains("wins the game")) {
			l_winnerName = l_lastLine.split("\\s+")[1];
		}

		return l_winnerName;
	}
}
