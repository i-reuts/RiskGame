package ca.concordia.risk.game;

import java.util.HashMap;
import java.util.Map;

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
	
	private static enum GameMode { EDITOR, STARTUP, GAMEPLAY }
	
	private static ConsoleView m_View; 
	private static GameMode m_ActiveMode;
	private static CommandParser m_ActiveParser;
	private static Map<GameMode, CommandParser> m_ParserMap = new HashMap<GameMode, CommandParser>();
	
	
	/**
	 * Initializes the <code>GameEngine</code> and starts
	 * main application loop.
	 */
	public static void start() {
		initialize();
		runMainLoop();
	}
	
	/**
	 * Returns the current view.
	 * @return view currently used by the game.
	 */
	public static ConsoleView getView() {
		return m_View;
	}
	
	/** Changes active game mode to Startup */
	public static void switchToStartupMode() {
		changeMode(GameMode.STARTUP);
	}
	
	/** Changes active game mode to Gameplay */
	public static void switchToGameplayMode() {
		changeMode(GameMode.GAMEPLAY);
	}
	
	/** Initializes the <code>GameEngine</code> */
	private static void initialize() {
		// Initialize the view
		m_View = new ConsoleView();
		
		// Initialize and register Command Parsers
		m_ParserMap.put(GameMode.EDITOR, new EditorCommandParser());
		m_ParserMap.put(GameMode.STARTUP, new StartupCommandParser());
		m_ParserMap.put(GameMode.GAMEPLAY, new GameplayCommandParser());
		
		// Initialize GameMode
		changeMode(GameMode.EDITOR);
	}
	
	/** 
	 * Changes active game mode.
	 * @param p_newMode mode to change to.
	 */
	private static void changeMode(GameMode p_newMode) {
		m_ActiveMode = p_newMode;
		m_ActiveParser = m_ParserMap.get(m_ActiveMode);
	}
	
	/** Executes the main application loop */
	private static void runMainLoop() {
		while(true) {
			processUserCommand();
		}
	}
	
	/** Processes one command inputed by user */
    private static void processUserCommand() {
    	m_View.display("Please enter your command");
    	String l_userInput = m_View.getInput();
    	Command l_command = m_ActiveParser.parse(l_userInput);
    	l_command.execute();
    }
}
