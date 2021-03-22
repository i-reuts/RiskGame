package ca.concordia.risk;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import ca.concordia.risk.game.GameMap;
import ca.concordia.risk.game.Player;
import ca.concordia.risk.game.orders.Order;
import ca.concordia.risk.game.phases.Phase;
import ca.concordia.risk.game.phases.GameplayPhase;
import ca.concordia.risk.game.phases.MapEditorPhase;
import ca.concordia.risk.game.phases.StartupPhase;
import ca.concordia.risk.io.commands.Command;
import ca.concordia.risk.io.commands.OrderCommand;
import ca.concordia.risk.io.views.ConsoleView;

/**
 * Main game class containing the game loop and acting as the main controller
 * for the game.
 */
public class GameEngine {

	private static Phase d_ActivePhase;
	private static ConsoleView d_View;
	private static GameMap d_ActiveMap;
	private static Map<String, Player> d_ActivePlayers = new TreeMap<String, Player>();

	/**
	 * Startup method.
	 * <p>
	 * Initializes the <code>GameEngine</code> and starts main application loop.
	 * 
	 * @param p_args command-line arguments.
	 */
	public static void main(String[] p_args) {
		Initialize();
		RunMainLoop();
	}

	/**
	 * Transitions to the next phase according to the currently active phase.
	 */
	public static void SwitchToNextPhase() {
		d_ActivePhase.executeOnPhaseEnd();
		d_ActivePhase = d_ActivePhase.getNextPhase();
		d_ActivePhase.executeOnPhaseStart();
	}

	/**
	 * Gets the current view.
	 * 
	 * @return view currently used by the game.
	 */
	public static ConsoleView GetView() {
		return d_View;
	}

	/**
	 * Gets the active game map.
	 * 
	 * @return active game map.
	 */
	public static GameMap GetMap() {
		return d_ActiveMap;
	}
	
	/**
	 * Get the active phase of the game
	 * @return active phase
	 */
	public static Phase GetActivePhase() {
		return d_ActivePhase;
	}

	/**
	 * Sets the active game map.
	 * 
	 * @param p_map game map to set as an active map.
	 */
	public static void SetMap(GameMap p_map) {
		d_ActiveMap = p_map;
	}

	/**
	 * Gets the collection of active players.
	 * 
	 * @return collection of active players.
	 */
	public static Collection<Player> GetPlayers() {
		return d_ActivePlayers.values();
	}

	/**
	 * Gets a player from the list of active players.
	 * 
	 * @param p_name name of the player to get.
	 * @return <code>Player</code> object if the player with specified name
	 *         exists.<br>
	 *         <code>null</code> if the player with specified name does not exist.
	 */
	public static Player GetPlayer(String p_name) {
		return d_ActivePlayers.get(p_name);
	}

	/**
	 * Gets the number of active players.
	 * 
	 * @return number of active players.
	 */
	public static int GetNumberOfPlayers() {
		return d_ActivePlayers.size();
	}

	/**
	 * Adds a new player to the list of active players.
	 * 
	 * @param p_name name of the player to add.
	 */
	public static void AddPlayer(String p_name) {
		Player l_player = new Player(p_name);
		d_ActivePlayers.put(p_name, l_player);
	}

	/**
	 * Removes a player from the list of active players.
	 * 
	 * @param p_name name of the player to remove.
	 */
	public static void RemovePlayer(String p_name) {
		d_ActivePlayers.remove(p_name);
	}

	/** Processes one general application command inputed by user. */
	public static void ProcessUserCommand() {
		d_View.display("\nPlease enter your command:");
		String l_userInput = d_View.getInput();
		Command l_command = d_ActivePhase.parseCommand(l_userInput);
		l_command.execute();
	}

	/**
	 * Processes one player order command inputed by user.
	 * <p>
	 * Keeps asking user for to provide a command until a valid order is received.
	 * 
	 * @param p_player player that is issuing the command.
	 * @return order representing the order issued by the player.
	 */
	public static Order ProcessOrderCommand(Player p_player) {
		Order l_order = null;
		while (l_order == null) {
			d_View.display("\n" + p_player.getName() + ", please enter your command ("
					+ p_player.getRemainingReinforcements() + " reinforcements left):");

			Command l_command = d_ActivePhase.parseCommand(d_View.getInput());
			if (l_command instanceof OrderCommand) {
				l_order = ((OrderCommand) l_command).buildOrder(p_player);
			} else {
				l_command.execute();
			}
		}

		return l_order;
	}

	/** Initializes the <code>GameEngine</code>. */
	protected static void Initialize() {
		// Initialize the view
		d_View = new ConsoleView();

		// Initialize and connect all phases
		Phase l_editorPhase = new MapEditorPhase();
		Phase l_startupPhase = new StartupPhase();
		Phase l_gameplayPhase = new GameplayPhase();

		l_editorPhase.setNextPhase(l_startupPhase);
		l_startupPhase.setNextPhase(l_gameplayPhase);
		l_gameplayPhase.setNextPhase(l_editorPhase);

		// Setup initial phase
		d_ActivePhase = l_editorPhase;
	}

	/**
	 * Executes the main application loop.
	 * <p>
	 * The main loop repeatedly executes the currently active phase.
	 */
	private static void RunMainLoop() {
		while (true) {
			d_ActivePhase.execute();
		}
	}
}
