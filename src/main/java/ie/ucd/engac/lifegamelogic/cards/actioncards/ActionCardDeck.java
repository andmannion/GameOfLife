package ie.ucd.engac.lifegamelogic.cards.actioncards;

import ie.ucd.engac.lifegamelogic.cards.CardConfigHandler;
import ie.ucd.engac.lifegamelogic.cards.CardDeck;

public class ActionCardDeck extends CardDeck<ActionCard> {
	public ActionCardDeck(String configPath) {
		initialiseCards(configPath);
	}

	private void initialiseCards(String configPath){
		CardConfigHandler<ActionCard> actionCardConfigHandler = new DefaultActionCardConfigHandler(configPath);
		cards.addAll(actionCardConfigHandler.initialiseCards());
	}
}
