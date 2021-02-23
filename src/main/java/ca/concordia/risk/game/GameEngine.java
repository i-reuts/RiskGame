package ca.concordia.risk.game;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import ca.concordia.risk.io.commands.Command;
import ca.concordia.risk.io.parsers.CommandParser;
import ca.concordia.risk.io.parsers.EditorCommandParser;
import ca.concordia.risk.io.parsers.GameplayCommandParser;
import ca.concordia.risk.io.parsers.StartupCommandParser;
import ca.concordia.risk.io.views.ConsoleView;

/**
 * Main game class containing the game loop.
 */
public class GameEngine {

	private static enum GameMode {
		EDITOR, STARTUP, GAMEPLAY
	}

	private static ConsoleView d_View;
	private static GameMode d_ActiveMode;
	private static CommandParser d_ActiveParser;
	private static Map<GameMode, CommandParser> d_ParserMap = new TreeMap<GameMode, CommandParser>();
	private static Map<String, Player> d_ActivePlayers = new TreeMap<String, Player>();
	private static GameMap d_ActiveMap;

	/**
	 * Initializes the <code>GameEngine</code> and starts main application loop.
	 */
	public static void Start() {
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

	/** Changes active game mode to Startup */
	public static void SwitchToStartupMode() {
		ChangeMode(GameMode.STARTUP);
	}

	/** Changes active game mode to Gameplay */
	public static void SwitchToGameplayMode() {
		ChangeMode(GameMode.GAMEPLAY);
	}

	/**
	 * Returns the active game map.
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

	/**
	 * Get Player
	 * 
	 * @param p_name The <code>string</code> name of the <code>player</code>
	 * @return <code>Player</code> entity.
	 */
	public static Player GetPlayer(String p_name) {
		return d_ActivePlayers.get(p_name);
	}

	/**
	 * Add a new player
	 * 
	 * @param p_name <code>Player</code> to be added.
	 */
	public static void AddPlayer(String p_name) {
		Player l_player = new Player(p_name);
		d_ActivePlayers.put(p_name, l_player);
	}

	/**
	 * Remove a player
	 * 
	 * @param p_name <code>Player</code> to be removed.
	 */
	public static void RemovePlayer(String p_name) {
		d_ActivePlayers.remove(p_name);
	}

	/** Assign countries randomly to players */
	public static void AssignCountries() {
		// Get all countries and shuffle them randomly
		List<Country> p_countryList = d_ActiveMap.getCountries();
		Collections.shuffle(p_countryList);

		// While there are countries remaining, assign shuffled countries one by one to
		// players in a round-robin fashion
		while (!p_countryList.isEmpty()) {
			for (Player p_player : d_ActivePlayers.values()) {
				if (p_countryList.isEmpty()) {
					break;
				}
				p_player.addCountry(p_countryList.remove(p_countryList.size() - 1));
			}
		}
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

	/**
	 * Changes active game mode.
	 * 
	 * @param p_newMode mode to change to.
	 */
	private static void ChangeMode(GameMode p_newMode) {
		d_ActiveMode = p_newMode;
		d_ActiveParser = d_ParserMap.get(d_ActiveMode);
	}

	/** Executes the main application loop */
	private static void RunMainLoop() {
		while (true) {
			while(d_ActiveMode != GameMode.GAMEPLAY) {
				ProcessUserCommand();
			}
			
			while(d_ActiveMode == GameMode.GAMEPLAY) {
				AssignReinforcements();
				IssueOrders();
				ExecuteOrders();
			}
		}
	}
	
	private static void AssignReinforcements() {
		for (Player l_p : d_ActivePlayers.values()) {
			l_p.assignReinfocements();
		}
	}
	
	private static void IssueOrders() {
		boolean l_allPlayersIssued = false;
		while (!l_allPlayersIssued) {
			l_allPlayersIssued = false;
			for (Player l_p : d_ActivePlayers.values()) {
				if (!l_p.finishedIssuingOrders()) {
					l_p.issueOrder();
					l_allPlayersIssued = false;
				}
			}
		}
	}
	
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
					
					// d_View.display(l_order.getStatus());
				}
			}
		}
	}

	/** Processes one command inputed by user */
	private static void ProcessUserCommand() {
		d_View.display("\nPlease enter your command:");
		String l_userInput = d_View.getInput();
		Command l_command = d_ActiveParser.parse(l_userInput);
		l_command.execute();
	}
}
