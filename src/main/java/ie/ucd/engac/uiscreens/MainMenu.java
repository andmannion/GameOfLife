package ie.ucd.engac.uiscreens;

import ie.ucd.engac.LifeGame;
import ie.ucd.engac.uiscreens.uisubpanels.MainMenuButtonPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class MainMenu extends JPanel implements ActionListener {

    private static final int PANWIDTH = 640;
    private static final int PANHEIGHT = 480;

    private LifeGame lifeGameParent;
    private MainMenuButtonPanel buttonArea;
    
    private int numPlayers = 2;

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
        buttonArea = new MainMenuButtonPanel(this);
        add(buttonArea,buttonAreaConstraints);

    }
    private void newGame(){
        buttonArea.setVisibilityMainScreen(false);
        buttonArea.setVisibilityNumPlayers(true);
     }

    @Override
    public void actionPerformed(ActionEvent e) {
        JComponent source = (JComponent) e.getSource(); //TODO this better
        if (source instanceof JButton){
            JButton buttonSource = (JButton)source;
            switch (buttonSource.getText()){
                case "New Game": newGame(); break;
                case "Quit":     lifeGameParent.dispose(); break;
                case "Play":     lifeGameParent.initialiseGame(numPlayers);
            }
        }
        else if (e.getSource() instanceof JComboBox){
            JComboBox comboBox = (JComboBox)e.getSource();
            numPlayers = comboBox.getSelectedIndex()+2;
        }
        else
            System.out.println(e.getSource());
    }
}
