package ie.ucd.engac.lifegamelogic.gamestatehandling;

import ie.ucd.engac.lifegamelogic.playerlogic.Player;
import ie.ucd.engac.messaging.AckRequestMessage;
import ie.ucd.engac.messaging.LifeGameMessage;
import ie.ucd.engac.messaging.LifeGameMessageTypes;

import java.util.ArrayList;

@SuppressWarnings("SpellCheckingInspection")
public class GameOverState implements GameState {

    private String eventMessage;

    public GameOverState(){}

    //TODO constructor with the situational event message
    public void enter(GameLogic gameLogic){

        ArrayList<Player> rankings = gameLogic.getRankedRetiredPlayers();

    }

    public GameState handleInput(GameLogic gameLogic, LifeGameMessage lifeGameMessage){
        return null;
    }

    public void exit(GameLogic gameLogic){

    }
}
