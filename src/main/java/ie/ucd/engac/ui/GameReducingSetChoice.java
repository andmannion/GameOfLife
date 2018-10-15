package ie.ucd.engac.ui;

import ie.ucd.engac.messaging.Chooseable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class GameReducingSetChoice implements Drawable {

    private static final int CARD_CHOICE_WIDTH = 192;
    private static final int CARD_CHOICE_HEIGHT = 36;
    private static final int CARD_CHOICE_LHS_GAP = 326;
    private static final int CARD_CHOICE_INTER_GAP = 227;
    private static final int CARD_CHOICE_Y_POS = 504;
    private static final int MAX_NUM_CHOICES = 10;

    private final int panelHeight;
    private final int panelWidth;

    private ArrayList<JButton> buttons;

    private JPanel renderTarget;

    private GameUI gameUIParent;

    private ActionListener actionListener;


    GameReducingSetChoice(GameUI gameUI,JPanel renderTarget){
        this.gameUIParent = gameUI;
        this.renderTarget = renderTarget;
        panelHeight = gameUI.getPanelHeight();
        panelWidth = gameUI.getPanelWidth();
        actionListener = gameUI.getGameActionListener();
        buttons = new ArrayList<>();
        //TODO without for?
        for (int i=0;i<MAX_NUM_CHOICES;i++){
            JButton element = new JButton("");
            element.setVisible(false);
            element.setEnabled(false);
            element.setActionCommand("item_"+ i);
            element.setText("Choose item " + i + ".");
            buttons.add(i,element);
        }
    }

    void setChooseableChoices(ArrayList<Chooseable> choices){
        for (int i=0;i<choices.size();i++){
            JButton button = buttons.get(i);
            button.setText("Choose: " + choices.get(i).toString());
            button.setEnabled(true);
            button.setVisible(true);
            //TODO set position dynamically

        }

    }

    //create a button per object equally spaced on screen
    //create corresponding text string to demonstrate mapping
    @Override
    public void draw(Graphics graphics){}

}
