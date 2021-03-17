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
	
	public void issueOrders() {

	}

}
