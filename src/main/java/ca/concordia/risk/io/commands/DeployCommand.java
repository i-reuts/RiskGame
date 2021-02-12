package ca.concordia.risk.io.commands;

import ca.concordia.risk.game.GameEngine;
import ca.concordia.risk.io.views.ConsoleView;

/** Command representing <i>"deploy"</i> operation. */
public class DeployCommand implements Command {

	private String d_deployCountry;
	private int d_numberOfArmies;

	/**
	 * Creates a new <code>DeployCommand</code> object.
	 * 
	 * @param p_deployCountry  country to deploy armies to.
	 * @param p_numberOfArmies number of armies to deploy.
	 */
	public DeployCommand(String p_deployCountry, int p_numberOfArmies) {
		d_deployCountry = p_deployCountry;
		d_numberOfArmies = p_numberOfArmies;
	}

	/** Create a deploy order to deploy armies to the specified country. */
	@Override
	public void execute() {
		// TODO Replace with actual implementation
		ConsoleView l_view = GameEngine.GetView();
		l_view.display("\nDeploying " + d_numberOfArmies + " armies to country " + d_deployCountry + "\n");
	}

}
