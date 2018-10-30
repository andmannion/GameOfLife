package ie.ucd.engac.messaging;

import ie.ucd.engac.lifegamelogic.playerlogic.Player;

import java.util.ArrayList;

public class EndGameMessage extends LifeGameMessage {
    // Need to tell what is to be chosen between
    private ArrayList<Player> rankedPlayers;

    public EndGameMessage(ArrayList<Player> rankedPlayers) {
        super(LifeGameMessageTypes.EndGameMessage);
        this.rankedPlayers = rankedPlayers;
    }

    public ArrayList<Player> getRankedPlayers() {
        return rankedPlayers;
    }
}
