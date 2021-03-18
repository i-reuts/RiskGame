package ca.concordia.risk.game;

import java.util.Random;

public class Card 
{
	private enum d_cardType
	{
		BOMB, BLOCKADE, AIRLIFT, NEGOCIATE;
	}
	
	private d_cardType d_type;

	public void issueRandomCard()
	{
		this.d_type = d_cardType.values()[new Random().nextInt(d_cardType.values().length)];		
	}
	
	public d_cardType getCard()
	{
		return d_type;
	}
	
	public static void main(String[] args)
	{
		Card c = new Card();
		c.issueRandomCard();
		System.out.println(c.getCard());
		
	}
}
