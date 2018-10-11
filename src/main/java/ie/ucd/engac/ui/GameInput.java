package ie.ucd.engac.ui;

import java.awt.*;

public class GameInput implements Drawable {//TODO rename this
    //private *button spinButton;
    //private *button exitGame;
    //private *button ???
    //private *button left/centre/right option; //cards, careers, forks,...
    //private *
    private int panelHeight;
    private int panelWidth;

    public GameInput(GameUI gameUI){
        panelHeight = gameUI.getPanelHeight();
        panelWidth = gameUI.getPanelWidth();
    }
    @Override
    public void draw(Graphics graphics){

    }
}
