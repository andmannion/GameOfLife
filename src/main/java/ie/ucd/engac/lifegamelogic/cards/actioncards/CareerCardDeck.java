package ie.ucd.engac.lifegamelogic.cards.actioncards;

import java.util.ArrayList;

import ie.ucd.engac.lifegamelogic.cards.CardConfigHandler;
import ie.ucd.engac.lifegamelogic.cards.CardDeck;
import ie.ucd.engac.lifegamelogic.cards.occupationcards.careercards.CareerCard;
import ie.ucd.engac.lifegamelogic.cards.occupationcards.careercards.DefaultCareerCardConfigHandler;

public class CareerCardDeck extends CardDeck {
	private final String configString;
	
	public CareerCardDeck(String configString) {
		this.configString = configString;
		
		initialiseCards();
	}
	
	private void initialiseCards() {
		CardConfigHandler<CareerCard> careerCardConfigHandler = new DefaultCareerCardConfigHandler(configString);
		
		ArrayList<CareerCard> careerCards = careerCardConfigHandler.initialiseCards();
		cards.addAll(careerCards);
	}
}
