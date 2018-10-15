package ie.ucd.engac.lifegamelogic.cards.occupationcards;

import ie.ucd.engac.lifegamelogic.cards.CardConfigHandler;
import ie.ucd.engac.lifegamelogic.cards.CardDeck;

public class OccupationCardDeck extends CardDeck<OccupationCard> {
	private String configString;

	public OccupationCardDeck(String configString) {
		this.configString = configString;
		
		initialiseCards();
	}

	private void initialiseCards() {
		CardConfigHandler<OccupationCard> occupationCardConfigHandler = new DefaultOccupationCardConfigHandler(configString);
		cards.addAll(occupationCardConfigHandler.initialiseCards());
	}
}
