package ie.ucd.engAC;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

import ie.ucd.engAC.UIScreens.MainMenu;
import ie.ucd.engAC.UIScreens.SplashScreen;
import ie.ucd.engAC.LifeGameLogic.PlayerLogic.*;
import ie.ucd.engAC.LifeGameLogic.*;

public class LifeGame extends JFrame implements WindowListener{

    private int PANWIDTH = 640;
    private int PANHEIGHT = 480;
    private JTextField gameTitle;
    private SplashScreen splashScreen;
    private Dimension dimensions;
    private MainMenu mainMenu;
    private Container container;
    private List<Player> PlayerObjects;
    private Bank bank;


    public LifeGame() {
        super("Life: The Game");
        constructUI();
        addWindowListener(this);
        setLocationRelativeTo(null);
        pack();
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
        PlayerObjects = new ArrayList<Player>();
        bank = new Bank();
        for(int i = 0;i<numPlayers;i++){
            Player player = new Player(i);
            PlayerObjects.add(player);
        }
        //...
        //make list & call constructors for players
        //initialise "bank" etc
        //remove menu and call run function
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
