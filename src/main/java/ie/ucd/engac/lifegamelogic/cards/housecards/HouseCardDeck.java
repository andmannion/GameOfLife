package ie.ucd.engac.lifegamelogic.cards.housecards;

import java.util.ArrayList;

import com.google.gson.JsonElement;
import ie.ucd.engac.lifegamelogic.cards.CardConfigHandler;
import ie.ucd.engac.lifegamelogic.cards.CardDeck;

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
