package ca.concordia.risk.game.orders;

import ca.concordia.risk.game.Country;
import ca.concordia.risk.game.Player;


/**
 * This class represents a bomb order.
 * 
 * @author Ishika
 *
 */
public class BombOrder implements Order {

	private Player d_player;
	private Country d_bombCountry;
	private int d_armiesToBeDestroyed;
	private String d_status;

	/**
	 * Creates a new <code>BombOrder</code>.
	 * 
	 * @param p_player    player giving the order.
	 * @param p_country   country to be bombed.
	 */
	public BombOrder(Player p_player, Country p_country) {
		d_player = p_player;
		d_bombCountry = p_country;
		d_armiesToBeDestroyed = d_bombCountry.getArmies();
		d_status = "Bomb the country " + d_bombCountry.getName();
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Bomb the chosen country if the player is adjacent to it
	 * Does nothing otherwise.
	 */
	@Override
	public void execute() {
		if (isValid()) {
			d_status = d_player.getName() + " bombed the country " + d_bombCountry.getName();
			//execute the order
			d_armiesToBeDestroyed = (d_armiesToBeDestroyed)/2;
			d_bombCountry.removeArmies(d_armiesToBeDestroyed);
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
	 * Checks if the country to be bombed is adjacent to one of the current player’s territories.
	 * 
	 * @return <code>true</code>if the country to be bombed is adjacent to one of the current player’s territories. <br>
	 *         <code>false</code> otherwise.
	 */
	private boolean isValid() {
		//Need to change the condition
		if (!d_player.ownsCountry(d_bombCountry)) {
			//Need to change
			d_status = " Bombing failed: " + d_bombCountry + " no longer adjacent to " + d_player.getName(); 
			return false;
		}

		return true;
	}
}
