package ca.concordia.risk.io.commands;

import ca.concordia.risk.GameEngine;
import ca.concordia.risk.game.Card;
import ca.concordia.risk.game.Player;
import ca.concordia.risk.game.orders.NegotiateOrder;
import ca.concordia.risk.game.orders.Order;
import ca.concordia.risk.io.views.ConsoleView;

/** Command representing <i>"negotiate"</i> operation. */
public class NegotiateOrderCommand implements OrderCommand {

	private String d_playerName;

	/**
	 * Creates a new <code>NegotiateOrderCommand</code>.
	 * 
	 * @param p_playerName name of the player to negotiate with.
	 */
	public NegotiateOrderCommand(String p_playerName) {
		d_playerName = p_playerName;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Displays information about the order.
	 */
	@Override
	public void execute() {
		ConsoleView l_view = GameEngine.GetView();
		l_view.display("\nNegotiate command to negotiate with " + d_playerName);

	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Builds a negotiate order using the data provided by the user. The order is
	 * considered invalid if the player to negotiate with does not exist.
	 */
	@Override
	public Order buildOrder(Player p_player) {
		ConsoleView l_view = GameEngine.GetView();

		// Ensure target player exists
		Player l_targetPlayer = GameEngine.GetPlayer(d_playerName);
		if (l_targetPlayer == null) {
			l_view.display("Invalid order: player " + d_playerName + " does not exist");
		}
		
		// Validate if the player has a diplomacy card
		if (!p_player.useCard(Card.getDiplomacyCard())) {
			l_view.display("Invalid order: player " + p_player.getName() + " does not have a diplomacy card");
			return null;
		}

		// Build and return the order
		Order l_order = new NegotiateOrder(p_player, l_targetPlayer);
		return l_order;
	}

}
