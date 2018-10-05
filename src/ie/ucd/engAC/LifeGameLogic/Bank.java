package ie.ucd.engAC.LifeGameLogic;

public class Bank {
//	private CardDeck actionCardDeck = new CardDeck();
//	private CardDeck houseCardDeck = new CardDeck();
//	private CardDeck careerCardDeck = new CardDeck();
//	private CardDeck collegeCareerCardDeck = new CardDeck();
	
	private ActionCardDeck actionCardDeck;
	private HouseCardDeck houseCardDeck;
	private CareerCardDeck careerCardDeck;
	
	// Pull in the action card deck config from the config file.
	// Where should this config be stored, what format should it be in
	
	public Bank() {

		
	}
	
	private void InitialiseDecks() {
		// Initialise decks of different types
		actionCardDeck = new ActionCardDeck();
		//houseCardDeck = new HouseCardDeck();
		//careerCardDeck = new CareerCardDeck();
	}
}
