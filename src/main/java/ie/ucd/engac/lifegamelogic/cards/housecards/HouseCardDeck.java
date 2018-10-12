package ie.ucd.engac.lifegamelogic.cards.housecards;

import java.util.ArrayList;

import ie.ucd.engac.lifegamelogic.cards.CardConfigHandler;
import ie.ucd.engac.lifegamelogic.cards.CardDeck;

public class HouseCardDeck extends CardDeck {
	private final String configString;
	
	public HouseCardDeck(String configString) {
		this.configString = configString;
		
		initialiseCards();
	}
	
	private void initialiseCards() {
		CardConfigHandler<HouseCard> houseCardConfigHandler = new DefaultHouseCardConfigHandler(configString);
		ArrayList<HouseCard> houseCards = houseCardConfigHandler.initialiseCards();
		cards.addAll(houseCards);
	}
}
