package ca.concordia.risk.game.orders;

import ca.concordia.risk.game.Country;
import ca.concordia.risk.game.Player;

/**
 * Class to represent an Airlift command
 * @author Enrique
 *
 */
public class AirliftOrder implements Order{

	private Player d_player;
	private Country d_sourceCountry;
	private Country d_targetCountry;
	private int d_armiesToAirlift;
	private String d_status;

	/**
	 * Creates a new <code>AirliftOrder</code>.
	 * 
	 * @param p_player    		player giving the order.
	 * @param p_sourceCountry   country which armies are airlift from.
	 * @param p_targetCountry   country where armies are airlift to.
	 * @param p_numArmies 		number of armies to airlift.
	 */
	public AirliftOrder(Player p_player, Country p_sourceCountry, Country p_targetCountry, int p_numArmies) {
		d_player = p_player;
		d_sourceCountry = p_sourceCountry;
		d_targetCountry = p_targetCountry;
		d_armiesToAirlift = p_numArmies;
		
		d_status = "Airlift " + d_armiesToAirlift + " armies from " + d_sourceCountry.getName() + " to " + d_targetCountry.getName();
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Moves the requested number of armies from the source country to the target 
	 * country if the player still owns them both and has enough armies. 
	 * Does nothing if the player no longer owns the country.
	 */
	@Override
	public void execute() {
		if (isValid()) {
			d_status = d_player.getName() + " airlift " + d_armiesToAirlift + " armies from " + d_sourceCountry.getName() + " to " + d_targetCountry.getName();
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
	 * Checks if the player still owns both source and target countries and still has enough armies.
	 * If player doesn't has enough armies but still has armies, deploy the number of armies remaining.
	 * 
	 * @return <code>true</code> if the player owns both countries and has armies.<br>
	 *         <code>false</code> otherwise.
	 */
	private boolean isValid() {
		int l_currentNoArmies = d_sourceCountry.getArmies();
		
		if (!d_player.ownsCountry(d_sourceCountry)) {
			d_status = "Airlift failed: " + d_sourceCountry.getName() + " no longer owned by " + d_player.getName();
			return false;
		}
		
		if (!d_player.ownsCountry(d_targetCountry)) {
			d_status = "Airlift failed: " + d_sourceCountry.getName() + " no longer owned by " + d_player.getName();
			return false;
		}
		
		if (d_armiesToAirlift < 0) {
			d_status = "Airlift failed: Can't Airlift negative amounts";
			return false;
		}
		
		/* TODO: This order is supposed to be executed before any attack takes place. 
		 * Until this behavior is achieve, this condition will remain*/
		if (l_currentNoArmies > 0 && l_currentNoArmies < d_armiesToAirlift) {
			d_armiesToAirlift = l_currentNoArmies;
		}

		return true;
	}
}
