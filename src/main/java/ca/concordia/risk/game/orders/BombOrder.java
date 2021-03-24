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
		d_status = "bomb country " + d_bombCountry.getName();
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Bombs the chosen country if it is adjacent to one of the player's countries
	 * and is not owned by the player. Does nothing otherwise.<br>
	 * Bombing reduces the number of armies deployed in the target country by half.
	 */
	@Override
	public void execute() {
		if (isValid()) {
			int l_armiesToBeDestroyed = (d_bombCountry.getArmies()) / 2;
			d_bombCountry.removeArmies(l_armiesToBeDestroyed);

			d_status = d_player.getName() + " bombed the country " + d_bombCountry.getName();
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
		// Check if the player is the owner of the country
		if (d_player.ownsCountry(d_bombCountry)) {
			d_status = "Bombing failed: " + d_player.getName() + " owns the country to be bombed";
			return false;
		}

		// Check if the target country is adjacent to one of the player's countries
		boolean l_isAdjacent = false;
		for (Country l_ownedCountry : d_player.getCountries()) {
			if (l_ownedCountry.hasNeighbor(d_bombCountry)) {
				l_isAdjacent = true;
				break;
			}
		}
		if (l_isAdjacent == false) {
			d_status = "Bombing failed: country " + d_bombCountry.getName()
					+ " is not adjacent to any country owned by " + d_player.getName();
			return false;
		}
		
		// Validate that there is no negotiation in place
		if (d_player.isNegotiating(d_bombCountry.getOwner())) {
			d_status = "Bombing failed: players " + d_player + " and " + d_bombCountry.getOwner()
					+ " are currently negotiating";
			return false;
		}

		return true;
	}
}
