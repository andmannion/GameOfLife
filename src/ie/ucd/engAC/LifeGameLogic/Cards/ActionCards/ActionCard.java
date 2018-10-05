package ie.ucd.engAC.LifeGameLogic.Cards.ActionCards;

import ie.ucd.engAC.LifeGameLogic.Cards.Card;

public abstract class ActionCard extends Card{
	protected ActionCardType actionCardType;
	
	public ActionCardType getActionCardType() {
		return actionCardType;
	}
}
