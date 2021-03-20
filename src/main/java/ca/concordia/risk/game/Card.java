package ca.concordia.risk.game;

import java.util.Random;

/**
 * This class provides the implementation of Cards in the Risk game.
 *
 */
public class Card {
	private static Random d_random = new Random();

	enum d_cardType {
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
	 * This is a factory method that generates and returns a Card at random.
	 * 
	 * @return l_card random Card.
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

	/**
	 * This method sets the type of the card.
	 * 
	 * @param p_type type of card.
	 */
	public void setCardType(d_cardType p_type) {
		this.d_type = p_type;
	}

	/**
	 * This method returns a BOMB Card.
	 * 
	 * @return BOMB Card.
	 */
	public static Card getBombCard() {
		Card l_card = new Card();
		l_card.setCardType(d_cardType.values()[0]);
		return l_card;
	}

	/**
	 * This method returns a BLOCKADE Card.
	 * 
	 * @return BLOCAKDE Card.
	 */
	public static Card getBlockadeCard() {
		Card l_card = new Card();
		l_card.setCardType(d_cardType.values()[1]);
		return l_card;
	}

	/**
	 * This method returns an AIRLIFT Card.
	 * 
	 * @return AIRLIFT Card.
	 */
	public static Card getAirliftCard() {
		Card l_card = new Card();
		l_card.setCardType(d_cardType.values()[2]);
		return l_card;
	}

	/**
	 * This method returns a DIPLOMACY Card.
	 * 
	 * @return DIPLOMACY Card.
	 */
	public static Card getDiplomacyCard() {
		Card l_card = new Card();
		l_card.setCardType(d_cardType.values()[3]);
		return l_card;
	}

	/**
	 * This method compares two cards on the basis of their type.
	 *
	 */
	@Override
	public boolean equals(Object p_other) {
		if (!(p_other instanceof Card)) {
			return false;
		}
		Card l_otherCard = (Card) p_other;
		return this.d_type.equals(l_otherCard.d_type);
	}
}
