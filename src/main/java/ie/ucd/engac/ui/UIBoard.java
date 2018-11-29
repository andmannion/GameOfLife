package ie.ucd.engac.ui;

import ie.ucd.engac.messaging.Pawn;
import ie.ucd.engac.messaging.Tile;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class UIBoard implements Drawable {

    private int panelHeight;
    private int panelWidth;
    private int tileDimension;

    //pawns and board
    private HashMap<Integer,UIPawn> pawnMap;
    private ArrayList<UITile> uiTiles;

    UIBoard(GameUI gameUI){
        panelHeight = gameUI.getPanelHeight();
        panelWidth = gameUI.getPanelWidth();
        uiTiles = new ArrayList<>();
        tileDimension = (int)Math.floor(0.05*panelWidth);
    }

    void setLayout(ArrayList<Tile> tiles) {
        for(Tile tile:tiles){
            uiTiles.add(new UITile(tile, tile.getXLocation()*panelWidth,tile.getYLocation()*panelHeight, tileDimension));
        }
    }

    void setPawnMap(HashMap<Integer,UIPawn> pawnMap){
        this.pawnMap = pawnMap;
    }

    void updatePawns(int playerNumber, int xPos, int yPos){
        Pawn pawn = pawnMap.get(playerNumber);
        pawn.setXLocation(xPos);
        pawn.setYLocation(yPos);
    }

    @Override
    public void draw(Graphics graphics){
        if (uiTiles != null) {
            for (UITile uiTile : uiTiles) {
                uiTile.draw(graphics);
                }
        }
        if(pawnMap != null){
            for (UIPawn pawnEntry:pawnMap.values()){
                pawnEntry.draw(graphics);
            }
        }
    }
}

