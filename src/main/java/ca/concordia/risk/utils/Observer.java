package ca.concordia.risk.utils;

/**
 * Observer interface.
 * <p>
 * Represents a class that wishes to be notified whenever changes happen to the
 * state of an Observable object it is subscribed to.
 */
public interface Observer {
	/**
	 * Notifies the Observer that a change occurred in an Observable it is
	 * subscribed to.
	 * 
	 * @param p_observable observable object the state of which changed.
	 */
	public void update(Observable p_observable);
}
