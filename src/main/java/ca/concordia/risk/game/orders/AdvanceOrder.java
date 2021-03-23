package ca.concordia.risk.game.orders;

import ca.concordia.risk.game.Country;
import ca.concordia.risk.game.Player;

/**
 * This Class represents Advance Order
 * @author Sindu
 *
 */
public class AdvanceOrder implements Order{
	
	private String d_status;
	private int d_armiesToDeploy;
	private Player d_source_player;
	private Player d_target_player;
	private Country d_source_teritory;
	private Country d_target_teritory;
	
	/**
	 * Creates a new <code>AdvanceOrder</code>.
	 * 
	 * @param p_numArmies 		number of armies to battle.
	 * @param p_sourceCountry   country which armies are battle from.
	 * @param p_targetCountry   country where armies are battle to.
	 * @param p_sourceTeritory  teritory which armies are battle from.
	 * @param p_targetTeritory  teritory where armies are battle to.
	 */
	public AdvanceOrder(String d_status, int d_armiesToDeploy, Player d_source_player, Player d_target_player,
			Country d_source_teritory, Country d_target_teritory) {
		this.d_status = d_status;
		this.d_armiesToDeploy = d_armiesToDeploy;
		this.d_source_player = d_source_player;
		this.d_target_player = d_target_player;
		this.d_source_teritory = d_source_teritory;
		this.d_target_teritory = d_target_teritory;
	}

	@Override
	public void execute() {
		if (isValid()) {
			d_status = d_source_player.getName() + " deployed " + d_armiesToDeploy + " armies from "
					+ d_source_teritory.getName() + " to " + d_target_teritory.getName() + " of "
					+ d_target_player.getName();
			d_source_teritory.removeArmies(d_armiesToDeploy);
			d_target_teritory.addArmies(d_armiesToDeploy);
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
	 * Checks if the player still owns the deploy country.
	 * 
	 * @return <code>true</code> if the player owns the target deploy country.<br>
	 *         <code>false</code> otherwise.
	 */
	private boolean isValid() {
		if (!d_source_player.ownsCountry(d_source_teritory)) {
			d_status = "Deployment failed: " + d_source_teritory + " no longer owned by " + d_source_player.getName();
			return false;
		}
		if (!d_source_player.ownsCountry(d_source_teritory)) {
			d_status = "Deployment failed: " + d_target_teritory + " no longer owned by " + d_target_player.getName();
			return false;
		}
		if (d_source_teritory.getArmies() < d_armiesToDeploy) {
			d_status = "Deployment failed: " + d_source_teritory.getName() + " does not have enough army";
			return false;
		}
		return true;
	}
}	