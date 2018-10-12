package ie.ucd.engac.ui;

import ie.ucd.engac.GameEngine;
import ie.ucd.engac.messaging.ShadowPlayer;

import java.awt.*;

public class GameHUD implements Drawable {
    //TODO check that all printout requirements are met
    //add #actioncards

    private ShadowPlayer sPlayer;

    private GameEngine gameEngine;
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

    private int HOUSETEXTSKIP = 15;

    GameHUD(GameEngine gameEngine,GameUI gameUI){//TODO remove gameEngine reference
        this.gameEngine = gameEngine;

        panelHeight = gameUI.getPanelHeight();
        panelWidth = gameUI.getPanelWidth();

        boxStartY = Math.round(((0.7f)*panelHeight));
        boxLengthY = panelHeight-boxStartY;
        boxStartX = 0;
        boxLengthX = panelWidth;

        firstStringX = 0;
        firstStringY = boxStartY+Math.round(0.08f*boxLengthY);
        stringLengthX = Math.round(0.1f*boxLengthX);
        stringLengthY = Math.round(0.125f*boxLengthY);

        rectangle = new Rectangle(boxStartX, boxStartY, boxLengthX, boxLengthY);
    }

    private void updateFields(ShadowPlayer shadowPlayer){
        this.sPlayer = shadowPlayer;
    }
    public void draw(Graphics graphics){
        try{
            graphics.setColor(Color.darkGray);
            graphics.fillRect(rectangle.x,rectangle.y,rectangle.width,rectangle.height);
            graphics.setColor(Color.black);
            graphics.drawString("Player: " + sPlayer.playerNumToString() + " Colour: " + sPlayer.playerColourToString(),
                                                                                        firstStringX, firstStringY+stringLengthY*PLAYER_LOC);
            graphics.drawString("Bank Balance: "+ sPlayer.bankBalToString(),        firstStringX, firstStringY+stringLengthY*BANK_LOC);
            graphics.drawString("Number of loans: " + sPlayer.numLoansToString(),   firstStringX, firstStringY+stringLengthY*LOANS_LOC);
            graphics.drawString("Career Card: " + sPlayer.careerCardToString(),     firstStringX, firstStringY+stringLengthY*DEPEND_LOC);
            //TODO house cards need to respect border & wrap around?
            graphics.drawString("House Cards: " + sPlayer.houseCardsToString(),     firstStringX, firstStringY+stringLengthY*CAREER_LOC);
            graphics.drawString("Dependants: "+ sPlayer.dependantsToString(),       firstStringX, firstStringY+stringLengthY*HOUSE_LOC);
            graphics.drawString("Action Cards: " + sPlayer.actionCardsToString(),   firstStringX, firstStringY+stringLengthY*ACTION_LOC);
        }
        catch (Exception e){
            System.out.println("Exception in GameHUD.draw() " + e);
        }
    }
}


