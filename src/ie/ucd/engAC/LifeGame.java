package ie.ucd.engAC;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import ie.ucd.engAC.UIScreens.SplashScreen;

public class LifeGame extends JFrame implements WindowListener{

    private JTextField gameTitle;
    private SplashScreen splashScreen;

    public LifeGame() {
        super("Life: The Game");
        constructUI();
        addWindowListener(this);
        setLocationRelativeTo(null);
        pack();
        setResizable(false);
        setVisible(true);
    } // end of Main (constructor)

    private void constructUI(){
        Container container = getContentPane();
        splashScreen = new SplashScreen(this);
        container.add(splashScreen);
        JPanel mainMenu = new JPanel();
        BoxLayout mainMenuManager = new BoxLayout(mainMenu,BoxLayout.X_AXIS);
        mainMenu.setLayout(mainMenuManager);
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
