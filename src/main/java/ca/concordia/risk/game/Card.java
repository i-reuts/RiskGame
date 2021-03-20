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
	 * Default constructor to support factory method.
	 * 
	 */
	private Card() {
	}

	/**
	 * This is a factory method that generates and returns a card at random.
	 * 
	 * @return l_card random card.
	 */
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
	public d_cardType getCardType() {
		return d_type;
	}

	public void setCardType(d_cardType p_type) {
		this.d_type = p_type;
	}

	public Card getCard(d_cardType p_type) {
		Card l_card = new Card();
		l_card.setCardType(p_type);
		return l_card;
	}

	public static Card getBombCard() {
		Card l_card = new Card();
		l_card.setCardType(d_cardType.values()[0]);
		return l_card;
	}

	public static Card getBlockadeCard() {
		Card l_card = new Card();
		l_card.setCardType(d_cardType.values()[1]);
		return l_card;
	}

	public static Card getAirliftCard() {
		Card l_card = new Card();
		l_card.setCardType(d_cardType.values()[2]);
		return l_card;
	}

	public static Card getDiplomacyCard() {
		Card l_card = new Card();
		l_card.setCardType(d_cardType.values()[3]);
		return l_card;
	}

	public boolean equals(Card p_card) {
		if (p_card.d_type == this.d_type)
			return true;
		else
			return false;
	}
}
