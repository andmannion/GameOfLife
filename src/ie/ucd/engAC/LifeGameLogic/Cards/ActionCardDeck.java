package ie.ucd.engAC.LifeGameLogic.Cards;

import java.util.ArrayList;

public class ActionCardDeck extends CardDeck {
	public ActionCardDeck() {
		super();

		InitialiseSubGroups();
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

	private void InitialiseSubGroups() {
		// Must initialise each subgroup of the action card.
		ArrayList<CardConfigHandler> myCardConfigHandlers = new ArrayList<CardConfigHandler>();

		myCardConfigHandlers.add(new CareerChangeConfigHandler());
		myCardConfigHandlers.add(new PlayersPayConfigHandler());
		myCardConfigHandlers.add(new PayTheBankConfigHandler());

		for (CardConfigHandler cardConfigHandler : myCardConfigHandlers) {
			cards.addAll(cardConfigHandler.InitialiseCardSubGroup());
		}
	}
}
