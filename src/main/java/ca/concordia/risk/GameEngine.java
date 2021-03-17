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
import ca.concordia.risk.phases.IssueOrderPhase;
import ca.concordia.risk.phases.MapEditorPhase;
import ca.concordia.risk.phases.Phase;
import ca.concordia.risk.phases.StartupPhase;

/**
 * Main game class containing the game loop and acting as the main controller
 * for the game.
 */
public class GameEngine {

	/** Enumerable representing supported game modes. **/
	private static enum GameMode {
		EDITOR, STARTUP, GAMEPLAY
	}

	private static Phase d_ActivePhase;
	private static MapEditorPhase d_MapEditorPhase;
	private static StartupPhase d_StartupPhase;
	private static IssueOrderPhase d_IssueOrderPhase;
	
	private static ConsoleView d_View;
	private static GameMode d_ActiveMode;
	private static CommandParser d_ActiveParser;
	private static Map<GameMode, CommandParser> d_ParserMap = new TreeMap<GameMode, CommandParser>();
	private static GameMap d_ActiveMap;
	public static Map<String, Player> d_ActivePlayers = new TreeMap<String, Player>();

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
	
	public static void Parse() {
		
	}

	/**
	 * Sets the active game map.
	 * 
	 * @param p_map game map to set as an active map.
	 */
	public static void SetMap(GameMap p_map) {
		d_ActiveMap = p_map;
	}

	/** Changes active game mode to Startup. */
	public static void SwitchToStartupMode() {
		d_ActivePhase = d_StartupPhase;
	}

	/** Changes active game mode to Gameplay. */
	public static void SwitchToIssueOrderMode() {
		d_ActivePhase = d_IssueOrderPhase;
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

	/** Assigns countries randomly to active players. */
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

	/** Initializes the <code>GameEngine</code>. */
	private static void Initialize() {
		// Initialize the view
		d_View = new ConsoleView();

		// Initialize and register Command Parsers
		d_ParserMap.put(GameMode.EDITOR, new EditorCommandParser());
		d_ParserMap.put(GameMode.STARTUP, new StartupCommandParser());
		d_ParserMap.put(GameMode.GAMEPLAY, new GameplayCommandParser());

		// Initialize all phases
		d_MapEditorPhase = new MapEditorPhase(new EditorCommandParser());
		d_StartupPhase = new StartupPhase(new StartupCommandParser());
		d_IssueOrderPhase = new IssueOrderPhase(new GameplayCommandParser());
		
		// Setup initial phase
		d_ActivePhase = d_MapEditorPhase;

		// Initialize GameMode
		ChangeMode(GameMode.EDITOR);
	}

	/** Executes the main application loop. */
	private static void RunMainLoop() {
		while (true) {
			while (d_ActivePhase.equals(d_MapEditorPhase) || d_ActivePhase.equals(d_StartupPhase)) {
				ProcessUserCommand();
			}

			while (d_ActivePhase.equals(d_IssueOrderPhase)) {
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

	/** Processes one general application command inputed by user. */
	private static void ProcessUserCommand() {
		d_View.display("\nPlease enter your command:");
		String l_userInput = d_View.getInput();
		d_ActivePhase.executeCommand(l_userInput);
	}
}
