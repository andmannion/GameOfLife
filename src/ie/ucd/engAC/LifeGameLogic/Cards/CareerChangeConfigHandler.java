package ie.ucd.engAC.LifeGameLogic.Cards;

import java.util.ArrayList;

public class CareerChangeConfigHandler implements CardConfigHandler {
	public CareerChangeConfigHandler() { // Should take as input some config parameter
	}

	public ArrayList<ActionCard> InitialiseCardSubGroup() {
		int numToInitialise = 5;
		ArrayList<ActionCard> myCareerChangeActionCards = new ArrayList<ActionCard>();

		for (int i = 0; i < numToInitialise; i++) {
			myCareerChangeActionCards.add(new CareerChangeActionCard());
		}
		
		return myCareerChangeActionCards;
	}
}
