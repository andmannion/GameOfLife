package ie.ucd.engac.messaging;

public class AckRequestMessage extends LifeGameMessage {
    // Need to tell what is to be chosen between
    private String eventMsg;

    public AckRequestMessage(String eventMsg) { //TODO remove index
        super(LifeGameMessageTypes.AckRequest);
        this.eventMsg = eventMsg;
    }

    public String getEventMsg(){
        return eventMsg;
    }

}
