package ie.ucd.engac.messaging;

public class SpinResultMessage extends LifeGameMessage {
    private int spinResult;

    public SpinResultMessage(int spinResult){
        super(LifeGameMessageTypes.SpinResult);
        this.spinResult = spinResult;
    }

    public int getSpinResult() {
        return spinResult;
    }
}
