package ie.ucd.engac.ui;

import ie.ucd.engac.messaging.Tile;

import java.awt.*;

public class UITile extends Tile implements Drawable{
    private Color tileColour;

    UITile(Tile tile,double xLocation, double yLocation, double xDimension,double yDimension){
        super(tile.getType(),xLocation,yLocation,xDimension,yDimension);
        tileColour = UIColours.getTileColour(tile.getType());
    }

    @Override
    public void draw(Graphics graphics){
        double xDimension = getXDimension();
        double yDimension = getYDimension();
        graphics.setColor(tileColour);
        graphics.fillRoundRect((int) getXLocation(), (int)getYLocation(), (int)xDimension, (int)yDimension,(int)(0.25*xDimension),(int)(0.25*yDimension));
        graphics.setColor(Color.black);
        graphics.drawRoundRect((int) getXLocation(), (int)getYLocation(), (int)xDimension, (int)yDimension,(int)(0.25*yDimension),(int)(0.25*yDimension));
    }
}
