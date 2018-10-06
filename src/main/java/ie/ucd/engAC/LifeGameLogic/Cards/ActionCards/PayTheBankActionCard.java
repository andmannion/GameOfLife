package main.java.ie.ucd.engAC.LifeGameLogic.Cards.ActionCards;

public class PayTheBankActionCard extends ActionCard {	
	private int value;
	
	public int getValue() {
		return value;
	}
	
	public PayTheBankActionCard(int value) {
		actionCardType = ActionCardType.PayTheBank;
		this.value = value;
	}
}
