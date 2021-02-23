package ca.concordia.risk;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import ca.concordia.risk.game.Country;
import ca.concordia.risk.game.GameMap;
import ca.concordia.risk.game.Player;
import ca.concordia.risk.game.orders.Order;
import ca.concordia.risk.io.commands.Command;
import ca.concordia.risk.io.commands.OrderCommand;
import ca.concordia.risk.io.parsers.CommandParser;
import ca.concordia.risk.io.parsers.EditorCommandParser;
import ca.concordia.risk.io.parsers.GameplayCommandParser;
import ca.concordia.risk.io.parsers.StartupCommandParser;
import ca.concordia.risk.io.views.ConsoleView;

/**
 * Main game class containing the game loop and acting as the main controller
 * for the game.
 */
public class GameEngine {

	/** Enumerable representing supported game modes **/
	private static enum GameMode {
		EDITOR, STARTUP, GAMEPLAY
	}

	private static ConsoleView d_View;
	private static GameMode d_ActiveMode;
	private static CommandParser d_ActiveParser;
	private static Map<GameMode, CommandParser> d_ParserMap = new TreeMap<GameMode, CommandParser>();
	private static GameMap d_ActiveMap;
	private static Map<String, Player> d_ActivePlayers = new TreeMap<String, Player>();

	/**
	 * Startup method.
	 * <p>
	 * Initializes the <code>GameEngine</code> and starts main application loop.
	 * 
	 * @param args command-line arguments.
	 */
	public static void main(String[] args) {
		Initialize();
		RunMainLoop();
	}

	/**
	 * Returns the current view.
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
	 * Sets the active game map.
	 * 
	 * @param p_map game map to set as an active map.
	 */
	public static void SetMap(GameMap p_map) {
		d_ActiveMap = p_map;
	}

	/** Changes active game mode to Startup */
	public static void SwitchToStartupMode() {
		ChangeMode(GameMode.STARTUP);
	}

	/** Changes active game mode to Gameplay */
	public static void SwitchToGameplayMode() {
		ChangeMode(GameMode.GAMEPLAY);
	}

	/**
	 * Gets a player from he list of active players.
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
	 * Adds a new player to the list of active players.
	 * 
	 * @param p_name name of the player to added.
	 */
	public static void AddPlayer(String p_name) {
		Player l_player = new Player(p_name);
		d_ActivePlayers.put(p_name, l_player);
	}

	/**
	 * Removes a player from the list of active players.
	 * 
	 * @param p_name name of the player to be removed.
	 */
	public static void RemovePlayer(String p_name) {
		d_ActivePlayers.remove(p_name);
	}

	/** Assign countries randomly to active players. */
	public static void AssignCountries() {
		// Get all countries and shuffle them randomly
		List<Country> l_countryList = d_ActiveMap.getCountries();
		Collections.shuffle(l_countryList);

		// While there are countries remaining, assign shuffled countries one by one to
		// players in a round-robin fashion
		while (!l_countryList.isEmpty()) {
			for (Player l_player : d_ActivePlayers.values()) {
				if (l_countryList.isEmpty()) {
					break;
				}

				Country l_country = l_countryList.remove(l_countryList.size() - 1);
				l_player.addCountry(l_country);
				l_country.setOwner(l_player);
			}
		}
	}

	/**
	 * Process one player order command inputed by user.
	 * <p>
	 * Keeps asking user for to provide a command until a valid order is received.
	 * 
	 * @param p_player player that is issuing the command.
	 * @return order representing the order issued by the user.
	 */
	public static Order ProcessOrderCommand(Player p_player) {
		Order l_order = null;
		while (l_order == null) {
			d_View.display("\n" + p_player.getName() + ", please enter your command ("
					+ p_player.getRemainingReinforcements() + " reinforcements left):");

			Command l_command = d_ActiveParser.parse(d_View.getInput());
			if (l_command instanceof OrderCommand) {
				l_order = ((OrderCommand) l_command).buildOrder(p_player);
			} else {
				l_command.execute();
			}
		}

		return l_order;
	}

	/**
	 * Changes active game mode.
	 * 
	 * @param p_newMode mode to change to.
	 */
	private static void ChangeMode(GameMode p_newMode) {
		d_ActiveMode = p_newMode;
		d_ActiveParser = d_ParserMap.get(d_ActiveMode);
	}

	/** Initializes the <code>GameEngine</code> */
	private static void Initialize() {
		// Initialize the view
		d_View = new ConsoleView();

		// Initialize and register Command Parsers
		d_ParserMap.put(GameMode.EDITOR, new EditorCommandParser());
		d_ParserMap.put(GameMode.STARTUP, new StartupCommandParser());
		d_ParserMap.put(GameMode.GAMEPLAY, new GameplayCommandParser());

		// Initialize GameMode
		ChangeMode(GameMode.EDITOR);
	}

	/** Executes the main application loop */
	private static void RunMainLoop() {
		while (true) {
			while (d_ActiveMode != GameMode.GAMEPLAY) {
				ProcessUserCommand();
			}

			while (d_ActiveMode == GameMode.GAMEPLAY) {
				AssignReinforcements();
				IssueOrders();
				ExecuteOrders();
			}
		}
	}

	/**
	 * Assigns reinforcements to each player.
	 */
	private static void AssignReinforcements() {
		for (Player l_p : d_ActivePlayers.values()) {
			l_p.assignReinfocements();
		}
	}

	/**
	 * Asks each player to issue orders in a round-robin fashion one order at a time
	 * until no players have orders left to give.
	 */
	private static void IssueOrders() {
		boolean l_allPlayersIssued = false;
		while (!l_allPlayersIssued) {
			l_allPlayersIssued = true;
			for (Player l_p : d_ActivePlayers.values()) {
				if (!l_p.finishedIssuingOrders()) {
					l_p.issueOrder();
					l_allPlayersIssued = false;
				}
			}
		}
	}

	/**
	 * Asks each player to execute their orders in a round-robin fashion one order
	 * at a time until no players have orders remaining in their order queue.
	 */
	private static void ExecuteOrders() {
		d_View.display("\nExecuting orders...");

		boolean l_allOrdersExecuted = false;
		while (!l_allOrdersExecuted) {
			l_allOrdersExecuted = true;
			for (Player l_p : d_ActivePlayers.values()) {
				Order l_order = l_p.nextOrder();
				if (l_order != null) {
					l_order.execute();
					l_allOrdersExecuted = false;

					d_View.display(l_order.getStatus());
				}
			}
		}
	}

	/** Processes one general application command inputed by user */
	private static void ProcessUserCommand() {
		d_View.display("\nPlease enter your command:");
		String l_userInput = d_View.getInput();
		Command l_command = d_ActiveParser.parse(l_userInput);
		l_command.execute();
	}
}
