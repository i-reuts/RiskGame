package ca.concordia.risk.io.commands;

import ca.concordia.risk.game.Player;
import ca.concordia.risk.game.orders.Order;

/**
 * Interface implemented by all player order commands.
 * <p>
 * Extends the <code>Command</code> interface to provide additional
 * functionality required to support player orders.
 */
public interface OrderCommand extends Command {
	/**
	 * Builds and gets the order for the given player.
	 * 
	 * @param p_player player to build the order for.
	 * @return order if the order was built successfully.<br>
	 *         <code>null</code> if a valid order could not be built.
	 * 
	 */
	public Order buildOrder(Player p_player);
}
