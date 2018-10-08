package ie.ucd.engac;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import ie.ucd.engac.uiscreens.*;

public class LifeGame implements WindowListener{

    private int PANWIDTH = 1280;
    private int PANHEIGHT = 720;
    private JFrame jFrame;
    private JTextField gameTitle;
    private Dimension dimensions;
    private MainMenu mainMenu;
    private Container container;
    private PlayPanel playPanel;


    LifeGame() {
        jFrame = new JFrame("Life: The Game");
        dimensions = new Dimension(PANWIDTH,PANHEIGHT);
        constructUI();
        jFrame.addWindowListener(this);
        //setLocationRelativeTo(null);
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
        playPanel = new PlayPanel(this,numPlayers); //TODO remove "this" if possible
        container.add(playPanel);
        mainMenu.setVisible(false);
        playPanel.setVisible(true);
        playPanel.beginGame();
    } //end of intialiseGame

    public void dispose(){
        jFrame.dispose();
        System.exit(0);
        //TODO quit the lifegame function;
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
