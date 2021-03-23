package ca.concordia.risk.game.orders;

import ca.concordia.risk.game.Country;
import ca.concordia.risk.game.Player;

/**
 * This Class represents Advance Order
 * 
 * @author Sindu
 *
 */
public class AdvanceOrder implements Order {

	private Player d_player;
	private String d_status;
	private Country d_sourceCountry;
	private Country d_targetCountry;
	private int d_armiesToAdvance;
	

	/**
	 * Creates a new <code>AdvanceOrder</code>.
	 * 
	 * @param p_player          player giving the order.
	 * @param p_armiesToAdvance number of armies to deploy
	 * @param p_sourceCountry   teritory which armies are battle from.
	 * @param p_targetCountry   teritory where armies are battle to.
	 */
	public AdvanceOrder(Player p_player, Country p_sourceCountry, Country p_targetCountry, int p_armiesToAdvance) {
		d_armiesToAdvance = p_armiesToAdvance;
		d_sourceCountry = p_sourceCountry;
		d_targetCountry = p_targetCountry;
		d_player = p_player;

		d_status = "advance " + d_armiesToAdvance + " armies from " + p_sourceCountry + " to " + p_targetCountry;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Advances the requested amount of armies from source country to target country
	 * if the order is valid or does nothing otherwise.
	 */
	@Override
	public void execute() {
		if (isValid()) {
			if (!d_player.ownsCountry(d_targetCountry)) {
				// TODO: Do the battle

			} else {
				// Check if the source country has enough armies to advance.
				// If not, advance the amount to the number of armies available.
				int l_armiesAvailable = d_sourceCountry.getArmies();
				if (l_armiesAvailable < d_armiesToAdvance) {
					d_status = d_player.getName() + " advanced " + l_armiesAvailable + " armies (out of "
							+ d_armiesToAdvance + " requested) from " + d_sourceCountry.getName() + " to "
							+ d_targetCountry.getName();
					d_armiesToAdvance = l_armiesAvailable;
				} else {
					d_status = d_player.getName() + " advanced " + d_armiesToAdvance + " armies from "
							+ d_sourceCountry.getName() + " to " + d_targetCountry.getName();
				}

				// Advance the armies
				d_sourceCountry.removeArmies(d_armiesToAdvance);
				d_targetCountry.addArmies(d_armiesToAdvance);
			}

		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getStatus() {
		return d_status;
	}

	/**
	 * Checks if the player still owns the source country and has armies on it.
	 * 
	 * @return <code>true</code> if the player owns the source country and has
	 *         armies on it.<br>
	 *         <code>false</code> otherwise.
	 */
	private boolean isValid() {
		// Validate that player owns source country
		if (!d_player.ownsCountry(d_sourceCountry)) {
			d_status = "Advance failed: " + d_sourceCountry.getName() + " no longer owned by " + d_player.getName();
			return false;
		}

		// Validate that player owns target country
		if (!d_player.ownsCountry(d_sourceCountry)) {
			d_status = "Advance failed: " + d_targetCountry.getName() + " no longer owned by " + d_player.getName();
			return false;
		}

		// Validate that source country has no armies
		if (d_sourceCountry.getArmies() == 0) {
			d_status = "Advance failed: " + d_sourceCountry.getName() + " has no armies";
			return false;
		}

		return true;
	}
}