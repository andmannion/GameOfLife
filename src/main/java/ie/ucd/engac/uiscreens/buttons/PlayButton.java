package ie.ucd.engac.uiscreens.buttons;

import ie.ucd.engac.uiscreens.MainMenu;

import javax.swing.*;
import java.awt.*;

public class PlayButton extends JButton {
    public PlayButton(JPanel jPanel, MainMenu mainMenu, GridBagConstraints gridBagConstraints){
        super("Play");
        setVisible(false);
        super.addActionListener(mainMenu);
        jPanel.add(this,gridBagConstraints);
    }
}
