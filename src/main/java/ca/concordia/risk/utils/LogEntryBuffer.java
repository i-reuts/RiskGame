package ca.concordia.risk.utils;

/**
 * This class keeps a log entry of the latest gameplay event.
 * <p>
 * <code>LogEntryBuffer</code> is an Observable, notifying attached observers
 * whenever changes to its internal log entry buffer occur.
 */
public class LogEntryBuffer extends Observable {

	private String d_buffer;

	/**
	 * Sets the buffer to a log message.
	 * 
	 * @param p_logMessage log message to set the buffer to.
	 */
	public void write(String p_logMessage) {
		d_buffer = p_logMessage + "\n";
		notifyObservers();
	}

	/**
	 * Returns the contents of the buffer.
	 * 
	 * @return string representing the buffer contents.
	 */
	public String getBufferContent() {
		return d_buffer;
	}
}
