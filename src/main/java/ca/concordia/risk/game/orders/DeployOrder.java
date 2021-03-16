package ca.concordia.risk.game.orders;

import ca.concordia.risk.game.Country;
import ca.concordia.risk.game.Player;

/**
 * This class represents a deploy order.
 * 
 * @author Sindu
 *
 */
public class DeployOrder implements Order {

	private Player d_player;
	private Country d_deployCountry;
	private int d_armiesToDeploy;
	private String d_status;

	/**
	 * Creates a new <code>DeployOrder</code>.
	 * 
	 * @param p_player    player giving the order.
	 * @param p_country   country to deploy armies to.
	 * @param p_numArmies number of armies to deploy.
	 */
	public DeployOrder(Player p_player, Country p_country, int p_numArmies) {
		d_player = p_player;
		d_deployCountry = p_country;
		d_armiesToDeploy = p_numArmies;
		d_status = "Order not executed";
	}

	/**
	 *{@inheritDoc}
	 * <p>
	 * Checks if the  if the player still owns the deploy country.
	 */
	public void isValid() {
		if (d_player.ownsCountry(d_deployCountry))	{
			return;
		}		

		else {
			d_status = "Deployment failed: " + d_deployCountry + " no longer owned by " + d_player.getName();
			return;	
		}
	}
	
	/**
	 * {@inheritDoc}
	 * <p>
	 * Adds the requested number of armies to the deploy country if the player still
	 * owns it. Does nothing if the player no longer owns the country.
	 */
	@Override
	public void execute() {
		isValid(); 
			
		d_status = d_player.getName() + " deployed " + d_armiesToDeploy + " armies to " + d_deployCountry.getName();
		d_deployCountry.addArmies(d_armiesToDeploy);
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getStatus() {
		return d_status;
	}
}
