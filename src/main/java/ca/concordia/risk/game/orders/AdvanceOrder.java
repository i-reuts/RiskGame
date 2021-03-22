package ca.concordia.risk.game.orders;

import ca.concordia.risk.game.Country;
import ca.concordia.risk.game.Player;

/**
 * Class to represent AdvanceOrder command
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


	@Override
	public String getStatus() {
		return d_status;
	}
	

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