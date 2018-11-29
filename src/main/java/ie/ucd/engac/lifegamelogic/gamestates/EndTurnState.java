package ie.ucd.engac.lifegamelogic.gamestates;

import ie.ucd.engac.lifegamelogic.GameLogic;
import ie.ucd.engac.messaging.LifeGameMessage;
import ie.ucd.engac.messaging.LifeGameMessageTypes;
import ie.ucd.engac.messaging.LifeGameRequestMessage;

public class EndTurnState extends GameState {

    private String eventMessage;

    public EndTurnState(){}

    public EndTurnState(String eventMessage){
        this.eventMessage = eventMessage;
    }

    public void enter(GameLogic gameLogic){
        if (eventMessage == null){
            int playNum = gameLogic.getCurrentPlayer().getPlayerNumber();
            eventMessage = "Player " + playNum + "'s turn is over.";

        }
        LifeGameRequestMessage ackRequestMessage = new LifeGameRequestMessage(LifeGameMessageTypes.AckRequest, eventMessage, null);
        gameLogic.setResponseMessage(ackRequestMessage);
    }

    public GameState handleInput(GameLogic gameLogic, LifeGameMessage lifeGameMessage){
        if (lifeGameMessage.getLifeGameMessageType() == LifeGameMessageTypes.AckResponse){

            gameLogic.setNextPlayerToCurrent();
            if (gameLogic.getNumberOfUninitialisedPlayers() > 0) {
                // Must send a message to choose a career path, etc.
                LifeGameMessage replyMessage = PathChoiceState.constructPathChoiceMessage(gameLogic.getCurrentPlayer().getPlayerNumber(), gameLogic.getCurrentShadowPlayer());
                gameLogic.setResponseMessage(replyMessage);

                return new PathChoiceState();
            }
            return new HandlePlayerMoveState(); // didnt receive the correct message, looping
        }

        return null;
    }

}
