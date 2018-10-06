package ie.ucd.engAC.LifeGameLogic.Cards.CareerCards;

import java.util.ArrayList;

import ie.ucd.engAC.LifeGameLogic.Cards.CardConfigHandler;

public class DefaultCareerCardConfigHandler implements CardConfigHandler<CareerCard> {

	public ArrayList<CareerCard> initialiseCards() {
		return new ArrayList<CareerCard>();
	}
	
}
