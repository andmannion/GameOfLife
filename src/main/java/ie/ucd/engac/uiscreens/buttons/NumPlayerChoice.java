package ie.ucd.engac.uiscreens.buttons;

import ie.ucd.engac.uiscreens.MainMenu;

import javax.swing.*;
import java.awt.*;

public class NumPlayerChoice extends JComboBox {
    private String[] numPlayersList = { "2", "3", "4"};

    public NumPlayerChoice(JPanel jPanel, MainMenu mainMenu, GridBagConstraints gridBagConstraints){
        super();
        for(String num:numPlayersList){
            super.addItem(num);
        }
        super.setAlignmentX(Component.CENTER_ALIGNMENT);
        super.addActionListener(mainMenu);
        super.setSelectedIndex(0);
        super.addActionListener(mainMenu);
        setVisible(false);
        jPanel.add(this,gridBagConstraints);
    }
}
