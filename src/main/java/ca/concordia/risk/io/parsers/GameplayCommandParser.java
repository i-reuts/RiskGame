package ca.concordia.risk.io.parsers;

import java.util.List;

import ca.concordia.risk.io.commands.AdvanceOrderCommand;
import ca.concordia.risk.io.commands.AirliftOrderCommand;
import ca.concordia.risk.io.commands.BlockadeOrderCommand;
import ca.concordia.risk.io.commands.Command;
import ca.concordia.risk.io.commands.DeployOrderCommand;
import ca.concordia.risk.io.commands.BombOrderCommand;
import ca.concordia.risk.io.commands.InvalidCommand;
import ca.concordia.risk.io.commands.ShowMapCommand;

/**
 * A <code>CommandParser</code> implementation for the Gameplay mode.
 * <p>
 * Implements parsers for the gameplay phase commands.
 */
public class GameplayCommandParser extends CommandParser {

	/**
	 * Overrides parser methods for operations supported in Gameplay mode.
	 */
	@Override
	protected void registerParserMethods() {
		super.registerParserMethods();

		d_commandParsers.put("showmap", this::parseShowMapCommand);
		d_commandParsers.put("deploy", this::parseDeployCommand);
		d_commandParsers.put("advance", this::parseAdvanceCommand);
		d_commandParsers.put("bomb", this::parseBombCommand);
		d_commandParsers.put("blockade", this::parseBlockadeCommand);
		d_commandParsers.put("airlift", this::parseAirliftCommand);
	}

	/**
	 * Parses a <i>"showmap"</i> command.
	 * 
	 * @param p_argumentList list of command arguments.
	 * @return <code>ShowMapCommand</code>.
	 */
	@Override
	protected Command parseShowMapCommand(List<String> p_argumentList) {
		return new ShowMapCommand(true);
	}

	/**
	 * Parses a <i>"deploy"</i> command.
	 * 
	 * @param p_argumentList list of command arguments.
	 * @return <code>DeployCommand</code> if the command was parsed successfully.
	 *         <code>InvalidCommand</code> if a parsing error occurred.
	 */
	private Command parseDeployCommand(List<String> p_argumentList) {
		if (p_argumentList.size() < 2) {
			return new InvalidCommand("deploy command expects two arguments");
		}

		String l_deployCountry = p_argumentList.remove(0).replace('_', ' ');
		int l_numberOfArmies;
		try {
			l_numberOfArmies = Integer.parseInt(p_argumentList.remove(0));
			if (l_numberOfArmies <= 0) {
				return new InvalidCommand("number of armies value was negative or zero");
			}
		} catch (NumberFormatException l_e) {
			return new InvalidCommand("number of armies value was not a number");
		}

		return new DeployOrderCommand(l_deployCountry, l_numberOfArmies);
	}

	/**
	 * Parses a <i>"advance"</i> command.
	 * 
	 * @param p_argumentList list of command arguments.
	 * @return <code>AdvanceCommand</code> if the command was parsed successfully.
	 *         <code>InvalidCommand</code> if a parsing error occurred.
	 */
	private Command parseAdvanceCommand(List<String> p_argumentList) {
		if (p_argumentList.size() < 4) {
			return new InvalidCommand("advance command expects three arguments");
		}

		String l_targetPlayer = p_argumentList.remove(0).replace('_', ' ');
		String l_sourceCountry = p_argumentList.remove(0).replace('_', ' ');
		String l_targetCountry = p_argumentList.remove(0).replace('_', ' ');

		int l_numberOfArmies;
		try {
			l_numberOfArmies = Integer.parseInt(p_argumentList.remove(0));
			if (l_numberOfArmies <= 0) {
				return new InvalidCommand("number of armies value was negative or zero");
			}

		} catch (NumberFormatException l_e) {
			return new InvalidCommand("number of armies value was not a number");
		}

		return new AdvanceOrderCommand(l_sourceCountry, l_targetCountry, l_targetPlayer, l_numberOfArmies);
	}

	/**
	 * Parses a <i>"bomb"</i> command.
	 * 
	 * @param p_argumentList list of command arguments.
	 * @return <code>BombCommand</code> if the command was parsed successfully.
	 *         <code>InvalidCommand</code> if a parsing error occurred.
	 */
	private Command parseBombCommand(List<String> p_argumentList) {
		if (p_argumentList.size() < 1) {
			return new InvalidCommand("Bomb command expects one argument");
		}

		String l_bombCountry = p_argumentList.remove(0).replace('_', ' ');

		return new BombOrderCommand(l_bombCountry);
	}

	/**
	 * Parses a <i>"blockade"</i> command.
	 * 
	 * @param p_argumentList list of command arguments.
	 * @return <code>BlockadeCommand</code> if the command was parsed successfully.
	 *         <code>InvalidCommand</code> if a parsing error occurred.
	 */
	private Command parseBlockadeCommand(List<String> p_argumentList) {
		if (p_argumentList.size() < 1) {
			return new InvalidCommand("Blockade command expects one argument");
		}

		String l_blockadeCountry = p_argumentList.remove(0).replace('_', ' ');
		return new BlockadeOrderCommand(l_blockadeCountry);
	}

	/**
	 * Parses a <i>"airlift"</i> command.
	 * 
	 * @param p_argumentList list of command arguments.
	 * @return <code>AirliftCommand</code> if the command was parsed successfully.
	 *         <code>InvalidCommand</code> if a parsing error occurred.
	 */
	private Command parseAirliftCommand(List<String> p_argumentList) {
		if (p_argumentList.size() < 3) {
			return new InvalidCommand("airlift command expects three arguments");
		}

		String l_sourceCountry = p_argumentList.remove(0).replace('_', ' ');
		String l_targetCountry = p_argumentList.remove(0).replace('_', ' ');
		int l_numberOfArmies;
		try {
			l_numberOfArmies = Integer.parseInt(p_argumentList.remove(0));
			if (l_numberOfArmies <= 0) {
				return new InvalidCommand("number of armies value was negative or zero");
			}

		} catch (NumberFormatException l_e) {
			return new InvalidCommand("number of armies value was not a number");
		}

		return new AirliftOrderCommand(l_sourceCountry, l_targetCountry, l_numberOfArmies);
	}
}
