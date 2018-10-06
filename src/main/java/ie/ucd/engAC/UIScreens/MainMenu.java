package main.java.ie.ucd.engAC.UIScreens;

import main.java.ie.ucd.engAC.LifeGame;
import main.java.ie.ucd.engAC.UIScreens.UISubPanels.MainMenuButtonPanel;
import main.java.ie.ucd.engAC.UIScreens.buttons.NewGameButton;
import main.java.ie.ucd.engAC.UIScreens.buttons.PlayButton;
import main.java.ie.ucd.engAC.UIScreens.buttons.QuitGameButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class MainMenu extends JPanel implements ActionListener {

    private LifeGame lifeGameParent;
    private MainMenuButtonPanel buttonArea;
    private int numPlayers = 2;
    private static final int PANWIDTH = 640;
    private static final int PANHEIGHT = 480;

    public MainMenu(LifeGame lifeGame){
        super();
        lifeGameParent = lifeGame;
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
        buttonArea = new MainMenuButtonPanel(this,lifeGameParent);
        add(buttonArea,buttonAreaConstraints);

    }
    public void newGame(){
        buttonArea.setVisibilityMainScreen(false);
        buttonArea.setVisibilityNumPlayers(true);
     }


    @Override
    public void actionPerformed(ActionEvent e) {
        JComponent source = (JComponent) e.getSource(); //TODO this better
        if (source instanceof JButton){
            JButton buttonSource = (JButton)source;
            if (buttonSource.getText().equals("New Game")){//TODO make switch
                newGame();
            }
            else if(buttonSource.getText().equals("Quit")){
                lifeGameParent.dispose();
            }
            else if(buttonSource.getText().equals("Play")){
                lifeGameParent.initialiseGame(numPlayers);
            }
        }
        else if (e.getSource() instanceof JComboBox){
            JComboBox cb = (JComboBox)e.getSource();
            numPlayers = cb.getSelectedIndex()+2;
        }
        else
            System.out.println(e.getSource());
    }
}
