package ca.concordia.risk.game.phases;

import java.io.FileNotFoundException;

import ca.concordia.risk.GameEngine;
import ca.concordia.risk.game.Card;
import ca.concordia.risk.game.Player;
import ca.concordia.risk.game.orders.Order;
import ca.concordia.risk.io.parsers.GameplayCommandParser;
import ca.concordia.risk.io.views.ConsoleView;
import ca.concordia.risk.utils.LogEntryBuffer;
import ca.concordia.risk.utils.LogFileWriter;

/**
 * Class representing the Gameplay Phase.
 * 
 * <p>
 * Executing this phase runs one iteration of the gameplay loop.
 * 
 * @author Enrique
 *
 */
public class GameplayPhase extends Phase {

	private LogEntryBuffer d_logBuffer = new LogEntryBuffer();
	private LogFileWriter d_logFileWriter = new LogFileWriter();
	private int d_turnNumber;

	/**
	 * Creates a new <code>StartupPhase</code> object.
	 */
	public GameplayPhase() {
		d_commandParser = new GameplayCommandParser();
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Sets up the log buffer and log file writer.
	 */
	@Override
	public void executeOnPhaseStart() {
		try {
			d_turnNumber = 1;

			d_logFileWriter.openLogFile();
			d_logBuffer.attach(d_logFileWriter);

			d_logBuffer.write("Game started");
		} catch (FileNotFoundException l_e) {
			GameEngine.GetView().display("\nError: Failed to open the log file");
		}
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Cleans up the log buffer and writer.
	 */
	@Override
	public void executeOnPhaseEnd() {
		d_logBuffer.write("\nGame Ended");

		d_logBuffer.detach(d_logFileWriter);
		d_logFileWriter.closeLogFile();
	}

	/**
	 * Executes one iteration of the gameplay loop.
	 */
	@Override
	public void execute() {
		d_logBuffer.write("\nTurn " + d_turnNumber + " begins");

		assignReinforcements();
		issueCards();
		clearNegotiations();

		issueOrders();
		executeOrders();

		d_turnNumber++;
	}

	/**
	 * Assigns reinforcements to each player.
	 */
	private void assignReinforcements() {
		d_logBuffer.write("\nAssigning reinforcements...");

		for (Player l_p : GameEngine.GetPlayers()) {
			l_p.assignReinfocements();

			d_logBuffer.write(
					"Player " + l_p.getName() + " assigned " + l_p.getRemainingReinforcements() + " reinforcements");
		}
	}

	/**
	 * Issues a random card to each player that conquered a country last turn.
	 */
	private void issueCards() {
		d_logBuffer.write("\nIssuing cards...");

		for (Player l_p : GameEngine.GetPlayers()) {
			if (l_p.getEarnedCard()) {
				Card l_card = Card.issueCard();
				l_p.addCard(l_card);
				l_p.setEarnedCard(false);

				d_logBuffer.write("Player " + l_p.getName() + " issued " + l_card.toString());
			}
		}
	}

	/**
	 * Clears all active negotiations from the previous turn.
	 */
	private void clearNegotiations() {
		for (Player l_p : GameEngine.GetPlayers()) {
			l_p.clearActiveNegotiations();
		}
	}

	/**
	 * Asks each player to issue orders in a round-robin fashion one order at a time
	 * until no players have orders left to give.
	 */
	private void issueOrders() {
		d_logBuffer.write("\nIssuing orders...");

		// Clear the issued order flag for all players
		for (Player l_p : GameEngine.GetPlayers()) {
			l_p.setFinishedIssuingOrder(false);
		}

		// Issue orders until none of the players has orders left to give
		boolean l_allPlayersIssued = false;
		while (!l_allPlayersIssued) {
			l_allPlayersIssued = true;
			for (Player l_p : GameEngine.GetPlayers()) {
				if (!l_p.getFinishedIssuingOrders()) {
					l_p.issueOrder();
					l_allPlayersIssued = false;

					if (l_p.getFinishedIssuingOrders()) {
						d_logBuffer.write("Player " + l_p.getName() + " passed");
					} else {
						Order l_issuedOrder = l_p.peekLastOrder();
						d_logBuffer.write("Player " + l_p.getName() + " issued order: " + l_issuedOrder.getStatus());
					}
				}
			}
		}
	}

	/**
	 * Asks each player to execute their orders in a round-robin fashion one order
	 * at a time until no players have orders remaining in their order queue.
	 */
	private void executeOrders() {
		ConsoleView l_view = GameEngine.GetView();
		l_view.display("\nExecuting orders...");
		d_logBuffer.write("\nExecuting orders...");

		boolean l_allOrdersExecuted = false;
		while (!l_allOrdersExecuted) {
			l_allOrdersExecuted = true;
			for (Player l_p : GameEngine.GetPlayers()) {
				Order l_order = l_p.nextOrder();
				if (l_order != null) {
					l_order.execute();
					l_allOrdersExecuted = false;

					l_view.display(l_order.getStatus());
					d_logBuffer.write(l_order.getStatus());
				}
			}
		}
	}
}