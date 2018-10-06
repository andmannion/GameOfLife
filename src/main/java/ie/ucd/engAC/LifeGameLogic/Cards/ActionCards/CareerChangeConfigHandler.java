package main.java.ie.ucd.engAC.LifeGameLogic.Cards.ActionCards;

import java.util.ArrayList;

import main.java.ie.ucd.engAC.LifeGameLogic.Cards.ActionCards.ActionCardConfigHandler;

public class CareerChangeConfigHandler implements ActionCardConfigHandler {
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
