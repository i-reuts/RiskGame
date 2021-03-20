package ca.concordia.risk.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * This class represents a log file writer.
 * <p>
 * It acts as an observer of <code>LogEntryBuffer</code> and updates the content
 * of the log file whenever it is notified that the buffer changed.
 */
public class LogFileWriter implements Observer {

	private static final String d_LogFilePath = "logs/gamelog.log";
	private static final Charset d_Encoding = StandardCharsets.ISO_8859_1;

	private PrintWriter d_logWriter;

	/**
	 * Opens the game log file to write the logs to.
	 * <p>
	 * Creates the the log file if it does not exist.
	 * 
	 * @throws FileNotFoundException thrown if opening the log file fails.
	 */
	public void openLogFile() throws FileNotFoundException {
		File l_logFile = new File(d_LogFilePath);
		if (!l_logFile.exists()) {
			l_logFile.getParentFile().mkdirs();
		}

		d_logWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(l_logFile, false), d_Encoding));
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
		if (p_observable instanceof LogEntryBuffer) {
			LogEntryBuffer l_entryBuffer = (LogEntryBuffer) p_observable;

			d_logWriter.print(l_entryBuffer.getBufferContent());
			d_logWriter.flush();
		}
	}
}
