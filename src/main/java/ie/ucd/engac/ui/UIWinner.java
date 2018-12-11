package ie.ucd.engac.ui;

import ie.ucd.engac.lifegamelogic.playerlogic.Player;

import java.awt.*;
import java.util.ArrayList;

public class UIWinner implements Drawable{

    private ArrayList<Player> rankedPlayers;
    private GameUI gameUIParent;
    private int rectWidth;
    private int rectHeight;
    private int rectX;
    private int rectY;

    UIWinner(GameUI gameUI){
        this.gameUIParent = gameUI;
        int panelHeight = gameUI.getPanelHeight();
        int panelWidth = gameUI.getPanelWidth();
        rectWidth = panelWidth/3;
        rectHeight = panelHeight/2;
        rectX = (panelWidth-rectWidth)/2;
        rectY = (panelHeight-rectHeight)/2;
    }

    void setRankedPlayers(ArrayList<Player> rankedPlayers){
        this.rankedPlayers = rankedPlayers;
    }

    private int getStringWidth(String string,Graphics graphics){
        return graphics.getFontMetrics().stringWidth(string); //centring
    }

    @Override
    public void draw(Graphics graphics) {//TODO finish this
        if(gameUIParent.getUIState() == UIState.EndGame) {

            graphics.setColor(UIColours.HUD_AREA_COLOUR);
            graphics.fillRoundRect(rectX,rectY,rectWidth,rectHeight,rectWidth/4,rectHeight/4);
            graphics.setColor(Color.black);
            graphics.drawRoundRect(rectX,rectY,rectWidth,rectHeight,rectWidth/4,rectHeight/4);
            graphics.setColor(Color.black);
            String title = "Results:";
            int titleX = getStringWidth("Results:",graphics);
            int titleY = rectY-rectHeight/6;
            graphics.drawString(title, titleX, titleY);
            int playerNum;
            for (Player player : rankedPlayers) {
                playerNum = player.getPlayerNumber();
                graphics.drawString("Player " + playerNum + " has " + player.getCurrentMoney(), 100, 100 + playerNum * 12);
            }
        }
    }
}
