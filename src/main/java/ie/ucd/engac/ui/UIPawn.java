package ie.ucd.engac.ui;

import ie.ucd.engac.messaging.Pawn;

import java.awt.*;

public class UIPawn extends Pawn implements Drawable {

    public UIPawn(Pawn pawn){
        super(pawn.getXLocation(),pawn.getYLocation(),pawn.getColour(),pawn.getPlayerNumber(),pawn.getNumDependants());
    }

    @Override
    public void draw(Graphics graphics) {
        //pass
    }
}
