package ie.ucd.engAC.UIScreens;

import ie.ucd.engAC.LifeGame;
import ie.ucd.engAC.UIScreens.buttons.NewGameButton;
import ie.ucd.engAC.UIScreens.buttons.QuitGameButton;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MainMenu extends JPanel {

    private static final int PANWIDTH = 640;
    private static final int PANHEIGHT = 480;

    private BufferedImage splashImage;

    private LifeGame lifeGameParent;
    private NewGameButton newGameButton;
    private QuitGameButton quitGameButton;
    private Dimension sizePreferences;

    public MainMenu(LifeGame lifeGame){
        super();
        lifeGameParent = lifeGame;

        setBackground(Color.gray);
        sizePreferences = new Dimension(PANWIDTH,PANHEIGHT);
        setPreferredSize(sizePreferences);
        setFocusable(true);
        requestFocus();

        BoxLayout mainMenuManager = new BoxLayout(this,BoxLayout.Y_AXIS);
        setLayout(mainMenuManager);

        newGameButton = new NewGameButton(this);

        quitGameButton = new QuitGameButton(this,lifeGameParent);

    }
}
