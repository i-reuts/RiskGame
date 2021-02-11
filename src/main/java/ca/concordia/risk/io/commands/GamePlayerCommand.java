package ca.concordia.risk.io.commands;

import java.util.ArrayList;
import java.util.List;

import ca.concordia.risk.game.GameEngine;
import ca.concordia.risk.io.views.ConsoleView;


/** Command representing <i>"gameplayer"</i> operation. */
public class GamePlayerCommand implements Command {

	private List<String> m_playersToAdd = new ArrayList<String>();
	private List<String> m_playersToRemove = new ArrayList<String>();
	
	/** Adds or removes requested players. */
	@Override
	public void execute() {
		ConsoleView l_view = GameEngine.getView();
		
		// TODO: Replace by the actual implementation
		l_view.display("\nExecuting gameplayer command");
		l_view.display("Players to add: " + m_playersToAdd);
		l_view.display("Players to remove: " + m_playersToRemove + "\n");
	}
	
	/**
	 * Adds a player to the list of players to be added.
	 * @param p_playerName name of the player to add.
	 */
	public void addPlayer(String p_playerName) {
		m_playersToAdd.add(p_playerName);
	}
	
	/**
	 * Adds a player to the list of players to be removed.
	 * @param p_playerName name of the player to remove.
	 */
	public void removePlayer(String p_playerName) {
		m_playersToRemove.add(p_playerName);
	}

}
