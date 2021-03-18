package ca.concordia.risk.game;


public class Card 
{
	private static int d_cardCount = 0;
	private enum d_cardType
	{
		BOMB, BLOCAKDE, AIRLIFT, NEGOCIATE;
	}
	
	private d_cardType d_type;
	
	public Card()
	{
		d_cardCount++;	
	}
	
	public int getCardCount()
	{
		return d_cardCount;
	}
	
	public d_cardType getCard()
	{
		return d_type;
	}
	
	public static void main(String[] args)
	{
		Card c = new Card();
		System.out.println(c.getCardCount());
		
	}
}
