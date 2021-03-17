package ca.concordia.risk.phases;

import ca.concordia.risk.GameEngine;
import ca.concordia.risk.game.Player;
import ca.concordia.risk.game.orders.Order;
import ca.concordia.risk.io.parsers.CommandParser;
import ca.concordia.risk.io.views.ConsoleView;

public class OrderExecutionPhase extends Phase{

	public OrderExecutionPhase(CommandParser p_parser) {
		super(p_parser);
	}

	@Override
	public void execute() {
		this.executeOrders();
	}
	
	@Override
	protected void nextPhase() {
		GameEngine.SwitchToIssueOrderMode();
	}
	
	/**
	 * Asks each player to execute their orders in a round-robin fashion one order
	 * at a time until no players have orders remaining in their order queue.
	 */
	private void executeOrders() {
		ConsoleView l_view = GameEngine.GetView();
		l_view.display("\nExecuting orders...");

		boolean l_allOrdersExecuted = false;
		while (!l_allOrdersExecuted) {
			l_allOrdersExecuted = true;
			for (Player l_p : GameEngine.d_ActivePlayers.values()) {
				Order l_order = l_p.nextOrder();
				if (l_order != null) {
					l_order.execute();
					l_allOrdersExecuted = false;

					l_view.display(l_order.getStatus());
				}
			}
		}
		this.nextPhase();
	}
}
