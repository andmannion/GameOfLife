package ie.ucd.engac.ui;

import ie.ucd.engac.messaging.Tile;

import java.awt.*;

public class UITile extends Tile implements Drawable{
    private int dimension;

    UITile(Tile tile,double xLocation, double yLocation ,int dimension){
        super(tile.getType(),xLocation,yLocation);
        this.dimension = dimension;
    }

    @Override
    public void draw(Graphics graphics) { graphics.setColor(Color.CYAN);
        graphics.fillRect((int) getXLocation(), (int)getYLocation(), dimension, dimension);
    }
}
