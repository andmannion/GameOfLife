package ie.ucd.engAC.LifeGameLogic.Cards.ActionCards;

public class PlayersPayActionCard extends ActionCard {
	private int amountToPay;
	
	public int getAmountToPay() {
		return amountToPay;
	}
	
	public PlayersPayActionCard(int amountToPay) {
		actionCardType = ActionCardType.PlayersPay;
		this.amountToPay = amountToPay;
	}
}
