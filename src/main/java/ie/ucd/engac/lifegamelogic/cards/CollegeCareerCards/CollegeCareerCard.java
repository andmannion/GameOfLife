package ie.ucd.engac.lifegamelogic.cards.CollegeCareerCards;

import ie.ucd.engac.lifegamelogic.cards.Card;

public class CollegeCareerCard extends Card {
	private final CollegeCareerTypes collegeCareerType;

	public CollegeCareerTypes getCareerType() {
		return collegeCareerType;
	}

	private final int salary;

	public int getSalary() {
		return salary;
	}

	private final int bonusNumber;

	public int getBonusNumber() {
		return bonusNumber;
	}

	private final int bonusPaymentAmount;

	public int getBonusPaymentAmount() {
		return bonusPaymentAmount;
	}

	public CollegeCareerCard(CollegeCareerTypes careerType,
							 int salary,
							 int bonusNumber,
							 int bonusPaymentAmount) {
		this.collegeCareerType = careerType;
		this.salary = salary;
		this.bonusNumber = bonusNumber;
		this.bonusPaymentAmount = bonusPaymentAmount;
	}
}
