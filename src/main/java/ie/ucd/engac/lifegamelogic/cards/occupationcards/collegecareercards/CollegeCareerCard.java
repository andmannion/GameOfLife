package ie.ucd.engac.lifegamelogic.cards.occupationcards.collegecareercards;

import ie.ucd.engac.lifegamelogic.cards.occupationcards.OccupationCard;
import ie.ucd.engac.lifegamelogic.cards.occupationcards.OccupationCardTypes;

public class CollegeCareerCard extends OccupationCard {
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
	public String convertDrawableString() {
		String string = "";
		string.concat("Type: " + occupationCardType.toString());
		string.concat(" Subtype:" + collegeCareerType.toString());
		string.concat(" Salary: " + salary);
		string.concat(" Bonus payout: " + bonusPaymentAmount);
		string.concat(" Bonus number: " + bonusNumber);
		return string;
	}
}
