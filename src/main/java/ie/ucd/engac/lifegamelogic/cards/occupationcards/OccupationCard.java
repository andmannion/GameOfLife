package ie.ucd.engac.lifegamelogic.cards.occupationcards;

import ie.ucd.engac.lifegamelogic.cards.Card;

public abstract class OccupationCard extends Card {
	protected OccupationCardTypes occupationCardType;
	
	public OccupationCardTypes getOccupationCardType() {
		return occupationCardType;
	}
	
	protected int salary;

	public int getSalary() {
		return salary;
	}

	protected int bonusNumber;

	public int getBonusNumber() {
		return bonusNumber;
	}

	protected int bonusPaymentAmount;

	public int getBonusPaymentAmount() {
		return bonusPaymentAmount;
	}
	
	public String convertDrawableString(){
        StringBuilder displayChoiceStringBuilder = new StringBuilder();
        displayChoiceStringBuilder.append("Type: " + occupationCardType);
        displayChoiceStringBuilder.append(" Subtype: " + occupationCardType.toString());
        displayChoiceStringBuilder.append(" Salary: " + salary);
        displayChoiceStringBuilder.append(" Bonus payout: " + bonusPaymentAmount);
        displayChoiceStringBuilder.append(" Bonus number: " + bonusNumber);
        return displayChoiceStringBuilder.toString();
	}
}
