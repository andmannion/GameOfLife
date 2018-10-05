package ie.ucd.engAC.LifeGameLogic;

import ie.ucd.engAC.LifeGameLogic.Cards.CardDeck;
import ie.ucd.engAC.LifeGameLogic.Cards.ActionCards.ActionCardDeck;
import ie.ucd.engAC.LifeGameLogic.Cards.ActionCards.CareerCardDeck;
import ie.ucd.engAC.LifeGameLogic.Cards.HouseCards.HouseCardDeck;

public class Bank {
	
	private ActionCardDeck actionCardDeck;
	private HouseCardDeck houseCardDeck;
	private CareerCardDeck careerCardDeck;
	private CardDeck collegeCareerCardDeck;
	
	// Pull in the action card deck config from the config file.
	// Where should this config be stored, what format should it be in?	
	public Bank() {
		InitialiseDecks();
	}
	
	private void InitialiseDecks() {
		// Initialise decks of different types
		actionCardDeck = new ActionCardDeck();
		actionCardDeck.Shuffle();
		
		//houseCardDeck = new HouseCardDeck();
		//houseCardDeck.Shuffle();
		//careerCardDeck = new CareerCardDeck();
		//careerCardDeck.Shuffle();
		//collegeCareerCardDeck = new CollegeCareerCardDeck();
		//collegeCareerCardDeck.Shuffle();
	}
}
