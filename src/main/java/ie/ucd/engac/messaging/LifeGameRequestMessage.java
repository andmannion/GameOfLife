package ie.ucd.engac.messaging;

public class LifeGameRequestMessage extends LifeGameMessage {

    private String eventMsg;

    public LifeGameRequestMessage(LifeGameMessageTypes lifeGameMessageType, String eventMsg){
        super(lifeGameMessageType);
        this.eventMsg = eventMsg;
    }

    public String getEventMsg(){
        return eventMsg;
    }
}
