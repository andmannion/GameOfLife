package ie.ucd.engAC.LifeGameLogic.Cards.ActionCards;

import java.util.ArrayList;

import ie.ucd.engAC.LifeGameLogic.Cards.CardConfigHandler;

public class PayTheBankConfigHandler implements CardConfigHandler<ActionCard> {
	public PayTheBankConfigHandler() { // TODO: Should take as input some config parameter		
	}
	
	public ArrayList<ActionCard> initialiseCards() {
		ArrayList<ActionCard> myPayTheBankActionCards = new ArrayList<ActionCard>();

		for(int i=1;i<6;i++) {
			for(int j=0;j<4;j++) {
				myPayTheBankActionCards.add(new PayTheBankActionCard(i*10000));
			}
		}
		
		return myPayTheBankActionCards;
	}
}
