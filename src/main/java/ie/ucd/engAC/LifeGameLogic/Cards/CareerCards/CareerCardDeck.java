package ie.ucd.engAC.LifeGameLogic.Cards.CareerCards;

import ie.ucd.engAC.LifeGameLogic.Cards.CardConfigHandler;
import ie.ucd.engAC.LifeGameLogic.Cards.CardDeck;

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
