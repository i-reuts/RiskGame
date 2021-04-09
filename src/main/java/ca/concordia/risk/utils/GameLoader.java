package ca.concordia.risk.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;

import ca.concordia.risk.GameEngine;
import ca.concordia.risk.game.GameMap;
import ca.concordia.risk.game.Player;

public class GameLoader {

	public static void SaveGame() {
		// Create the file path
		File l_saveFile = new File("save\save01");
		if (!l_saveFile.exists()) {
			l_saveFile.getParentFile().mkdirs();
		}
		
		// Build the game state
		GameState l_state = new GameState();
		l_state.turn = 0;
		l_state.map = GameEngine.GetMap();
		l_state.players = GameEngine.GetPlayers();
		
		try(ObjectOutputStream l_objectStream = new ObjectOutputStream(new FileOutputStream(l_saveFile))) {
			l_objectStream.writeObject(l_state);
		} catch(IOException l_e) {
			//TODO: handle exception
		}
	}	
	
	
private static class GameState implements Serializable {
	private int turn;
	private GameMap map;
	private Collection<Player> players;
}

}
