package main.java.ie.ucd.engAC.UIScreens.UISubPanels;

import ie.ucd.engAC.LifeGameLogic.PlayerLogic.Player;
import ie.ucd.engAC.UIScreens.PlayPanel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GameHUD{
    private ArrayList fields;
    private PlayPanel playPanel;
    private String martialStatus;
    private int numDependants;
    private String career; //TODO make this a CareerCard
    private int loans;
    private int money;
    private Rectangle rectangle;

    private int BOXSTARTX = 0;
    private int BOXSTARTY = 504;
    private int BOXLENX = 1280;
    private int BOXLENY = 216;

    public GameHUD(PlayPanel playPanel){
        this.playPanel = playPanel;
        rectangle = new Rectangle(BOXSTARTX,BOXSTARTY,BOXLENX,BOXLENY);
    }
    private void updateFields(){
        Player player = this.playPanel.getCurrentPlayer();
        martialStatus = player.getMaritalStatus().toString();
        numDependants = player.getNumDependants();
        career = player.getCareerCard();
        loans = player.getCurrentLoans();
        money = player.getCurrentMoney();
    }
    synchronized public void draw(Graphics graphics){ //synch adds safety
        updateFields();
        try{
            graphics.setColor(Color.darkGray);
            graphics.fillRect(rectangle.x,rectangle.y,rectangle.width,rectangle.height);
        }
        catch (Exception e){
            System.out.println("Exception in GameHUD.draw() " + e);
        }
        //display current player
        //display house
        //display career
        //display num loans
        //display $
        //display maritial
    }
}


