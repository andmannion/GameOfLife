package ie.ucd.engac.ui;

import ie.ucd.engac.messaging.Chooseable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;

public class UIInput implements Drawable {

    private static final int SPIN_WIDTH = 192;
    private static final int QUIT_WIDTH = 128;
    private static final int OK_WIDTH = 128;
    private static final int CARD_CHOICE_WIDTH = 192;
    private static final int JCOMBO_WIDTH = 164*2;

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

    UIInput(GameUI gameUI, JPanel renderTarget){
        this.gameUIParent = gameUI;
        this.renderTarget = renderTarget;
        panelHeight = gameUI.getPanelHeight();
        panelWidth = gameUI.getPanelWidth();
        actionListener = gameUI.getUiActionListener();

        createEndButton(renderTarget);
        createSpinButton(renderTarget);
        createQuitButton(renderTarget);
        createMultiChoice(renderTarget);
        createCardChoice(renderTarget);
    }

    private void createCardChoice(JPanel renderTarget) {
        int xOffset = panelWidth/3;
        int leftX = xOffset - CARD_CHOICE_WIDTH/2;
        int rightX = 2*xOffset - CARD_CHOICE_WIDTH/2;

        int cardChoiceHeight = (int)(CARD_CHOICE_WIDTH/1.618)/3;
        int bothY = panelHeight/2 - cardChoiceHeight/2;

        String string;
        string = "Choose Left Option";
        chooseLeftCardButton = createButton(string,leftX, bothY,CARD_CHOICE_WIDTH, cardChoiceHeight, actionListener);
        renderTarget.add(chooseLeftCardButton);

        string = "Choose Right Option";
        chooseRightCardButton = createButton(string,rightX, bothY,CARD_CHOICE_WIDTH, cardChoiceHeight, actionListener);
        renderTarget.add(chooseRightCardButton);
    }

    private void createMultiChoice(JPanel renderTarget) {
        int comboX = (panelWidth-JCOMBO_WIDTH)/2;
        int comboHeight = (int)(JCOMBO_WIDTH/1.618)/6;
        int comboY = panelHeight/2-(int)(comboHeight*1.5);

        int submitWidth = JCOMBO_WIDTH/2;
        int submitX = (panelWidth-submitWidth)/2;
        int submitHeight = (int)(submitWidth/1.618)/3;
        int submitY = panelHeight/2+(int)(submitHeight*1.5);

        String string = "Submit Choice";

        String[] placeholder = { ".","." };
        SpinnerModel model = new SpinnerListModel(Arrays.asList(placeholder));
        reducingChoice = new JSpinner();
        reducingChoice.setModel(model);
        reducingChoice.addChangeListener(e -> setSpinnerIndex());
        reducingChoice.setVisible(false);
        ((JSpinner.DefaultEditor) reducingChoice.getEditor()).getTextField().setEditable(false);
        reducingChoice.setBounds(comboX,comboY,JCOMBO_WIDTH,comboHeight);
        renderTarget.add(reducingChoice);

        submitChoice = createButton(string,submitX, submitY,submitWidth, submitHeight, actionListener);
        renderTarget.add(submitChoice);
    }

    private void createEndButton(JPanel renderTarget) {
        int endX = (panelWidth-OK_WIDTH)/2;
        int endHeight = (int)(OK_WIDTH/1.618)/3;
        int endY = (panelHeight-endHeight)/2;
        endTurnButton = createButton("OK",endX, endY,OK_WIDTH, endHeight, actionListener);
        renderTarget.add(endTurnButton);
    }

    private void createQuitButton(JPanel renderTarget) {
        String string;
        string = "Quit Game";
        int quitHeight = (int)(QUIT_WIDTH/1.618)/3;
        int quitBorder = QUIT_WIDTH/10;
        int quitX = panelWidth-QUIT_WIDTH-quitBorder;
        quitButton = createButton(string,quitX, quitBorder,QUIT_WIDTH, quitHeight, actionListener);
        quitButton.setVisible(true);
        renderTarget.add(quitButton);
    }

    private void createSpinButton(JPanel renderTarget) {
        String string = "Spin The Wheel";
        int spinHeight = (int)(SPIN_WIDTH/1.618)/4;
        int spinBorder = SPIN_WIDTH/10;
        int spinX = panelWidth-SPIN_WIDTH-spinBorder;
        int spinY = panelHeight-spinHeight-spinBorder;
        spinButton = createButton(string,spinX, spinY,SPIN_WIDTH, spinHeight, actionListener);
        renderTarget.add(spinButton);
    }

    private JButton createButton(String string,int xPos, int yPos, int width, int height, ActionListener actionListener){
        JButton button = new JButton(string);
        button.setActionCommand(string);
        button.setBounds(xPos,yPos,width,height);
        button.setVisible(false);
        button.addActionListener(actionListener);
        return button;
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
