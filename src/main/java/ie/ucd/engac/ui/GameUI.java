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
    private Graphics drawTarget;

    public GameUI(GameEngine gameEngine){
        gameBoard = new GameBoard();
        gameHUD = new GameHUD(gameEngine);
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
