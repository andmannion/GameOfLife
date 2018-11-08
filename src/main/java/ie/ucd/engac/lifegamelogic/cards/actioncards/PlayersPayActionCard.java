package ie.ucd.engac.lifegamelogic.cards.actioncards;

public class PlayersPayActionCard extends ActionCard {
	private int amountToPay;
	
	public int getAmountToPay() {
		return amountToPay;
	}
	
	public PlayersPayActionCard(int amountToPay) {
		actionCardType = ActionCardTypes.PlayersPay;
		this.amountToPay = amountToPay;
	}
}
