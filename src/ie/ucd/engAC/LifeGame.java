package ie.ucd.engAC;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import ie.ucd.engAC.UIScreens.MainMenu;
import ie.ucd.engAC.UIScreens.buttons.NewGameButton;
import ie.ucd.engAC.UIScreens.SplashScreen;
import ie.ucd.engAC.UIScreens.buttons.QuitGameButton;

public class LifeGame extends JFrame implements WindowListener{

    private int PANWIDTH = 640;
    private int PANHEIGHT = 480;
    private JTextField gameTitle;
    private SplashScreen splashScreen;
    private Dimension dimensions;
    private MainMenu mainMenu;

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
        Container container = getContentPane();
        splashScreen = new SplashScreen(this);
        container.add(splashScreen);

        mainMenu = new MainMenu(this);
        
        container.removeAll();
        container.add(mainMenu);
    } // end of constructUI

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
