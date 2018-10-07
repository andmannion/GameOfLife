package ie.ucd.engAC.LifeGameLogic.Cards.ActionCards;

public class GetCashFromBankActionCard extends ActionCard {
	private int amountToPay;

	public int getAmountToPay() {
		return amountToPay;
	}

	public GetCashFromBankActionCard(int amountToPay) {
		actionCardType = ActionCardTypes.GetCashFromBank;
		this.amountToPay = amountToPay;
	}
}