package ie.ucd.engac.ui;

import ie.ucd.engac.messaging.Pawn;
import ie.ucd.engac.messaging.Tile;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class UIBoard implements Drawable {

    private int KERNEL_SIZE = 11;
    private int boardAreaHeight;
    private int boardAreaWidth;

    private GameUI gameUI;
    private BufferedImage boardImage;
    private BufferedImage blurredBoardImage;
    private Graphics boardGraphics;

    //pawns and board
    private HashMap<Integer,UIPawn> pawnMap;
    private ArrayList<UITile> uiTiles;

    UIBoard(GameUI gameUI, int boardAreaHeight, int boardAreaWidth, int panelHeight){
        this.gameUI = gameUI;
        this.boardAreaHeight = boardAreaHeight;
        this.boardAreaWidth = boardAreaWidth;
        uiTiles = new ArrayList<>();
        int tileDimension = (int) Math.floor(0.05 * boardAreaWidth);

        boardImage = new BufferedImage(boardAreaWidth,panelHeight, BufferedImage.TYPE_INT_RGB);
        boardGraphics = boardImage.getGraphics();
        boardGraphics.setColor(Color.lightGray);
        boardGraphics.fillRect(0,0,boardAreaWidth,panelHeight);
        blurredBoardImage = new BufferedImage(boardAreaWidth,panelHeight, BufferedImage.TYPE_INT_RGB);
        Graphics blurredGraphics = blurredBoardImage.getGraphics();
        blurredGraphics.setColor(Color.lightGray);
        blurredGraphics.fillRect(0,0,boardAreaWidth,panelHeight);
    }

    void setLayout(ArrayList<Tile> tiles) {
        for(Tile tile:tiles){
            uiTiles.add(new UITile(tile, tile.getXLocation()*boardAreaWidth,tile.getYLocation()*boardAreaHeight,
                    tile.getXDimension()*boardAreaWidth,tile.getYDimension()*boardAreaHeight) );
        }
        drawBoard();
        drawBlurredBoard();
    }

    void setPawnMap(HashMap<Integer,UIPawn> pawnMap){
        this.pawnMap = pawnMap;
    }

    void updatePawns(int playerNumber, double xPos, double yPos, int numDependants){
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
            case EndGame:
            case WaitingForAck:
                graphics.drawImage(blurredBoardImage,0,0, null);
                //drawPawns(graphics);
                break;
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

    private void drawBoard(){
        boardGraphics.setColor(Color.LIGHT_GRAY);
        boardGraphics.fillRect(0,0,boardAreaWidth,boardAreaHeight);
        if (uiTiles != null) {
            for (UITile uiTile : uiTiles) {
                uiTile.draw(boardGraphics);
            }
        }
    }

    private void drawBlurredBoard(){
        BufferedImage temp;
        int kernelSize = KERNEL_SIZE;
        float[] kernelValues = new float[kernelSize*kernelSize];
        Arrays.fill(kernelValues, 1F/(kernelSize*kernelSize));
        Kernel kernel = new Kernel(kernelSize, kernelSize,kernelValues);

        /*
        begin http://www.informit.com/articles/article.aspx?p=1013851&seqNum=5
         */
        int xOffset = (kernelSize - 1);
        int yOffset = (kernelSize - 1);
        BufferedImage paddedImage = new BufferedImage(
                boardImage.getWidth() + 2*kernelSize - 1,
                boardImage.getHeight() + 2*kernelSize - 1,
                BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = paddedImage.createGraphics();
        g2.setColor(Color.lightGray);
        g2.fillRect(0,0,paddedImage.getWidth(),paddedImage.getHeight());
        g2.drawImage(boardImage, xOffset, yOffset, null);
        g2.dispose();
        /*
        end http://www.informit.com/articles/article.aspx?p=1013851&seqNum=5
         */

        BufferedImageOp op = new ConvolveOp(kernel);
        temp = op.filter(paddedImage, null);
        //temp = op.filter(temp, null);
        op.filter(temp, blurredBoardImage);
    }
}

