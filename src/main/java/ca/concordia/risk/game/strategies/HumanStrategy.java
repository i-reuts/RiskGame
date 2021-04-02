package ca.concordia.risk.game.strategies;

import ca.concordia.risk.GameEngine;
import ca.concordia.risk.game.Player;
import ca.concordia.risk.game.orders.Order;

/**
 * This class represents the human player strategy.
 * <p>
 * A <code>Player</code> using this strategy asks the user to input orders to be
 * issued.
 */
public class HumanStrategy extends PlayerStrategy {

	/**
	 * {@inheritDoc}
	 */
	public HumanStrategy(Player p_player) {
		super(p_player);
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Human player implementation asks the user to input the order to be issued.
	 */
	@Override
	public Order issueOrder() {
		return GameEngine.ProcessOrderCommand(d_player);
	}
}
