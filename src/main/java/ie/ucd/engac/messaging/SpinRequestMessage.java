package ie.ucd.engac.messaging;

public class SpinRequestMessage extends LifeGameRequestMessage {
    // Need to tell what is to be chosen between
    private final int relatedPlayerNumber;
    //TODO delete this
    public SpinRequestMessage(ShadowPlayer shadowPlayer, int relatedPlayerNumber, String eventMsg) {
        super(LifeGameMessageTypes.SpinRequest, eventMsg, shadowPlayer);
        this.relatedPlayerNumber = relatedPlayerNumber;
    }

    public int getRelatedPlayerNumber() {
        return relatedPlayerNumber;
    }

}
