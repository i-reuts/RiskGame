package ca.concordia.risk.io.commands;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import ca.concordia.risk.GameEngine;
import ca.concordia.risk.game.gamemodes.Tournament;
import ca.concordia.risk.io.views.ConsoleView;
import ca.concordia.risk.utils.MapLoader;

/** Command representing <i>"tournament"</i> operation. */
public class TournamentCommand implements Command {

	private static final Set<String> d_SupportedStrategies = Set.of("human", "aggressive", "benevolent", "random",
			"cheater");

	private List<String> d_mapFilenames = new ArrayList<String>();
	private List<String> d_playerStrategies = new ArrayList<String>();
	private int d_numberOfGames;
	private int d_maxTurns;

	/**
	 * {@inheritDoc}
	 * <p>
	 * Validates the tournament and runs it if it is valid.
	 */
	@Override
	public void execute() {
		ConsoleView l_view = GameEngine.GetView();

		l_view.display("Creating the tournament...");
		Tournament l_tournament = new Tournament(d_mapFilenames, d_numberOfGames, d_maxTurns);
		if (configureTournament(l_tournament)) {
			try {
				l_view.display("Running the tournament games...");
				l_tournament.run();

				l_view.display("Tournament finished. Displaying results\n");
				l_view.display(l_tournament.buildResultsTable());
			} catch (Exception l_e) {
				l_view.display("Error: unexpected exception when running a tournament " + l_e.getMessage());
			}
		}
	}

	/**
	 * Adds a map filename to the list of maps to use in the tournament.
	 * 
	 * @param p_filename filename of the map file to add.
	 */
	public void addMapFile(String p_filename) {
		d_mapFilenames.add(p_filename);
	}

	/**
	 * Adds a player strategy name to the list of strategies to use in the
	 * tournament.
	 * 
	 * @param p_strategyName strategy name to add.
	 */
	public void addPlayerStrategy(String p_strategyName) {
		d_playerStrategies.add(p_strategyName);
	}

	/**
	 * Sets the number of games to be played on each map.
	 * 
	 * @param p_numGames number of games to play on each map.
	 */
	public void setNumberOfGames(int p_numGames) {
		d_numberOfGames = p_numGames;
	}

	/**
	 * Sets the turn limit for each tournament game.
	 * 
	 * @param p_maxTurns maximum number of turns to play in each game.
	 */
	public void setMaxTurns(int p_maxTurns) {
		d_maxTurns = p_maxTurns;
	}

	/**
	 * Validates and configures the tournament.
	 * 
	 * @param p_tournament tournament to configure.
	 * @return <code>true</code> if the tournament was successfully configured.<br>
	 *         <code>false</code> if some of the parameters were invalid and
	 *         configuration failed.
	 */
	private boolean configureTournament(Tournament p_tournament) {
		ConsoleView l_view = GameEngine.GetView();

		// Ensure all map files exist
		if (!validateMapFiles()) {
			return false;
		}

		// Validate player strategies
		for (String l_playerStrategy : d_playerStrategies) {
			// Check if the strategy is a known supported strategy
			if (!d_SupportedStrategies.contains(l_playerStrategy)) {
				l_view.display("Error: invalid player strategy " + l_playerStrategy);
				return false;
			}

			// Add the player strategy to the tournament if it is not duplicated
			if (!p_tournament.addPlayerStrategy(l_playerStrategy)) {
				l_view.display("Error: duplicated player strategy " + l_playerStrategy);
				return false;
			}
		}

		// Map files were validated and all players were created successfully
		return true;
	}

	/**
	 * Validates the existence of map files used for the tournament.
	 * 
	 * @return <code>true</code> if all map files exist.<br>
	 *         <code>false</code> otherwise.
	 */
	private boolean validateMapFiles() {
		for (String l_mapFilename : d_mapFilenames) {
			File l_mapFile = new File(MapLoader.GetMapFolderPath() + l_mapFilename);
			if (!l_mapFile.exists()) {
				GameEngine.GetView().display("Error: map file " + l_mapFilename + " does not exist");
				return false;
			}
		}
		return true;
	}

}
