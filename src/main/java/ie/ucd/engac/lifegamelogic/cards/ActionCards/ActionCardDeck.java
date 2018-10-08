package ie.ucd.engac.lifegamelogic.cards.ActionCards;

import java.util.ArrayList;

import ie.ucd.engac.lifegamelogic.cards.CardConfigHandler;
import ie.ucd.engac.lifegamelogic.cards.CardDeck;

public class ActionCardDeck extends CardDeck {
	public ActionCardDeck() {
		super();

		initialiseSubGroups();
		/*
		 * For each subtype of action card, must initialise a list of them with the
		 * correct size. Each subtype card must contain the values as laid out in the
		 * specification.
		 * 
		 * This indicates some config information must be passed down from upper to
		 * lower elements - what design pattern would be suitable to avoid passing down
		 * through multiple elements?
		 */
	}

	private void initialiseSubGroups() {
		// Must initialise each subgroup of the action card.
		ArrayList<CardConfigHandler<ActionCard>> myActionCardConfigHandlers = new ArrayList<CardConfigHandler<ActionCard>>();

		myActionCardConfigHandlers.add(new CareerChangeConfigHandler());
		myActionCardConfigHandlers.add(new PlayersPayConfigHandler());
		myActionCardConfigHandlers.add(new PayTheBankConfigHandler());
		myActionCardConfigHandlers.add(new GetCashFromBankConfigHandler());

		for (CardConfigHandler<ActionCard> actionCardConfigHandler : myActionCardConfigHandlers) {
			cards.addAll(actionCardConfigHandler.initialiseCards());
		}
	}
}
