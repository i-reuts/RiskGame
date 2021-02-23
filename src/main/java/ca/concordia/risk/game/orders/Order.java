package ca.concordia.risk.game.orders;

/**
 * Interface for Order entities.
 * 
 * @author Enrique
 *
 */
public interface Order {

	/**
	 * Executes the order.
	 */
	public void execute();

	/**
	 * Returns the current status of the order.
	 * <p>
	 * Order status represents the result of executing an order.
	 * 
	 * @return order status string.
	 */
	public String getStatus();
}
