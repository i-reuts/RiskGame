package ca.concordia.risk.game.orders;

import ca.concordia.risk.game.Player;

/**
 * This class represents a negotiate order.
 */
public class NegotiateOrder implements Order {

	private Player d_player1;
	private Player d_player2;
	private String d_status;

	/**
	 * Creates a new <code>NegotiateOrder</code>.
	 * 
	 * @param p_player1 player initiating the negotiate.
	 * @param p_player2 target player of the negotiate.
	 */
	public NegotiateOrder(Player p_player1, Player p_player2) {
		d_player1 = p_player1;
		d_player2 = p_player2;

		d_status = "negotiate with " + p_player2.getName();
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Makes the two players involved unable to attack each other until the end of
	 * turn.
	 */
	@Override
	public void execute() {
		d_player1.addActiveNegotiation(d_player2);
		d_player2.addActiveNegotiation(d_player1);

		d_status = "Player " + d_player1.getName() + " negotiating with " + d_player2.getName();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getStatus() {
		return d_status;
	}

}
