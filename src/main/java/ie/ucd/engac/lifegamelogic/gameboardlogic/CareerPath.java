package ie.ucd.engac.lifegamelogic.gameboardlogic;

import ie.ucd.engac.lifegamelogic.cards.occupationcards.OccupationCardTypes;
import ie.ucd.engac.messaging.Chooseable;

public class CareerPath implements Chooseable {
	private final OccupationCardTypes occupationType;
	
	public CareerPath(OccupationCardTypes occupationType) {
		this.occupationType = occupationType;
	}
	
	@Override
	public String displayChoiceDetails() {
		return "Career Path Selection:\n" + 
				"Option: " + occupationType.toString();
	}

}
