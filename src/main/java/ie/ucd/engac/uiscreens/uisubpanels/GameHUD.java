package ie.ucd.engac.uiscreens.uisubpanels;

import ie.ucd.engac.lifegamelogic.cards.CareerCards.CareerCard;
import ie.ucd.engac.lifegamelogic.cards.HouseCards.HouseCard;
import ie.ucd.engac.lifegamelogic.playerlogic.Player;
import ie.ucd.engac.uiscreens.GameEngine;

import java.awt.*;
import java.util.ArrayList;

public class GameHUD {
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

    private static final int BOXSTARTX = 0;
    private static final int BOXSTARTY = 504;
    private static final int BOXLENX = 1280;
    private static final int BOXLENY = 216;

    private static final int STR1X = 0;
    private static final int STR1Y = 520;
    private static final int STR1LX = 100;
    private static final int STR1LY = 30;

    private static final int HOUSETEXTSKIP = 15;

    public GameHUD(GameEngine gameEngine){
        this.gameEngine = gameEngine;
        rectangle = new Rectangle(BOXSTARTX,BOXSTARTY,BOXLENX,BOXLENY);
    }
    private void updateFields(){
        Player player = this.gameEngine.getCurrentPlayer();
        playerNumber = player.getPlayerNumber();
        martialStatus = player.getMaritalStatus().toInt();
        numDependants = player.getNumDependants();
        if (martialStatus == 1){
            familyString = martialStatus + "spouse and " + numDependants + " children";
        }
        else familyString = "No spouse and " + numDependants + " children";

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
            graphics.drawString("Player: " + playerNumber + 1 + " Colour: ", STR1X, STR1Y);
            drawBankBal(graphics, STR1X,STR1Y+1*STR1LY);
            drawNumLoans(graphics,STR1X,STR1Y+2*STR1LY);
            drawCareerCard(graphics,STR1X,STR1Y+3*STR1LY);
            drawHouseCards(graphics,STR1X, STR1Y+4*STR1LY);
            drawDependants(graphics, STR1X,STR1Y+5*STR1LY);
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


