package ca.concordia.risk.io.commands;

import java.io.IOException;

import ca.concordia.risk.GameEngine;
import ca.concordia.risk.game.GameMap;
import ca.concordia.risk.io.views.ConsoleView;
import ca.concordia.risk.utils.MapLoader;
import ca.concordia.risk.utils.MapValidator;

/** Command representing <i>"savemap"</i> operation. */
public class SaveMapCommand implements Command {

	private String d_filename;
	private String d_fileFormat;

	/**
	 * Creates a new <code>SaveMapCommand</code> object.
	 * 
	 * @param p_filename   filename of the map file to save the active map into.
	 * @param p_fileFormat file format to use for the map file.
	 */
	public SaveMapCommand(String p_filename, String p_fileFormat) {
		d_filename = p_filename;
		d_fileFormat = p_fileFormat;
	}

	/** Saves the active map into the requested map file. */
	@Override
	public void execute() {
		ConsoleView l_view = GameEngine.GetView();
		GameMap l_gameMap = GameEngine.GetMap();

		if (l_gameMap != null) {
			try {
				l_view.display("Validating the map...");
				if (MapValidator.Validate(l_gameMap)) {
					l_view.display("Map is valid. Saving...");
					MapLoader.SaveMap(d_filename, l_gameMap, d_fileFormat);
					l_view.display("Map saved");
				} else {
					l_view.display("Map is invalid: " + MapValidator.getStatus());
					l_view.display("Please fix the map before saving");
				}
			} catch (IOException | IllegalArgumentException l_e) {
				l_view.display("Error when saving the map: " + l_e.getMessage());
			}
		} else {
			l_view.display("No map to save - please load a map first");
		}
	}

}