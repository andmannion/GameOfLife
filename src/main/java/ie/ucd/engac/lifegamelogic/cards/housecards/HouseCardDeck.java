package ie.ucd.engac.lifegamelogic.cards.housecards;

import ie.ucd.engac.lifegamelogic.cards.CardConfigHandler;
import ie.ucd.engac.lifegamelogic.cards.CardDeck;

import java.util.ArrayList;

public class HouseCardDeck extends CardDeck<HouseCard> {

	public HouseCardDeck(String configPath) {
		initialiseCards(configPath);
	}
	
	private void initialiseCards(String configPath) {
		CardConfigHandler<HouseCard> houseCardConfigHandler = new DefaultHouseCardConfigHandler(configPath);
		ArrayList<HouseCard> houseCards = houseCardConfigHandler.initialiseCards();
		cards.addAll(houseCards);
	}
}
