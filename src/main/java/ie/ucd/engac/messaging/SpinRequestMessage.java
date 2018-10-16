package ie.ucd.engac.messaging;

public class SpinRequestMessage extends LifeGameMessage {
    // Need to tell what is to be chosen between
    private final int relatedPlayerIndex;
    private final ShadowPlayer shadowPlayer;

    public SpinRequestMessage(ShadowPlayer shadowPlayer, int relatedPlayerIndex) {
        super(LifeGameMessageTypes.SpinRequest);
        this.relatedPlayerIndex = relatedPlayerIndex;
        this.shadowPlayer = shadowPlayer;
    }

    public int getRelatedPlayer() {
        return relatedPlayerIndex;
    }

    public ShadowPlayer getShadowPlayer(){
        return shadowPlayer;
    }
}
