package ie.ucd.engAC.LifeGameLogic.Cards.CollegeCareerCards;

import ie.ucd.engAC.LifeGameLogic.Cards.CardConfigHandler;
import ie.ucd.engAC.LifeGameLogic.Cards.CardDeck;

public class CollegeCareerCardDeck extends CardDeck {
	private String configString;

	public CollegeCareerCardDeck(String configString) {
		super();

		this.configString = configString;
		
		initialiseCards();
	}

	private void initialiseCards() {
		CardConfigHandler<CollegeCareerCard> collegeCareerCardConfigHandler = new DefaultCollegeCareerCardConfigHandler(configString);

		collegeCareerCardConfigHandler.initialiseCards();
	}
}
