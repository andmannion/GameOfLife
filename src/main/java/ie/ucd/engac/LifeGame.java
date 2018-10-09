package ie.ucd.engac;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import ie.ucd.engac.uiscreens.*;

public class LifeGame implements WindowListener{

    private static final int PANWIDTH = 1280;
    private static final int PANHEIGHT = 720;
    private JFrame jFrame;
    private MainMenu mainMenu;
    private Container container;
    private GameEngine gameEngine;


    LifeGame() {
        jFrame = new JFrame("Life: The Game");
        Dimension dimensions = new Dimension(PANWIDTH,PANHEIGHT);
        constructUI();
        jFrame.addWindowListener(this);
        jFrame.pack();
        jFrame.setSize(dimensions);
        jFrame.setResizable(false);
        jFrame.setVisible(true);
        jFrame.setIgnoreRepaint(true);
    } // end of Main (constructor)

    private void constructUI(){
        container = jFrame.getContentPane();

        mainMenu = new MainMenu(this);

        container.removeAll();
        container.add(mainMenu);

    } // end of constructUI

    public void initialiseGame(int numPlayers){
        System.out.println("Initialising game.");
        gameEngine = new GameEngine(this,numPlayers);
        container.add(gameEngine.getJPanel());
        mainMenu.setVisible(false);
        gameEngine.setVisible(true);
        gameEngine.beginGame();
    } //end of initialiseGame

    public void dispose(){
        jFrame.dispose();
        System.exit(0);
    }

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
            gameEngine.closeGame();
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
