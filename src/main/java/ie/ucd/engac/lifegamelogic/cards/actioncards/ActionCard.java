package ie.ucd.engac.lifegamelogic.cards.actioncards;

import ie.ucd.engac.lifegamelogic.cards.Card;

public abstract class ActionCard extends Card{
	protected ActionCardTypes actionCardType;
	
	public ActionCardTypes getActionCardType() {
		return actionCardType;
	}
}
