package ie.ucd.engac.messaging;

public class DecisionResponseMessage extends LifeGameMessage {
	private final int choiceIndex;	
	
	public DecisionResponseMessage(int choiceIndex, LifeGameMessageTypes decisionType) {
		super(decisionType);
		
		this.choiceIndex = choiceIndex;
	}
	
	public int getChoiceIndex() {
		return choiceIndex;
	}
}
