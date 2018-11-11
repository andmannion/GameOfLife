package ie.ucd.engac.messaging;

public class AckRequestMessage extends LifeGameRequestMessage {
    // Need to tell what is to be chosen between

    public AckRequestMessage(String eventMsg) { //TODO remove index
        super(LifeGameMessageTypes.AckRequest,eventMsg);
    }

}
