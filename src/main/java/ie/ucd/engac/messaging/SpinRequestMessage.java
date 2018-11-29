package ie.ucd.engac.messaging;

public class SpinRequestMessage extends LifeGameRequestMessage {
    // Need to tell what is to be chosen between
    public SpinRequestMessage(String eventMsg, ShadowPlayer shadowPlayer) {
        super(LifeGameMessageTypes.SpinRequest, eventMsg, shadowPlayer);
    }
}
