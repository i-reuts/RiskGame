package ca.concordia.risk.io.parsers;

import java.util.List;

import ca.concordia.risk.io.commands.AssignCountriesCommand;
import ca.concordia.risk.io.commands.Command;
import ca.concordia.risk.io.commands.GamePlayerCommand;
import ca.concordia.risk.io.commands.InvalidCommand;


/**
 * A <code>CommandParser</code> implementation for the Startup mode. 
 * <p>Implements parsers for the startup phase commands.
 */
public class StartupCommandParser extends CommandParser {

	/**
	 * Overrides parser methods for operations supported in Startup mode.
	 */
	@Override
	protected void registerParserMethods( ) {
		super.registerParserMethods();
		
		m_commandParsers.put("gameplayer", 	this::parseGamePlayerCommand);
		m_commandParsers.put("assigncountries", this::parseAssignCountriesCommand);
	}

	/**
	 * Parses a <i>"gameplayer"</i> command
	 * 
	 * @param p_argumentList list of command arguments.
	 * @return <code>GamePlayerCommand</code> if the command was parsed successfully.
	 * 		   <code>InvalidCommand</code> if a parsing error occurred.
	 */
	private Command parseGamePlayerCommand(List<String> p_argumentList)  {	
		if(p_argumentList.isEmpty()) {
			return new InvalidCommand("no parameters supplied to gameplayer command");
		}
		
		GamePlayerCommand l_editPlayersCommand = new GamePlayerCommand();
		
		try {
			while(!p_argumentList.isEmpty()) {
				String l_subcommand = p_argumentList.remove(0);
				switch(l_subcommand) {
					case "-add": 		parseAddPlayerBlock(p_argumentList, l_editPlayersCommand);
								  		break;
					case "-remove": 	parseRemovePlayerBlock(p_argumentList, l_editPlayersCommand);
									 	break;
					default:			return new InvalidCommand("unsupported subcommand " + l_subcommand);
				}	
			}
		}
		catch(ParsingException l_e) {
			return new InvalidCommand(l_e.getMessage());
		}
		
		return l_editPlayersCommand;
	}
	
	/**
	 * Parses an <i>"assigncounties"</i> command
	 * 
	 * @param p_argumentList list of command arguments.
	 * @return <code>AssignCountriesCommand</code>.
	 */
	private Command parseAssignCountriesCommand(List<String> p_argumentList)  {	
		return new AssignCountriesCommand();
	}
	
	/**
	 * Parses one <b>-add</b> block of the "gameplayer" command.
	 * 
	 * @param p_argumentList list of command arguments.
	 * @param p_command command that the parsed data will be stored into.
	 * @throws ParsingException thrown if the number of arguments received is invalid.
	 */
	private void parseAddPlayerBlock(List<String> p_argumentList, GamePlayerCommand p_command) throws ParsingException {
		if(p_argumentList.size() < 1) {
			throw new ParsingException("-add command expects one parameter");
		}
		
		String l_playerName = p_argumentList.remove(0).replace('_', ' ');
		
		p_command.addPlayer(l_playerName);
	}

	/**
	 * Parses one <b>-remove</b> block of the "gameplayer" command.
	 * 
	 * @param p_argumentList list of command arguments.
	 * @param p_command command that the parsed data will be stored into.
	 * @throws ParsingException thrown if the number of arguments received is invalid.
	 */
	private void parseRemovePlayerBlock(List<String> p_argumentList, GamePlayerCommand p_command) throws ParsingException {
		if(p_argumentList.size() < 1) {
			throw new ParsingException("-remove command expects one parameter");
		}
		
		String l_playerName = p_argumentList.remove(0).replace('_', ' ');
		p_command.removePlayer(l_playerName);	
	}
}
