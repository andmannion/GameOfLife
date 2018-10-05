package ie.ucd.engAC.UIScreens.buttons;

import ie.ucd.engAC.UIScreens.MainMenu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class NewGameButton extends JButton{

    public NewGameButton(JPanel jPanel, MainMenu mainMenu, GridBagConstraints gridBagConstraints){
        super("New Game");
        super.setAlignmentX(Component.CENTER_ALIGNMENT);
        super.addActionListener(mainMenu);
        jPanel.add(this,gridBagConstraints);
    }
}
