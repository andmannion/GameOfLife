package ie.ucd.engac.uiscreens.buttons;

import ie.ucd.engac.uiscreens.MainMenu;
import ie.ucd.engac.uiscreens.uisubpanels.MainMenuButtonPanel;

import javax.swing.*;
import java.awt.*;

public class QuitGameButton extends JButton{

    public QuitGameButton(MainMenuButtonPanel mainMenuButtonPanel, MainMenu mainMenu, GridBagConstraints gridBagConstraints){
        super("Quit");
        super.setAlignmentX(Component.CENTER_ALIGNMENT);
        super.addActionListener(mainMenu);
        mainMenu.add(this,gridBagConstraints);
    }
}
