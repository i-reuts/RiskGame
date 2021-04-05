package ca.concordia.risk.game.strategies;

import java.util.ArrayList;

import ca.concordia.risk.GameEngine;
import ca.concordia.risk.game.Country;
import ca.concordia.risk.game.Player;
import ca.concordia.risk.game.orders.Order;

/**
 * This class represents the cheater player strategy.
 * <p>
 * A <code>Player</code> that cheats.
 */
public class CheaterStrategy extends PlayerStrategy{
	
	ArrayList<Country> d_countryList;
	
	/**
	 * {@inheritDoc}
	 */
	public CheaterStrategy(Player p_player) {
		super(p_player);
	}
	
	/**
	 * {@inheritDoc}
	 * <p>
	 * Cheater behavior for "issuing" orders.
	 */
	@Override
	public Order issueOrder() {
		d_countryList = new ArrayList<Country>(d_player.getCountries());

		for (Country l_c : d_countryList) {
			for (Country l_n : l_c.getNeighbors()) {
				if (!l_n.getOwner().getName().equals(d_player.getName())) {
					// Get the instance of the game engine
					Country l_conqueredCountry = GameEngine.GetMap().getCountry(l_n.getName());
					
					l_conqueredCountry.getOwner().removeCountry(l_conqueredCountry);
					l_conqueredCountry.setOwner(d_player);
					d_player.addCountry(l_conqueredCountry);
				}
			}
		}
		
		d_countryList = new ArrayList<Country>(d_player.getCountries());
		for (Country l_c : d_countryList) {
			boolean l_duplicateArmies = false;
			int l_maxArmies = 0;
			for (Country l_n : l_c.getNeighbors()) {
				// If a country has a neighbor enemy, double the armies.
				if (!l_n.getOwner().getName().equals(d_player.getName())) {
					l_duplicateArmies = true;
					
					if (l_c.getArmies() > 0) {
						break;
					}
					
					// If owned country does not has armies, then calculate the strongest enemy
					if (l_maxArmies < l_n.getArmies()) {
						l_maxArmies = l_n.getArmies();
					}
				}
			}
			if (l_duplicateArmies) {
				Country l_gameCountry = GameEngine.GetMap().getCountry(l_c.getName());
				
				// Add initial armies based on strongest country if owned country does not has an army
				if (l_gameCountry.getArmies() == 0 && l_maxArmies > 0) {
					l_gameCountry.addArmies(l_maxArmies / 2);
				}

				l_gameCountry.addArmies(l_gameCountry.getArmies() * 2);
			}
		}
		
		d_player.setFinishedIssuingOrder(true);
		return null;
	}

}
