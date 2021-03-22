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

	/**
	 * Creates a new <code>AdvanceOrderCommand</code>.
	 * 
	 * @param p_sourceCountry  source country which armies are moved from.
	 * @param p_targetCountry  target country where armies are moved to.
	 * @param d_targetPlayer   target player whose territory armies are moved to.
	 * @param p_numberOfArmies number of armies to deploy.
	 */
	public AdvanceOrderCommand(String d_sourceCountry, String d_targetCountry, String d_targetPlayer,
			int d_numberOfArmies) {
		super();
		this.d_sourceCountry = d_sourceCountry;
		this.d_targetCountry = d_targetCountry;
		this.d_targetPlayer = d_targetPlayer;
		this.d_numberOfArmies = d_numberOfArmies;
	}

	@Override
	public Order buildOrder(Player p_player) {
		ConsoleView l_view = GameEngine.GetView();

		// Validate if the source country exists
		Country l_sourceCountry = GameEngine.GetMap().getCountry(d_sourceCountry);
		if (l_sourceCountry == null) {
			l_view.display("Invalid order: source country " + d_sourceCountry + " does not exist");
			return null;
		}

		// Validate if player owns the source country
		if (!p_player.ownsCountry(l_sourceCountry)) {
			l_view.display("Invalid order: current player does not own the source country " + d_sourceCountry);
			return null;
		}

		// Validate if the target country exists
		Country l_targetCountry = GameEngine.GetMap().getCountry(d_targetCountry);
		if (l_targetCountry == null) {
			l_view.display("Invalid order: target country " + d_targetCountry + " does not exist");
			return null;
		}
		Player t_player = GameEngine.GetPlayer(d_targetPlayer);
		if (t_player == null) {
			l_view.display("Invalid order: target player " + d_targetPlayer + " does not exist");
			return null;
		}
		// Validate if player owns the target country
		if (!t_player.ownsCountry(l_targetCountry)) {
			l_view.display("Invalid order: target player does not own the target country " + d_targetCountry);
			return null;
		}

		// Validate if player has enough reinforcements. If so, retrieve reinforcements
		if (!p_player.retrieveReinforcements(d_numberOfArmies)) {
			l_view.display("Invalid order: can't move " + d_numberOfArmies + " armies. Only "
					+ p_player.getRemainingReinforcements() + " reinforcements left");
			return null;
		}

		// Build and return the order
		Order l_order = new AdvanceOrder(d_numberOfArmies, p_player, t_player, l_sourceCountry, l_targetCountry);
		return l_order;
	}

	@Override
	public void execute() {
		ConsoleView l_view = GameEngine.GetView();
		l_view.display("\nAdvancecommand to move " + d_numberOfArmies + " armies from country " + d_sourceCountry
				+ " to country " + d_targetCountry + "\n");

	}

}
