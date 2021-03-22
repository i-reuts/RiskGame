package ca.concordia.risk.io.commands;

import java.util.HashSet;
import java.util.Set;

import ca.concordia.risk.GameEngine;
import ca.concordia.risk.game.Card;
import ca.concordia.risk.game.Country;
import ca.concordia.risk.game.Player;
import ca.concordia.risk.game.orders.BombOrder;
import ca.concordia.risk.game.orders.Order;
import ca.concordia.risk.io.views.ConsoleView;

/** Command representing <i>"bomb"</i> operation. */
public class BombOrderCommand implements OrderCommand {

	private String d_bombCountry;

	/**
	 * 
	 * @param p_bombCountry
	 */
	public BombOrderCommand(String p_bombCountry) {
		d_bombCountry = p_bombCountry;
	}

	/** Displays information about the order. */
	@Override
	public void execute() {
		ConsoleView l_view = GameEngine.GetView();
		l_view.display("\nBomb command to bomb the country " + d_bombCountry + "\n");
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Builds a bomb order using the data provided by the user. The order is
	 * considered invalid if the bomb country does not exist, the opponent’s
	 * territory is not adjacent to one of the current player’s territories or if
	 * the player does not have the bomb card.
	 * 
	 */
	@Override
	public Order buildOrder(Player p_player) {
		ConsoleView l_view = GameEngine.GetView();
		boolean l_existNeighbor = false;

		// Validate if the player has a bomb card
		if (!p_player.useCard(Card.getBombCard())) {
			l_view.display("Invalid order: player " + p_player.getName() + " does not has a bomb card");
			return null;
		}

		// Validate if country to be bombed exists
		Country l_bombCountry = GameEngine.GetMap().getCountry(d_bombCountry);
		if (l_bombCountry == null) {
			l_view.display("Invalid order: country " + d_bombCountry + " does not exist");
			return null;
		}

		// Validate that the opponent’s territory is adjacent to one of the current
		// player’s territories
		Set<Country> l_ownedCountries = p_player.getCountries();

		for (Country l_ownedCountry : l_ownedCountries) {
			if (l_ownedCountry.isNeighbor(l_bombCountry)) {
				l_existNeighbor = true;
			}
		}

		if (!l_existNeighbor) {
			l_view.display("Invalid order: country " + d_bombCountry + " is not adjacent to the player's territories");
			return null;
		}

		// Build and return the order
		Order l_order = new BombOrder(p_player, l_bombCountry);
		return l_order;
	}
}
