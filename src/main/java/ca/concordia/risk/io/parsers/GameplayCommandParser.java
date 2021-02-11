package ca.concordia.risk.io.parsers;

import java.util.List;

import ca.concordia.risk.io.commands.Command;
import ca.concordia.risk.io.commands.DeployCommand;
import ca.concordia.risk.io.commands.InvalidCommand;


/**
 * A <code>CommandParser</code> implementation for the Gameplay mode. 
 * <p>Implements parsers for the gameplay phase commands.
 */
public class GameplayCommandParser extends CommandParser {

	/**
	 * Overrides parser methods for operations supported in Gameplay mode.
	 */
	@Override
	protected void registerParserMethods( ) {
		super.registerParserMethods();
		
		m_commandParsers.put("deploy", 	this::parseDeployCommand);
	}
	
	/**
	 * Parses a <i>"deploy"</i> command
	 * 
	 * @param p_argumentList list of command arguments.
	 * @return <code>DeployCommand</code> if the command was parsed successfully.
	 * 		   <code>InvalidCommand</code> if a parsing error occurred.
	 */
	private Command parseDeployCommand(List<String> p_argumentList) {	
		if(p_argumentList.size() < 2) {
			return new InvalidCommand("deploy command expects two arguments");
		}
		
		String l_deployCountry = p_argumentList.remove(0);
		int l_numberOfArmies;
		try {
			l_numberOfArmies = Integer.parseInt(p_argumentList.remove(0));
		} catch(NumberFormatException l_e) {
			return new InvalidCommand("number of armies value was not a number");
		}
		
		return new DeployCommand(l_deployCountry, l_numberOfArmies);
	}
	
}
