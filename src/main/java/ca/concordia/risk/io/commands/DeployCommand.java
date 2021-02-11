package ca.concordia.risk.io.commands;

import ca.concordia.risk.game.GameEngine;
import ca.concordia.risk.io.views.ConsoleView;

/** Command representing <i>"deploy"</i> operation. */
public class DeployCommand implements Command {
	
	private String m_deployCountry;
	private int m_numberOfArmies;

	/**
	 * Creates a new <code>DeployCommand</code> object.
	 * @param p_deployCountry country to deploy armies to.
	 * @param p_numberOfArmies number of armies to deploy.
	 */
	public DeployCommand(String p_deployCountry, int p_numberOfArmies) {
		m_deployCountry = p_deployCountry;
		m_numberOfArmies = p_numberOfArmies;
	}
	
	/** Deploys armies to the specified country. */
	@Override
	public void execute() {
		// TODO Replace with actual implementation
		ConsoleView l_view = GameEngine.getView();
		l_view.display("\nDeploying " + m_numberOfArmies + " armies to country " + m_deployCountry +"\n");
	}

}
