package ie.ucd.engac.lifegamelogic.cards.occupationcards.careercards;

import ie.ucd.engac.lifegamelogic.cards.occupationcards.OccupationCard;
import ie.ucd.engac.lifegamelogic.cards.occupationcards.OccupationCardTypes;
import ie.ucd.engac.messaging.Chooseable;

public class CareerCard extends OccupationCard implements Chooseable {
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
	public String displayChoiceDetails() {
	    String string = "";
		string = string.concat(occupationCardType + ":\n ");
		string = string.concat(careerType.toString() + ",\n");
		string = string.concat(" Salary: " + salary + ",\n");
		string = string.concat(" Bonus payout: " + bonusPaymentAmount + ",\n");
		string = string.concat(" Bonus number: " + bonusNumber + "\n");
		return string;
	}
}
