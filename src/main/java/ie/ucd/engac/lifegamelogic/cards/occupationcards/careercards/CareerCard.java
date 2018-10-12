package ie.ucd.engac.lifegamelogic.cards.occupationcards.careercards;

import ie.ucd.engac.lifegamelogic.cards.occupationcards.OccupationCard;
import ie.ucd.engac.lifegamelogic.cards.occupationcards.OccupationCardTypes;

public class CareerCard extends OccupationCard {
	private final CareerTypes careerType;	

	public CareerCard(CareerTypes careerType, 
				      int salary,
				      int bonusNumber, 
				      int bonusPaymentAmount) {
		
		occupationCardType = OccupationCardTypes.Career;
		this.careerType = careerType;
		this.salary = salary;
		this.bonusNumber = bonusNumber;
		this.bonusPaymentAmount = bonusPaymentAmount;
	}
	
	public CareerTypes getCareerType() {
		return careerType;
	}

	@Override
	public String convertDrawableString() {
		String string = "";
		string.concat("Type: " + occupationCardType.toString());
		string.concat("Subtype:" + careerType.toString());
		string.concat(" Salary: " + salary);
		string.concat(" Bonus payout: " + bonusPaymentAmount);
		string.concat(" Bonus number: " + bonusNumber);
		return string;
	}
}
