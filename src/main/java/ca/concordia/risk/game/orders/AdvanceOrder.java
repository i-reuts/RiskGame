package ca.concordia.risk.game.orders;

import ca.concordia.risk.game.Country;
import ca.concordia.risk.game.Player;

/**
 * This Class represents Advance Order.
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
				// Check if the source country has enough armies to attack with.
				// If not, attack with the amount of armies available.
				d_status = d_player.getName() + " attacked " + d_targetCountry.getName() + " beloging to "
						+ d_targetCountry.getOwner().getName() + " from " + d_sourceCountry.getName() + ". ";

				performAttack();
			} else {
				// Check if the source country has enough armies to advance.
				// If not, advance the amount of armies available.
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
			d_status = "Advance failed: " + d_sourceCountry.getName() + " not owned by " + d_player.getName();
			return false;
		}

		// Validate that there is no negotiation in place
		if (d_player.isNegotiating(d_targetCountry.getOwner())) {
			d_status = "Advance failed: players " + d_player.getName() + " and " + d_targetCountry.getOwner().getName()
					+ " are currently negotiating";
			return false;
		}

		// Validate that source country has armies deployed in it
		if (d_sourceCountry.getArmies() == 0) {
			d_status = "Advance failed: " + d_sourceCountry.getName() + " has no armies";
			return false;
		}

		return true;
	}

	/**
	 * Executes battle between the attacking and the defending armies.
	 * <p>
	 * If there are attacker armies remaining and no defender armies remaining, the
	 * target country is conquered.<br>
	 * Otherwise remaining armies are returned to their respective countries.
	 */
	private void performAttack() {
		// Remove armies from their countries
		int l_defenderArmies = d_targetCountry.getArmies();
		int l_availableAttackerArmies = d_sourceCountry.getArmies();
		int l_attackerArmies;
		if (d_armiesToAdvance > l_availableAttackerArmies) {
			l_attackerArmies = l_availableAttackerArmies;
			d_status += "Attacker armies: " + l_attackerArmies + " (out of " + d_armiesToAdvance
					+ " requested), defender armies: " + l_defenderArmies + ". ";
		} else {
			l_attackerArmies = d_armiesToAdvance;
			d_status += "Attacker armies: " + l_attackerArmies + ", defender armies: " + l_defenderArmies + ". ";
		}

		d_sourceCountry.removeArmies(l_attackerArmies);
		d_targetCountry.removeArmies(l_defenderArmies);

		// Calculate the number of defenders defeated
		int l_defendersDefeated = 0;
		for (int l_i = 0; l_i < l_attackerArmies; l_i++) {
			if (Math.random() <= 0.6) {
				l_defendersDefeated++;
			}
		}

		// Calculate the number of attackers defeated
		int l_attackersDefeated = 0;
		for (int l_i = 0; l_i < l_defenderArmies; l_i++) {
			if (Math.random() <= 0.7) {
				l_attackersDefeated++;
			}
		}

		// Decrease the number of attackers and defenders
		l_attackerArmies -= l_attackersDefeated;
		if (l_attackerArmies < 0) {
			l_attackerArmies = 0;
		}

		l_defenderArmies -= l_defendersDefeated;
		if (l_defenderArmies < 0) {
			l_defenderArmies = 0;
		}

		// If remaining attacker armies are greater than 0 and defender armies is 0,
		// capture the territory
		if (l_attackerArmies > 0 && l_defenderArmies == 0) {
			d_targetCountry.getOwner().removeCountry(d_targetCountry);

			d_targetCountry.setOwner(d_player);
			d_player.addCountry(d_targetCountry);

			d_targetCountry.addArmies(l_attackerArmies);

			d_status += "Country conquered succesfully with " + l_attackerArmies + " armies remaining";

			// Set the flag indicated that the player conquered a country
			d_player.setEarnedCard(true);
		} else {
			// Otherwise return remaining armies back to their respective countries
			d_sourceCountry.addArmies(l_attackerArmies);
			d_targetCountry.addArmies(l_defenderArmies);

			d_status += "Country was not conquered. " + l_attackerArmies + " attacker armies and " + l_defenderArmies
					+ " defender armies remained.";
		}
	}

}