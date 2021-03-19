package ca.concordia.risk.game.phases;

import ca.concordia.risk.io.parsers.EditorCommandParser;

/**
 * Class representing the Map Editor Phase.
 * 
 * <p>
 * Inherits the default implementation of the parent <code>Phase</code> class
 * for it's execution method. Executing this phase waits for and processes one
 * user command using the <code>EditorCommandParser</code>.
 * 
 * @author Enrique
 *
 */
public class MapEditorPhase extends Phase {
	/**
	 * Creates a new <code>MapEditorPhase</code> object.
	 */
	public MapEditorPhase() {
		d_commandParser = new EditorCommandParser();
	}
}
