package ie.ucd.engac.messaging;

public class DecisionResponseMessage extends LifeGameResponseMessage {
	private final int choiceIndex;	
	
	public DecisionResponseMessage(int choiceIndex) {
		super(LifeGameMessageTypes.OptionDecisionResponse);
		
		this.choiceIndex = choiceIndex;
	}
	
	public int getChoiceIndex() {
		return choiceIndex;
	}
}
