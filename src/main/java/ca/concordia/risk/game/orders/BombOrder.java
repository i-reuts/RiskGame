package ca.concordia.risk.game.orders;

import java.util.Set;

import ca.concordia.risk.GameEngine;
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
	 * @param p_player  player giving the order.
	 * @param p_country country to be bombed.
	 */
	public BombOrder(Player p_player, Country p_country) {
		d_player = p_player;
		d_bombCountry = p_country;
		d_armiesToBeDestroyed = d_bombCountry.getArmies();
		d_status = d_player.getName() + " wants to bomb the country " + d_bombCountry.getName();
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Bomb the chosen country if the player is adjacent to it and the chosen
	 * country is not owned by the player. Does nothing otherwise.
	 */
	@Override
	public void execute() {
		if (isValid()) {
			d_status = d_player.getName() + " bombed the country " + d_bombCountry.getName();
			d_armiesToBeDestroyed = (d_armiesToBeDestroyed) / 2;
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
	 * Checks if the country to be bombed is owned by the player. Also, it checks if
	 * it is adjacent to one of the current player’s territories.
	 * 
	 * @return <code>true</code>if the country to be bombed is not owned by the
	 *         player itself and is adjacent to one of the current player’s
	 *         territories. <br>
	 *         <code>false</code> otherwise.
	 */
	private boolean isValid() {

		Set<Country> l_ownedCountries = d_player.getCountries();
		boolean l_validator1 = false;
		boolean l_validator2 = false;

		for (Country l_ownedCountry : l_ownedCountries) {
			if (l_ownedCountry.equals(d_bombCountry)) {
				l_validator1 = true;
				break;
			}
		}

		for (Country l_ownedCountry : l_ownedCountries) {
			if (l_ownedCountry.isNeighbor(d_bombCountry)) {
				l_validator2 = true;
				break;
			}
		}

		if (l_validator1 == true) {
			d_status = "Bombing failed: Player: " + d_player.getName() + " owns the country to be bombed";
			return false;
		}

		if (l_validator2 == false) {
			d_status = "Bombing failed: None of the current player’s territories is adjacent to the opponent";
			return false;
		}

		return true;
	}
}
