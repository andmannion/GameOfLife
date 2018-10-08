package ie.ucd.engac.lifegamelogic.cards.HouseCards;

import ie.ucd.engac.lifegamelogic.cards.CardConfigHandler;
import ie.ucd.engac.lifegamelogic.cards.CardDeck;

public class HouseCardDeck extends CardDeck {
	private final String configString;
	
	public HouseCardDeck(String configString) {
		super();
		
		this.configString = configString;
		
		initialiseCards();
	}
	
	private void initialiseCards() {
		CardConfigHandler<HouseCard> houseCardConfigHandler = new DefaultHouseCardConfigHandler(configString);
		
		houseCardConfigHandler.initialiseCards();
	}
}
