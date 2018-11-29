package ie.ucd.engac.messaging;

import java.util.ArrayList;

public class UIConfigMessage extends LifeGameRequestMessage {

    private ArrayList<Pawn> pawns;


    public UIConfigMessage(ArrayList<Pawn> pawns, String eventMessage, ShadowPlayer shadowPlayer){
        super(LifeGameMessageTypes.UIConfigMessage,eventMessage, shadowPlayer); //uhh?
        this.pawns = pawns;
    }

    public ArrayList<Pawn> getPawns() {
        return pawns;
    }
}
