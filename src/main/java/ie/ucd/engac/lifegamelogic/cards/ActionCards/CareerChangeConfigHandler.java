package ie.ucd.engac.lifegamelogic.cards.ActionCards;

import java.util.ArrayList;

import ie.ucd.engac.lifegamelogic.cards.CardConfigHandler;

public class CareerChangeConfigHandler implements CardConfigHandler<ActionCard> {
	public CareerChangeConfigHandler() { // Should take as input some config parameter
	}

	public ArrayList<ActionCard> initialiseCards() {
		int numToInitialise = 5;
		ArrayList<ActionCard> myCareerChangeActionCards = new ArrayList<ActionCard>();

		for (int i = 0; i < numToInitialise; i++) {
			myCareerChangeActionCards.add(new CareerChangeActionCard());
		}
		
		return myCareerChangeActionCards;
	}
}
