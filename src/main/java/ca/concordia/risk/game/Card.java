package ca.concordia.risk.game;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * This class provides the implementation of Cards in the Risk game.
 *
 */
public class Card {

	/** Represents the type of a card. */
	private enum CardType {
		BOMB, BLOCKADE, AIRLIFT, DIPLOMACY;
	}

	private static Random d_Random = new Random();
	private static Map<CardType, Card> d_CardMap = new HashMap<>();

	/*
	 * Initializes the card table with cards of all available types. <p> The game
	 * will then reuse these cards, ensuring that no new cards are created.
	 */
	static {
		d_CardMap.put(CardType.BOMB, new Card(CardType.BOMB));
		d_CardMap.put(CardType.BLOCKADE, new Card(CardType.BLOCKADE));
		d_CardMap.put(CardType.AIRLIFT, new Card(CardType.AIRLIFT));
		d_CardMap.put(CardType.DIPLOMACY, new Card(CardType.DIPLOMACY));
	}

	private CardType d_type;

	/**
	 * Constructor that creates a card of a specific type.
	 * <p>
	 * The constructor is made private in order to ensure that only one instance of
	 * the card of each type exists.
	 * 
	 * @param p_type type of the card to create.
	 */
	private Card(CardType p_type) {
		d_type = p_type;
	}

	/**
	 * This is a factory method that generates and returns a Card at random.
	 * 
	 * @return l_card random Card.
	 */
	public static Card issueCard() {
		CardType l_randomType = CardType.values()[d_Random.nextInt(CardType.values().length)];
		return d_CardMap.get(l_randomType);
	}

	/**
	 * This method returns a BOMB Card.
	 * 
	 * @return BOMB Card.
	 */
	public static Card getBombCard() {
		return d_CardMap.get(CardType.BOMB);
	}

	/**
	 * This method returns a BLOCKADE Card.
	 * 
	 * @return BLOCKADE Card.
	 */
	public static Card getBlockadeCard() {
		return d_CardMap.get(CardType.BLOCKADE);
	}

	/**
	 * This method returns an AIRLIFT Card.
	 * 
	 * @return AIRLIFT Card.
	 */
	public static Card getAirliftCard() {
		return d_CardMap.get(CardType.AIRLIFT);
	}

	/**
	 * This method returns a DIPLOMACY Card.
	 * 
	 * @return DIPLOMACY Card.
	 */
	public static Card getDiplomacyCard() {
		return d_CardMap.get(CardType.DIPLOMACY);
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Compares two cards on the basis of their type.
	 */
	@Override
	public boolean equals(Object p_other) {
		if (!(p_other instanceof Card)) {
			return false;
		}
		Card l_otherCard = (Card) p_other;
		return this.d_type.equals(l_otherCard.d_type);
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Return a string representing the card type.
	 */
	@Override
	public String toString() {
		switch (d_type) {
		case AIRLIFT:
			return "Airlift Card";
		case BLOCKADE:
			return "Blockade Card";
		case BOMB:
			return "Bomb Card";
		case DIPLOMACY:
			return "Diplomacy Card";
		default:
			return d_type.toString();
		}
	}

	/**
	 * Gets the card from its string representation.
	 * 
	 * @param p_cardString string representation of the card.
	 * @return <code>Card</code> corresponding to the appropriate card.<br>
	 *         <code>null</code> if the card string was invalid.
	 */
	public static Card getCardFromString(String p_cardString) {
		switch (p_cardString) {
		case "Airlift Card":
			return getAirliftCard();
		case "Blockade Card":
			return getBlockadeCard();
		case "Bomb Card":
			return getBombCard();
		case "Diplomacy Card":
			return getDiplomacyCard();
		default:
			return null;
		}
	}
}
