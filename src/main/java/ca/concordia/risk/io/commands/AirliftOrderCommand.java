package ca.concordia.risk.io.commands;

import ca.concordia.risk.GameEngine;
import ca.concordia.risk.game.Card;
import ca.concordia.risk.game.Country;
import ca.concordia.risk.game.Player;
import ca.concordia.risk.game.orders.AirliftOrder;
import ca.concordia.risk.game.orders.Order;
import ca.concordia.risk.io.views.ConsoleView;

/** Command representing <i>"airlift"</i> operation. */
public class AirliftOrderCommand implements OrderCommand {

	private String d_sourceCountry;
	private String d_targetCountry;
	private int d_numberOfArmies;

	/**
	 * Creates a new <code>AirliftOrderCommand</code>.
	 * 
	 * @param p_sourceCountry  source country which armies are airlift from.
	 * @param p_targetCountry  target country where armies are airlift to.
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
		l_view.display("\nAirlift command to airlift " + d_numberOfArmies + " armies from country " + d_sourceCountry
				+ " to country " + d_targetCountry + "\n");
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Builds an airlift order using the data provided by the user. The order is
	 * considered invalid if the airlift source of target country does not exist or
	 * the player doesn't have an airlift card.
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

		// Validate if the target country exists
		Country l_targetCountry = GameEngine.GetMap().getCountry(d_targetCountry);
		if (l_targetCountry == null) {
			l_view.display("Invalid order: target country " + d_targetCountry + " does not exist");
			return null;
		}

		// Validate if the player has an airlift card
		if (!p_player.useCard(Card.getAirliftCard())) {
			l_view.display("Invalid order: player " + p_player.getName() + " does not have an airlift card");
			return null;
		}

		// Build and return the order
		Order l_order = new AirliftOrder(p_player, l_sourceCountry, l_targetCountry, d_numberOfArmies);
		return l_order;
	}
}