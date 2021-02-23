package ca.concordia.risk.io.commands;

import ca.concordia.risk.game.Player;
import ca.concordia.risk.game.orders.Order;

public interface OrderCommand extends Command {
	public Order buildOrder(Player p_player);
}
