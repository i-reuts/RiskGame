package ca.concordia.risk.io.parsers;

import java.util.List;

import ca.concordia.risk.io.commands.Command;
import ca.concordia.risk.io.commands.EditContinentCommand;
import ca.concordia.risk.io.commands.EditCountryCommand;
import ca.concordia.risk.io.commands.EditMapCommand;
import ca.concordia.risk.io.commands.EditNeighborCommand;
import ca.concordia.risk.io.commands.InvalidCommand;
import ca.concordia.risk.io.commands.LoadMapCommand;
import ca.concordia.risk.io.commands.SaveMapCommand;
import ca.concordia.risk.io.commands.ValidateMapCommand;

/**
 * A <code>CommandParser</code> implementation for the Editor mode.
 * <p>
 * Implements parsers for the editor commands.
 */
public class EditorCommandParser extends CommandParser {

	/**
	 * Overrides parser methods for operations supported in Editor mode.
	 */
	@Override
	protected void registerParserMethods() {
		super.registerParserMethods();

		d_commandParsers.put("editmap", this::parseEditMapCommand);
		d_commandParsers.put("savemap", this::parseSaveMapCommand);
		d_commandParsers.put("validatemap", this::parseValidateMapCommand);
		d_commandParsers.put("editcontinent", this::parseEditContinentCommand);
		d_commandParsers.put("editcountry", this::parseEditCountryCommand);
		d_commandParsers.put("editneighbor", this::parseEditNeighborCommand);
		d_commandParsers.put("loadmap", this::parseLoadMapCommand);
	}

	/**
	 * Parses an <i>"editmap"</i> command.
	 * 
	 * @param p_argumentList list of command arguments.
	 * @return <code>EditMapCommand</code> if the command was parsed successfully.
	 *         <code>InvalidCommand</code> if a parsing error occurred.
	 */
	private Command parseEditMapCommand(List<String> p_argumentList) {
		if (p_argumentList.isEmpty()) {
			return new InvalidCommand("no parameters supplied to editmap command");
		}

		String l_filename = p_argumentList.remove(0);
		return new EditMapCommand(l_filename);
	}

	/**
	 * Parses a <i>"savemap"</i> command.
	 * 
	 * @param p_argumentList list of command arguments.
	 * @return <code>SaveMapCommand</code> if the command was parsed successfully.
	 *         <code>InvalidCommand</code> if a parsing error occurred.
	 */
	private Command parseSaveMapCommand(List<String> p_argumentList) {
		if (p_argumentList.isEmpty()) {
			return new InvalidCommand("savemap command expects two parameters");
		}

		String l_filename = p_argumentList.remove(0);
		String l_fileFormat = p_argumentList.remove(0);
		
		return new SaveMapCommand(l_filename, l_fileFormat);
	}

	/**
	 * Parses a <i>"validatemap"</i> command.
	 * 
	 * @param p_argumentList list of command arguments.
	 * @return <code>ValidateMapCommand</code>.
	 */
	private Command parseValidateMapCommand(List<String> p_argumentList) {
		return new ValidateMapCommand();
	}

	/**
	 * Parses a <i>"loadmap"</i> command.
	 * 
	 * @param p_argumentList list of command arguments.
	 * @return <code>LoadMapCommand</code> if the command was parsed successfully.
	 *         <code>InvalidCommand</code> if a parsing error occurred.
	 */
	private Command parseLoadMapCommand(List<String> p_argumentList) {
		if (p_argumentList.isEmpty()) {
			return new InvalidCommand("no parameters supplied to loadmap command");
		}

		String l_filename = p_argumentList.remove(0);
		return new LoadMapCommand(l_filename);
	}

