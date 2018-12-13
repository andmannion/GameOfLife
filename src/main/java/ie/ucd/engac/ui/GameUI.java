package ie.ucd.engac.ui;

import ie.ucd.engac.GameEngine;
import ie.ucd.engac.messaging.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

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
    private ArrayList<Drawable> drawables;

    //tracking the UI state to draw the correct items
    private volatile UIState uiState;

    //flags for edge detection of state changes
    private volatile boolean wasStateUpdatedD = false;
    private boolean wasStateUpdatedQ = false;

    // ...
    private int panelHeight;
    private int panelWidth;
    private int hudStartY;

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

        hudStartY = Math.round(((0.7f)*panelHeight));

        uiActionListener = new UIActionListener();

        uiHUD = new UIHUD(this, hudStartY);
        uiBoard = new UIBoard(this, hudStartY, panelWidth);
        uiCardChoice = new UICardChoice(this,hudStartY);
        uiWinner = new UIWinner(this);
        uiEventMessage = new UIEventMessage();
        uiInput = new UIInput(this,renderTarget);

        drawables = new ArrayList<>();
        drawables.addAll(Arrays.asList(uiBoard, uiCardChoice, uiHUD, uiWinner, uiEventMessage,uiInput)); //

    }

    /**
     * Looks at edges of uistate to determine if UI view needs to be changed.
     */
    public void updateCurrentUIScreen(){

        if (wasStateUpdatedD != wasStateUpdatedQ){
            switch (lastResponse.getLifeGameMessageType()) {
                case StartupMessage:
                    break;
                case LargeDecisionRequest:
                    DecisionRequestMessage pendingLargeDecision = (DecisionRequestMessage) lastResponse;
                    uiEventMessage.updateEventMessage(pendingLargeDecision.getEventMsg());
                    uiInput.setSpinnerOptions(pendingLargeDecision.getChoices());
                    uiState = LargeChoice;
                    uiInput.setEnableSubmitButton(true);
                    uiHUD.updateFields(pendingLargeDecision.getShadowPlayer());
                    break;
                case SpinRequest:
                    LifeGameRequestMessage spinRequest = (LifeGameRequestMessage) lastResponse;
                    uiState = WaitingForSpin;
                    uiEventMessage.updateEventMessage(spinRequest.getEventMsg());
                    uiInput.setEnableSpinButton(true);
                    handleShadowPlayer(spinRequest.getShadowPlayer());
                    break;
                case OptionDecisionRequest:
                    DecisionRequestMessage pendingDecision = (DecisionRequestMessage) lastResponse;
                    uiState = CardChoice;
                    uiEventMessage.updateEventMessage(pendingDecision.getEventMsg());
                    uiCardChoice.setChoices(pendingDecision.getChoices());
                    uiInput.setEnableCardChoice(true);
                    handleShadowPlayer(pendingDecision.getShadowPlayer());
                    break;
                case AckRequest:
                    LifeGameRequestMessage ackRequest = (LifeGameRequestMessage) lastResponse;
                    uiState = WaitingForAck;
                    uiEventMessage.updateEventMessage(ackRequest.getEventMsg());
                    uiInput.setEnableEndTurnButton(true);
                    handleShadowPlayer(ackRequest.getShadowPlayer());
                    break;
                case EndGameMessage:
                    EndGameMessage endGameMessage = (EndGameMessage) lastResponse;
                    uiWinner.setRankedPlayers(endGameMessage.getRankedPlayers());
                    uiState = EndGame;
                    uiEventMessage.updateEventMessage("Game Over.");
                    break;
                case UIConfigMessage:
                    UIConfigMessage uiConfigMessage = (UIConfigMessage) lastResponse;
                    handleConfigMessage(uiConfigMessage);
                    uiState = WaitingForAck;
                    uiEventMessage.updateEventMessage(uiConfigMessage.getEventMsg());
                    uiInput.setEnableEndTurnButton(true);
                    break;
                default:
                    System.err.println("A message needs handling code written, or was null:");
                    System.err.println(lastResponse.getLifeGameMessageType());

                    uiState = UIState.Init;
            }
        }
        wasStateUpdatedQ = wasStateUpdatedD;
    }

    private void handleConfigMessage(UIConfigMessage uiConfigMessage){
        ArrayList<Pawn> pawns = uiConfigMessage.getPawns();
        ArrayList<Tile> tiles = uiConfigMessage.getBoard().getTiles();
        HashMap<Integer,UIPawn> pawnMap = new HashMap<>();

        uiBoard.setLayout(tiles);

        for(Pawn pawn:pawns){
            UIPawn uiPawn = new UIPawn(pawn,panelWidth/128);
            uiPawn.setScalingFactors(panelWidth,hudStartY);
            pawnMap.put(pawn.getPlayerNumber(), uiPawn);
        }
        uiBoard.setPawnMap(pawnMap);
    }

    private void handleShadowPlayer(ShadowPlayer shadowPlayer){
        if(shadowPlayer != null) {
            uiBoard.updatePawns(shadowPlayer.getPlayerNumber(), shadowPlayer.getXLocation(), shadowPlayer.getYLocation(), shadowPlayer.getNumDependants());
            uiHUD.updateFields(shadowPlayer);
        }
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
        LifeGameMessage message = new DecisionResponseMessage(choice,LifeGameMessageTypes.OptionDecisionResponse);
        lastResponse = messagingInterface.sendMessageAcceptResponse(message);
        invertWasStateUpdatedD();
    }

    /**
     * Send a large decision response message using the interface.
     * @param choice the user's choice.
     */
    private void sendLargeDecisionResponse(int choice){
        LifeGameMessage message = new DecisionResponseMessage(choice,LifeGameMessageTypes.LargeDecisionResponse);
        lastResponse = messagingInterface.sendMessageAcceptResponse(message);
        invertWasStateUpdatedD();
    }

    /**
     * Send a spin response message using the interface.
     */
    private void sendSpinResponseMessage(){
        LifeGameMessage message = new LifeGameMessage(LifeGameMessageTypes.SpinResponse);
        lastResponse = messagingInterface.sendMessageAcceptResponse(message);
        SpinResultMessage spinResultMessage = (SpinResultMessage) lastResponse;
        uiHUD.setSpinResult(spinResultMessage.getSpinResult());
        message = new LifeGameMessage(LifeGameMessageTypes.AckResponse);
        lastResponse = messagingInterface.sendMessageAcceptResponse(message);
        invertWasStateUpdatedD();
    }

    /**
     * Send an ack response message using the interface.
     */
    private void sendAckResponseMessage(){
        LifeGameMessage message = new LifeGameMessage(LifeGameMessageTypes.AckResponse);
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
    public void draw(Graphics graphics){
        for (Drawable d:drawables){
            d.draw(graphics);
        }
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
                case "OK":
                    uiInput.setEnableEndTurnButton(false);
                    sendAckResponseMessage();
                    break;
            }
        }
    }

}
