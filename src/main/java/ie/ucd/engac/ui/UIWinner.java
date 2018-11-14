package ie.ucd.engac.ui;

import ie.ucd.engac.lifegamelogic.playerlogic.Player;

import java.awt.*;
import java.util.ArrayList;

public class UIWinner implements Drawable{

    private ArrayList<Player> rankedPlayers;
    private GameUI gameUIParent;

    UIWinner(GameUI gameUI){this.gameUIParent = gameUI;}

    void setRankedPlayers(ArrayList<Player> rankedPlayers){
        this.rankedPlayers = rankedPlayers;
    }

    @Override
    public void draw(Graphics graphics) {//TODO finish this
        if(gameUIParent.getUIState() == UIState.EndGame) {
            graphics.setColor(Color.black);
            graphics.drawString("Results:", 100, 100);
            int playerNum;
            for (Player player : rankedPlayers) {
                playerNum = player.getPlayerNumber();
                graphics.drawString("Player " + playerNum + " has " + player.getCurrentMoney(), 100, 100 + playerNum * 12);
            }
        }
    }
}
