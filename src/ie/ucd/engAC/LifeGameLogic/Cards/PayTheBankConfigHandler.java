package ie.ucd.engAC.LifeGameLogic.Cards;

import java.util.ArrayList;
import java.util.HashMap;

public class PayTheBankConfigHandler implements CardConfigHandler {
	public PayTheBankConfigHandler() { // TODO: Should take as input some config parameter		
	}
	
	public ArrayList<ActionCard> InitialiseCardSubGroup() {
		ArrayList<ActionCard> myPayTheBankActionCards = new ArrayList<ActionCard>();

		for(int i=0;i<6;i++) {
			for(int j=0;j<3;j++) {
				myPayTheBankActionCards.add(new PayTheBankActionCard(i*10000));
			}
		}
		
		return myPayTheBankActionCards;
	}
}
