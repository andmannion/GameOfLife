package ie.ucd.engac.lifegamelogic.cards.actioncards;

public class PayTheBankActionCard extends ActionCard {	
	private int value;
	
	public int getValue() {
		return value;
	}
	
	public PayTheBankActionCard(int value) {
		actionCardType = ActionCardTypes.PayTheBank;
		this.value = value;
	}
}
