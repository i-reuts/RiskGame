package ca.concordia.risk.io.commands;

import ca.concordia.risk.GameEngine;
import ca.concordia.risk.game.Card;
import ca.concordia.risk.game.Country;
import ca.concordia.risk.game.Player;
import ca.concordia.risk.game.orders.BombOrder;
import ca.concordia.risk.game.orders.Order;
import ca.concordia.risk.io.views.ConsoleView;

/** Command representing <i>"bomb"</i> operation. */
public class BombOrderCommand implements OrderCommand {

	private String d_bombCountry;

	/**
	 * Creates a new <code>BombOrderCommand</code>.
	 * 
	 * @param p_bombCountry Country to be bombed
	 */
	public BombOrderCommand(String p_bombCountry) {
		d_bombCountry = p_bombCountry;
	}

	/** Displays information about the order. */
	@Override
	public void execute() {
		ConsoleView l_view = GameEngine.GetView();
		l_view.display("\nBomb command to bomb the country " + d_bombCountry + "\n");
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Builds a bomb order using the data provided by the user. The order is
	 * considered invalid if the player does not have the bomb card or the target
	 * country does not exist.
	 */
	@Override
	public Order buildOrder(Player p_player) {
		ConsoleView l_view = GameEngine.GetView();

		// Validate if country to be bombed exists
		Country l_bombCountry = GameEngine.GetMap().getCountry(d_bombCountry);
		if (l_bombCountry == null) {
			l_view.display("Invalid order: country " + d_bombCountry + " does not exist");
			return null;
		}

		// Validate if the player has a bomb card
		if (!p_player.useCard(Card.getBombCard())) {
			l_view.display("Invalid order: player " + p_player.getName() + " does not have a bomb card");
			return null;
		}

		// Build and return the order
		Order l_order = new BombOrder(p_player, l_bombCountry);
		return l_order;
	}
}
