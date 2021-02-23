package ca.concordia.risk.game.orders;

/**
 * Interface class for the Orders entities.
 * 
 * @author Enrique
 *
 */
public interface Order {

	/**
	 * Order API for executing an order.
	 */
	public void execute();
	public String getStatus();
}
