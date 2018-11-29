package ie.ucd.engac.ui;

import ie.ucd.engac.messaging.Tile;

import java.awt.*;

public class UITile extends Tile implements Drawable{

    UITile(Tile tile){
        super(tile.getType(),tile.getxLocation(),tile.getyLocation());
    }

    @Override
    public void draw(Graphics graphics) {

    }
}
