package ie.ucd.engac.ui;

import ie.ucd.engac.GameEngine;
import ie.ucd.engac.messaging.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static ie.ucd.engac.ui.UIState.*;

public class GameUI implements Drawable {

    //objects that manage comm. protocol
    private MessagingInterface<LifeGameMessage> messagingInterface;
    private LifeGameMessage lastResponse;

    //parent GameEngine
    private GameEngine gameEngine;

    //UI sub elements
    private UIBoard uiBoard;
    private UIHUD uiHUD;
    private UIInput uiInput;
    private UICardChoice uiCardChoice;
    private UIActionListener uiActionListener;
    private UIEventMessage uiEventMessage;
    private UIWinner uiWinner;

    //tracking the UI state to draw the correct items
    private UIState uiState;

    //flags for edge detection of state changes
    private volatile boolean wasStateUpdatedD = false;
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

        uiActionListener = new UIActionListener();
        uiBoard = new UIBoard(this);
        uiHUD = new UIHUD(this);
        uiInput = new UIInput(this,renderTarget);
        uiCardChoice = new UICardChoice(this);
        uiEventMessage = new UIEventMessage();
        uiWinner = new UIWinner(this);

        //updateCurrentUIScreen();
    }

    /**
     * Looks at edges of uistate to determine if UI view needs to be changed.
     */
    public void updateCurrentUIScreen(){

        if (wasStateUpdatedD != wasStateUpdatedQ){// || wasStateUpdatedQ == wasStateUpdatedQQ) {
            switch (lastResponse.getLifeGameMessageType()) {
                case StartupMessage:
                    break;
                case LargeDecisionRequest:
                    LargeDecisionRequestMessage pendingLargeDecision = (LargeDecisionRequestMessage) lastResponse;
                    uiEventMessage.updateEventMessage(pendingLargeDecision.getEventMsg());
                    uiInput.setSpinnerOptions(pendingLargeDecision.getChoices());
                    uiState = LargeChoice;
                    uiInput.setEnableSubmitButton(true);
                    break;
                case SpinRequest:
                    SpinRequestMessage spinRequest = (SpinRequestMessage) lastResponse;
                    uiState = WaitingForSpin;
                    uiEventMessage.updateEventMessage(spinRequest.getEventMsg());
                    uiInput.setEnableSpinButton(true);
                    uiHUD.updateFields(spinRequest.getShadowPlayer());
                    break;
                case OptionDecisionRequest:
                    DecisionRequestMessage pendingDecision = (DecisionRequestMessage) lastResponse;
                    uiState = CardChoice;
                    uiEventMessage.updateEventMessage(pendingDecision.getEventMsg());
                    uiCardChoice.setChoices(pendingDecision.getChoices());
                    uiInput.setEnableCardChoice(true);
                    break;
                case AckRequest:
                    AckRequestMessage ackRequest = (AckRequestMessage) lastResponse;
                    uiState = WaitingForAck;
                    uiEventMessage.updateEventMessage(ackRequest.getEventMsg());
                    uiInput.setEnableEndTurnButton(true);
                    break;
                case EndGameMessage:
                    EndGameMessage endGameMessage = (EndGameMessage) lastResponse;
                    uiWinner.setRankedPlayers(endGameMessage.getRankedPlayers());
                    uiState = EndGame;
                    uiEventMessage.updateEventMessage("Game Over.");
                    break;
                default:
                    System.err.println("A message needs handling code written, or was null:");
                    System.err.println(lastResponse.getLifeGameMessageType());

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
        LifeGameMessage message = new StartupMessage();
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
     * Send an ack response message using the interface.
     */
    private void sendAckResponseMessage(){
        LifeGameMessage message = new AckResponseMessage();
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
    UIActionListener getUiActionListener() { return uiActionListener; }

    @Override
    public void draw(Graphics graphics){ //TODO convert to array
        uiHUD.draw(graphics);
        uiBoard.draw(graphics);
        uiInput.draw(graphics);
        uiCardChoice.draw(graphics);
        uiEventMessage.draw(graphics);
        uiWinner.draw(graphics);
    }

    /**
     * UI's ActionListener that responds to button presses
     */
    private class UIActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            switch(e.getActionCommand()){
                case "Quit Game":
                    gameEngine.quitGame();
                    break;
                case "Choose Left Option":
                    uiInput.setEnableCardChoice(false);
                    sendDecisionResponseMessage(0);
                    break;
                case "Choose Right Option":
                    uiInput.setEnableCardChoice(false);
                    sendDecisionResponseMessage(1);
                    break;
                case "Spin The Wheel":
                    uiInput.setEnableSpinButton(false);
                    sendSpinResponseMessage();
                    break;
                case "Submit Choice":
                    uiInput.setEnableSubmitButton(false);
                    sendLargeDecisionResponse(uiInput.getSpinnerIndex());
                    break;
                case "End Turn":
                    uiInput.setEnableEndTurnButton(false);
                    sendAckResponseMessage();
                    break;
            }
        }
    }

}
