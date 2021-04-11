package ca.concordia.risk.game;

import java.util.ArrayList;

/**
 * This class provides the implementation for the Tournament mode.
 *
 */
public class Tournament {

	private int d_gameId;
	private int d_turns;
	private ArrayList<String> d_mapFiles;
	private ArrayList<String> d_playerStrategies;
	private ArrayList<String> d_tournamentResult;

	/**
	 * Creates a new <code>Tournament</code>.
	 * 
	 * @param p_mapFiles         list of maps.
	 * @param p_playerStrategies player strategy type.
	 * @param p_gameId           number of games.
	 * @param p_turns            number of turns.
	 */
	public Tournament(ArrayList<String> p_mapFiles, ArrayList<String> p_playerStrategies, int p_gameId, int p_turns) {
		d_mapFiles = p_mapFiles;
		d_playerStrategies = p_playerStrategies;
		d_gameId = p_gameId;
		d_turns = p_turns;
		d_tournamentResult = new ArrayList<String>();
	}

	/**
	 * Getter for tournament result.
	 * 
	 * @return tournament result.
	 */
	public ArrayList<String> getTournamentResult() {
		return d_tournamentResult;
	}

	/**
	 * Getter for list of tournament maps.
	 * 
	 * @return list of maps.
	 */
	public ArrayList<String> getMapFiles() {
		return d_mapFiles;
	}

	/**
	 * Setter for the tournament maps.
	 * 
	 * @param p_mapFiles list of maps.
	 */
	public void setMapFiles(ArrayList<String> p_mapFiles) {
		d_mapFiles = p_mapFiles;
	}

	/**
	 * Getter for the tournament player strategies.
	 * 
	 * @return tournament player strategies.
	 */
	public ArrayList<String> getPlayerStrategies() {
		return d_playerStrategies;
	}

	/**
	 * Setter for the tournament player strategies.
	 * 
	 * @param p_playerStrategies tournament player strategies.
	 */
	public void setPlayerStrategies(ArrayList<String> p_playerStrategies) {
		d_playerStrategies = p_playerStrategies;
	}

	/**
	 * Getter for number of games in a tournament.
	 * 
	 * @return number of games.
	 */
	public int getGameId() {
		return d_gameId;
	}

	/**
	 * Setter for number of games in a tournament.
	 * 
	 * @param p_gameId number of games.
	 */
	public void setGameId(int p_gameId) {
		d_gameId = p_gameId;
	}

	/**
	 * Getter for number of max turns in a tournament.
	 * 
	 * @return number of turns.
	 */
	public int getTurns() {
		return d_turns;
	}

	/**
	 * Setter for number of max turns in a tournament.
	 * 
	 * @param p_turns number of turns.
	 */
	public void setTurns(int p_turns) {
		d_turns = p_turns;
	}
}
