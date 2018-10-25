package ie.ucd.engac.messaging;

public class AckRequestMessage extends LifeGameMessage {
    // Need to tell what is to be chosen between
    private final int relatedPlayerIndex;
    private String eventMsg;

    public AckRequestMessage(int relatedPlayerIndex, String eventMsg) {
        super(LifeGameMessageTypes.AckRequest);
        this.relatedPlayerIndex = relatedPlayerIndex;
        this.eventMsg = eventMsg;
    }

    public String getEventMsg(){
        return eventMsg;
    }

    public int getRelatedPlayer() {
        return relatedPlayerIndex;
    }

}
