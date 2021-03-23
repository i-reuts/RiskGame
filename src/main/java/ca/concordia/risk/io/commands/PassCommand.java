package ca.concordia.risk.io.commands;

import ca.concordia.risk.game.Player;

/** Command representing a <i>"pass"</i> command. */
public class PassCommand implements Command {

	private Player d_player;

	/**
	 * {@inheritDoc}
	 * <p>
	 * Sets the flags signifying that the player finished issuing orders.
	 */
	@Override
	public void execute() {
		d_player.setFinishedIssuingOrder(true);
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
