package ie.ucd.engac.ui;

import ie.ucd.engac.messaging.Pawn;

import java.awt.*;

public class UIPawn extends Pawn implements Drawable {
    private static final int PER_ROW = 2;
    private int width;
    private int height;
    private int xScaleFactor;
    private int yScaleFactor;
    private int dependantRadius;

    private int currentX;
    private int currentY;

    UIPawn(Pawn pawn, int dimension){
        super(pawn.getXLocation(),pawn.getYLocation(),pawn.getColour(),pawn.getPlayerNumber(),pawn.getNumDependants());
        this.width = dimension;
        this.height = dimension*2;
        this.xScaleFactor = 0;
        this.yScaleFactor = 0;
        dependantRadius = (int)Math.floor(width/3.0);
    }

    void setScalingFactors(int xScaleFactor, int yScaleFactor){
        this.xScaleFactor = xScaleFactor;
        this.yScaleFactor = yScaleFactor;

    }
    @Override
    public void draw(Graphics graphics) {
        currentX = (int) ((xLocation*xScaleFactor)+(1.45*playerNumber-1)*width);
        currentY = (int) ((yLocation*yScaleFactor)+2);
        graphics.setColor(colour);
        graphics.fillRect(currentX,currentY,width,height);
        graphics.setColor(Color.black);
        graphics.drawRect(currentX,currentY,width,height);
        drawDependants(graphics);
    }

    private void drawDependants(Graphics graphics){
        for(int inc=0;inc<(numDependants+1);inc++){
            int xPos = currentX + (width/3) * (1 + (inc%PER_ROW)) - dependantRadius/2;
            int yPos = currentY + (height/4) * (1 + (inc/PER_ROW)) - dependantRadius/2;
            drawDependant(graphics,xPos,yPos);
        }
    }

    private void drawDependant(Graphics graphics, int xTopLeft, int yTopLeft){
        graphics.setColor(Color.black);
        graphics.fillOval(xTopLeft,yTopLeft,dependantRadius,dependantRadius);
    }
}
