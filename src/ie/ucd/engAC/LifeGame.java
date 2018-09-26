package ie.ucd.engAC;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LifeGame extends JFrame implements WindowListener{

    private JTextField game_title;

    public LifeGame() {
        super("Life: The Game");
        constructUI();
        addWindowListener(this);
        pack();
        setResizable(false);
        setVisible(true);
    } // end of Main (constructor)

    private void constructUI(){
        //Container container = getContentPane();
        JPanel main_menu = new JPanel();
        BoxLayout main_menu_manager = new BoxLayout(main_menu,BoxLayout.X_AXIS);
        main_menu.setLayout(main_menu_manager);

        game_title = new JTextField("Life: The Game");
        game_title.setEditable(false);
        main_menu.add(game_title);
    } // end of constructUI
    @Override
    public void windowActivated(WindowEvent window_event) {}
    @Override
    public void windowDeactivated(WindowEvent window_event) {}
    @Override
    public void windowDeiconified(WindowEvent window_event) {}
    @Override
    public void windowIconified(WindowEvent window_event) {}
    @Override
    public void windowClosing(WindowEvent window_event) {}
    @Override
    public void windowClosed(WindowEvent window_event) {}
    @Override
    public void windowOpened(WindowEvent window_event) {}

}
