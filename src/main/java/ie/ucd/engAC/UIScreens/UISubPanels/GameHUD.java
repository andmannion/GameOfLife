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

    public GameHUD(PlayPanel playPanel){
        this.playPanel = playPanel;
    }
    private void updateFields(){
        Player player = this.playPanel.getCurrentPlayer();
        martialStatus = player.getMaritalStatus().toString();
        numDependants = player.getNumDependants();
        career = player.getCareerCard();
        loans = player.getCurrentLoans();
        money = player.getCurrentMoney();

    }
    public void draw(Graphics graphics){
        updateFields();
        //display current player
        //display house
        //display career
        //display num loans
        //display $
        //display maritial
    }
}


