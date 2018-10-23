package ie.ucd.engac.ui;

import java.awt.*;

public class UIBoard implements Drawable {
    //private StateMachine stateMachine (from elsewhere)
    private int panelHeight;
    private int panelWidth;

    UIBoard(GameUI gameUI){
        panelHeight = gameUI.getPanelHeight();
        panelWidth = gameUI.getPanelWidth();
    }

    @Override
    public void draw(Graphics graphics){
        //TODO board draw method
    }
}

