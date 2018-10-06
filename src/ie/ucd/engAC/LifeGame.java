package ie.ucd.engAC;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

import ie.ucd.engAC.UIScreens.*;
import ie.ucd.engAC.UIScreens.SplashScreen;
import ie.ucd.engAC.LifeGameLogic.PlayerLogic.*;
import ie.ucd.engAC.LifeGameLogic.*;

public class LifeGame extends JFrame implements WindowListener{

    private int PANWIDTH = 1280;
    private int PANHEIGHT = 720;
    private JTextField gameTitle;
    private SplashScreen splashScreen;
    private Dimension dimensions;
    private MainMenu mainMenu;
    private Container container;
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

        playPanel = new PlayPanel(this,numPlayers); //TODO remove this if possible
        container.add(playPanel);
        mainMenu.setVisible(false);
    } //end of intialiseGame

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
