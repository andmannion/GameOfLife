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
		StringBuilder displayChoiceStringBuilder = new StringBuilder();
		displayChoiceStringBuilder.append("Type: " + occupationCardType);
		displayChoiceStringBuilder.append(" Subtype: " + collegeCareerType.toString());
		displayChoiceStringBuilder.append(" Salary: " + salary);
		displayChoiceStringBuilder.append(" Bonus payout: " + bonusPaymentAmount);
		displayChoiceStringBuilder.append(" Bonus number: " + bonusNumber);
		return displayChoiceStringBuilder.toString();
	}
}
