package ca.concordia.risk.io.commands;

import ca.concordia.risk.GameEngine;
import ca.concordia.risk.game.Country;
import ca.concordia.risk.game.Player;
import ca.concordia.risk.game.orders.DeployOrder;
import ca.concordia.risk.game.orders.Order;
import ca.concordia.risk.io.views.ConsoleView;

/** Command representing <i>"deploy"</i> operation. */
public class DeployOrderCommand implements OrderCommand {

	private String d_deployCountry;
	private int d_numberOfArmies;

	/**
	 * Creates a new <code>DeployOrderCommand</code>.
	 * 
	 * @param p_deployCountry  country to deploy armies to.
	 * @param p_numberOfArmies number of armies to deploy.
	 */
	public DeployOrderCommand(String p_deployCountry, int p_numberOfArmies) {
		d_deployCountry = p_deployCountry;
		d_numberOfArmies = p_numberOfArmies;
	}

	/** Displays information about the order. */
	@Override
	public void execute() {
		ConsoleView l_view = GameEngine.GetView();
		l_view.display(
				"\nDeploy command to deploy " + d_numberOfArmies + " armies to country " + d_deployCountry + "\n");
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Builds a deploy order using the data provided by the user. The order is
	 * considered invalid if the deploy country does not exist, is not owned by the
	 * player or the player does not have enough reinforcements to deploy.
	 */
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
					+ p_player.getRemainingReinforcements() + " reinforcements left");
			return null;
		}

		// Build and return the order
		Order l_order = new DeployOrder(p_player, l_deployCountry, d_numberOfArmies);
		return l_order;
	}
}