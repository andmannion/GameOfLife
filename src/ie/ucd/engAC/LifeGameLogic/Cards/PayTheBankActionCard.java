package ie.ucd.engAC.LifeGameLogic.Cards;

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
