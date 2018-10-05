package ie.ucd.engAC.UIScreens.buttons;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class QuitGameButton extends JButton implements ActionListener {

    private JFrame parentFrame;
    private int numClicks;

    public QuitGameButton(JPanel jPanel,JFrame jFrame,GridBagConstraints gridBagConstraints){
        super("Quit");
        parentFrame = jFrame;
        super.setAlignmentX(Component.CENTER_ALIGNMENT);
        super.addActionListener(this);
        jPanel.add(this,gridBagConstraints);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        parentFrame.dispose();
    }

}