	/**
	 * Parses an <i>"editcontinent"</i> command.
	 * 
	 * @param p_argumentList list of command arguments.
	 * @return <code>EditContinentCommand</code> if the command was parsed
	 *         successfully. <code>InvalidCommand</code> if a parsing error
	 *         occurred.
	 */
	private Command parseEditContinentCommand(List<String> p_argumentList) {
		if (p_argumentList.isEmpty()) {
			return new InvalidCommand("no parameters supplied to editcontinent command");
		}

		EditContinentCommand l_editCommand = new EditContinentCommand();

		try {
			while (!p_argumentList.isEmpty()) {
				String l_subcommand = p_argumentList.remove(0);
				switch (l_subcommand) {
				case "-add":
					parseAddContinentBlock(p_argumentList, l_editCommand);
					break;
				case "-remove":
					parseRemoveContinentBlock(p_argumentList, l_editCommand);
					break;
				default:
					return new InvalidCommand("unsupported subcommand " + l_subcommand);
				}
			}
		} catch (ParsingException l_e) {
			return new InvalidCommand(l_e.getMessage());
		}

		return l_editCommand;
	}

	/**
	 * Parses an <i>"editcountry"</i> command.
	 * 
	 * @param p_argumentList list of command arguments.
	 * @return <code>EditCountryCommand</code> if the command was parsed
	 *         successfully. <code>InvalidCommand</code> if a parsing error
	 *         occurred.
	 */
	private Command parseEditCountryCommand(List<String> p_argumentList) {
		if (p_argumentList.size() < 2) {
			return new InvalidCommand("no parameters supplied to editcountry command");
		}

		EditCountryCommand l_editCommand = new EditCountryCommand();

		try {
			while (!p_argumentList.isEmpty()) {
				String l_subcommand = p_argumentList.remove(0);
				switch (l_subcommand) {
				case "-add":
					parseAddCountryBlock(p_argumentList, l_editCommand);
					break;
				case "-remove":
					parseRemoveCountryBlock(p_argumentList, l_editCommand);
					break;
				default:
					return new InvalidCommand("unsupported subcommand " + l_subcommand);
				}
			}
		} catch (ParsingException l_e) {
			return new InvalidCommand(l_e.getMessage());
		}

		return l_editCommand;
	}

	/**
	 * Parses an <i>"editneighbor"</i> command.
	 * 
	 * @param p_argumentList list of command arguments.
	 * @return <code>EditNeighborCommand</code> if the command was parsed
	 *         successfully. <code>InvalidCommand</code> if a parsing error
	 *         occurred.
	 */
	private Command parseEditNeighborCommand(List<String> p_argumentList) {
		if (p_argumentList.isEmpty()) {
			return new InvalidCommand("no parameters supplied to editneighbor command");
		}

		EditNeighborCommand l_editCommand = new EditNeighborCommand();

		try {
			while (!p_argumentList.isEmpty()) {
				String l_subcommand = p_argumentList.remove(0);
				switch (l_subcommand) {
				case "-add":
					parseAddNeighborBlock(p_argumentList, l_editCommand);
					break;
				case "-remove":
					parseRemoveNeighborBlock(p_argumentList, l_editCommand);
					break;
				default:
					return new InvalidCommand("unsupported subcommand " + l_subcommand);
				}
			}
		} catch (ParsingException l_e) {
			return new InvalidCommand(l_e.getMessage());
		}

		return l_editCommand;
	}

	/**
	 * Parses one <b>-add</b> block of the <i>"editcontinent"</i> command.
	 * 
	 * @param p_argumentList list of command arguments.
	 * @param p_command      command that the parsed data will be stored into.
	 * @throws ParsingException thrown if the number of arguments received is
	 *                          invalid or inputed Continent Value is not a number.
	 */
	private void parseAddContinentBlock(List<String> p_argumentList, EditContinentCommand p_command)
			throws ParsingException {
		if (p_argumentList.size() < 2) {
			throw new ParsingException("-add command expects two parameters");
		}

		String l_continentName = p_argumentList.remove(0).replace('_', ' ');
		int l_continentValue;
		try {
			l_continentValue = Integer.parseInt(p_argumentList.remove(0));

			if (l_continentValue < 0) {
				throw new ParsingException("continent value cannot be negative");
			}
		} catch (NumberFormatException l_e) {
			throw new ParsingException("continent value was not a number");
		}

		p_command.addContinent(l_continentName, l_continentValue);
	}

