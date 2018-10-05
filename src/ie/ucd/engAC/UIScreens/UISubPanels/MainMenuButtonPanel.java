package ie.ucd.engAC.UIScreens.UISubPanels;

import ie.ucd.engAC.LifeGame;
import ie.ucd.engAC.UIScreens.buttons.NewGameButton;
import ie.ucd.engAC.UIScreens.buttons.QuitGameButton;

import javax.swing.*;
import java.awt.*;

public class MainMenuButtonPanel extends JPanel{
    public MainMenuButtonPanel(JFrame jFrame){
        super(new GridBagLayout());
        GridBagConstraints newGameButtonConstraints = new GridBagConstraints();
        newGameButtonConstraints.fill = GridBagConstraints.HORIZONTAL;
        newGameButtonConstraints.gridx = 1;
        newGameButtonConstraints.gridy = 0;
        //newGameButtonConstraints.anchor = GridBagConstraints.NORTH;
        NewGameButton newGameButton = new NewGameButton(this,newGameButtonConstraints);

        GridBagConstraints quitGameButtonConstraints = new GridBagConstraints();
        quitGameButtonConstraints.fill = GridBagConstraints.HORIZONTAL;
        quitGameButtonConstraints.gridx = 1;
        quitGameButtonConstraints.gridy = 2;
        //quitGameButtonConstraints.anchor = GridBagConstraints.SOUTH;
        QuitGameButton quitGameButton = new QuitGameButton(this,jFrame,quitGameButtonConstraints);

    }
}
