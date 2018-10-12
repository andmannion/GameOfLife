package ie.ucd.engac.lifegamelogic.cards.actioncards;

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