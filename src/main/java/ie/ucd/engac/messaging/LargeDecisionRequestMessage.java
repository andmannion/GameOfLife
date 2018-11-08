package ie.ucd.engac.messaging;

import java.util.ArrayList;

public class LargeDecisionRequestMessage extends LifeGameMessage {
	// Need to tell what is to be chosen between
	private final int relatedPlayerNumber;
	private final ArrayList<Chooseable> choices;
	private String eventMsg;

	public LargeDecisionRequestMessage(ArrayList<Chooseable> choices, int relatedPlayerNumber, String eventMessage) {
		super(LifeGameMessageTypes.LargeDecisionRequest);
		this.relatedPlayerNumber = relatedPlayerNumber;
		this.choices = choices;
		this.eventMsg = eventMessage;
	}

	public String getEventMsg(){
		return eventMsg;
	}
	public int getRelatedPlayer() {
		return relatedPlayerNumber;
	}
	
	public ArrayList<Chooseable> getChoices(){
		return choices;
	}
}
