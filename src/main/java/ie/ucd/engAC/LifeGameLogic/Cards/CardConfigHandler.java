package ie.ucd.engAC.LifeGameLogic.Cards;

import java.util.ArrayList;

public interface CardConfigHandler<T extends Card> {
	ArrayList<T> initialiseCards();
}
