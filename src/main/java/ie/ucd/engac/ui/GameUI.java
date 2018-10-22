package ie.ucd.engac.ui;

import ie.ucd.engac.GameEngine;
import ie.ucd.engac.messaging.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static ie.ucd.engac.ui.UIState.CardChoice;
import static ie.ucd.engac.ui.UIState.WaitingForSpin;

public class GameUI implements Drawable {

    //objects that manage comm. protocol
    private MessagingInterface<LifeGameMessage> messagingInterface;
    private LifeGameMessage lastResponse;

    //parent GameEngine
    private GameEngine gameEngine;

    //UI sub elements
    private GameBoard gameBoard;
    private GameHUD gameHUD;
    private GameInput gameInput;
    private GameCardChoice gameCardChoice;
    private GameActionListener gameActionListener;

    //tracking the UI state to draw the correct items
    private UIState uiState;

    //flags for edge detection of state changes
    private volatile boolean wasStateUpdatedD = false; //init with different values
    private boolean wasStateUpdatedQ = false;

    // ...
    private int panelHeight;
    private int panelWidth;

    /**
     * Constructor for GameUI.
     * @param gameEngine            Parent GameEngine.
     * @param renderTarget          JPanel to act as render target
     * @param messagingInterface    MessagingInterface used to comm. with logic
     */
    public GameUI(GameEngine gameEngine, JPanel renderTarget, MessagingInterface<LifeGameMessage> messagingInterface){
        this.gameEngine = gameEngine;
        this.messagingInterface = messagingInterface;
        sendStartupMessage();
        uiState = UIState.Init;

        panelHeight = gameEngine.getPanelHeight();
        panelWidth = gameEngine.getPanelWidth();

        gameActionListener = new GameActionListener();
        gameBoard = new GameBoard(this);
        gameHUD = new GameHUD(this);
        gameInput = new GameInput(this,renderTarget);
        gameCardChoice = new GameCardChoice(this);

        //updateCurrentUIScreen();
    }

    /**
     * Looks at edges of uistate to determine if UI view needs to be changed.
     */
    public void updateCurrentUIScreen(){

        boolean risingEdgeDetected = wasStateUpdatedD && (!wasStateUpdatedQ);
        boolean fallingEdgeDetected = (!wasStateUpdatedD) && wasStateUpdatedQ;

       if (risingEdgeDetected || fallingEdgeDetected) {
            System.out.println(lastResponse.getLifeGameMessageType());
            switch (lastResponse.getLifeGameMessageType()) {
                case StartupMessage:
                    break;
                case LargeDecisionRequest: //TODO this is untested
                    DecisionRequestMessage pendingLargeDecision = (DecisionRequestMessage) lastResponse;
                    currentPlayer = pendingLargeDecision.getRelatedPlayer();
                    gameInput.setSpinnerOptions(pendingLargeDecision.getChoices());
                    gameInput.setEnableCardChoice(true);
                    gameInput.setVisibleCardChoice(true);
                    break;
                case SpinRequest:
                    SpinRequestMessage spinRequest = (SpinRequestMessage) lastResponse;
                    uiState = WaitingForSpin;
                    gameHUD.updateFields(spinRequest.getShadowPlayer());
                    break;
                case OptionDecisionRequest:
                    uiState = CardChoice;
                    DecisionRequestMessage pendingDecision = (DecisionRequestMessage) lastResponse;
                    currentPlayer = pendingDecision.getRelatedPlayer();
                    System.out.println(pendingDecision.getChoices().get(0).displayChoiceDetails());
                    gameCardChoice.setChoices(pendingDecision.getChoices());
                    gameInput.setEnableCardChoice(true);
                    gameInput.setVisibleCardChoice(true);
                    break;
                default:
                    System.out.println("A message needs handling code written"); //TODO remove
                    uiState = UIState.Init;
            }
        }
        wasStateUpdatedQ = wasStateUpdatedD;
    }

    /**
     * Update the flag for edge detection.
     */
    private synchronized void invertWasStateUpdatedD(){
        wasStateUpdatedD = !wasStateUpdatedD;
    }

    /**
     * Returns the UI state for subelements to draw themselves
     * @return The state of the ui as a UIState enum
     */
    UIState getUIState(){
        return uiState;
    }

    /**
     * Send a startup message using the interface.
     */
    private void sendStartupMessage(){
        LifeGameMessage message = new LifeGameMessage(LifeGameMessageTypes.StartupMessage);
        lastResponse = messagingInterface.sendMessageAcceptResponse(message);
        invertWasStateUpdatedD();
    }

    /**
     * Send a decision response message using the interface.
     * @param choice the user's choice.
     */
    private void sendDecisionResponseMessage(int choice){
        LifeGameMessage message = new DecisionResponseMessage(choice);
        lastResponse = messagingInterface.sendMessageAcceptResponse(message);
        invertWasStateUpdatedD();
    }

    /**
     * Send a spin response message using the interface.
     */
    private void sendSpinResponseMessage(){
        LifeGameMessage message = new SpinResponseMessage();
        lastResponse = messagingInterface.sendMessageAcceptResponse(message);
        invertWasStateUpdatedD();
    }

    /**
     * Send a large decision response message using the interface.
     * @param choice the user's choice.
     */
    private void sendLargeDecisionResponse(int choice){ //TODO Required? Probably not
        LifeGameMessage message = new LargeDecisionResponseMessage(choice);
        lastResponse = messagingInterface.sendMessageAcceptResponse(message);
        invertWasStateUpdatedD();
    }

    /**
     * Returns the panel height.
     * @return the panel height as an integer.
     */
    int getPanelHeight() {
        return panelHeight;
    }

    /**
     * Returns the panel width.
     * @return the panel width as an integer.
     */
    int getPanelWidth() {
        return panelWidth;
    }

    /**
     * Returns the UI's button press action listener.
     * @return the UI's ActionListener.
     */
    GameActionListener getGameActionListener() { return gameActionListener; }

    @Override
    public void draw(Graphics graphics){
        gameHUD.draw(graphics);
        gameBoard.draw(graphics);
        gameInput.draw(graphics);
        gameCardChoice.draw(graphics);
    }

    /**
     * UI's ActionListener that responds to button presses
     */
    private class GameActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            switch(e.getActionCommand()){
                case "Quit Game":
                    gameEngine.quitGame();
                    break;
                case "Choose Left Card":
                    gameInput.setEnableCardChoice(false);
                    System.out.println("Chose");
                    sendDecisionResponseMessage(0);
                    break;
                case "Choose Right Card":
                    gameInput.setEnableCardChoice(false);
                    sendDecisionResponseMessage(1);
                    break;
                case "Spin The Wheel":
                    gameInput.setEnableSpinButton(false);
                    sendSpinResponseMessage();
                case "Submit Choice":
                    gameInput.setEnableSubmitButton(false);
                    sendLargeDecisionResponse(gameInput.getSpinnerIndex());
            }
        }
    }

}
