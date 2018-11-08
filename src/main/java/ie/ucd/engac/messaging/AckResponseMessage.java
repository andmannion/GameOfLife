package ie.ucd.engac.messaging;

public class AckResponseMessage extends LifeGameMessage {
    public AckResponseMessage(){
        super(LifeGameMessageTypes.AckResponse);
    }
}
