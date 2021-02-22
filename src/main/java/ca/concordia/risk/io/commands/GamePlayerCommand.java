package ca.concordia.risk.io.commands;

import java.util.ArrayList;
import java.util.List;

import ca.concordia.risk.game.GameEngine;
import ca.concordia.risk.io.views.ConsoleView;

/** Command representing <i>"gameplayer"</i> operation. */
public class GamePlayerCommand implements Command {

	private List<String> d_playersToAdd = new ArrayList<String>();
	private List<String> d_playersToRemove = new ArrayList<String>();

	/** Adds or removes requested players. */
	@Override
	public void execute() {
		ConsoleView l_view = GameEngine.GetView();

		// Add players
		for (String l_playerName : d_playersToAdd) {
			if (GameEngine.GetPlayer(l_playerName) != null) {
				l_view.display("Add failed: player named " + l_playerName + " already exists");
			} else {
				GameEngine.AddPlayer(l_playerName);
				l_view.display("Player " + l_playerName + " added");
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
	 */
	public void addPlayer(String p_playerName) {
		d_playersToAdd.add(p_playerName);
	}

	/**
	 * Adds a player to the list of players to be removed.
	 * 
	 * @param p_playerName name of the player to remove.
	 */
	public void removePlayer(String p_playerName) {
		d_playersToRemove.add(p_playerName);
	}

}
