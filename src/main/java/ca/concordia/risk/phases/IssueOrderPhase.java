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
