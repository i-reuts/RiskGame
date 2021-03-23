package ca.concordia.risk.io.commands;

import ca.concordia.risk.GameEngine;
import ca.concordia.risk.game.Country;
import ca.concordia.risk.game.GameMap;
import ca.concordia.risk.game.Player;
import ca.concordia.risk.game.orders.AdvanceOrder;
import ca.concordia.risk.game.orders.Order;
import ca.concordia.risk.io.views.ConsoleView;

/**
 * Command representing <i>"advance"</i> operation.
 * 
 * @author Sindu
 *
 */
public class AdvanceOrderCommand implements OrderCommand {

	private String d_sourceCountry;
	private String d_targetCountry;
	private int d_numberOfArmies;

	/**
	 * Creates a new <code>AdvanceOrderCommand</code>.
	 * 
	 * @param p_sourceCountry  source country which armies are moved from.
	 * @param p_targetCountry  target country where armies are moved to.
	 * @param p_numberOfArmies number of armies to deploy.
	 */
	public AdvanceOrderCommand(String p_sourceCountry, String p_targetCountry, int p_numberOfArmies) {
		d_sourceCountry = p_sourceCountry;
		d_targetCountry = p_targetCountry;
		d_numberOfArmies = p_numberOfArmies;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Builds an Advance order using the data provided by the user. The order is
	 * considered invalid if the source or target country does not exist or the
	 * target country is not adjacent to the source country.
	 */
	@Override
	public Order buildOrder(Player p_player) {
		ConsoleView l_view = GameEngine.GetView();
		GameMap l_map = GameEngine.GetMap();

		// Validate if the source country exists
		Country l_sourceCountry = l_map.getCountry(d_sourceCountry);
		if (l_sourceCountry == null) {
			l_view.display("Invalid order: source country " + d_sourceCountry + " does not exist");
			return null;
		}

		// Validate if the target country exists
		Country l_targetCountry = l_map.getCountry(d_targetCountry);
		if (l_targetCountry == null) {
			l_view.display("Invalid order: target country " + d_targetCountry + " does not exist");
			return null;
		}

		// Validate if target country is adjacent to the source country
		if (!l_sourceCountry.hasNeighbor(l_targetCountry)) {
			l_view.display("Invalid order: " + d_targetCountry + " is not adjacent to " + d_sourceCountry);
			return null;
		}

		// Build and return the order
		Order l_order = new AdvanceOrder(p_player, l_sourceCountry, l_targetCountry, d_numberOfArmies);
		return l_order;
	}

	/** Displays information about the order. */
	@Override
	public void execute() {
		ConsoleView l_view = GameEngine.GetView();
		l_view.display("\nAdvance command to move " + d_numberOfArmies + " armies from country " + d_sourceCountry
				+ " to country " + d_targetCountry + "\n");
	}
}
