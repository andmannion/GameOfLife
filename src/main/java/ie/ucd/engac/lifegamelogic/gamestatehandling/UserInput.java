package ie.ucd.engac.lifegamelogic.gamestatehandling;

import ie.ucd.engac.messaging.LifeGameMessageTypes;

public class UserInput {
	private LifeGameMessageTypes lifeGameMessageType;
	
	public LifeGameMessageTypes getLifeGameMessageType() {
		return lifeGameMessageType;
	}
	
	public UserInput(LifeGameMessageTypes lifeGameMessageType) {
		this.lifeGameMessageType = lifeGameMessageType;
	}
}
