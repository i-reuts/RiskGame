package ca.concordia.risk.utils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

/**
 * This class represents a log file writer.
 * <p>
 * It acts as an observer of <code>LogEntryBuffer</code> and updates the content
 * of the log file whenever it is notified that the buffer changed.
 */
public class LogFileWriter implements Observer {

	private static final String d_LogFilePath = "logs/gamelog.log";
	private PrintWriter d_logWriter;

	/**
	 * Opens the game log file to write the logs to.
	 * <p>
	 * Creates the the log file if it does not exist.
	 * 
	 * @throws FileNotFoundException thrown if opening the log file fails.
	 */
	public void openLogFile() throws FileNotFoundException {
		d_logWriter = new PrintWriter(new FileOutputStream(d_LogFilePath, false));
	}

	/**
	 * Closes the log file.
	 */
	public void closeLogFile() {
		d_logWriter.close();
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Writes updated log buffer content to the log file.
	 */
	@Override
	public void update(Observable p_observable) {
		d_logWriter.write(d_LogFilePath);
	}
}
