package ie.ucd.engAC.UIScreens.UISubPanels;

import ie.ucd.engAC.LifeGame;
import ie.ucd.engAC.UIScreens.MainMenu;
import ie.ucd.engAC.UIScreens.buttons.*;
import javax.swing.*;
import java.awt.*;

public class MainMenuButtonPanel extends JPanel{

    private JButton newGameButton;
    private JButton quitGameButton;
    private JButton playButton;
    private JComboBox jCombo;

    public MainMenuButtonPanel(MainMenu mainMenu,JFrame jFrame){
        super(new GridBagLayout());
        GridBagConstraints newGameButtonConstraints = new GridBagConstraints();
        newGameButtonConstraints.fill = GridBagConstraints.HORIZONTAL;
        newGameButtonConstraints.gridx = 1;
        newGameButtonConstraints.gridy = 0;
        //newGameButton = new NewGameButton(this,mainMenu,newGameButtonConstraints);
        newGameButton = new JButton("New Game");
        newGameButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        newGameButton.addActionListener(mainMenu);
        add(newGameButton,newGameButtonConstraints);

        GridBagConstraints quitGameButtonConstraints = new GridBagConstraints();
        quitGameButtonConstraints.fill = GridBagConstraints.HORIZONTAL;
        quitGameButtonConstraints.gridx = 1;
        quitGameButtonConstraints.gridy = 2;
        //quitGameButtonConstraints.anchor = GridBagConstraints.SOUTH;
        quitGameButton = new JButton("Quit");
        quitGameButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        quitGameButton.addActionListener(mainMenu);
        add(quitGameButton,quitGameButtonConstraints);


        GridBagConstraints playButtonConstraints = new GridBagConstraints();
        playButtonConstraints.gridx = 1;
        playButtonConstraints.gridy = 0;
        playButton = new JButton("Play");
        playButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        playButton.setVisible(false);
        playButton.addActionListener(mainMenu);
        add(playButton,playButtonConstraints);

        GridBagConstraints jComboConstraints = new GridBagConstraints();
        jComboConstraints.fill = GridBagConstraints.HORIZONTAL;
        jComboConstraints.gridx = 1;
        jComboConstraints.gridy = 2;
        String[] numPlayersList = { "2", "3", "4"};
        jCombo = new JComboBox(numPlayersList);
        jCombo.setAlignmentX(Component.CENTER_ALIGNMENT);
        jCombo.addActionListener(mainMenu);
        jCombo.setSelectedIndex(0);
        jCombo.addActionListener(mainMenu);
        jCombo.setVisible(false);
        add(jCombo,jComboConstraints);

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
