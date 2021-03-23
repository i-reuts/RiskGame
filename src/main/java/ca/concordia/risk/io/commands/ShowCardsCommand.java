package ca.concordia.risk.io.commands;

import java.util.Collection;
import java.util.Iterator;

import ca.concordia.risk.GameEngine;
import ca.concordia.risk.game.Card;
import ca.concordia.risk.game.Player;
import ca.concordia.risk.io.views.ConsoleView;

/** Command representing <i>"showcards"</i> operation. */
public class ShowCardsCommand implements Command {

	private Player d_player;

	/**
	 * {@inheritDoc}
	 * <p>
	 * Constructs and displays the list of cards owned by a player.
	 */
	@Override
	public void execute() {
		ConsoleView l_view = GameEngine.GetView();

		if (d_player == null) {
			l_view.display("Error: Player is null in showcards command");
			return;
		}

		Collection<Card> l_playerCards = d_player.getCards();
		if (l_playerCards.isEmpty()) {
			l_view.display(d_player.getName() + " has no cards");
		} else {
			StringBuilder l_builder = new StringBuilder("Owned cards: ");
			Iterator<Card> l_it = l_playerCards.iterator();
			while (l_it.hasNext()) {
				l_builder.append(l_it.next().toString());
				if (l_it.hasNext()) {
					l_builder.append(", ");
				}
			}
			l_view.display(l_builder.toString());
		}
	}

	/**
	 * Sets the player used by the command.
	 * 
	 * @param p_player player to display the cards for.
	 */
	public void setPlayer(Player p_player) {
		d_player = p_player;
	}
}
