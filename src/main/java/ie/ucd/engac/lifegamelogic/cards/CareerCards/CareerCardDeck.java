package ie.ucd.engac.lifegamelogic.cards.CareerCards;

import ie.ucd.engac.lifegamelogic.cards.CardConfigHandler;
import ie.ucd.engac.lifegamelogic.cards.CardDeck;

public class CareerCardDeck extends CardDeck {
	private String configString;
	
	public CareerCardDeck(String configString) {
		super();

		this.configString = configString;
		
		initialiseCards();
	}

	private void initialiseCards() {
		CardConfigHandler<CareerCard> careerCardConfigHandler = new DefaultCareerCardConfigHandler(configString);

		careerCardConfigHandler.initialiseCards();
	}
}
