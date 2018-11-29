package ie.ucd.engac.ui;

import ie.ucd.engac.messaging.Pawn;
import ie.ucd.engac.messaging.Tile;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class UIBoard implements Drawable {

    private int boardAreaHeight;
    private int boardAreaWidth;
    private int tileDimension;

    //pawns and board
    private HashMap<Integer,UIPawn> pawnMap;
    private ArrayList<UITile> uiTiles;

    UIBoard(int boardAreaHeight, int boardAreaWidth){
        this.boardAreaHeight = boardAreaHeight;
        this.boardAreaWidth = boardAreaWidth;
        uiTiles = new ArrayList<>();
        tileDimension = (int)Math.floor(0.05* boardAreaWidth);
    }

    void setLayout(ArrayList<Tile> tiles) {
        for(Tile tile:tiles){
            uiTiles.add(new UITile(tile, tile.getXLocation()* boardAreaWidth,tile.getYLocation()* boardAreaHeight, tileDimension));
        }
    }

    void setPawnMap(HashMap<Integer,UIPawn> pawnMap){
        this.pawnMap = pawnMap;
    }

    void updatePawns(int playerNumber, int xPos, int yPos, int numDependants){
        Pawn pawn = pawnMap.get(playerNumber);
        pawn.setXLocation(xPos);
        pawn.setYLocation(yPos);
        pawn.setNumDependants(numDependants);
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

