package ie.ucd.engac.ui;

import ie.ucd.engac.messaging.Chooseable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;

public class TestInput implements Drawable {

    private static final int SPIN_WIDTH = 192;
    private static final int SPIN_HEIGHT = 36;
    private static final int SPIN_BORDER = 13;

    private static final int QUIT_WIDTH = 128;
    private static final int QUIT_HEIGHT = 36;
    private static final int QUIT_BORDER = 13;

    private static final int CARD_CHOICE_WIDTH = 192;
    private static final int CARD_CHOICE_HEIGHT = 36;
    private static final int CARD_CHOICE_LHS_GAP = 326;
    private static final int CARD_CHOICE_INTER_GAP = 240;
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
    private JPanel panel1;
    private JTextArea textArea1;

    private ActionListener actionListener;

    private JPanel renderTarget;

    private GameUI gameUIParent;

    private void setupButton(String string, JButton jButton, ActionListener actionListener){
        //JButton button = new JButton(string);
        jButton.setActionCommand(string);
        //button.setBounds(xPos,yPos,width,height);
        jButton.setVisible(false);
        jButton.addActionListener(actionListener);
    }

    TestInput(GameUI gameUI, JPanel renderTarget){
        this.gameUIParent = gameUI;
        this.renderTarget = renderTarget;
        panelHeight = gameUI.getPanelHeight();
        panelWidth = gameUI.getPanelWidth();
        actionListener = gameUI.getUiActionListener();

        setupButton("OK",endTurnButton, actionListener);
        renderTarget.add(endTurnButton);

        setupButton("Spin The Wheel",spinButton,actionListener);
        renderTarget.add(spinButton);

        setupButton("Submit Choice",submitChoice, actionListener);
        renderTarget.add(submitChoice);

        setupButton("Quit Game",quitButton, actionListener);
        quitButton.setVisible(true);
        renderTarget.add(quitButton);

        setupButton("Choose Left Option",chooseLeftCardButton, actionListener);
        renderTarget.add(chooseLeftCardButton);

        setupButton("Choose Right Option",chooseRightCardButton, actionListener);
        renderTarget.add(chooseRightCardButton);

        String[] placeholder = { ".","." };
        SpinnerModel model = new SpinnerListModel(Arrays.asList(placeholder));
        //reducingChoice = new JSpinner();
        reducingChoice.setModel(model);
        reducingChoice.addChangeListener(e -> setSpinnerIndex());
        reducingChoice.setVisible(false);
        ((JSpinner.DefaultEditor) reducingChoice.getEditor()).getTextField().setEditable(false);
        //reducingChoice.setBounds(JCOMBO_LHS_GAP,JCOMBO_Y_POS,JCOMBO_WIDTH,JCOMBO_HEIGHT);
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
            System.err.println("Error in spinner values: "+ error.toString());
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
