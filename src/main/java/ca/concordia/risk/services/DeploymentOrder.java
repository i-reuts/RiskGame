
package ca.concordia.risk.services;

import ca.concordia.risk.game.Country;
import ca.concordia.risk.game.Player;

/**
 * Implement the deployment order
 * 
 * @author Sindu
 *
 */
public class DeploymentOrder {
	/**
	 * Check player is owner of country or not
	 * 
	 * @param player
	 * @param country
	 * @return
	 */
	public boolean isPlayerCountryOwer(Player player, Country country) {
		boolean valid = false;
		if (player != null && country != null && country.getOwner() != null) {
			valid = country.getOwner().equals(player);//TODO Add Code to check reinforcement
		} 
		return valid;
	}
}
