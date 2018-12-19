package ie.ucd.engac.messaging;

import java.util.ArrayList;

public class UIConfigMessage extends LifeGameRequestMessage {

    private ArrayList<Pawn> pawns;
    private Board board;


    public UIConfigMessage(ArrayList<Pawn> pawns, String eventMessage, ShadowPlayer shadowPlayer, Board board){
        super(LifeGameMessageTypes.UIConfigMessage,eventMessage, shadowPlayer);
        this.pawns = pawns;
        this.board = board;
    }

    public ArrayList<Pawn> getPawns() {
        return pawns;
    }
    public Board getBoard(){
        return board;
    }
}
