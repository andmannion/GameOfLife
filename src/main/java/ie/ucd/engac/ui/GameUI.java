package ie.ucd.engac.ui;

import ie.ucd.engac.GameEngine;
import ie.ucd.engac.ui.Drawable;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameUI implements ActionListener,Drawable {

    private GameEngine gameEngine;
    private GameBoard gameBoard;
    private GameHUD gameHUD;
    private GameInput gameInput;
    private int panelHeight;
    private int panelWidth;

    public GameUI(GameEngine gameEngine){
        panelHeight = gameEngine.getPanelHeight();
        panelWidth = gameEngine.getPanelWidth();
        gameBoard = new GameBoard(this);
        gameHUD = new GameHUD(gameEngine,this);
    }

    int getPanelHeight() {
        return panelHeight;
    }

    int getPanelWidth() {
        return panelWidth;
    }


    @Override
    public void draw(Graphics graphics){
        gameHUD.draw(graphics);
        gameBoard.draw(graphics);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //TODO
    }
}
