package main.java.ie.ucd.engAC.LifeGameLogic;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import main.java.ie.ucd.engAC.LifeGameLogic.Cards.CardDeck;
import main.java.ie.ucd.engAC.LifeGameLogic.Cards.ActionCards.ActionCardDeck;
import main.java.ie.ucd.engAC.LifeGameLogic.Cards.HouseCards.HouseCardDeck;
//import main.java.ie.ucd.engAC.LifeGameLogic.Cards.ActionCards.CareerCardDeck;

public class Bank {

	private ActionCardDeck actionCardDeck;
	private HouseCardDeck houseCardDeck;
	//private CareerCardDeck careerCardDeck;
	//private CardDeck collegeCareerCardDeck;

	// Pull in the action card deck config from the config file.
	// Where should this config be stored, what format should it be in?
	public Bank() {
		InitialiseDecks();
	}

	private void InitialiseDecks() {
		// Initialise decks of different types
		actionCardDeck = new ActionCardDeck();
		actionCardDeck.shuffle();

		String houseCardConfigFileLocation = "C:\\Users\\Andrew Mannion\\Google Drive\\Stage 5\\Software Engineering\\Project\\HouseCardConfig.json";
		
		byte[] encoded = new byte[0];
		
		try {
			encoded = Files.readAllBytes(Paths.get(houseCardConfigFileLocation));
		} catch (Exception e) {
			System.out.println(e.toString());
		}

		Charset charset = Charset.defaultCharset();
		String configString = new String(encoded, charset);

		houseCardDeck = new HouseCardDeck(configString);
		houseCardDeck.shuffle();
		// careerCardDeck = new CareerCardDeck();
		// careerCardDeck.Shuffle();
		// collegeCareerCardDeck = new CollegeCareerCardDeck();
		// collegeCareerCardDeck.Shuffle();
	}
}
