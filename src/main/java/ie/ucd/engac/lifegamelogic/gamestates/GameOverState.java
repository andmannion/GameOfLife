package ie.ucd.engac.lifegamelogic.gamestates;

import ie.ucd.engac.lifegamelogic.GameLogic;
import ie.ucd.engac.lifegamelogic.playerlogic.Player;
import ie.ucd.engac.messaging.EndGameMessage;
import ie.ucd.engac.messaging.LifeGameMessage;

import java.util.ArrayList;

public class GameOverState extends GameState {


    GameOverState(){}

    //TODO constructor with the situational event message
    public void enter(GameLogic gameLogic){

        ArrayList<Player> rankings = gameLogic.getRankedRetiredPlayers();
        LifeGameMessage responseMessage = new EndGameMessage(rankings);
        gameLogic.setResponseMessage(responseMessage);

    }

    public GameState handleInput(GameLogic gameLogic, LifeGameMessage lifeGameMessage){
        return null;
    }

    public void exit(GameLogic gameLogic){

    }
}
