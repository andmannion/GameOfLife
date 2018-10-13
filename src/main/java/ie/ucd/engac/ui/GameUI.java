package ie.ucd.engac.ui;

import ie.ucd.engac.GameEngine;
import ie.ucd.engac.messaging.LifeGameMessage;
import ie.ucd.engac.messaging.LifeGameMessageTypes;
import ie.ucd.engac.messaging.MessagingInterface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameUI implements Drawable {

    private MessagingInterface<LifeGameMessage> messagingInterface;
    private GameEngine gameEngine;
    private GameBoard gameBoard;
    private GameHUD gameHUD;
    private GameInput gameInput;
    private int panelHeight;
    private int panelWidth;

    private GameActionListener gameActionListener;

    public GameUI(GameEngine gameEngine, JPanel renderTarget, MessagingInterface<LifeGameMessage> messagingInterface){
        this.gameEngine = gameEngine;
        this.messagingInterface = messagingInterface;
        sendStartupMessage();

        panelHeight = gameEngine.getPanelHeight();
        panelWidth = gameEngine.getPanelWidth();

        gameActionListener = new GameActionListener();
        gameBoard = new GameBoard(this);
        gameHUD = new GameHUD(gameEngine,this);
        gameInput = new GameInput(this,renderTarget);

    }

    private void sendStartupMessage(){
        LifeGameMessage message = new LifeGameMessage(LifeGameMessageTypes.StartupMessage);
        messagingInterface.sendMessageAcceptResponse(message);
    }

    private void sendActionMessage(LifeGameMessageTypes messageType){
        LifeGameMessage message = new LifeGameMessage(messageType);
        messagingInterface.sendMessageAcceptResponse(message);
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
    }

    private class GameActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            switch(e.getActionCommand()){
                case "Quit Game": gameEngine.quitGame();

            }

        }
    }
}
