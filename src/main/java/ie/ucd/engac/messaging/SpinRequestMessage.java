package ie.ucd.engac.messaging;

public class SpinRequestMessage extends LifeGameRequestMessage {
    // Need to tell what is to be chosen between
    private final int relatedPlayerNumber;
    private final ShadowPlayer shadowPlayer;

    public SpinRequestMessage(ShadowPlayer shadowPlayer, int relatedPlayerNumber, String eventMsg) {
        super(LifeGameMessageTypes.SpinRequest, eventMsg);
        this.relatedPlayerNumber = relatedPlayerNumber;
        this.shadowPlayer = shadowPlayer;
    }

    public int getRelatedPlayerNumber() {
        return relatedPlayerNumber;
    }

    public ShadowPlayer getShadowPlayer(){
        return shadowPlayer;
    }
}
