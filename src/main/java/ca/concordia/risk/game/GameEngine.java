package ca.concordia.risk.game;

import ca.concordia.risk.io.views.ConsoleView;

/**
 * Main game class containing the game loop.
 */
public class GameEngine {
	
	private static ConsoleView m_View; 
	
	/**
	 * Initializes the <code>GameEngine</code> and starts
	 * the main application loop.
	 */
	public static void start() {
		initialize();
		runMainLoop();
	}
	
	/**
	 * Returns the current view.
	 * @return view currently used by the game.
	 */
	public static ConsoleView getView( ) {
		return m_View;
	}
	
	/** Initializes the <code>GameEngine</code> */
	private static void initialize() {
		m_View = new ConsoleView();
	}
	
	/** Implements the main application loop */
	private static void runMainLoop( ) {
		
	}
	
}
