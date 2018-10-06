package ie.ucd.engAC.LifeGameLogic.Cards.ActionCards;

import java.util.ArrayList;


public class GetCashFromBankConfigHandler implements ActionCardConfigHandler {
	public GetCashFromBankConfigHandler() { // TODO: Should take as input some config parameter
	}
	
	public ArrayList<ActionCard> InitialiseCardSubGroup() {
		ArrayList<ActionCard> myGetCashFromBankActionCards = new ArrayList<ActionCard>();

		for(int i=1;i<6;i++) {
			for(int j=0;j<4;j++) {
				myGetCashFromBankActionCards.add(new GetCashFromBankActionCard(i*10000));
			}
		}
		
		return myGetCashFromBankActionCards;
	}
}

