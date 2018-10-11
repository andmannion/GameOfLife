package ie.ucd.engac.ui;

import ie.ucd.engac.GameEngine;
import ie.ucd.engac.lifegamelogic.cards.CareerCards.CareerCard;
import ie.ucd.engac.lifegamelogic.cards.HouseCards.HouseCard;
import ie.ucd.engac.lifegamelogic.playerlogic.Player;
import java.awt.*;
import java.util.ArrayList;

public class GameHUD implements Drawable {
    //TODO check that all printout requirements are met
    private int playerNumber;
    private int numDependants;
    private int actionCards;
    private int loans;
    private int bankBalance;

    private CareerCard career;
    private int martialStatus;
    private String familyString;
    private ArrayList<HouseCard> houses;


    private GameEngine gameEngine;
    private Rectangle rectangle;

    private int panelHeight;
    private int panelWidth;

    private int boxStartX;
    private int boxStartY;// = 504;
    private int boxLengthX;// = 1280;
    private int boxLengthY;// = 216;

    private int firstStringX;// = 0;
    private int firstStringY;// = 520;
    private int stringLengthX;// = 100;
    private int stringLengthY;// = 30;

    private int HOUSETEXTSKIP = 15;

    public GameHUD(GameEngine gameEngine,GameUI gameUI){//TODO remove gameEngine reference
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
    private void updateFields(){
        Player player = gameEngine.getCurrentPlayer();
        playerNumber = player.getPlayerNumber();
        martialStatus = player.getMaritalStatus().toInt();
        numDependants = player.getNumDependants();
        if (martialStatus == 1){
            familyString = martialStatus + "spouse and " + (numDependants -1 )+ " children";
        }
        else familyString = "No dependants";

        career = player.getCareerCard();
        houses = player.getHouseCards();
        loans = 0; // TODO: Number of loans of each player should be obtained through the Bank object
        bankBalance = player.getCurrentMoney();
        actionCards = player.getActionCards().size();

    }
    synchronized public void draw(Graphics graphics){ //synch adds safety
        if (true) {
            updateFields();
        }
        else{

        }
        try{
            graphics.setColor(Color.darkGray);
            graphics.fillRect(rectangle.x,rectangle.y,rectangle.width,rectangle.height);
            graphics.setColor(Color.black);
            graphics.drawString("Player: " + playerNumber + 1 + " Colour: ", firstStringX, firstStringY);
            drawBankBal(graphics, firstStringX, firstStringY +1* stringLengthY);
            drawNumLoans(graphics, firstStringX, firstStringY +2* stringLengthY);
            drawCareerCard(graphics, firstStringX, firstStringY +3* stringLengthY);
            drawHouseCards(graphics, firstStringX, firstStringY +4* stringLengthY);
            drawDependants(graphics, firstStringX, firstStringY +5* stringLengthY);
        }
        catch (Exception e){
            System.out.println("Exception in GameHUD.draw() " + e);
        }
    }
    //display house
    private void drawHouseCards(Graphics graphics,int xpos, int ypos){
        if (houses.size() == 0){
            graphics.drawString("House Cards: No house cards.",xpos,ypos);
        }
        else{
            String string; //TODO test that this obeys boundaries (it wont)
            int i = 0;
            for(HouseCard house:houses){
                string = house.convertDrawableString();
                graphics.drawString(string,xpos,ypos+i++*HOUSETEXTSKIP);
            }
        }
    }
    //display career
    private void drawCareerCard(Graphics graphics,int xpos, int ypos){
        if (career == null){
            graphics.drawString("Career Card: No career card.",xpos,ypos);
        }
        else{
            String string;
            string = career.convertDrawableString();
            graphics.drawString(string,xpos,ypos);
        }
    }
    private void drawNumLoans(Graphics graphics, int xpos, int ypos){
        graphics.drawString("Number of Loans: " + loans, xpos, ypos);
    }
    private void drawBankBal(Graphics graphics, int xpos, int ypos){
        graphics.drawString("Bank Balance: " + bankBalance, xpos, ypos);
    }
    private void drawDependants(Graphics graphics, int xpos, int ypos){
        graphics.drawString("Number of Dependants: " + familyString, xpos,ypos);
    }
}


