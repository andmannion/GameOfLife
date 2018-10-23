package ie.ucd.engac.messaging;

public class SpinRequestMessage extends LifeGameMessage {
    // Need to tell what is to be chosen between
    private final int relatedPlayerIndex;
    private final ShadowPlayer shadowPlayer;
    private String eventMsg;

    public SpinRequestMessage(ShadowPlayer shadowPlayer, int relatedPlayerIndex, String eventMsg) {
        super(LifeGameMessageTypes.SpinRequest);
        this.relatedPlayerIndex = relatedPlayerIndex;
        this.shadowPlayer = shadowPlayer;
        this.eventMsg = eventMsg;
    }

    public String getEventMsg(){
        return eventMsg;
    }

    public int getRelatedPlayer() {
        return relatedPlayerIndex;
    }

    public ShadowPlayer getShadowPlayer(){
        return shadowPlayer;
    }
}
