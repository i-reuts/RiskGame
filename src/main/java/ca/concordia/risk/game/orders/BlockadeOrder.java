package ca.concordia.risk.game.orders;

import ca.concordia.risk.game.Country;
import ca.concordia.risk.game.Player;

public class BlockadeOrder implements Order{
	
	private Player d_player;
	private Country d_blockadeCountry;
	private int d_armies;
	private String d_status;
	
	public BlockadeOrder(Player p_player, Country p_blockadeCountry, int p_armies) {
		
		d_player = p_player;
		d_blockadeCountry = p_blockadeCountry;
		d_armies = p_armies;
		d_status = "Blockade " + d_armies + " armies in " + p_blockadeCountry.getName();
	}
	
	public String getStatus() {
		return d_status;
	}

	@Override
	public void execute() {
		
		if(isValid()) {	
			d_status = d_player.getName() + " performed the blockade order on " + d_blockadeCountry.getName();
			// triples the no. of armies.
			d_armies = d_armies*3;
			// assign triple no. of armies to the respective country.
			d_blockadeCountry.addArmies(d_armies);
			// makes the respective country as a neutral territory.
			d_player.removeCountry(d_blockadeCountry);
		}
	}

	private boolean isValid() {
		
		if (!d_player.ownsCountry(d_blockadeCountry)) {
			d_status = "Blockade failed: " + d_blockadeCountry.getName() + " is not owned by " + d_player.getName();
			return false;
		}
		
		if (d_armies < 0) {
			d_status = "Blockade failed: Cannot blockade zero or negative amounts";
			return false;
		}
		return true;
	}
}
