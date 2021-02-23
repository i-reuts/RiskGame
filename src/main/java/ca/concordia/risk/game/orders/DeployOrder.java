package ca.concordia.risk.game.orders;

import ca.concordia.risk.game.Country;
import ca.concordia.risk.game.Player;

/**
 * Implement the deployment order
 * 
 * @author Sindu
 *
 */
public class DeployOrder implements Order {

	private Player d_player;
	private Country d_deployCountry;
	private int d_armiesToDeploy;
	private String d_status;

	public DeployOrder(Player p_player, Country p_country, int p_numArmies) {
		d_player = p_player;
		d_deployCountry = p_country;
		d_armiesToDeploy = p_numArmies;
		d_status = "Order not executed";
	}

	@Override
	public void execute() {
		if (!d_player.ownsCountry(d_deployCountry)) {
			d_status = "Deployment failed: " + d_deployCountry + " no longer owned by " + d_player.getName();
			return;
		}

		d_status = d_player.getName() + " deployed " + d_armiesToDeploy + " armies to " + d_deployCountry.getName();
		d_deployCountry.addArmies(d_armiesToDeploy);
	}

	@Override
	public String getStatus() {
		return d_status;
	}
}
