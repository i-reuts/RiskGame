package ca.concordia.risk.game.phases;

import ca.concordia.risk.GameEngine;
import ca.concordia.risk.game.Player;
import ca.concordia.risk.game.orders.Order;
import ca.concordia.risk.io.parsers.GameplayCommandParser;
import ca.concordia.risk.io.views.ConsoleView;

/**
 * Class representing the Gameplay Phase.
 * 
 * <p>
 * Executing this phase runs one iteration of the gameplay loop.
 * 
 * @author Enrique
 *
 */
public class GameplayPhase extends Phase {

	/**
	 * Creates a new <code>StartupPhase</code> object.
	 */
	public GameplayPhase() {
		d_commandParser = new GameplayCommandParser();
	}

	/**
	 * Executes one iteration of the gameplay loop.
	 */
	@Override
	public void execute() {
		assignReinforcements();
		issueOrders();
		executeOrders();
	}

	/**
	 * Assigns reinforcements to each player.
	 */
	private void assignReinforcements() {
		for (Player l_p : GameEngine.GetPlayers()) {
			l_p.assignReinfocements();
		}
	}

	/**
	 * Asks each player to issue orders in a round-robin fashion one order at a time
	 * until no players have orders left to give.
	 */
	private void issueOrders() {
		boolean l_allPlayersIssued = false;
		while (!l_allPlayersIssued) {
			l_allPlayersIssued = true;
			for (Player l_p : GameEngine.GetPlayers()) {
				if (!l_p.finishedIssuingOrders()) {
					l_p.issueOrder();
					l_allPlayersIssued = false;
				}
			}
		}
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
			for (Player l_p : GameEngine.GetPlayers()) {
				Order l_order = l_p.nextOrder();
				if (l_order != null) {
					l_order.execute();
					l_allOrdersExecuted = false;

					l_view.display(l_order.getStatus());
				}
			}
		}
	}
}