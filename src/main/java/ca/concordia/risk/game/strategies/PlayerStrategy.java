package ca.concordia.risk.game.strategies;

import ca.concordia.risk.game.Player;
import ca.concordia.risk.game.orders.Order;

/**
 * This abstract class represents the base class for player strategies.
 * <p>
 * All player strategies extend this class.
 */
public abstract class PlayerStrategy {

	/** Player context for this strategy */
	protected Player d_player;

	/**
	 * Creates a new player strategy with the specified player as its context.
	 * 
	 * @param p_player player to use as strategy context.
	 */
	public PlayerStrategy(Player p_player) {
		d_player = p_player;
	}

	/**
	 * Issues one player order.
	 * 
	 * @return an <code>Order</code> object if an order was issued.<br>
	 *         <code>null</code> if no valid order was issued.
	 */
	public abstract Order issueOrder();
}
