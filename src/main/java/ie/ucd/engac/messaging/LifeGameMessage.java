package ie.ucd.engac.messaging;

// TODO: Probably make this an abstract class
public abstract class LifeGameMessage {
	private LifeGameMessageTypes lifeGameMessageType;
	//private ArrayList<GameCommand> commandsToUI;
	//private ArrayList<GameUpdate> updates;
	
	public LifeGameMessage(LifeGameMessageTypes lifeGameMessageType) {
		this.lifeGameMessageType = lifeGameMessageType;
	}
	
	public LifeGameMessageTypes getLifeGameMessageType() {
		return lifeGameMessageType;
	}

}
