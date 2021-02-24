package ca.concordia.risk.io.commands;

import ca.concordia.risk.GameEngine;
import ca.concordia.risk.io.views.ConsoleView;
import ca.concordia.risk.utils.MapValidator;

/**
 * Command representing <i>"validatemap"</i> operation.
 * 
 * @author ishika
 */
public class ValidateMapCommand implements Command {

	/** Validates the active map. */
	@Override
	public void execute() {

		ConsoleView l_view = GameEngine.GetView();
		l_view.display("Validating the active map...");

		if (MapValidator.Validate(GameEngine.GetMap())) {
			System.out.println("The map is valid");
		} else {
			System.out.println("The map is not valid: " + MapValidator.getStatus());
		}
	}
}