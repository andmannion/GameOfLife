package ie.ucd.engac.ui;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EventListener;

import ie.ucd.engac.messaging.Chooseable;
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

    private static final int JCOMBO_WIDTH = 128;
    private static final int JCOMBO_HEIGHT = 30;
    private static final int JCOMBO_LHS_GAP = 576;
    private static final int JCOMBO_Y_POS = 345;

    private final int panelHeight;
    private final int panelWidth;

    private JSpinner reducingChoice;

    private JButton spinButton;
    private JButton quitButton;
    private JButton submitChoice;
    private JButton chooseLeftCardButton;
    private JButton chooseRightCardButton;

    private ActionListener actionListener;
    private ChangeListener changeListener;

    private JPanel renderTarget;

    private GameUI gameUIParent;

    GameInput(GameUI gameUI,JPanel renderTarget){
        this.gameUIParent = gameUI;
        this.renderTarget = renderTarget;
        panelHeight = gameUI.getPanelHeight();
        panelWidth = gameUI.getPanelWidth();
        actionListener = gameUI.getGameActionListener();
        changeListener = gameUI.getGameActionListener();

        spinButton = new JButton("Spin The Wheel");
        int spinX = panelWidth-SPIN_WIDTH-SPIN_BORDER;
        int spinY = panelHeight-SPIN_HEIGHT-2*SPIN_BORDER;
        spinButton.setActionCommand("Spin The Wheel");
        spinButton.setBounds(spinX,spinY,SPIN_WIDTH,SPIN_HEIGHT);
        spinButton.setVisible(false);
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
        chooseLeftCardButton.setVisible(false);
        chooseLeftCardButton.addActionListener(actionListener);
        renderTarget.add(chooseLeftCardButton);

        chooseRightCardButton = new JButton("Choose Right Card");
        int cRightX = CARD_CHOICE_LHS_GAP+CARD_CHOICE_WIDTH+CARD_CHOICE_INTER_GAP;
        int cRighY = CARD_CHOICE_Y_POS;
        chooseRightCardButton.setActionCommand("Choose Right Card");
        chooseRightCardButton.setBounds(cRightX,cRighY,CARD_CHOICE_WIDTH,CARD_CHOICE_HEIGHT);
        chooseRightCardButton.setVisible(false);
        chooseRightCardButton.addActionListener(actionListener);
        renderTarget.add(chooseRightCardButton);

        String[] placeholder = { "Placeholder","222" };
        SpinnerModel model = new SpinnerListModel(Arrays.asList(placeholder));
        reducingChoice = new JSpinner();
        reducingChoice.setModel(model);
        reducingChoice.addChangeListener(changeListener);
        reducingChoice.setVisible(true);
        ((JSpinner.DefaultEditor) reducingChoice.getEditor()).getTextField().setEditable(false);
        reducingChoice.setBounds(JCOMBO_LHS_GAP,JCOMBO_Y_POS,JCOMBO_WIDTH,JCOMBO_HEIGHT);
        renderTarget.add(reducingChoice);

        submitChoice = new JButton("Submit Choice");
        submitChoice.setActionCommand("Submit Choice");
        submitChoice.setBounds(JCOMBO_LHS_GAP,JCOMBO_Y_POS+50,JCOMBO_WIDTH,JCOMBO_HEIGHT);
        submitChoice.setVisible(true);
        submitChoice.addActionListener(actionListener);
        renderTarget.add(submitChoice);

    }

    void setSpinnerOptions(ArrayList<Chooseable> choices){
        ArrayList<String> temp = new ArrayList<>();
        for(Chooseable choice:choices) {
            temp.add(choice.displayChoiceDetails());
        }
        SpinnerModel model = new SpinnerListModel(temp);
        reducingChoice.setModel(model);
    }


    void setEnableSubmitButton(boolean bool){
        spinButton.setEnabled(bool);
    }
    void setEnableSpinButton(boolean bool){
        spinButton.setEnabled(bool);
    }
    void setVisibleSpinButton(boolean bool){
        spinButton.setEnabled(bool);
    }
    void setEnableCardChoice(boolean bool){
        chooseLeftCardButton.setEnabled(bool);
        chooseRightCardButton.setEnabled(bool);
    }
    void setVisibleCardChoice(boolean bool){
        chooseLeftCardButton.setVisible(bool);
        chooseRightCardButton.setVisible(bool);
    }

    @Override
    public void draw(Graphics graphics){
        switch(gameUIParent.getUIState()){
            case Init:
                break;
            case WaitingForSpin:
                setVisibleSpinButton(true);
                setEnableSpinButton(true);

                setVisibleCardChoice(false);
                break;
            case PostSpin:
                break;
            case CardChoice://in normal play screen
                setVisibleSpinButton(false);

                setVisibleCardChoice(true);
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

        renderTarget.paintComponents(graphics);
    }
}
