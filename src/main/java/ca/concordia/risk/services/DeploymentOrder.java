package ca.concordia.risk.services;

import ca.concordia.risk.game.Country;

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
	 * @param country
	 * @param armies
	 * @return
	 */
	public static boolean isPlayerCountryOwer(Country country, int armies) {
		return country != null && country.getOwner() != null && country.getOwner().retrieveReinforcements(armies);
	}
}

