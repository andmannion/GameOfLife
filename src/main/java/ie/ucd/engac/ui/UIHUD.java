package ie.ucd.engac.ui;

import ie.ucd.engac.messaging.ShadowPlayer;
import ie.ucd.engac.messaging.Tile;

import java.awt.*;

public class UIHUD implements Drawable {

    private ShadowPlayer sPlayer;
    private GameUI gameUI;
    private Rectangle rectangle;

    private final static int PLAYER_LOC = 0;
    private final static int BANK_LOC   = 1;
    private final static int LOANS_LOC  = 2;
    private final static int DEPEND_LOC = 3;
    private final static int CAREER_LOC = 4;
    private final static int HOUSE_LOC  = 5;
    private final static int ACTION_LOC = 6;


    private final int panelHeight;
    private final int panelWidth;

    private int boxStartX;
    private int boxStartY;// = 504;
    private int boxLengthX;// = 1280;
    private int boxLengthY;// = 216;

    private int firstStringX;// = 0;
    private int firstStringY;// = 520;
    private int stringLengthX;// = 100;
    private int stringLengthY;// = 30;

    private int cropAvoidance;

    UIHUD(GameUI gameUI, int hudStartY){
        this.gameUI = gameUI;

        panelHeight = gameUI.getPanelHeight();
        panelWidth = gameUI.getPanelWidth();

        boxStartY = hudStartY;
        boxLengthY = panelHeight-boxStartY;
        boxStartX = 0;
        boxLengthX = panelWidth;

        firstStringX = 5; //gap
        firstStringY = boxStartY+Math.round(0.08f*boxLengthY);
        stringLengthX = Math.round(0.1f*boxLengthX);
        stringLengthY = Math.round(0.125f*boxLengthY);
        cropAvoidance = panelHeight/10;

        rectangle = new Rectangle(boxStartX, boxStartY, boxLengthX, boxLengthY);
    }

    void updateFields(ShadowPlayer shadowPlayer){
        this.sPlayer = shadowPlayer;
    }
    public void draw(Graphics graphics){
        switch(gameUI.getUIState()){
            case Init:
                break;
            case WaitingForSpin: case PostSpin:
                if(sPlayer != null){
                    try{
                        graphics.setColor(UIColours.HUD_AREA_COLOUR);
                        graphics.fillRoundRect(rectangle.x-cropAvoidance,rectangle.y,(rectangle.width/2)+cropAvoidance,rectangle.height+cropAvoidance,rectangle.height/4, rectangle.height/4);
                        graphics.setColor(Color.black);
                        graphics.drawRoundRect(rectangle.x-cropAvoidance,rectangle.y,(rectangle.width/2)+cropAvoidance,rectangle.height+cropAvoidance,rectangle.height/4, rectangle.height/4);
                        graphics.setColor(sPlayer.getPlayerColour());
                        graphics.drawString("Player " + sPlayer.playerNumToString(),firstStringX, firstStringY+stringLengthY*PLAYER_LOC);
                        graphics.setColor(Color.black);                                     //TODO this colour "string" is horrid
                        //+ " (" + sPlayer.playerColourToString() + ") " + sPlayer.currentTileToString(),    firstStringX, firstStringY+stringLengthY*PLAYER_LOC);
                        graphics.drawString("Bank Balance: "+ sPlayer.bankBalToString(),        firstStringX, firstStringY+stringLengthY*BANK_LOC);
                        graphics.drawString("Number of loans: " + sPlayer.numLoansToString(),   firstStringX, firstStringY+stringLengthY*LOANS_LOC);
                        graphics.drawString("Career Card: " + sPlayer.careerCardToString(),     firstStringX, firstStringY+stringLengthY*DEPEND_LOC);
                        graphics.drawString("House Cards: " + sPlayer.houseCardsToString(),     firstStringX, firstStringY+stringLengthY*CAREER_LOC);
                        graphics.drawString("Dependants: "+ sPlayer.dependantsToString(),       firstStringX, firstStringY+stringLengthY*HOUSE_LOC);
                        graphics.drawString("Action Cards: " + sPlayer.actionCardsToString(),   firstStringX, firstStringY+stringLengthY*ACTION_LOC);
                        //graphics.drawString("Current Tile: " + ,panelWidth/2,firstStringY+stringLengthY*ACTION_LOC);
                    }
                    catch (Exception e){
                        System.err.println("Exception in UIHUD.draw() " + e.toString());
                    }
                }
                break;
            case CardChoice:
            case LargeChoice:
                if (sPlayer != null) {
                    graphics.drawString("Current Tile: " + sPlayer.currentTileToString(), firstStringX + 400, firstStringY + stringLengthY * ACTION_LOC);
                }
            default:
                break;
        }
    }
}


