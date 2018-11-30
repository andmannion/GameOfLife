package ie.ucd.engac.ui;

import ie.ucd.engac.messaging.Pawn;
import ie.ucd.engac.messaging.Tile;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

public class UIBoard implements Drawable {

    private int boardAreaHeight;
    private int boardAreaWidth;
    private int tileDimension;

    private GameUI gameUI;
    private BufferedImage boardImage;
    private Graphics boardGraphics;

    //pawns and board
    private HashMap<Integer,UIPawn> pawnMap;
    private ArrayList<UITile> uiTiles;

    UIBoard(GameUI gameUI, int boardAreaHeight, int boardAreaWidth){
        this.gameUI = gameUI;
        this.boardAreaHeight = boardAreaHeight;
        this.boardAreaWidth = boardAreaWidth;
        uiTiles = new ArrayList<>();
        tileDimension = (int)Math.floor(0.05* boardAreaWidth);

        boardImage = new BufferedImage(boardAreaWidth,boardAreaHeight, BufferedImage.TYPE_INT_RGB);
        boardGraphics = boardImage.getGraphics();
        boardGraphics.setColor(Color.lightGray);
        boardGraphics.fillRect(0,0,boardAreaWidth,boardAreaHeight);
    }

    void setLayout(ArrayList<Tile> tiles) {
        for(Tile tile:tiles){
            uiTiles.add(new UITile(tile, tile.getXLocation()* boardAreaWidth,tile.getYLocation()* boardAreaHeight, tileDimension));
        }
        drawBoard(boardGraphics);
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
        switch(gameUI.getUIState()){
            case Init:
                graphics.setColor(Color.LIGHT_GRAY);
                graphics.drawRect(0,0,boardAreaWidth,boardAreaHeight);
                break;
            case CardChoice:
            case LargeChoice:
                //graphics.drawImage(boardImage,0,0, null);
                //drawPawns(graphics);
                //break;
            case WaitingForSpin: case PostSpin:
                graphics.drawImage(boardImage,0,0, null);
                drawPawns(graphics);
                break;
            default:
                break;
        }
    }

    private void drawPawns(Graphics graphics){
        if(pawnMap != null){
            for (UIPawn pawnEntry:pawnMap.values()){
                pawnEntry.draw(graphics);
            }
        }
    }

    private void drawBoard(Graphics graphics){
        graphics.setColor(Color.LIGHT_GRAY);
        graphics.fillRect(0,0,boardAreaWidth,boardAreaHeight);
        if (uiTiles != null) {
            for (UITile uiTile : uiTiles) {
                uiTile.draw(graphics);
                System.err.println("doot");
            }
        }
    }
}

