package ie.ucd.engac.ui;

import ie.ucd.engac.messaging.Tile;

import java.awt.*;

public class UITile extends Tile implements Drawable{
    private int dimension;
    private Color tileColour;

    UITile(Tile tile,double xLocation, double yLocation ,int dimension){
        super(tile.getType(),xLocation,yLocation);
        this.dimension = dimension;
        tileColour = TileColours.getColour(tile.getType());
    }

    @Override
    public void draw(Graphics graphics){
        graphics.setColor(tileColour);
        graphics.fillRoundRect((int) getXLocation(), (int)getYLocation(), dimension, dimension,(int)(0.25*dimension),(int)(0.25*dimension));
        graphics.setColor(Color.black);
        graphics.drawRoundRect((int) getXLocation(), (int)getYLocation(), dimension, dimension,(int)(0.25*dimension),(int)(0.25*dimension));
    }
}
