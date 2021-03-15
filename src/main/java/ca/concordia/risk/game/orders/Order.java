package ca.concordia.risk.game.orders;

/**
 * Interface for player orders.
 * 
 * @author Enrique
 *
 */
public interface Order {
	
	/**
	 * Returns the validity of the order.
	 *  
	 * @return <code>true</code> if the given condition is valid <br>
	 *         <code>false</code> if the given condition is not valid
	 */
	public boolean isValid();
	
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
