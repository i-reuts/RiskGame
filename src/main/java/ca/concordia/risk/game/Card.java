package ca.concordia.risk.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Card 
{
	private enum d_cardType
	{
		BOMB, BLOCKADE, AIRLIFT, DIPLOMACY;
	}
	
	private d_cardType d_type;

	public Card()
	{
		issueRandomCard();
	}
	
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
		List<Card> c = new ArrayList<>();
		Card c1;
		for(int i =0; i<=10; i++)
		{
			c1 = new Card();
			c.add(c1);
		}
		
		
		for(Card o : c)
			System.out.println(o.getCard());
	}
}
