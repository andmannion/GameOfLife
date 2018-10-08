package ie.ucd.engac.uiscreens.uisubpanels;

import ie.ucd.engac.lifegamelogic.PlayerLogic.Player;
import ie.ucd.engac.uiscreens.PlayPanel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GameHUD {
    private ArrayList fields;
    private PlayPanel playPanel;
    private int playerNumber;
    private String martialStatus;
    private int numDependants;
    private String career; //TODO make this a CareerCard
    private int loans;
    private int money;
    private Rectangle rectangle;
    private ArrayList<JTextArea> textAreas;

    private final int BOXSTARTX = 0;
    private final int BOXSTARTY = 504;
    private final int BOXLENX = 1280;
    private final int BOXLENY = 216;

    private final int STR1X = 0;
    private final int STR1Y = 520;
    private final int STR1LX = 100;
    private final int STR1LY = 30;

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
        loans = player.getCurrentLoans();
        money = player.getCurrentMoney();
        textAreas = new ArrayList<>();
        for(int i=0;i<6;i++){
            textAreas.add(new JTextArea("test"));
        }
    }
    synchronized public void draw(Graphics graphics){ //synch adds safety
        updateFields();
        try{
            graphics.setColor(Color.darkGray);
            graphics.fillRect(rectangle.x,rectangle.y,rectangle.width,rectangle.height);
            graphics.setColor(Color.black);
            graphics.drawString("Player: " + playerNumber, STR1X, STR1Y);
            graphics.drawString(formatBankBal(),    STR1X,STR1Y+1*STR1LY);
            graphics.drawString(formatNumLoans(),   STR1X,STR1Y+2*STR1LY);
            graphics.drawString(formatCareerCard(), STR1X,STR1Y+3*STR1LY);
            graphics.drawString(formatHouseCard(),  STR1X,STR1Y+4*STR1LY);
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
    private String formatHouseCard(){
        return "TODO";
    }
    private String formatCareerCard(){
        return "TODO";
    }
    private String formatNumLoans(){
        return "TODO";
    }
    private String formatBankBal(){
        return "TODO";
    }
    private String formatDependants(){
        return "TODO";
    }
}


