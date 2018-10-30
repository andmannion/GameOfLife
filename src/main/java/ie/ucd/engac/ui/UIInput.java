package ie.ucd.engac.ui;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;

import ie.ucd.engac.messaging.Chooseable;

public class UIInput implements Drawable {

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

    private static final int JCOMBO_WIDTH = 164;
    private static final int JCOMBO_HEIGHT = 30;
    private static final int JCOMBO_LHS_GAP = 568;
    private static final int JCOMBO_Y_POS = 345;

    private final int panelHeight;
    private final int panelWidth;

    private JSpinner reducingChoice;
    private ArrayList<String> spinnerValues;
    private int spinnerIndex;

    private JButton spinButton;
    private JButton quitButton;
    private JButton submitChoice;
    private JButton endTurnButton;
    private JButton chooseLeftCardButton;
    private JButton chooseRightCardButton;

    private ActionListener actionListener;

    private JPanel renderTarget;

    private GameUI gameUIParent;

    private JButton createButton(String string,int xPos, int yPos, int width, int height, ActionListener actionListener){
        JButton button = new JButton(string);
        button.setActionCommand(string);
        button.setBounds(xPos,yPos,width,height);
        button.setVisible(false);
        button.addActionListener(actionListener);
        return button;
    }

    UIInput(GameUI gameUI, JPanel renderTarget){
        this.gameUIParent = gameUI;
        this.renderTarget = renderTarget;
        panelHeight = gameUI.getPanelHeight();
        panelWidth = gameUI.getPanelWidth();
        actionListener = gameUI.getUiActionListener();

        endTurnButton = createButton("End Turn",JCOMBO_LHS_GAP, JCOMBO_Y_POS - 50,JCOMBO_WIDTH, JCOMBO_HEIGHT, actionListener);
        renderTarget.add(endTurnButton);

        int spinX = panelWidth-SPIN_WIDTH-SPIN_BORDER;
        int spinY = panelHeight-SPIN_HEIGHT-2*SPIN_BORDER;
        spinButton = createButton("Spin The Wheel",spinX, spinY,SPIN_WIDTH, SPIN_HEIGHT, actionListener);
        renderTarget.add(spinButton);

        submitChoice = createButton("Submit Choice",JCOMBO_LHS_GAP, JCOMBO_Y_POS+50,JCOMBO_WIDTH, JCOMBO_HEIGHT, actionListener);
        renderTarget.add(submitChoice);

        int quitX = panelWidth-QUIT_WIDTH-QUIT_BORDER;
        quitButton = createButton("Quit Game",quitX, QUIT_BORDER,QUIT_WIDTH, QUIT_HEIGHT, actionListener);
        quitButton.setVisible(true);
        renderTarget.add(quitButton);

        chooseLeftCardButton = createButton("Choose Left Option",CARD_CHOICE_LHS_GAP, CARD_CHOICE_Y_POS,CARD_CHOICE_WIDTH, CARD_CHOICE_HEIGHT, actionListener);
        renderTarget.add(chooseLeftCardButton);

        int cRightX = CARD_CHOICE_LHS_GAP+CARD_CHOICE_WIDTH+CARD_CHOICE_INTER_GAP;
        chooseRightCardButton = createButton("Choose Right Option",cRightX, CARD_CHOICE_Y_POS,CARD_CHOICE_WIDTH, CARD_CHOICE_HEIGHT, actionListener);
        renderTarget.add(chooseRightCardButton);

        String[] placeholder = { "Placeholder","222" };
        SpinnerModel model = new SpinnerListModel(Arrays.asList(placeholder));
        reducingChoice = new JSpinner();
        reducingChoice.setModel(model);
        reducingChoice.addChangeListener(e -> {
            JSpinner spinner = (JSpinner) e.getSource();
            System.out.println(spinner.getValue());
            setSpinnerIndex();//TODO see if this works & remove println()
        }
        );
        reducingChoice.setVisible(false);
        ((JSpinner.DefaultEditor) reducingChoice.getEditor()).getTextField().setEditable(false);
        reducingChoice.setBounds(JCOMBO_LHS_GAP,JCOMBO_Y_POS,JCOMBO_WIDTH,JCOMBO_HEIGHT);
        renderTarget.add(reducingChoice);
    }

    void setSpinnerOptions(ArrayList<Chooseable> choices){
        spinnerValues = new ArrayList<>();
        for(Chooseable choice:choices) {
            spinnerValues.add(choice.displayChoiceDetails());
        }
        SpinnerModel model = new SpinnerListModel(spinnerValues);
        reducingChoice.setModel(model);
        setSpinnerIndex();
    }

    private void setSpinnerIndex(){
        try {
            int index = 0;
            for (Object o : spinnerValues) {
                if (o.equals(reducingChoice.getValue()))
                    spinnerIndex = index;
                index++;
            }
        }
        catch (Exception error){ //TODO fix this
            System.out.println(error.toString());
        }
    }
    int getSpinnerIndex() {
        return spinnerIndex;
    }
    void setVisibleChoiceSpinner(boolean bool){
        reducingChoice.setVisible(bool);
        submitChoice.setVisible(bool);
    }
    void setEnableSubmitButton(boolean bool){
        submitChoice.setEnabled(bool);
    }
    void setVisibleSubmitButton(boolean bool){
        submitChoice.setVisible(bool);
    }

    void setEnableEndTurnButton(boolean bool){
        endTurnButton.setEnabled(bool);
    }
    void setVisibleEndTurnButton(boolean bool){
        endTurnButton.setVisible(bool);
    }

    void setEnableSpinButton(boolean bool){
        spinButton.setEnabled(bool);
    }
    void setVisibleSpinButton(boolean bool){
        spinButton.setVisible(bool);
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
            case WaitingForAck:
                setVisibleEndTurnButton(true);
                setVisibleSpinButton(false);
                setVisibleCardChoice(false);
                setVisibleChoiceSpinner(false);
                break;
            case WaitingForSpin:
                setVisibleEndTurnButton(false);
                setVisibleSpinButton(true);
                setVisibleCardChoice(false);
                setVisibleChoiceSpinner(false);
                break;
            case PostSpin:
                break;
            case CardChoice://in normal play screen
                setVisibleEndTurnButton(false);
                setVisibleSpinButton(false);
                setVisibleCardChoice(true);
                setVisibleChoiceSpinner(false);
                break;
            case LargeChoice:
                setVisibleEndTurnButton(false);
                setVisibleSpinButton(false);
                setVisibleCardChoice(false);
                setVisibleChoiceSpinner(true);
                break;
            case EndGame:
                setVisibleSpinButton(false);
                break;
            default:
                break;
        }
        renderTarget.paintComponents(graphics);
    }
}
