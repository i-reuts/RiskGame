package ca.concordia.risk.game;

import ca.concordia.risk.io.commands.Command;

public interface OrderCommand extends Command {
	public Order buildOrder(Player p_player);
}
