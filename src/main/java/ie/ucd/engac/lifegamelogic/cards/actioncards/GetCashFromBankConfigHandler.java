package ie.ucd.engac.lifegamelogic.cards.actioncards;

import java.util.ArrayList;

import ie.ucd.engac.lifegamelogic.cards.CardConfigHandler;


public class GetCashFromBankConfigHandler implements CardConfigHandler<ActionCard> {
	public GetCashFromBankConfigHandler() { // TODO: Should take as input some config parameter
	}
	
	public ArrayList<ActionCard> initialiseCards() {
		ArrayList<ActionCard> myGetCashFromBankActionCards = new ArrayList<ActionCard>();

		for(int i=1;i<6;i++) {
			for(int j=0;j<4;j++) {
				myGetCashFromBankActionCards.add(new GetCashFromBankActionCard(i*10000));
			}
		}
		
		return myGetCashFromBankActionCards;
	}
}

