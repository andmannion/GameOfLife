package main.java.ie.ucd.engAC.LifeGameLogic.Cards.ActionCards;

import java.util.ArrayList;

import main.java.ie.ucd.engAC.LifeGameLogic.Cards.ActionCards.ActionCardConfigHandler;

public class PlayersPayConfigHandler implements ActionCardConfigHandler {
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
