package ie.ucd.engac.ui;

import ie.ucd.engac.messaging.Pawn;

import java.awt.*;

public class UIPawn extends Pawn implements Drawable {
    private int width = 5;
    private int height = 5;

    public UIPawn(Pawn pawn){
        super(pawn.getXLocation(),pawn.getYLocation(),pawn.getColour(),pawn.getPlayerNumber(),pawn.getNumDependants());
    }

    @Override
    public void draw(Graphics graphics) {
        graphics.setColor(Color.red);
        graphics.fillRect((int)getXLocation()+(getPlayerNumber()-1)*width,(int)getYLocation()+10,width,height);
    }
}
