package ca.concordia.risk.game;

import java.util.ArrayList;
import java.util.Map;

public class Tournament {
	
	private int d_gameId;
	private int d_turns;
	private ArrayList<String> d_mapFiles;
	private ArrayList<String> d_playerStrategies;
	private ArrayList<String> d_tournamentResult;
	
	
	public Tournament(ArrayList<String> p_mapFiles, ArrayList<String> p_playerStrategies, int p_gameId, int p_turns) {
		d_mapFiles = p_mapFiles;
		d_playerStrategies = p_playerStrategies;
		d_gameId = p_gameId;
		d_turns = p_turns;
		d_tournamentResult = new ArrayList<String>();  
	}

	public ArrayList<String> getTournamentResult() {
		return d_tournamentResult;
	}
	
	public ArrayList<String> getMapFiles() {
		return d_mapFiles;
	}
	
	public void setMapFiles(ArrayList<String> p_mapFiles) {
		d_mapFiles = p_mapFiles;
	}

	public ArrayList<String> getPlayerStrategies() {
		return d_playerStrategies;
	}

	public void setPlayerStrategies(ArrayList<String> p_playerStrategies) {
		d_playerStrategies = p_playerStrategies;
	}
	
	public int getGameId() {
		return d_gameId;
	}

	public void setGameId(int p_gameId) {
		d_gameId = p_gameId;
	}

	public int getTurns() {
		return d_turns;
	}

	public void setTurns(int p_turns) {
		d_turns = p_turns;
	}






	/**
	 * @param d_tournamentResult the d_tournamentResult to set
	 */
	public void setD_tournamentResult(ArrayList<String> d_tournamentResult) {
		this.d_tournamentResult = d_tournamentResult;
	}
	
	
	
}
