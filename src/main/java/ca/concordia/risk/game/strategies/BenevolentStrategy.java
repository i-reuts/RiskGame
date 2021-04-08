package ca.concordia.risk.game.strategies;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import ca.concordia.risk.game.Card;
import ca.concordia.risk.game.Country;
import ca.concordia.risk.game.Player;
import ca.concordia.risk.game.orders.AirliftOrder;
import ca.concordia.risk.game.orders.BlockadeOrder;
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
	Country d_weakestCountry;

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
				if(l_country.getArmies() == 0) {
					d_weakestCountry = l_country;
					break;
			}
				else
					d_minArmies = Math.min(l_country.getArmies(), d_minArmies);
			}	
			
			for (Country l_country : d_countryList) {
				if(d_minArmies == l_country.getArmies() && l_country.getArmies() != 0)	
					d_weakestCountry = l_country;
			}

			// Add them to the local array of countries (to keep track of armies deploy in
			// this turn)
			d_weakestCountry.addArmies(l_amountToDeploy);

			// Add this country to the list of countries that can advance armies
			d_countrySet.add(d_weakestCountry);

			return new DeployOrder(d_player, d_weakestCountry, l_amountToDeploy);
		}

		d_countryToAdvance = new ArrayList<Country>(d_countrySet);
		
		// Play cards if available
		//Moves its armies in order to reinforce its weaker country 
		//Can only advance armies to its own weaker country
		if (!d_player.getCards().isEmpty()) {
			// Blockade
			if (d_player.useCard(Card.getBlockadeCard())) {
				Country l_c = d_countryToAdvance.get(0);
				d_countryToAdvance.remove(0);
				return new BlockadeOrder(d_player, l_c);
			}
			
			// Airlift
			if (d_player.useCard(Card.getAirliftCard())) {
				Country l_c = d_countryToAdvance.get(0);
				d_countryToAdvance.remove(0);
				d_countryToAdvance.add(d_countryList.get(0));
				return new AirliftOrder(d_player, l_c, d_countryList.get(0), l_c.getArmies());
			}
		}
		// Reset all values for the next round
		d_countrySet = new HashSet<Country>();

		// Finish issuing orders
		d_player.setFinishedIssuingOrder(true);

		return null;
	}
}
