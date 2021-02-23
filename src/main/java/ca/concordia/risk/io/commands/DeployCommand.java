package ca.concordia.risk.io.commands;

import ca.concordia.risk.game.Country;
import ca.concordia.risk.game.GameEngine;
import ca.concordia.risk.game.Player;
import ca.concordia.risk.game.orders.DeployOrder;
import ca.concordia.risk.game.orders.Order;
import ca.concordia.risk.io.views.ConsoleView;

/** Command representing <i>"deploy"</i> operation. */
public class DeployCommand implements OrderCommand {

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
		ConsoleView l_view = GameEngine.GetView();
		l_view.display(
				"\nDeploy command to deploy " + d_numberOfArmies + " armies to country " + d_deployCountry + "\n");
	}

	@Override
	public Order buildOrder(Player p_player) {
		ConsoleView l_view = GameEngine.GetView();

		// Validate if deploy country exists
		Country l_deployCountry = GameEngine.GetMap().getCountry(d_deployCountry);
		if (l_deployCountry == null) {
			l_view.display("Invalid order: country " + d_deployCountry + " does not exist");
			return null;
		}

		// Validate if player owns the deploy country
		if (!p_player.ownsCountry(l_deployCountry)) {
			l_view.display("Invalid order: current player does not own country " + d_deployCountry);
			return null;
		}

		// Validate if player has enough reinforcements. If so, retrieve reinforcements
		if (!p_player.retrieveReinforcements(d_numberOfArmies)) {
			l_view.display("Invalid order: can't deploy " + d_numberOfArmies + " armies. Only "
					+ p_player.numberOfReinforcementsLeft() + " reinforcements left");
			return null;
		}

		// Build and return the order
		Order l_order = new DeployOrder(p_player, l_deployCountry, d_numberOfArmies);
		return l_order;
	}

}