package ie.ucd.engAC.UIScreens;

import ie.ucd.engAC.LifeGame;
import ie.ucd.engAC.UIScreens.UISubPanels.MainMenuButtonPanel;
import ie.ucd.engAC.UIScreens.buttons.NewGameButton;
import ie.ucd.engAC.UIScreens.buttons.QuitGameButton;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class MainMenu extends JPanel {

    private static final int PANWIDTH = 640;
    private static final int PANHEIGHT = 480;

    public MainMenu(LifeGame lifeGame){
        super();
        setBackground(Color.gray);
        Dimension sizePreferences = new Dimension(PANWIDTH,PANHEIGHT);
        setPreferredSize(sizePreferences);
        setFocusable(true);
        requestFocus();

        GridBagLayout mainMenuManager = new GridBagLayout();
        setLayout(mainMenuManager);
        GridBagConstraints buttonAreaConstraints = new GridBagConstraints();
        buttonAreaConstraints.gridx = 1;
        buttonAreaConstraints.gridy = 1;
        JPanel buttonArea = new MainMenuButtonPanel(lifeGame);
        add(buttonArea,buttonAreaConstraints);

    }
}
