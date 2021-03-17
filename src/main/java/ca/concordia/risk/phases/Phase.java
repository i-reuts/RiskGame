package ca.concordia.risk.phases;

import ca.concordia.risk.io.parsers.CommandParser;

public abstract class Phase {
	protected CommandParser d_parser;
	protected void nextPhase() {
		return;
	}
}
