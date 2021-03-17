package ca.concordia.risk.phases;

import ca.concordia.risk.GameEngine;
import ca.concordia.risk.game.Player;
import ca.concordia.risk.game.orders.Order;
import ca.concordia.risk.io.commands.Command;
import ca.concordia.risk.io.commands.OrderCommand;
import ca.concordia.risk.io.parsers.CommandParser;
import ca.concordia.risk.io.views.ConsoleView;

/**
 * Class representing the Issue Order Phase
 * @author Enrique
 *
 */
public class IssueOrderPhase extends Phase{

	public IssueOrderPhase(CommandParser p_parser) {
		super(p_parser);
	}
	
	public void execute() {
		issueOrders();
	}
	
	/**
	 * Asks each player to issue orders in a round-robin fashion one order at a time
	 * until no players have orders left to give.
	 */
	private void issueOrders() {
		boolean l_allPlayersIssued = false;
		while (!l_allPlayersIssued) {
			l_allPlayersIssued = true;
			for (Player l_p : GameEngine.d_ActivePlayers.values()) {
				if (!l_p.finishedIssuingOrders()) {
					l_p.addOrder(issuePlayerOrder(l_p));
					l_allPlayersIssued = false;
				}
			}
		}
	}
	
	/**
	 * Processes one player order command inputed by user.
	 * <p>
	 * Keeps asking user for to provide a command until a valid order is received.
	 * 
	 * @param p_player player that is issuing the command.
	 * @return order representing the order issued by the player.
	 */
	private Order issuePlayerOrder(Player p_player) {
		Order l_order = null;
		ConsoleView l_view = GameEngine.GetView();
		while (l_order == null) {
			l_view.display("\n" + p_player.getName() + ", please enter your command ("
					+ p_player.getRemainingReinforcements() + " reinforcements left):");

			Command l_command = d_parser.parse(l_view.getInput());
			if (l_command instanceof OrderCommand) {
				l_order = ((OrderCommand) l_command).buildOrder(p_player);
			} else {
				l_command.execute();
			}
		}

		return l_order;
	}

}
