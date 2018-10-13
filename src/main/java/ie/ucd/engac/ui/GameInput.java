package ie.ucd.engac.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import ie.ucd.engac.ui.*;

public class GameInput implements Drawable {
    //TODO scalable buttons
    private static final int SPIN_WIDTH = 192;
    private static final int SPIN_HEIGHT = 36;
    private static final int SPIN_BORDER = 13;

    private static final int QUIT_WIDTH = 128;
    private static final int QUIT_HEIGHT = 36;
    private static final int QUIT_BORDER = 13;

    private static final int CARD_CHOICE_WIDTH = 192;
    private static final int CARD_CHOICE_HEIGHT = 36;
    private static final int CARD_CHOICE_LHS_GAP = 326;
    private static final int CARD_CHOICE_INTER_GAP = 227;
    private static final int CARD_CHOICE_Y_POS = 504;

    private final int panelHeight;
    private final int panelWidth;

    private JButton spinButton;
    private JButton quitButton;
    private JButton chooseLeftCardButton;
    private JButton chooseRightCardButton;

    private ActionListener actionListener;

    private JPanel renderTarget;

    private GameUI gameUIParent;

    GameInput(GameUI gameUI,JPanel renderTarget){
        this.gameUIParent = gameUI;
        this.renderTarget = renderTarget;
        panelHeight = gameUI.getPanelHeight();
        panelWidth = gameUI.getPanelWidth();
        actionListener = gameUI.getGameActionListener();

        spinButton = new JButton("Spin The Wheel");
        int spinX = panelWidth-SPIN_WIDTH-SPIN_BORDER;
        int spinY = panelHeight-SPIN_HEIGHT-2*SPIN_BORDER;
        spinButton.setActionCommand("Spin The Wheel");
        spinButton.setBounds(spinX,spinY,SPIN_WIDTH,SPIN_HEIGHT);
        spinButton.setVisible(true);
        spinButton.addActionListener(actionListener);
        renderTarget.add(spinButton);

        quitButton = new JButton("Quit Game");
        int quitX = panelWidth-QUIT_WIDTH-QUIT_BORDER;
        int quitY = QUIT_BORDER;
        quitButton.setActionCommand("Quit Game");
        quitButton.setBounds(quitX,quitY,QUIT_WIDTH,QUIT_HEIGHT);
        quitButton.setVisible(true);
        quitButton.addActionListener(actionListener);
        renderTarget.add(quitButton);

        chooseLeftCardButton = new JButton("Choose Left Card");
        int cLeftX = CARD_CHOICE_LHS_GAP;
        int cLeftY = CARD_CHOICE_Y_POS;
        chooseLeftCardButton.setActionCommand("Choose Left Card");
        chooseLeftCardButton.setBounds(cLeftX,cLeftY,CARD_CHOICE_WIDTH,CARD_CHOICE_HEIGHT);
        chooseLeftCardButton.setVisible(true);
        chooseLeftCardButton.addActionListener(actionListener);
        renderTarget.add(chooseLeftCardButton);

        chooseRightCardButton = new JButton("Choose Right Card");
        int cRightX = CARD_CHOICE_LHS_GAP+CARD_CHOICE_WIDTH+CARD_CHOICE_INTER_GAP;
        int cRighY = CARD_CHOICE_Y_POS;
        chooseRightCardButton.setActionCommand("Choose Right Card");
        chooseRightCardButton.setBounds(cRightX,cRighY,CARD_CHOICE_WIDTH,CARD_CHOICE_HEIGHT);
        chooseRightCardButton.setVisible(true);
        chooseRightCardButton.addActionListener(actionListener);
        renderTarget.add(chooseRightCardButton);

    }
    @Override
    public void draw(Graphics graphics){
        switch(gameUIParent.getUIState()){
            case Init:
                break;
            case WaitingForSpin:
                break;
            case PostSpin:
                break;
            case CardChoice://in normal play screen
                quitButton.setVisible(true);
                spinButton.setVisible(false);
                chooseLeftCardButton.setVisible(true);
                chooseRightCardButton.setVisible(true);
                break;
            case Spin2WinPicking:
                break;
            case Spin2WinRolling:
                break;
            case BabyAcquisition:
                break;
            case Wedding:
                break;
        }
        /*
        if (true){ //card selection screen
            spinButton.setVisible(false);
            chooseLeftCardButton.setVisible(true);
            chooseRightCardButton.setVisible(true);
        }
        else if (true){ //some other screen

        }*/
        renderTarget.paintComponents(graphics);
    }
}
