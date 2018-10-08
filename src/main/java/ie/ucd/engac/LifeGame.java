package ie.ucd.engac;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import ie.ucd.engac.uiscreens.*;
import ie.ucd.engac.uiscreens.SplashScreen;

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
        //setLocationRelativeTo(null);
        pack();
        setSize(dimensions);
        setResizable(false);
        setVisible(true);
        setIgnoreRepaint(true);
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
        playPanel = new PlayPanel(this,numPlayers); //TODO remove "this" if possible
        container.add(playPanel);
        mainMenu.setVisible(false);
        playPanel.setVisible(true);
        playPanel.beginGame();
    } //end of intialiseGame


    @Override
    public void windowActivated(WindowEvent window_event) {}
    @Override
    public void windowDeactivated(WindowEvent window_event) {}
    @Override
    public void windowDeiconified(WindowEvent window_event) {}
    @Override
    public void windowIconified(WindowEvent window_event) {}
    @Override
    public void windowClosing(WindowEvent window_event) {
        try {
            playPanel.closeGame();
        }
        catch (Exception e){
            //Is it even worth returning this?
        }
        dispose();
    }
    @Override
    public void windowClosed(WindowEvent window_event) {}
    @Override
    public void windowOpened(WindowEvent window_event) {}

}
