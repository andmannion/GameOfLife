package ie.ucd.engac.lifegamelogic.cards.occupationcards;

import com.google.gson.JsonElement;
import ie.ucd.engac.lifegamelogic.cards.CardConfigHandler;
import ie.ucd.engac.lifegamelogic.cards.CardDeck;

public class OccupationCardDeck extends CardDeck<OccupationCard> {

	public OccupationCardDeck(String configPath) {
		initialiseCards(configPath);
	}

	private void initialiseCards(String configPath) {
		CardConfigHandler<OccupationCard> occupationCardConfigHandler = new DefaultOccupationCardConfigHandler(configPath);
		cards.addAll(occupationCardConfigHandler.initialiseCards());
	}
}
