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
    public void draw(Graphics graphics) {
        if(gameUIParent.getUIState() == UIState.EndGame) {
            Font oldFont = graphics.getFont();
            Font newFont = oldFont.deriveFont(oldFont.getSize() * 2.5F);
            graphics.setFont(newFont); //want to use a bigger font

            graphics.setColor(UIColours.HUD_AREA_COLOUR);
            graphics.fillRoundRect(rectX,rectY,rectWidth,rectHeight,rectWidth/4,rectHeight/4);
            graphics.setColor(Color.black);
            graphics.drawRoundRect(rectX,rectY,rectWidth,rectHeight,rectWidth/4,rectHeight/4);
            graphics.setColor(Color.black);
            String title = "Results:";
            int titleX = rectX+(rectWidth-getStringWidth("Results:",graphics))/2;
            int titleY = rectY+rectHeight/6;
            graphics.drawString(title, titleX, titleY);
            int playerNum;
            int stringX;
            int inc = 1;
            for (Player player : rankedPlayers) {
                playerNum = player.getPlayerNumber();
                String string = "Player " + playerNum + " has " + player.getCurrentMoney();
                stringX = rectX+(rectWidth-getStringWidth(string,graphics))/2;
                graphics.drawString(string, stringX, titleY + inc * (rectHeight/6));
                inc++;
            }
            graphics.setFont(oldFont); //reset font
        }
    }
}
