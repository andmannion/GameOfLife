package main.java.ie.ucd.engAC.UIScreens.UISubPanels;

import main.java.ie.ucd.engAC.LifeGame;
import main.java.ie.ucd.engAC.UIScreens.MainMenu;
import main.java.ie.ucd.engAC.UIScreens.buttons.*;

import javax.swing.*;
import java.awt.*;

public class MainMenuButtonPanel extends JPanel{

    private NewGameButton newGameButton;
    private QuitGameButton quitGameButton;
    private JButton playButton;
    private JComboBox jCombo;

    public MainMenuButtonPanel(MainMenu mainMenu,JFrame jFrame){
        super(new GridBagLayout());
        GridBagConstraints newGameButtonConstraints = new GridBagConstraints();
        newGameButtonConstraints.fill = GridBagConstraints.HORIZONTAL;
        newGameButtonConstraints.gridx = 1;
        newGameButtonConstraints.gridy = 0;
        //newGameButtonConstraints.anchor = GridBagConstraints.NORTH;
        newGameButton = new NewGameButton(this,mainMenu,newGameButtonConstraints);

        GridBagConstraints quitGameButtonConstraints = new GridBagConstraints();
        quitGameButtonConstraints.fill = GridBagConstraints.HORIZONTAL;
        quitGameButtonConstraints.gridx = 1;
        quitGameButtonConstraints.gridy = 2;
        //quitGameButtonConstraints.anchor = GridBagConstraints.SOUTH;
        quitGameButton = new QuitGameButton(this,mainMenu,quitGameButtonConstraints);

        GridBagConstraints playButtonConstraints = new GridBagConstraints();
        playButtonConstraints.gridx = 1;
        playButtonConstraints.gridy = 0;
        playButton = new PlayButton(this,mainMenu,playButtonConstraints);

        GridBagConstraints jComboConstraints = new GridBagConstraints();
        jComboConstraints.fill = GridBagConstraints.HORIZONTAL;
        jComboConstraints.gridx = 1;
        jComboConstraints.gridy = 2;
        jCombo = new NumPlayerChoice(this,mainMenu,jComboConstraints);

    }
    public void setVisibilityMainScreen(boolean bool){
        newGameButton.setVisible(bool);
        quitGameButton.setVisible(bool);
    }
    public void setVisibilityNumPlayers(boolean bool){
        jCombo.setVisible(bool);
        playButton.setVisible(bool);
    }

}
