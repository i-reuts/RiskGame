package ca.concordia.risk.game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import ca.concordia.risk.GameEngine;
import ca.concordia.risk.game.orders.Order;

/**
 * This class is the representation of the game player.
 * 
 * @author Enrique
 *
 */
public class Player {
	private int d_reinforcements;
	private String d_name;
	private Queue<Order> d_orders;
	private Set<Country> d_countries;
	private List<Card> d_cards;
	private boolean d_earnedCard;
	private boolean d_finishedIssuingOrders;
	private Set<Player> d_activeNegotiations;

	/**
	 * Creates a new player.
	 * 
	 * @param p_name name of the player to create.
	 */
	public Player(String p_name) {
		d_name = p_name;
		d_reinforcements = 0;
		d_orders = new LinkedList<Order>();
		d_countries = new HashSet<Country>();
		d_cards = new ArrayList<>();
		d_activeNegotiations = new HashSet<Player>();
	}

	/**
	 * Gets the name of the player.
	 * 
	 * @return player name.
	 */
	public String getName() {
		return d_name;
	}

	/**
	 * Adds a country to the list of countries owned by the player.
	 * 
	 * @param p_country country to add to the list of owned countries.
	 */
	public void addCountry(Country p_country) {
		d_countries.add(p_country);
	}

	/**
	 * Removes a country from the list of countries owned by the player.
	 * 
	 * @param p_country country to remove from the list of owned countries.
	 */
	public void removeCountry(Country p_country) {
		d_countries.remove(p_country);
	}

	/**
	 * Checks if the players owns a country.
	 * 
	 * @param p_country country to check if it is owned by the player.
	 * @return <code>true</code> is the country is owned by the player.<br>
	 *         <code>false</code> otherwise.
	 */
	public boolean ownsCountry(Country p_country) {
		return d_countries.contains(p_country);
	}

	/**
	 * Gets the set of countries that are owned by the player.
	 * 
	 * @return set of countries that are owned by the player.
	 */
	public Set<Country> getCountries() {
		return d_countries;
	}

	/**
	 * Gets the flag signifying if this player earned a card that hasn't been issued
	 * yet.
	 * 
	 * @return <code>true</code> if the player earned a card.<br>
	 *         <code>false</code> if the player hasn't earned a card.
	 */
	public boolean getEarnedCard() {
		return d_earnedCard;
	}

	/**
	 * Sets the flag signifying if this player earned a card that hasn't been issued
	 * yet.
	 * 
	 * @param p_value flag value to set.
	 */
	public void setEarnedCard(boolean p_value) {
		d_earnedCard = p_value;
	}

	/**
	 * Gets the collection of cards the player currently possesses.
	 * 
	 * @return collection of owned cards.
	 */
	public Collection<Card> getCards() {
		return d_cards;
	}

	/**
	 * Adds a player to the set of players negotiated with.
	 * 
	 * @param p_player player negotiated with.
	 */
	public void addActiveNegotiation(Player p_player) {
		d_activeNegotiations.add(p_player);
	}

	/**
	 * Gets the flag signifying if this player is negotiating with a given player.
	 * 
	 * @param p_player player to check negotiation status for.
	 * @return <code>true</code> if currently negotiating with with target
	 *         player.<br>
	 *         <code>false</code> otherwise.
	 * 
	 */
	public boolean isNegotiating(Player p_player) {
		return d_activeNegotiations.contains(p_player);
	}

	/**
	 * Clears all active negotiations.
	 */
	public void clearActiveNegotiations() {
		d_activeNegotiations.clear();
	}

	/**
	 * Issues one player order and adds it to the order queue of the player.
	 * <p>
	 * Current implementation asks the user to input the order to be issued.
	 */
	public void issueOrder() {
		Order l_order = GameEngine.ProcessOrderCommand(this);
		if (l_order != null) {
			d_orders.add(l_order);
		}
	}

