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
import ca.concordia.risk.io.commands.TournamentCommand;
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

		d_commandParsers.put("tournament", this::parseTournamentCommand);
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

	/**
	 * Parses a <i>"tournament"</i> command.
	 * 
	 * @param p_argumentList list of command arguments.
	 * @return <code>TournamentCommand</code> if the command was parsed
	 *         successfully. <code>InvalidCommand</code> if a parsing error
	 *         occurred.
	 */
	private Command parseTournamentCommand(List<String> p_argumentList) {
		// Set the flags for each required parameter to false
		// This is necessary to allow providing parameters in any order
		boolean l_mapFilesSet = false;
		boolean l_playerStrategiesSet = false;
		boolean l_maxTurnsSet = false;
		boolean l_numGamesSet = false;

		// Create a new tournament command
		TournamentCommand l_command = new TournamentCommand();
		// Parse the parameters until all parameters are parsed or a parsing error
		// occurs
		try {
			while (!p_argumentList.isEmpty()) {
				String l_flag = p_argumentList.remove(0);

				switch (l_flag) {
				case "-M":
					parseTournamentMapFiles(p_argumentList, l_command);
					l_mapFilesSet = true;
					break;
				case "-P":
					parseTournamentStrategies(p_argumentList, l_command);
					l_playerStrategiesSet = true;
					break;
				case "-G":
					parseTournamentNumGames(p_argumentList, l_command);
					l_numGamesSet = true;
					break;
				case "-D":
					parseTournamentMaxTurns(p_argumentList, l_command);
					l_maxTurnsSet = true;
					break;
				default:
					return new InvalidCommand("invalid tournament command flag " + l_flag);
				}
			}
		} catch (ParsingException l_e) {
			return new InvalidCommand(l_e.getMessage());
		}

		// Ensure all parameters were provided
		if (!l_mapFilesSet) {
			return new InvalidCommand("map list was not provided");
		}
		if (!l_playerStrategiesSet) {
			return new InvalidCommand("player strategy list was not provided");
		}
		if (!l_numGamesSet) {
			return new InvalidCommand("number of games was not provided");
		}
		if (!l_maxTurnsSet) {
			return new InvalidCommand("turn limit was not provided");
		}

		// Return the build command
		return l_command;
	}

	/**
	 * Parses the map file list for the tournament command.
	 * 
	 * @param p_argumentList list of command arguments.
	 * @param p_command      command to add the parsed map filenames to.
	 * @throws ParsingException thrown if no map files were provided.
	 */
	private void parseTournamentMapFiles(List<String> p_argumentList, TournamentCommand p_command)
			throws ParsingException {
		int l_mapFilesAdded = 0;
		while (l_mapFilesAdded < 5 && !p_argumentList.isEmpty()) {
			// Get the first argument
			String l_mapFile = p_argumentList.get(0);

			// If the first argument is a flag (starts with "-"), stop parsing map files
			if (l_mapFile.startsWith("-")) {
				break;
			}

			// Argument was a filename, remove it from the argument list and add it to the
			// tournament maps
			p_argumentList.remove(0);
			p_command.addMapFile(l_mapFile);
			l_mapFilesAdded++;
		}

		// If no maps were parsed, report an error
		if (l_mapFilesAdded == 0) {
			throw new ParsingException("no maps were provided in the map file list before the next flag");
		}
	}

	/**
	 * Parses the player strategy list for the tournament command.
	 * 
	 * @param p_argumentList list of command arguments.
	 * @param p_command      command to add the parsed strategy names to.
	 * @throws ParsingException thrown if less than two strategies are provided.
	 */
	private void parseTournamentStrategies(List<String> p_argumentList, TournamentCommand p_command)
			throws ParsingException {
		int l_strategiesAdded = 0;
		while (l_strategiesAdded < 4 && !p_argumentList.isEmpty()) {
			// Get the first argument
			String l_strategy = p_argumentList.get(0);

			// If the first argument is a flag (starts with "-"), stop parsing strategies
			if (l_strategy.startsWith("-")) {
				break;
			}

			// Argument was a strategy, remove it from the argument list and add it to the
			// player strategy list
			p_argumentList.remove(0);
			p_command.addPlayerStrategy(l_strategy);
			l_strategiesAdded++;
		}

		// If less than two strategies were parsed, report an error
		if (l_strategiesAdded < 2) {
			throw new ParsingException(
					"less than two strategies were provided in the strategy list before the next flag");
		}
	}

	/**
	 * Parses the number of games to play on each map for the tournament command.
	 * 
	 * @param p_argumentList list of command arguments.
	 * @param p_command      command to set the number of games for.
	 * @throws ParsingException thrown if the number of games is invalid or not a
	 *                          number.
	 */
	private void parseTournamentNumGames(List<String> p_argumentList, TournamentCommand p_command)
			throws ParsingException {
		try {
			if(p_argumentList.size() < 1) {
				throw new ParsingException("-G flag must have one argument");
			}
			
			int l_numGames = Integer.parseInt(p_argumentList.remove(0));

			if (l_numGames < 1 || l_numGames > 5) {
				throw new ParsingException("number of games should be between 1 and 5");
			}

			p_command.setNumberOfGames(l_numGames);
		} catch (NumberFormatException l_e) {
			throw new ParsingException("turn limit was not a number");
		}
	}

	/**
	 * Parses the maximum number of turns to play in each game for the tournament
	 * command.
	 * 
	 * @param p_argumentList list of command arguments.
	 * @param p_command      command to set the number of turns for.
	 * @throws ParsingException thrown if the number of turns is invalid or not a
	 *                          number.
	 */
	private void parseTournamentMaxTurns(List<String> p_argumentList, TournamentCommand p_command)
			throws ParsingException {
		try {
			if(p_argumentList.size() < 1) {
				throw new ParsingException("-D flag must have one argument");
			}
			
			int l_maxTurns = Integer.parseInt(p_argumentList.remove(0));

			if (l_maxTurns < 10 || l_maxTurns > 50) {
				throw new ParsingException("turn limit should be between 10 and 50");
			}

			p_command.setMaxTurns(l_maxTurns);
		} catch (NumberFormatException l_e) {
			throw new ParsingException("turn limit was not a number");
		}
	}

}
