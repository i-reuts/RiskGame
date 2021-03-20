package ca.concordia.risk.utils;

/**
 * This class keeps a log of all gameplay events.
 * <p>
 * Log entries are written to a buffer, which can be periodically cleared if
 * desired.<br>
 * <code>LogEntryBuffer</code> is an Observable, notifying attached observers
 * whenever changes to its internal log buffer occur.
 */
public class LogEntryBuffer extends Observable {

	private StringBuilder d_buffer = new StringBuilder();

	/**
	 * Writes a log message to the buffer.
	 * 
	 * @param p_logMessage log message to write.
	 */
	public void write(String p_logMessage) {
		d_buffer.append(p_logMessage);
		notifyObservers();
	}

	/**
	 * Clears the buffer.
	 */
	public void clear() {
		d_buffer = new StringBuilder();
	}

	/**
	 * Returns the contents of the buffer.
	 * 
	 * @return string representing the buffer contents.
	 */
	public String getBufferContent() {
		return d_buffer.toString();
	}
}
