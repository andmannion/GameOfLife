package ie.ucd.engac.uiscreens.buttons;

import ie.ucd.engac.uiscreens.MainMenu;

import javax.swing.*;
import java.awt.*;

public class NewGameButton extends JButton{

    public NewGameButton(JPanel jPanel, MainMenu mainMenu, GridBagConstraints gridBagConstraints){
        super("New Game");
        super.setAlignmentX(Component.CENTER_ALIGNMENT);
        super.addActionListener(mainMenu);
        jPanel.add(this,gridBagConstraints);
    }
}
