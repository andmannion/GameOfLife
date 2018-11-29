package ie.ucd.engac.ui;

import ie.ucd.engac.messaging.Pawn;

import java.awt.*;

public class UIPawn extends Pawn implements Drawable {
    private static final int PER_ROW = 2;
    private int width = 10;
    private int height = 20; //TODO
    private int dependantRadius = (int)Math.floor(width/3.0);

    private int currentX;
    private int currentY;

    UIPawn(Pawn pawn){
        super(pawn.getXLocation(),pawn.getYLocation(),pawn.getColour(),pawn.getPlayerNumber(),pawn.getNumDependants());
    }

    @Override
    public void draw(Graphics graphics) {
        currentX = (int) (xLocation+(1.45*playerNumber-1)*width);
        currentY = (int) (yLocation+10);
        graphics.setColor(colour);
        graphics.fillRect(currentX,currentY,width,height);
        graphics.setColor(Color.black);
        graphics.drawRect(currentX,currentY,width,height);
        graphics.setColor(Color.black);
        drawDependants(graphics);
    }

    private void drawDependants(Graphics graphics){
        for(int inc=0;inc<numDependants;inc++){
            int xPos = currentX + (int)(width * 0.333) * (1 + (inc % PER_ROW)) - dependantRadius/2;
            int yPos = currentY + (int)(height * 0.25) * (1 + (inc/PER_ROW)) - dependantRadius/2;
            drawDependant(graphics,xPos,yPos);
        }
    }

    private void drawDependant(Graphics graphics, int xTopLeft, int yTopLeft){
        graphics.setColor(Color.black);
        graphics.fillOval(xTopLeft,yTopLeft,dependantRadius,dependantRadius);
    }
}
