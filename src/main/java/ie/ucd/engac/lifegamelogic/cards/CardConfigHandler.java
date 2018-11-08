package ie.ucd.engac.lifegamelogic.cards;

import java.util.ArrayList;

public interface CardConfigHandler<T extends Card> {
	ArrayList<T> initialiseCards();
}
