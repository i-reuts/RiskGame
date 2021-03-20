package ca.concordia.risk.utils;

import java.util.HashSet;
import java.util.Set;

/**
 * Observable abstract class.
 * <p>
 * Represents an object that keeps a list of subscribed Observers and notifies
 * them whenever its state changes.
 */
public abstract class Observable {

	private Set<Observer> d_observers = new HashSet<>();

	/**
	 * Attaches a new Observer to this Observable by adding it to the set of
	 * subscribed observers.
	 * 
	 * @param p_observer observer to attach.
	 */
	public void attach(Observer p_observer) {
		d_observers.add(p_observer);
	}

	/**
	 * Detaches an observer from this Observable by removing it from the set of
	 * subscribed observers.
	 * 
	 * @param p_observer observer to detach.
	 */
	public void detach(Observer p_observer) {
		d_observers.remove(p_observer);
	}

	/**
	 * Notifies all attached observers that a change of state occurred.
	 */
	protected void notifyObservers() {
		for (Observer l_observer : d_observers) {
			l_observer.update(this);
		}
	}
}
