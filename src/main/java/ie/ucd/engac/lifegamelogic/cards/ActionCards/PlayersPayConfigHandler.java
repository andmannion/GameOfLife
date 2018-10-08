package ie.ucd.engac.lifegamelogic.cards.ActionCards;

import java.util.ArrayList;

import ie.ucd.engac.lifegamelogic.cards.CardConfigHandler;

public class PlayersPayConfigHandler implements CardConfigHandler<ActionCard> {
	public PlayersPayConfigHandler() { // TODO: Should take as input some config parameter
	}
	
	public ArrayList<ActionCard> initialiseCards() {
		int numToInitialise = 10;
		ArrayList<ActionCard> myPlayersPayActionCards = new ArrayList<ActionCard>();

		for (int i = 0; i < numToInitialise; i++) {
			myPlayersPayActionCards.add(new PlayersPayActionCard(20000)); // TODO: Make this constructor value originate in config file
		}
		
		return myPlayersPayActionCards;
	}
}
