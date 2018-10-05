package ie.ucd.engAC.UIScreens.buttons;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class NewGameButton extends JButton implements ActionListener {

    private int numClicks;

    public NewGameButton(JPanel jPanel,GridBagConstraints gridBagConstraints){
        super("New Game");
        super.setAlignmentX(Component.CENTER_ALIGNMENT);
        super.addActionListener(this);
        jPanel.add(this,gridBagConstraints);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        numClicks++;
        System.out.println(numClicks);
    }

}
