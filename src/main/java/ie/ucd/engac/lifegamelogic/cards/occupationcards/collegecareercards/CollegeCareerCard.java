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

}
