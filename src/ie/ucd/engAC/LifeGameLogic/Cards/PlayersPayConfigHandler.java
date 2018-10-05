package ie.ucd.engAC.LifeGameLogic.Cards;

import java.util.ArrayList;

public class PlayersPayConfigHandler implements CardConfigHandler {
	public PlayersPayConfigHandler() { // TODO: Should take as input some config parameter
	}
	
	public ArrayList<ActionCard> InitialiseCardSubGroup() {
		int numToInitialise = 10;
		ArrayList<ActionCard> myPlayersPayActionCards = new ArrayList<ActionCard>();

		for (int i = 0; i < numToInitialise; i++) {
			myPlayersPayActionCards.add(new PlayersPayActionCard(20000)); // TODO: Make this constructor value originate in config file
		}
		
		return myPlayersPayActionCards;
	}
}
