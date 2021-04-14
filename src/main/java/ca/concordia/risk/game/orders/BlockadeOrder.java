package ca.concordia.risk.game.orders;

import ca.concordia.risk.GameEngine;
import ca.concordia.risk.game.Country;
import ca.concordia.risk.game.Player;

/**
 * This class provides the implementation for the Blockade order.
 *
 */
public class BlockadeOrder implements Order {

	private Player d_player;
	private Country d_blockadeCountry;
	private String d_status;

	/**
	 * Creates a new <code>BlockadeOrder</code>.
	 * 
	 * @param p_player          player giving the order.
	 * @param p_blockadeCountry target country on which the blockade order is
	 *                          executed.
	 */
	public BlockadeOrder(Player p_player, Country p_blockadeCountry) {
		d_player = p_player;
		d_blockadeCountry = p_blockadeCountry;
		d_status = "blockade " + p_blockadeCountry.getName();
	}

	/**
	 * {@inheritDoc}
	 */
	public String getStatus() {
		return d_status;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * This method triples the no. of armies in the target territory and makes the
	 * very same territory neutral.
	 */
	@Override
	public void execute() {
		if (isValid()) {
			// triples no. of armies to the respective country.
			d_blockadeCountry.addArmies(d_blockadeCountry.getArmies() * 2);
			// makes the respective country as a neutral territory.
			d_player.removeCountry(d_blockadeCountry);
			Player l_neutralPlayer = GameEngine.GetNeutralPlayer();
			d_blockadeCountry.setOwner(l_neutralPlayer);
			l_neutralPlayer.addCountry(d_blockadeCountry);

			d_status = d_player.getName() + " blockaded " + d_blockadeCountry.getName();
		}
	}

	/**
	 * This method checks if the Player is eligible to give the Blockade Order. It
	 * checks if the Player owns the territory on which the order needs to be
	 * executed.
	 * 
	 * @return <code>true</code> if the player owns the target country and has
	 *         armies.<br>
	 *         <code>false</code> otherwise.
	 */
	private boolean isValid() {
		if (!d_player.ownsCountry(d_blockadeCountry)) {
			d_status = "Blockade failed: " + d_blockadeCountry.getName() + " is not owned by " + d_player.getName();
			return false;
		}

		return true;
	}
}
