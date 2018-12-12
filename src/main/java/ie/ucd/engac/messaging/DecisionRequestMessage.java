package ie.ucd.engac.messaging;

import java.util.ArrayList;

public class DecisionRequestMessage extends LifeGameRequestMessage {
	// Need to tell what is to be chosen between
	private final int relatedPlayerIndex;
	private final ArrayList<Chooseable> choices;
	
	public DecisionRequestMessage(ArrayList<Chooseable> choices,
								  int relatedPlayerIndex,
								  String eventMessage,
								  LifeGameMessageTypes requestType,
								  ShadowPlayer shadowPlayer) {
		super(requestType, eventMessage, shadowPlayer);
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
