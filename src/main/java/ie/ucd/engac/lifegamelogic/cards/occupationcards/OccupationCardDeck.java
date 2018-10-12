package ie.ucd.engac.lifegamelogic.cards.occupationcards;

import java.util.ArrayList;

import ie.ucd.engac.lifegamelogic.cards.CardConfigHandler;
import ie.ucd.engac.lifegamelogic.cards.CardDeck;

public abstract class OccupationCardDeck extends CardDeck {
	private String configString;

	protected abstract void initialiseCards();
}
