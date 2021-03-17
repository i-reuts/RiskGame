package ca.concordia.risk.phases;

import ca.concordia.risk.GameEngine;
import ca.concordia.risk.io.commands.Command;
import ca.concordia.risk.io.parsers.CommandParser;
import ca.concordia.risk.io.views.ConsoleView;

/**
 * Class representing the Startup Phase
 * @author Enrique
 *
 */
public class StartupPhase extends Phase{
	
	/**
	 * Constructor for Startup Phase
	 * @param p_parser parser used by this phase
	 */
	public StartupPhase(CommandParser p_parser) {
		super(p_parser);
	}
	
	
	@Override
	public void execute() {
		this.executeCommand();
	}
	
	@Override
	protected void nextPhase() {
		GameEngine.SwitchToIssueOrderMode();
	}
	
	/**
	 * Method to execute user inputs with the help of its own parser.
	 * @param p_userInput User command string
	 */
	private void executeCommand() {
		ConsoleView l_view = GameEngine.GetView();
		l_view.display("\nPlease enter your command:");
		Command l_command = d_parser.parse(l_view.getInput());
		l_command.execute();
		
		if(GameEngine.isCountryAssignationDone) {
			this.nextPhase();
		}
	}
}
