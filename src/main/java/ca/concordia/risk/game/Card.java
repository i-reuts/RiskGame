package ca.concordia.risk.game;

import java.util.Random;

/**
 * This class provides the implementation of Cards in the Risk game.
 *
 */
public class Card {
	private static Random d_random = new Random();

	private enum d_cardType {
		BOMB, BLOCKADE, AIRLIFT, DIPLOMACY;
	}

	private d_cardType d_type;

	/**
	 * This constructor creates a random card by using another helper function.
	 * 
	 */
	public Card() {
		issueRandomCard();
	}

	/**
	 * This method generates and issues a card at random.
	 * 
	 */
	public void issueRandomCard() {
		this.d_type = d_cardType.values()[d_random.nextInt(d_cardType.values().length)];
	}

	/**
	 * This method returns the card type.
	 * 
	 * @return type of card.
	 */
	public d_cardType getCard() {
		return d_type;
	}
}
