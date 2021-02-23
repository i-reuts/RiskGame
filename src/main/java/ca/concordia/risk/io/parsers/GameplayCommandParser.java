package ca.concordia.risk.io.parsers;

import java.util.List;

import ca.concordia.risk.io.commands.Command;
import ca.concordia.risk.io.commands.DeployOrderCommand;
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

		String l_deployCountry = p_argumentList.remove(0);
		int l_numberOfArmies;
		try {
			l_numberOfArmies = Integer.parseInt(p_argumentList.remove(0));
			if(l_numberOfArmies < 0) {
				return new InvalidCommand("number of armies value was negative");
			}
		} catch (NumberFormatException l_e) {
			return new InvalidCommand("number of armies value was not a number");
		}

		return new DeployOrderCommand(l_deployCountry, l_numberOfArmies);
	}
	
	/**
	 * Parses a <i>"showmap"</i> command.
	 * 
	 * @param p_argumentList list of command arguments.
	 * @return <code>ShowMapCommand</code>.
	 */
	private Command parseShowMapCommand(List<String> p_argumentList) {
		return new ShowMapCommand(true);
	}

}
