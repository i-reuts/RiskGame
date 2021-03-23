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
		d_status = "blockade " + d_armies + " armies in " + p_blockadeCountry.getName();
	}
	
	@Override
	public void execute() {
		
		if(isValid()) {
			
			d_status = d_player.getName() + " performed the blockade order on " + d_blockadeCountry.getName();
			d_armies = d_armies*3;
			d_player.removeCountry(d_blockadeCountry);
			
		}
		
	}

	private boolean isValid() {
		
		return false;
	}
	
}
