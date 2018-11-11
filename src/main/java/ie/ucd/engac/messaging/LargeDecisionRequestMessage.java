package ie.ucd.engac.messaging;

import java.util.ArrayList;

public class LargeDecisionRequestMessage extends LifeGameRequestMessage {
	// Need to tell what is to be chosen between
	private final int relatedPlayerNumber;
	private final ArrayList<Chooseable> choices;

	public LargeDecisionRequestMessage(ArrayList<Chooseable> choices, int relatedPlayerNumber, String eventMessage) {
		super(LifeGameMessageTypes.LargeDecisionRequest,eventMessage);
		this.relatedPlayerNumber = relatedPlayerNumber;
		this.choices = choices;
	}

	public int getRelatedPlayer() {
		return relatedPlayerNumber;
	}
	
	public ArrayList<Chooseable> getChoices(){
		return choices;
	}
}
