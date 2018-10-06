package main.java.ie.ucd.engAC;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

import main.java.ie.ucd.engAC.UIScreens.*;
import main.java.ie.ucd.engAC.UIScreens.SplashScreen;
import main.java.ie.ucd.engAC.LifeGameLogic.PlayerLogic.*;
import main.java.ie.ucd.engAC.LifeGameLogic.*;

public class LifeGame extends JFrame implements WindowListener{

    private int PANWIDTH = 1280;
    private int PANHEIGHT = 720;
    private JTextField gameTitle;
    private SplashScreen splashScreen; 
    private Dimension dimensions;
    private MainMenu mainMenu;
    private Container container;
    private ArrayList<Player> playerObjects;
    private Bank bank;
    private PlayPanel playPanel;


    public LifeGame() {
        super("Life: The Game");
        dimensions = new Dimension(PANWIDTH,PANHEIGHT);
        constructUI();
        addWindowListener(this);
        setLocationRelativeTo(null);
        pack();
        setSize(dimensions);
        setResizable(false);
        setVisible(true);
    } // end of Main (constructor)

    public void run(){

    } // end of run

    private void constructUI(){
        container = getContentPane();
        splashScreen = new SplashScreen();
        container.add(splashScreen);

        mainMenu = new MainMenu(this);

        container.removeAll();
        container.add(mainMenu);

    } // end of constructUI

    public void initialiseGame(int numPlayers){
        System.out.println("Initialising game.");
        playerObjects = new ArrayList<Player>();
        bank = new Bank();
        for(int i = 0;i<numPlayers;i++){
            Player player = new Player(i);
            playerObjects.add(player);
        }
        playPanel = new PlayPanel(this,playerObjects,bank); //TODO remove this if possible
        container.add(playPanel);
        mainMenu.setVisible(false);
        //
    }

    private void drawBoard(){}
    private void drawHUD(){}

    @Override
    public void windowActivated(WindowEvent window_event) {}
    @Override
    public void windowDeactivated(WindowEvent window_event) {}
    @Override
    public void windowDeiconified(WindowEvent window_event) {}
    @Override
    public void windowIconified(WindowEvent window_event) {}
    @Override
    public void windowClosing(WindowEvent window_event) {}
    @Override
    public void windowClosed(WindowEvent window_event) {}
    @Override
    public void windowOpened(WindowEvent window_event) {}

}
