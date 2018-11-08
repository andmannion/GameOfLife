package ie.ucd.engac.messaging;

public class SpinRequestMessage extends LifeGameMessage {
    // Need to tell what is to be chosen between
    private final int relatedPlayerNumber;
    private final ShadowPlayer shadowPlayer;
    private String eventMsg;

    public SpinRequestMessage(ShadowPlayer shadowPlayer, int relatedPlayerNumber, String eventMsg) {
        super(LifeGameMessageTypes.SpinRequest);
        this.relatedPlayerNumber = relatedPlayerNumber;
        this.shadowPlayer = shadowPlayer;
        this.eventMsg = eventMsg;
    }

    public String getEventMsg(){
        return eventMsg;
    }

    public int getRelatedPlayerNumber() {
        return relatedPlayerNumber;
    }

    public ShadowPlayer getShadowPlayer(){
        return shadowPlayer;
    }
}
