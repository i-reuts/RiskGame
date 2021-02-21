package ca.concordia.risk.game;

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
	
	public static Player GetPlayer(String p_name) {
		return d_ActivePlayers.get(p_name);
	}

	public static void AddPlayer(String p_name) {
		Player l_player = new Player(p_name);
		d_ActivePlayers.put(p_name, l_player);
	}

	public static void RemovePlayer(String p_name) {
		d_ActivePlayers.remove(p_name);
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
			ProcessUserCommand();
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
