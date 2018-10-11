package ie.ucd.engac.ui;

import ie.ucd.engac.GameEngine;
import ie.ucd.engac.ui.Drawable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameUI implements Drawable {

    private GameEngine gameEngine;
    private GameBoard gameBoard;
    private GameHUD gameHUD;
    private GameInput gameInput;
    private int panelHeight;
    private int panelWidth;

    private GameActionListener gameActionListener;

    public GameUI(GameEngine gameEngine, JPanel renderTarget){
        panelHeight = gameEngine.getPanelHeight();
        panelWidth = gameEngine.getPanelWidth();
        gameActionListener = new GameActionListener();
        gameBoard = new GameBoard(this);
        gameHUD = new GameHUD(gameEngine,this);
        gameInput = new GameInput(this,renderTarget);
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
            System.out.println(e.getActionCommand());
            switch(e.getActionCommand()){

            }

        }
    }
}
