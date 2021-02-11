package ca.concordia.risk.io.parsers;

import java.util.List;

import ca.concordia.risk.io.commands.Command;
import ca.concordia.risk.io.commands.EditCountryCommand;
import ca.concordia.risk.io.commands.InvalidCommand;

/**
 * A <code>CommandParser</code> implementation for the Editor mode. 
 * <p>Implements parsers for the editor commands.
 */
public class EditorCommandParser extends CommandParser {

	/**
	 * Overrides parser methods for operations supported in Editor mode.
	 */
	@Override
	protected void registerParserMethods( ) {
		super.registerParserMethods();
		
		m_commandParsers.put("editcountry", this::parseEditCountryCommand);
	}
	
	/**
	 * Parses an "editcountry" command
	 * 
	 * @param p_argumentList list of command arguments.
	 * @return <code>EditCountryCommand</code> if the command was parser successfully.
	 * 		   <code>InvalidCommand</code> if a parsing error occurred.
	 */
	private Command parseEditCountryCommand(List<String> p_argumentList)  {	
		if(p_argumentList.isEmpty()) {
			return new InvalidCommand("no parameters supplied to editcountry command");
		}
		
		EditCountryCommand l_editCommand = new EditCountryCommand();
		
		try {
			while(!p_argumentList.isEmpty()) {
				String l_subcommand = p_argumentList.remove(0);
				switch(l_subcommand) {
					case "-add": 		parseAddCountryBlock(p_argumentList, l_editCommand);
								  		break;
					case "-remove": 	parseRemoveCountryBlock(p_argumentList, l_editCommand);
									 	break;
					default:			return new InvalidCommand("unsupported subcommand " + l_subcommand);
				}	
			}
		}
		catch(ParsingException l_e) {
			return new InvalidCommand(l_e.getMessage());
		}
		
		return l_editCommand;
	}

	/**
	 * Parses one <b>-add</b> block of the "editcountry" command.
	 * 
	 * @param p_argumentList list of command arguments.
	 * @param p_command command that the parsed data will be stored into.
	 * @throws ParsingException thrown if the number of arguments received is invalid.
	 */
	private void parseAddCountryBlock(List<String> p_argumentList, EditCountryCommand p_command) throws ParsingException {
		if(p_argumentList.size() < 2) {
			throw new ParsingException("-add command expects two parameters");
		}
		
		String l_countryName = p_argumentList.remove(0);
		String l_continentName = p_argumentList.remove(0);
		
		p_command.addCountry(l_countryName, l_continentName);
	}

	/**
	 * Parses one <b>-remove</b> block of the "editcountry" command.
	 * 
	 * @param p_argumentList list of command arguments.
	 * @param p_command command that the parsed data will be stored into.
	 * @throws ParsingException thrown if the number of arguments received is invalid.
	 */
	private void parseRemoveCountryBlock(List<String> p_argumentList, EditCountryCommand p_command) throws ParsingException {
		if(p_argumentList.size() < 1) {
			throw new ParsingException("-remove command expects one parameter");
		}
		
		String l_countryName = p_argumentList.remove(0);
		p_command.removeCountry(l_countryName);	
	}
}
