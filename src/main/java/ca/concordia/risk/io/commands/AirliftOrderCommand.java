package ca.concordia.risk.io.commands;

import ca.concordia.risk.GameEngine;
import ca.concordia.risk.game.Country;
import ca.concordia.risk.game.Player;
import ca.concordia.risk.game.orders.AirliftOrder;
import ca.concordia.risk.game.orders.Order;
import ca.concordia.risk.io.views.ConsoleView;

public class AirliftOrderCommand implements OrderCommand {

	private String d_sourceCountry;
	private String d_targetCountry;
	private int d_numberOfArmies;

	/**
	 * Creates a new <code>AirliftOrderCommand</code>.
	 * 
	 * @param l_sourceCountry  source country which armies are airlift from.
	 * @param l_targetCountry  target country where armies are airlift to.
	 * @param p_numberOfArmies number of armies to deploy.
	 */
	public AirliftOrderCommand(String p_sourceCountry, String p_targetCountry, int p_numberOfArmies) {
		d_sourceCountry = p_sourceCountry;
		d_targetCountry = p_targetCountry;
		d_numberOfArmies = p_numberOfArmies;
	}

	/** Displays information about the order. */
	@Override
	public void execute() {
		ConsoleView l_view = GameEngine.GetView();
		l_view.display(
				"\nAirlift command to airlift " + d_numberOfArmies + " armies from country " + d_sourceCountry + " to country " + d_targetCountry + "\n");
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Builds a airlift order using the data provided by the user. The order is
	 * considered invalid if the airlift source of target country does not exist, 
	 * is not owned by the player, the player does not have enough reinforcements to deploy
	 * or it doesn't have a airlift card.
	 */
	@Override
	public Order buildOrder(Player p_player) {
		ConsoleView l_view = GameEngine.GetView();

		// Validate if the source country exists
		Country l_sourceCountry = GameEngine.GetMap().getCountry(d_sourceCountry);
		if (l_sourceCountry == null) {
			l_view.display("Invalid order: source country " + d_sourceCountry + " does not exist");
			return null;
		}

		// Validate if player owns the source country
		if (!p_player.ownsCountry(l_sourceCountry)) {
			l_view.display("Invalid order: current player does not own the source country " + d_sourceCountry);
			return null;
		}

		// Validate if the target country exists
		Country l_targetCountry = GameEngine.GetMap().getCountry(d_targetCountry);
		if (l_targetCountry == null) {
			l_view.display("Invalid order: target country " + d_targetCountry + " does not exist");
			return null;
		}

		// Validate if player owns the target country
		if (!p_player.ownsCountry(l_targetCountry)) {
			l_view.display("Invalid order: current player does not own the target country " + d_targetCountry);
			return null;
		}
		
		// Validate if player has enough reinforcements. If so, retrieve reinforcements
		if (!p_player.retrieveReinforcements(d_numberOfArmies)) {
			l_view.display("Invalid order: can't airlift " + d_numberOfArmies + " armies. Only "
					+ p_player.getRemainingReinforcements() + " reinforcements left");
			return null;
		}

		// Build and return the order
		Order l_order = new AirliftOrder(p_player, l_sourceCountry, l_targetCountry, d_numberOfArmies);
		return l_order;
	}
}