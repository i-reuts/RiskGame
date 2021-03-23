package ca.concordia.risk.io.commands;

import ca.concordia.risk.GameEngine;
import ca.concordia.risk.game.Card;
import ca.concordia.risk.game.Country;
import ca.concordia.risk.game.Player;
import ca.concordia.risk.game.orders.BlockadeOrder;
import ca.concordia.risk.game.orders.Order;
import ca.concordia.risk.io.views.ConsoleView;

/** Command representing <i>"blockade"</i> operation. */
public class BlockadeOrderCommand implements OrderCommand {

	private String d_blockadeCountry;

	/**
	 * Creates a new <code>BlockadeOrderCommand</code>.
	 * 
	 * @param p_blockadeCountry country to be blockade.
	 */
	public BlockadeOrderCommand(String p_blockadeCountry) {

		d_blockadeCountry = p_blockadeCountry;

	}

	/** This method displays information about the order. */
	@Override
	public void execute() {

		ConsoleView l_view = GameEngine.GetView();
		l_view.display("\nBlockade command to blockade " + d_blockadeCountry + "\n");
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Builds a blockade order using the data provided by the user. The order is
	 * considered invalid if the player does not have the blockade card, blockade
	 * country does not exist, or does not owns the same.
	 * 
	 */
	@Override
	public Order buildOrder(Player p_player) {

		ConsoleView l_view = GameEngine.GetView();

		// Validate if the player has a blockade card
		if (!p_player.useCard(Card.getBlockadeCard())) {
			l_view.display("Invalid order: player " + p_player.getName() + " does not has a bomb card");
			return null;
		}

		// Validate if country to be blockade exists
		Country l_blockadeCountry = GameEngine.GetMap().getCountry(d_blockadeCountry);
		if (l_blockadeCountry == null) {
			l_view.display("Invalid order: country " + d_blockadeCountry + " does not exist");
			return null;
		}

		// Validate if player owns the blockade country
		if (!p_player.ownsCountry(l_blockadeCountry)) {
			l_view.display("Invalid order: current player does not own " + d_blockadeCountry);
			return null;
		}
		// Build and return the order
		Order l_order = new BlockadeOrder(p_player, l_blockadeCountry);
		return l_order;

	}

}
