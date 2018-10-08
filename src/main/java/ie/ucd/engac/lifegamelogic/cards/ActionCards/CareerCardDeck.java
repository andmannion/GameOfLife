package ie.ucd.engac.lifegamelogic.cards.ActionCards;

import ie.ucd.engac.lifegamelogic.cards.CardConfigHandler;
import ie.ucd.engac.lifegamelogic.cards.CardDeck;
import ie.ucd.engac.lifegamelogic.cards.CareerCards.CareerCard;
import ie.ucd.engac.lifegamelogic.cards.CareerCards.DefaultCareerCardConfigHandler;

public class CareerCardDeck extends CardDeck {
	private final String configString;
	
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