	/**
	 * Parses one <b>-remove</b> block of the <i>"editcontinent"</i> command.
	 * 
	 * @param p_argumentList list of command arguments.
	 * @param p_command      command that the parsed data will be stored into.
	 * @throws ParsingException thrown if the number of arguments received is
	 *                          invalid.
	 */
	private void parseRemoveContinentBlock(List<String> p_argumentList, EditContinentCommand p_command)
			throws ParsingException {
		if (p_argumentList.size() < 1) {
			throw new ParsingException("-remove command expects one parameter");
		}

		String l_continentName = p_argumentList.remove(0).replace('_', ' ');
		p_command.removeContinent(l_continentName);
	}

	/**
	 * Parses one <b>-add</b> block of the <i>"editcountry"</i> command.
	 * 
	 * @param p_argumentList list of command arguments.
	 * @param p_command      command that the parsed data will be stored into.
	 * @throws ParsingException thrown if the number of arguments received is
	 *                          invalid.
	 */
	private void parseAddCountryBlock(List<String> p_argumentList, EditCountryCommand p_command)
			throws ParsingException {
		if (p_argumentList.size() < 2) {
			throw new ParsingException("-add command expects two parameters");
		}

		String l_countryName = p_argumentList.remove(0).replace('_', ' ');
		String l_continentName = p_argumentList.remove(0).replace('_', ' ');

		p_command.addCountry(l_countryName, l_continentName);
	}

	/**
	 * Parses one <b>-remove</b> block of the <i>"editcountry"</i> command.
	 * 
	 * @param p_argumentList list of command arguments.
	 * @param p_command      command that the parsed data will be stored into.
	 * @throws ParsingException thrown if the number of arguments received is
	 *                          invalid.
	 */
	private void parseRemoveCountryBlock(List<String> p_argumentList, EditCountryCommand p_command)
			throws ParsingException {
		if (p_argumentList.size() < 1) {
			throw new ParsingException("-remove command expects one parameter");
		}

		String l_countryName = p_argumentList.remove(0).replace('_', ' ');
		p_command.removeCountry(l_countryName);
	}

	/**
	 * Parses one <b>-add</b> block of the <i>"editneighbor"</i> command.
	 * 
	 * @param p_argumentList list of command arguments.
	 * @param p_command      command that the parsed data will be stored into.
	 * @throws ParsingException thrown if the number of arguments received is
	 *                          invalid.
	 */
	private void parseAddNeighborBlock(List<String> p_argumentList, EditNeighborCommand p_command)
			throws ParsingException {
		if (p_argumentList.size() < 2) {
			throw new ParsingException("-add command expects two parameters");
		}

		String l_countryName = p_argumentList.remove(0).replace('_', ' ');
		String l_neighborCountryName = p_argumentList.remove(0).replace('_', ' ');

		p_command.addNeighbor(l_countryName, l_neighborCountryName);
	}

	/**
	 * Parses one <b>-remove</b> block of the <i>"editneighbor"</i> command.
	 * 
	 * @param p_argumentList list of command arguments.
	 * @param p_command      command that the parsed data will be stored into.
	 * @throws ParsingException thrown if the number of arguments received is
	 *                          invalid.
	 */
	private void parseRemoveNeighborBlock(List<String> p_argumentList, EditNeighborCommand p_command)
			throws ParsingException {
		if (p_argumentList.size() < 2) {
			throw new ParsingException("-remove command expects two parameters");
		}

		String l_countryName = p_argumentList.remove(0).replace('_', ' ');
		String l_neighborCountryName = p_argumentList.remove(0).replace('_', ' ');

		p_command.removeNeighbor(l_countryName, l_neighborCountryName);
	}

}
