package ie.ucd.engac.ui;

import java.awt.*;

public class UIBoard implements Drawable {

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

