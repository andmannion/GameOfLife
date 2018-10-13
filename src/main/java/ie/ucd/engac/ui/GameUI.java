package ie.ucd.engac.ui;

import ie.ucd.engac.GameEngine;
import ie.ucd.engac.messaging.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static ie.ucd.engac.ui.UIState.CardChoice;

public class GameUI implements Drawable {

    private MessagingInterface<LifeGameMessage> messagingInterface;
    private LifeGameMessage lastResponse;
    private DecisionRequestMessage pendingDecision;
    private GameEngine gameEngine;
    private GameBoard gameBoard;
    private GameHUD gameHUD;
    private GameInput gameInput;
    private GameCardChoice gameCardChoice;

    private UIState uiState;
    private int currentPlayer;

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
        gameHUD = new GameHUD(gameEngine,this);
        gameInput = new GameInput(this,renderTarget);
        gameCardChoice = new GameCardChoice(this);

        if(true) { //edge detect the flag here
            setCurrentUIScreen();
        }
    }

    private void setCurrentUIScreen(){
        //System.out.println(lastResponse.getLifeGameMessageType().toString()); //TODO remove this
        switch (lastResponse.getLifeGameMessageType()){
            case StartupMessage:
                break;
            case SpinRequest:
                break;
            case CareerPathRequest:
                //ignore this state
                break;
            case OptionDecisionRequest:
                uiState = CardChoice;
                pendingDecision = (DecisionRequestMessage) lastResponse;
                currentPlayer = pendingDecision.getRelatedPlayer();
                gameCardChoice.setChoices(pendingDecision.getChoices());
                break;
            case OptionDecisionResponse:
                break;
            default:
                uiState = UIState.Init;
        }
    }

    UIState getUIState(){ return uiState; }

    private void sendStartupMessage(){
        LifeGameMessage message = new LifeGameMessage(LifeGameMessageTypes.StartupMessage);
        lastResponse = messagingInterface.sendMessageAcceptResponse(message);
    }

    private void sendActionMessage(LifeGameMessageTypes messageType){
        LifeGameMessage message = new LifeGameMessage(messageType);
        lastResponse = messagingInterface.sendMessageAcceptResponse(message);
    }

    int getPanelHeight() {
        return panelHeight;
    }

    int getPanelWidth() {
        return panelWidth;
    }

    public GameActionListener getGameActionListener() { return gameActionListener; }

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
                    sendActionMessage(LifeGameMessageTypes.OptionDecisionResponse);
                    break;


            }

        }
    }
}