	/**
	 * Gets the first order in the order queue of the player. The order is removed
	 * from the queue.
	 * 
	 * @return <code>Order</code> if there is an order in the queue.<br>
	 *         <code>null</code> if the queue is empty.
	 */
	public Order nextOrder() {
		return d_orders.poll();
	}

	/**
	 * Gets the first order in the order queue of the player without removing it
	 * from the queue.
	 * 
	 * @return <code>Order</code> top order in the player's queue.<br>
	 *         <code>null</code> if the queue is empty.
	 */
	public Order peekNextOrder() {
		return d_orders.peek();
	}

	/**
	 * Gets the flag signifying if the player finished issuing orders for the
	 * current turn.
	 * 
	 * @return <code>true</code> if the player finished issuing orders.<br>
	 *         <code>false</code> otherwise.
	 */
	public boolean getFinishedIssuingOrders() {
		return d_finishedIssuingOrders;
	}

	/**
	 * Set the flag signifying if the player finished issuing orders for the current
	 * turn.
	 * 
	 * @param p_value value of the flag to set.
	 */
	public void setFinishedIssuingOrder(boolean p_value) {
		d_finishedIssuingOrders = p_value;
	}

	/**
	 * Gets the number of reinforcements the player has left.
	 * 
	 * @return number of remaining reinforcements.
	 */
	public int getRemainingReinforcements() {
		return d_reinforcements;
	}

	/**
	 * Retrieves requested number of reinforcements from the player if available.
	 * <p>
	 * If successful, reduces the number of available reinforcements by the amount
	 * retrieved.
	 * 
	 * @param p_numReinforcements number of reinforcements to retrieve.
	 * @return <code>true</code> if reinforcements were successfully retrieved.<br>
	 *         <code>false</code> if the requested number of reinforcements is
	 *         invalid or the player did not have enough reinforcements.
	 */
	public boolean retrieveReinforcements(int p_numReinforcements) {
		if (p_numReinforcements <= 0 || p_numReinforcements > d_reinforcements) {
			return false;
		}

		d_reinforcements -= p_numReinforcements;

		return true;
	}

	/**
	 * This method computes the set of continents that are fully captured by the
	 * player.
	 * 
	 * @return set of continents that are fully captured by the player.
	 */
	public Set<Continent> getOwnedContinents() {
		Set<Continent> l_ownedContinents = new HashSet<Continent>();
		for (Country l_country : d_countries) {
			Continent l_continent = l_country.getContinent();
			if (d_countries.containsAll(l_continent.getCountries())) {
					l_ownedContinents.add(l_continent);
			}
		}
		return l_ownedContinents;
	}

	/**
	 * Assigns reinforcements to the player.
	 * <p>
	 * Reinforcements are assigned based on the minimum reinforcement number, number
	 * of countries owned and bonus value of wholly owned continents.
	 */
	public void assignReinfocements() {
		// Assign base reinforcements based on number of countries owned
		d_reinforcements = Math.max(3, d_countries.size() / 3);
		// Find all continents that the player fully owns
		Set<Continent> l_ownedContinents = getOwnedContinents();
		// Add the continent value of fully owned continents to reinforcements
		for (Continent l_c : l_ownedContinents) {
			d_reinforcements += l_c.getValue();
		}
	}

	/**
	 * This method adds the card to the list of cards owned by the Player.
	 * 
	 * @param p_card card to be added.
	 */
	public void addCard(Card p_card) {
		d_cards.add(p_card);
	}

	/**
	 * This method attempts to pick a specific type of Card from the list of Cards
	 * owned by the Player.
	 * <p>
	 * If card of the target type is found in the player deck, it is removed from
	 * the deck.
	 * 
	 * @param p_card card of the type to be picked.
	 * @return <code>true</code> if the card of the required type was in the player
	 *         deck and was picked successfully.<br>
	 *         <code>false</code> if the card was not found in the player deck and
	 *         thus could not be used.
	 */
	public boolean useCard(Card p_card) {
		return d_cards.remove(p_card);
	}

}
