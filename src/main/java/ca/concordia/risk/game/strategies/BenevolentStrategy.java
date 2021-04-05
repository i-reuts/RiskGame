package ca.concordia.risk.game.strategies;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import ca.concordia.risk.game.Country;
import ca.concordia.risk.game.Player;
import ca.concordia.risk.game.orders.DeployOrder;
import ca.concordia.risk.game.orders.Order;

/**
 * This class represents the Benevolent player strategy.
 * <p>
 * A <code>Computer Player</code> using this strategy focuses on protecting its
 * weak countries (deploys on its weakest country, never attacks, then moves its
 * armies in order to reinforce its weaker country)
 */
public class BenevolentStrategy extends PlayerStrategy {

	ArrayList<Country> d_countryList;
	ArrayList<Country> d_countryToAdvance;
	Set<Country> d_countrySet;
	int d_minArmies;
	Country d_weakCountry;

	/**
	 * {@inheritDoc}
	 */
	public BenevolentStrategy(Player p_player) {
		super(p_player);
		d_countrySet = new HashSet<Country>();
		d_minArmies = p_player.getRemainingReinforcements();
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Computer player deploys on its weakest country, never attacks, then moves its
	 * armies in order to reinforce its weaker country.
	 */
	@Override
	public Order issueOrder() {
		d_countryList = new ArrayList<Country>(d_player.getCountries());

		if (d_player.getRemainingReinforcements() > 0) {

			// Get the number of armies to be deployed
			int l_amountToDeploy = d_player.getRemainingReinforcements();

			for (Country l_country : d_countryList) {
				d_minArmies = Math.min(l_country.getArmies(), d_minArmies);
			}

			for (Country l_country : d_countryList) {
				if (l_country.getArmies() == d_minArmies) {
					d_weakCountry = l_country;
				}
			}

			// Add them to the local array of countries (to keep track of armies deploy in
			// this turn)
			d_weakCountry.addArmies(l_amountToDeploy);

			// Add this country to the list of countries that can advance armies
			d_countrySet.add(d_weakCountry);

			return new DeployOrder(d_player, d_weakCountry, l_amountToDeploy);
		}

		// Reset all values for the next round
		d_countrySet = new HashSet<Country>();

		// Finish issuing orders
		d_player.setFinishedIssuingOrder(true);

		return null;
	}
}
