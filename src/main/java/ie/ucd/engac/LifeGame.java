package ie.ucd.engac;

import ie.ucd.engac.ui.MainMenu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.InputStream;
import java.util.Properties;

public class LifeGame implements WindowListener{

    private int panelWidth = 1280;
    private int panelHeight = 720;
    private JFrame jFrame;
    private JPanel playPanel;
    private MainMenu mainMenu;
    private Container container;
    private GameEngine gameEngine;

    /**
     * LifeGame class constructor.
     */
    LifeGame() {
        jFrame = new JFrame("Life: The Game");
        InputStream inputStream;
        Properties properties = new Properties();
        try{
            inputStream = LifeGame.class.getClassLoader().getResourceAsStream("config.properties");//new FileInputStream("src/main/resources/config.properties");

            properties.load(inputStream);
            panelWidth = Integer.parseInt(properties.getProperty("PANWIDTH"));
            panelHeight = Integer.parseInt(properties.getProperty("PANHEIGHT"));
        }
        catch(Exception exception){
            System.err.println("config.properties not found.");
        }
        new GameConfig(properties);
        Dimension dimensions = new Dimension(panelWidth,panelHeight);
        constructUI();
        jFrame.addWindowListener(this);
        jFrame.pack();
        jFrame.setSize(dimensions);
        jFrame.setResizable(false);
        jFrame.setVisible(true);
        jFrame.setIgnoreRepaint(true);


    } // end of Main (constructor)

    /**
     * Constructs the main menu panel and adds it to the main JFrame.
     */
    private void constructUI(){
        container = jFrame.getContentPane();

        mainMenu = new MainMenu(this);

        container.removeAll();
        container.add(mainMenu);

    } // end of constructUI

    /**
     * Initialises the game by creating the a panel to act as the rendering
     * target, and then creates an instance of GameEngine to run the game.
     * @param numPlayers    Number of players.
     */
    public void initialiseGame(int numPlayers){
        playPanel = new JPanel();
        playPanel.setVisible(false);
        playPanel.setBackground(Color.white);
        playPanel.setPreferredSize( new Dimension(panelWidth, panelHeight));
        playPanel.setLayout(null);
        JTextField textField = new JTextField("Error: Rendering error");
        playPanel.add(textField);
        container.add(playPanel);

        gameEngine = new GameEngine(this,playPanel,numPlayers);
        mainMenu.setVisible(false);
        playPanel.setVisible(true);
        gameEngine.beginGame();
    } //end of initialiseGame

    /**
     * Toggles the current view to the play/quit screen.
     */
    void returnToMainMenu(){
        playPanel.setVisible(false);
        mainMenu.returnToMainMenu();
        mainMenu.setVisible(true);
    }

    /**
     * Calls the garbage collector on the JFrame and then exits.
     */
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

    /**
     * On window close terminates the rendering thread before exiting.
     * @param window_event window_event on closing
     */
    @Override
    public void windowClosing(WindowEvent window_event) {
        try {
            gameEngine.quitGame();
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
