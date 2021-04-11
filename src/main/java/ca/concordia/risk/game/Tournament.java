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
		d_tournamentResult = new ArrayList<String>;  
		
	}
	
	
}
