package ie.ucd.engac.messaging;

public class LifeGameRequestMessage extends LifeGameMessage {

    private String eventMsg;
    private ShadowPlayer shadowPlayer;

    public LifeGameRequestMessage(LifeGameMessageTypes lifeGameMessageType, String eventMsg, ShadowPlayer shadowPlayer){
        super(lifeGameMessageType);
        this.eventMsg = eventMsg;
        this.shadowPlayer = shadowPlayer;
    }

    public String getEventMsg(){
        return eventMsg;
    }

    public ShadowPlayer getShadowPlayer() {
        return shadowPlayer;
    }
}
