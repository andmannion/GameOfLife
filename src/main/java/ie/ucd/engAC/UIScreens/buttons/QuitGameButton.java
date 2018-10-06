package main.java.ie.ucd.engAC.UIScreens.buttons;

import main.java.ie.ucd.engAC.UIScreens.MainMenu;
import main.java.ie.ucd.engAC.UIScreens.UISubPanels.MainMenuButtonPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class QuitGameButton extends JButton{

    public QuitGameButton(MainMenuButtonPanel mainMenuButtonPanel, MainMenu mainMenu, GridBagConstraints gridBagConstraints){
        super("Quit");
        super.setAlignmentX(Component.CENTER_ALIGNMENT);
        super.addActionListener(mainMenu);
        mainMenu.add(this,gridBagConstraints);
    }
}
