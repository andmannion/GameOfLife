package ie.ucd.engac.lifegamelogic.cards.occupationcards.collegecareercards;

import ie.ucd.engac.lifegamelogic.cards.occupationcards.OccupationCard;
import ie.ucd.engac.lifegamelogic.cards.occupationcards.OccupationCardTypes;
import ie.ucd.engac.messaging.Chooseable;

public class CollegeCareerCard extends OccupationCard implements Chooseable {
	private final CollegeCareerTypes collegeCareerType;
	
	public CollegeCareerCard(CollegeCareerTypes careerType,
							 int salary,
							 int bonusNumber,
							 int bonusPaymentAmount) {
		occupationCardType = OccupationCardTypes.CollegeCareer;
		this.collegeCareerType = careerType;
		this.salary = salary;
		this.bonusNumber = bonusNumber;
		this.bonusPaymentAmount = bonusPaymentAmount;
	}
	
	public CollegeCareerTypes getCareerType() {
		return collegeCareerType;
	}

	@Override
	public String displayChoiceDetails() {
		String string = "";
		string = string.concat("Type: " + occupationCardType);
		string = string.concat(" Subtype: " + collegeCareerType.toString());
		string = string.concat(" Salary: " + salary);
		string = string.concat(" Bonus payout: " + bonusPaymentAmount);
		string = string.concat(" Bonus number: " + bonusNumber);
		return string;
	}
}
