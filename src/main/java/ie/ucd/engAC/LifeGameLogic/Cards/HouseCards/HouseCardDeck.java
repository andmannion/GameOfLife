package main.java.ie.ucd.engAC.LifeGameLogic.Cards.HouseCards;

import main.java.ie.ucd.engAC.LifeGameLogic.Cards.CardDeck;

public class HouseCardDeck extends CardDeck {
	private final String configString;
	
	public HouseCardDeck(String configString) {
		super();
		
		this.configString = configString;
		
		initialiseCards();
	}
	
	private void initialiseCards() {
		HouseCardConfigHandler houseCardConfigHandler = new DefaultHouseCardConfigHandler(configString);
		
		houseCardConfigHandler.initialiseCards();
	}
}
