package ie.ucd.engac.uiscreens.uisubpanels;

import ie.ucd.engac.lifegamelogic.cards.CareerCards.CareerCard;
import ie.ucd.engac.lifegamelogic.cards.HouseCards.HouseCard;
import ie.ucd.engac.lifegamelogic.playerlogic.Player;
import ie.ucd.engac.uiscreens.PlayPanel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GameHUD {
    //TODO check that all printout requirements are met
    private ArrayList fields;
    private PlayPanel playPanel;
    private int playerNumber;
    private String martialStatus;
    private int numDependants;
    private CareerCard career;
    private ArrayList<HouseCard> houses;
    private int loans;
    private int money;
    private int actionCards;
    private Rectangle rectangle;
    private ArrayList<JTextArea> textAreas;

    private static final int BOXSTARTX = 0;
    private static final int BOXSTARTY = 504;
    private static final int BOXLENX = 1280;
    private static final int BOXLENY = 216;

    private static final int STR1X = 0;
    private static final int STR1Y = 520;
    private static final int STR1LX = 100;
    private static final int STR1LY = 30;

    private static final int HOUSETEXTSKIP = 15;

    public GameHUD(PlayPanel playPanel){
        this.playPanel = playPanel;
        rectangle = new Rectangle(BOXSTARTX,BOXSTARTY,BOXLENX,BOXLENY);
    }
    private void updateFields(){
        Player player = this.playPanel.getCurrentPlayer();
        playerNumber = player.getPlayerNumber();
        martialStatus = player.getMaritalStatus().toString();
        numDependants = player.getNumDependants();
        career = player.getCareerCard();
        houses = player.getHouseCards();
        loans = player.getNumLoans();
        money = player.getCurrentMoney();
        actionCards = player.getActionCards().size();
        textAreas = new ArrayList<>();
        for(int i=0;i<6;i++){
            textAreas.add(new JTextArea("test"));
        }
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
            graphics.drawString(formatBankBal(),    STR1X,STR1Y+1*STR1LY);
            drawNumLoans(graphics,STR1X,STR1Y+2*STR1LY);
            drawCareerCard(graphics,STR1X,STR1Y+3*STR1LY);
            drawHouseCards(graphics,STR1X, STR1Y+4*STR1LY);
            graphics.drawString(formatDependants(), STR1X,STR1Y+5*STR1LY);
        }
        catch (Exception e){
            System.out.println("Exception in GameHUD.draw() " + e);
        }

        //display career
        //display num loans
        //display $
        //display maritial
    }
    //display house
    private void drawHouseCards(Graphics graphics,int xpos, int ypos){
        if (houses.size() == 0){
            graphics.drawString("House Cards: No house cards.",xpos,ypos);
        }
        else{
            String string;
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
    private String formatBankBal(){
        return "TODO";
    }
    private String formatDependants(){
        return "TODO";
    }
}


