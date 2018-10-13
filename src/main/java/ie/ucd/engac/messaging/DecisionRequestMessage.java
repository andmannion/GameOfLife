package ie.ucd.engac.messaging;

import java.util.ArrayList;

public class DecisionRequestMessage extends LifeGameMessage {
	// Need to tell what is to be chosen between
	private final int relatedPlayerIndex;
	private final ArrayList<Chooseable> choices;
	//private final Deci
	
	public DecisionRequestMessage(ArrayList<Chooseable> choices, int relatedPlayerIndex) {
		super(LifeGameMessageTypes.OptionDecisionRequest);
		this.relatedPlayerIndex = relatedPlayerIndex;
		this.choices = choices;
	}
	
	public int getRelatedPlayer() {
		return relatedPlayerIndex;
	}
	
	public ArrayList<Chooseable> getChoices(){
		return choices;
	}
}
