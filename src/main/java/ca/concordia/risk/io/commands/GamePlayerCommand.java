package ca.concordia.risk.io.commands;

import java.util.ArrayList;
import java.util.List;

import ca.concordia.risk.GameEngine;
import ca.concordia.risk.game.Player;
import ca.concordia.risk.game.strategies.AggressiveStrategy;
import ca.concordia.risk.game.strategies.CheaterStrategy;
import ca.concordia.risk.game.strategies.HumanStrategy;
import ca.concordia.risk.game.strategies.RandomStrategy;
import ca.concordia.risk.io.views.ConsoleView;

/** Command representing <i>"gameplayer"</i> operation. */
public class GamePlayerCommand implements Command {

	private List<NewPlayerData> d_playersToAdd = new ArrayList<NewPlayerData>();
	private List<String> d_playersToRemove = new ArrayList<String>();

	/** Adds or removes requested players. */
	@Override
	public void execute() {
		ConsoleView l_view = GameEngine.GetView();

		// Add players
		for (NewPlayerData l_playerData : d_playersToAdd) {
			if (GameEngine.GetPlayer(l_playerData.d_playerName) != null) {
				l_view.display("Add failed: player named " + l_playerData.d_playerName + " already exists");
			} else {
				Player l_player = createPlayer(l_playerData);
				if (l_player != null) {
					GameEngine.AddPlayer(l_player);
					l_view.display("Player " + l_playerData.d_playerName + " added");
				} else {
					l_view.display("Add failed: player type " + l_playerData.d_playerType + " does not exist");
				}
			}
		}

		// Remove players
		for (String l_playerName : d_playersToRemove) {
			if (GameEngine.GetPlayer(l_playerName) == null) {
				l_view.display("Remove failed: player named " + l_playerName + " does not exist");
			} else {
				GameEngine.RemovePlayer(l_playerName);
				l_view.display("Player " + l_playerName + " removed");
			}
		}
	}

	/**
	 * Adds a player to the list of players to be added.
	 * 
	 * @param p_playerName name of the player to add.
	 * @param p_playerType type of the player to add.
	 */
	public void addPlayer(String p_playerName, String p_playerType) {
		d_playersToAdd.add(new NewPlayerData(p_playerName, p_playerType));
	}

	/**
	 * Adds a player to the list of players to be removed.
	 * 
	 * @param p_playerName name of the player to remove.
	 */
	public void removePlayer(String p_playerName) {
		d_playersToRemove.add(p_playerName);
	}

	/**
	 * Creates a new player with a strategy corresponding to specified player type.
	 * 
	 * @param p_playerData player data containing the player name and type.
	 * @return a new <code>Player</code> with the strategy corresponding to the
	 *         requested type.<br>
	 *         <code>null</code> if the requested player type does not exist.
	 * 
	 */
	private Player createPlayer(NewPlayerData p_playerData) {
		Player l_player = new Player(p_playerData.d_playerName);

		switch (p_playerData.d_playerType) {
		case "human":
			l_player.SetStrategy(new HumanStrategy(l_player));
			return l_player;
		case "random":
			l_player.SetStrategy(new RandomStrategy(l_player));
			return l_player;
		case "cheater":
			l_player.SetStrategy(new CheaterStrategy(l_player));
			return l_player;
		case "aggressive":
			l_player.SetStrategy(new AggressiveStrategy(l_player));
			return l_player;
		default:
			return null;
		}
	}

	/**
	 * Helper class representing {PlayerName, PlayerType} tuple.
	 * <p>
	 * Used for storing players to be added.
	 */
	private static class NewPlayerData {

		public String d_playerName;
		public String d_playerType;

		/**
		 * Creates a new <code>NewPlayerData</code> tuple.
		 * 
		 * @param p_playerName name of the player to add.
		 * @param p_playerType player type representing the player strategy.
		 */
		public NewPlayerData(String p_playerName, String p_playerType) {
			d_playerName = p_playerName;
			d_playerType = p_playerType;
		}
	}

}
