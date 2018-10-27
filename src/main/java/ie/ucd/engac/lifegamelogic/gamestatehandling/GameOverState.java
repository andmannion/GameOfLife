package ie.ucd.engac.lifegamelogic.gamestatehandling;

import ie.ucd.engac.messaging.AckRequestMessage;
import ie.ucd.engac.messaging.LifeGameMessage;
import ie.ucd.engac.messaging.LifeGameMessageTypes;

@SuppressWarnings("SpellCheckingInspection")
public class GameOverState implements GameState {

    private String eventMessage;

    public GameOverState(){}

    //TODO constructor with the situational event message
    public void enter(GameLogic gameLogic){

        AckRequestMessage ackRequestMessage = new AckRequestMessage(0, "Game Over");
        gameLogic.setResponseMessage(ackRequestMessage);
    }

    public GameState handleInput(GameLogic gameLogic, LifeGameMessage lifeGameMessage){
        return null;
    }

    public void exit(GameLogic gameLogic){

    }
}
