package ca.concordia.risk.io.commands;

import ca.concordia.risk.GameEngine;
import ca.concordia.risk.game.Card;
import ca.concordia.risk.game.Country;
import ca.concordia.risk.game.Player;
import ca.concordia.risk.game.orders.AdvanceOrder;
import ca.concordia.risk.game.orders.AirliftOrder;
import ca.concordia.risk.game.orders.DeployOrder;
import ca.concordia.risk.game.orders.Order;
import ca.concordia.risk.io.views.ConsoleView;

/**
 * This Class represents an Advance Order Command
 * 
 * @author Sindu
 *
 */

public class AdvanceOrderCommand implements OrderCommand {
	private String d_sourceCountry;
	private String d_targetCountry;
	private String d_targetPlayer;
	private int d_numberOfArmies;
	
	public AdvanceOrderCommand(String d_sourceCountry, String d_targetCountry, String d_targetPlayer,
			int d_numberOfArmies) {
		super();
		this.d_sourceCountry = d_sourceCountry;
		this.d_targetCountry = d_targetCountry;
		this.d_targetPlayer = d_targetPlayer;
		this.d_numberOfArmies = d_numberOfArmies;
	}
	
	public Order buildOrder(Player p_player) {
		ConsoleView l_view = GameEngine.GetView();

		// Validate if the source country exists
		Country l_sourceCountry = GameEngine.GetMap().getCountry(d_sourceCountry);
		if (l_sourceCountry == null) {
			l_view.display("Invalid order: source country " + d_sourceCountry + " does not exist");
			return null;
		}
		return null;


	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		
	}	
	

}
