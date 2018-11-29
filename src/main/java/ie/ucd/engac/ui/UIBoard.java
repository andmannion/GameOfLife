package ie.ucd.engac.ui;

import ie.ucd.engac.messaging.Pawn;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class UIBoard implements Drawable {

    private int panelHeight;
    private int panelWidth;

    //pawns and board
    private HashMap<Integer,UIPawn> pawnMap;
    private ArrayList<UITile> uiTiles;

    UIBoard(GameUI gameUI){
        panelHeight = gameUI.getPanelHeight();
        panelWidth = gameUI.getPanelWidth();
    }

    void setLayout(ArrayList<UITile> uiTiles) {
        this.uiTiles = uiTiles;
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
                graphics.setColor(Color.CYAN);
                graphics.drawRect((int) uiTile.getxLocation() * panelWidth, (int) uiTile.getyLocation() * panelHeight, 10, 10);
            }
        }
    }
}

