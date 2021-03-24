package ca.concordia.risk.game.orders;

import ca.concordia.risk.game.Country;
import ca.concordia.risk.game.Player;

/**
 * Class to represent an Airlift command.
 * 
 * @author Enrique
 *
 */
public class AirliftOrder implements Order {

	private Player d_player;
	private Country d_sourceCountry;
	private Country d_targetCountry;
	private int d_armiesToAirlift;
	private String d_status;

	/**
	 * Creates a new <code>AirliftOrder</code>.
	 * 
	 * @param p_player        player giving the order.
	 * @param p_sourceCountry country which armies are airlift from.
	 * @param p_targetCountry country where armies are airlift to.
	 * @param p_numArmies     number of armies to airlift.
	 */
	public AirliftOrder(Player p_player, Country p_sourceCountry, Country p_targetCountry, int p_numArmies) {
		d_player = p_player;
		d_sourceCountry = p_sourceCountry;
		d_targetCountry = p_targetCountry;
		d_armiesToAirlift = p_numArmies;

		d_status = "Airlift " + d_armiesToAirlift + " armies from " + d_sourceCountry.getName() + " to "
				+ d_targetCountry.getName();
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Moves the requested number of armies from the source country to the target
	 * country if the player still owns them both and the source country has armies.
	 * If the number of armies in the source country is less than the requested
	 * number of armies, deploys the number of armies available. Does nothing if the
	 * player no longer owns the country.
	 */
	@Override
	public void execute() {
		if (isValid()) {
			// Check if the source country has enough armies to airlift.
			// If not, set the airlift amount to the number of armies available.
			int l_armiesAvailable = d_sourceCountry.getArmies();
			if (l_armiesAvailable < d_armiesToAirlift) {
				d_status = d_player.getName() + " airlift " + l_armiesAvailable + " armies (out of " + d_armiesToAirlift
						+ " requested) from " + d_sourceCountry.getName() + " to " + d_targetCountry.getName();
				d_armiesToAirlift = l_armiesAvailable;
			} else {
				d_status = d_player.getName() + " airlift " + d_armiesToAirlift + " armies from "
						+ d_sourceCountry.getName() + " to " + d_targetCountry.getName();
			}

			// Perform the transfer of armies
			d_sourceCountry.removeArmies(d_armiesToAirlift);
			d_targetCountry.addArmies(d_armiesToAirlift);
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
	 * Checks if the player still owns both source and target countries and still
	 * has enough armies. If player doesn't has enough armies but still has armies,
	 * deploy the number of armies remaining.
	 * 
	 * @return <code>true</code> if the player owns both countries and has
	 *         armies.<br>
	 *         <code>false</code> otherwise.
	 */
	private boolean isValid() {
		// Check if the player owns the source country
		if (!d_player.ownsCountry(d_sourceCountry)) {
			d_status = "Airlift failed: " + d_sourceCountry.getName() + " not owned by " + d_player.getName();
			return false;
		}

		// Check if the player owns the target country
		if (!d_player.ownsCountry(d_targetCountry)) {
			d_status = "Airlift failed: " + d_targetCountry.getName() + " not owned by " + d_player.getName();
			return false;
		}

		// Check if the source country has armies
		if (d_sourceCountry.getArmies() == 0) {
			d_status = "Airlift failed: " + d_sourceCountry.getName() + " has no armies";
			return false;
		}

		return true;
	}
}
