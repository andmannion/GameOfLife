package ie.ucd.engac.lifegamelogic.cards.occupationcards.collegecareercards;

import ie.ucd.engac.lifegamelogic.cards.CardConfigHandler;
import ie.ucd.engac.lifegamelogic.cards.CardDeck;

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
