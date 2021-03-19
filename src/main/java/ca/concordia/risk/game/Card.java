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

	
	private Card() {
	}

	public static Card issueCard() {
		Card l_card = new Card();
		l_card.d_type = d_cardType.values()[d_random.nextInt(d_cardType.values().length)];
		return l_card;
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
