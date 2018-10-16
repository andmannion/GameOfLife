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

    private MessagingInterface<LifeGameMessage> messagingInterface;
    private LifeGameMessage lastResponse;
    private GameEngine gameEngine;
    private GameBoard gameBoard;
    private GameHUD gameHUD;
    private GameInput gameInput;
    private GameCardChoice gameCardChoice;

    private UIState uiState;
    private int currentPlayer;

    private volatile boolean wasStateUpdatedD = false; //init with different values
    private boolean wasStateUpdatedQ = false;

    private int panelHeight;
    private int panelWidth;

    private GameActionListener gameActionListener;

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
                    gameCardChoice.setChoices(pendingDecision.getChoices());
                    gameInput.setEnableCardChoice(true);
                    gameInput.setVisibleCardChoice(true);
                    break;
                default:
                    System.out.println("This message needs handling"); //TODO remove
                    uiState = UIState.Init;
            }
        }
        wasStateUpdatedQ = wasStateUpdatedD;
    }

    private synchronized void invertWasStateUpdatedD(){
        wasStateUpdatedD = !wasStateUpdatedD;
    }

    UIState getUIState(){
        return uiState;
    }

    private void sendStartupMessage(){
        invertWasStateUpdatedD();
        LifeGameMessage message = new LifeGameMessage(LifeGameMessageTypes.StartupMessage);
        lastResponse = messagingInterface.sendMessageAcceptResponse(message);
    }

    private void sendDecisionResponseMessage(int choice){
        invertWasStateUpdatedD();
        LifeGameMessage message = new DecisionResponseMessage(choice);
        lastResponse = messagingInterface.sendMessageAcceptResponse(message);
    }

    private void sendSpinResponseMessage(){
        invertWasStateUpdatedD();
        LifeGameMessage message = new SpinResponseMessage();
        lastResponse = messagingInterface.sendMessageAcceptResponse(message);
    }

    private void sendLargeDecisionResponse(int choice){
        invertWasStateUpdatedD();
        LifeGameMessage message = new LargeDecisionResponseMessage(choice);
        lastResponse = messagingInterface.sendMessageAcceptResponse(message);
    }

    int getPanelHeight() {
        return panelHeight;
    }

    int getPanelWidth() {
        return panelWidth;
    }

    GameActionListener getGameActionListener() { return gameActionListener; }

    @Override
    public void draw(Graphics graphics){
        gameHUD.draw(graphics);
        gameBoard.draw(graphics);
        gameInput.draw(graphics);
        gameCardChoice.draw(graphics);
    }

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
