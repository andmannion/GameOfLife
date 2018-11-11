package ie.ucd.engac.messaging;

public class AckResponseMessage extends LifeGameResponseMessage {
    public AckResponseMessage(){
        super(LifeGameMessageTypes.AckResponse);
    }
}
