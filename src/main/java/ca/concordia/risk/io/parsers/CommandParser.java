package ca.concordia.risk.io.parsers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import ca.concordia.risk.io.commands.Command;
import ca.concordia.risk.io.commands.InvalidCommand;
import ca.concordia.risk.io.commands.ShowMapCommand;

/**
 * This abstract class provides the basic functionality of parsing command
 * strings provided by user and converting them into executable commands.
 * 
 * <p>
 * <code>CommandParser</code> operates as follows:
 * <ol>
 * <li>All user commands supported by the application are registered and
 * provided a default implementation that returns an <code>InvalidCommand</code>
 * object.
 * <li>Concrete subclasses of <code>CommandParser</code> can override
 * implementations for commands that they support. In this way, each subclass
 * can define a set of operations supported in the phase it represents.
 * </ol>
 */
public abstract class CommandParser {

	/**
	 * A <code>{CommandString, ParserMethod}</code> map containing the mapping of commands to
	 * their corresponding parsers.
	 */
	protected Map<String, ParserMethod> d_commandParsers = new HashMap<String, ParserMethod>();

	/**
	 * Initializes the <code>CommandParser</code> and registers all user commands
	 * supported by the application.
	 */
	public CommandParser() {
		registerParserMethods();
	}

	/**
	 * Parses a raw command string provided by user and converts it into an
	 * executable <code>Command</code> object.
	 * 
	 * @param p_rawCommand raw command string to be parsed.
	 * @return executable <code>Command</code> constructed from the given command
	 *         string.
	 */
	public Command parse(String p_rawCommand) {
		List<String> l_argList = preprocessCommandString(p_rawCommand);

		if (l_argList.size() == 0) {
			return new InvalidCommand("empty command received");
		}

		String l_rootCommand = l_argList.remove(0);
		ParserMethod l_parserMethod = d_commandParsers.get(l_rootCommand);

		if (l_parserMethod != null) {
			return l_parserMethod.parse(l_argList);
		}

		return new InvalidCommand("unknown command " + l_rootCommand);
	}

	/**
	 * Registers default parser methods for each supported user command.
	 */
	protected void registerParserMethods() {
		// Shared commands
		d_commandParsers.put("showmap", this::parseShowMapCommand);
		
		// Editor commands
		d_commandParsers.put("editmap", this::createUnavailableCommand);
		d_commandParsers.put("savemap", this::createUnavailableCommand);
		d_commandParsers.put("validatemap", this::createUnavailableCommand);
		d_commandParsers.put("editcontinent", this::createUnavailableCommand);
		d_commandParsers.put("editcountry", this::createUnavailableCommand);
		d_commandParsers.put("editneighbor", this::createUnavailableCommand);
		d_commandParsers.put("loadmap", this::createUnavailableCommand);

		// Startup commands
		d_commandParsers.put("gameplayer", this::createUnavailableCommand);
		d_commandParsers.put("assigncountries", this::createUnavailableCommand);

		// Gameplay commands
		d_commandParsers.put("deploy", this::createUnavailableCommand);
		d_commandParsers.put("advance", this::createUnavailableCommand);
		d_commandParsers.put("bomb", this::createUnavailableCommand);
		d_commandParsers.put("airlift", this::createUnavailableCommand);
	}

	/**
	 * Converts a raw command string into a list of arguments.
	 * 
	 * @param p_rawCommand raw command string to be processed.
	 * @return list of command arguments.
	 */
	private List<String> preprocessCommandString(String p_rawCommand) {
		String[] l_args = p_rawCommand.split("\s+");
		List<String> l_argList = new LinkedList<String>(Arrays.asList(l_args));

		return l_argList;
	}

	/**
	 * Parses a <i>"showmap"</i> command.
	 * 
	 * @param p_argumentList list of command arguments.
	 * @return <code>ShowMapCommand</code>.
	 */
	protected Command parseShowMapCommand(List<String> p_argumentList) {
		return new ShowMapCommand(false);
	}

	/**
	 * Creates an <code>InvalidCommand</code> specifying that the given user command
	 * is currently unavailable.
	 * 
	 * @param p_argumentList list of command arguments.
	 * @return <code>InvalidCommand</code> specifying that the give user command is
	 *         currently unavailable.
	 */
	private Command createUnavailableCommand(List<String> p_argumentList) {
		return new InvalidCommand("this command is not available in the current mode");
	}
	
	/** A custom <code>Exception</code> class thrown when a parsing error occurs. */
	@SuppressWarnings("serial")
	protected static class ParsingException extends Exception {
		/**
		 * Creates a new <code>ParsingException</code> object.
		 * 
		 * @param p_message exception message.
		 */
		ParsingException(String p_message) {
			super(p_message);
		}
	}

	/**
	 * A Functional Interface specifying the signature of parser methods used to
	 * parse user commands.
	 */
	@FunctionalInterface
	protected static interface ParserMethod {
		/**
		 * Method signature that all parser methods should be compatible with.
		 * 
		 * @param p_argumentList list of command arguments.
		 * @return <code>Command</code> object representing the parsed user command.
		 */
		Command parse(List<String> p_argumentList);
	}

}
