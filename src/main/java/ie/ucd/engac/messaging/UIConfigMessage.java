package ie.ucd.engac.messaging;

import java.util.ArrayList;

public class UIConfigMessage extends LifeGameRequestMessage {

    private ArrayList<Pawn> pawns;


    public UIConfigMessage(ArrayList<Pawn> pawns, String eventMessage){
        super(LifeGameMessageTypes.UIConfigMessage,eventMessage);
        this.pawns = pawns;
    }

    public ArrayList<Pawn> getPawns() {
        return pawns;
    }
}
