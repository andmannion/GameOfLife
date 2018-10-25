package ie.ucd.engac.lifegamelogic.gamestatehandling;

import ie.ucd.engac.messaging.*;

import java.util.ArrayList;

public class PickPlayerState implements GameState {

    @Override
    public void enter(GameLogic gameLogic) {
        //get number of players to initialise message, send all players apart from this user as options
        ArrayList<Chooseable> choices = new ArrayList<>();
        int numPlayers = gameLogic.getNumberOfPlayers();
        for (int i = 0;i<numPlayers;i++){
            if(i != gameLogic.getCurrentPlayer().getPlayerNumber()){
                String string = "Player " + i;
                ChooseableString cString = new ChooseableString(string);
                choices.add(cString);
            }
        }
        LifeGameMessage replyMessage = new LargeDecisionRequestMessage(choices,gameLogic.getCurrentPlayer().getPlayerNumber());

        gameLogic.setResponseMessage(replyMessage);

    }

    @Override
    @SuppressWarnings("Duplicates")
    public GameState handleInput(GameLogic gameLogic, LifeGameMessage lifeGameMessage) {
        GameState nextState = null;
        if (lifeGameMessage.getLifeGameMessageType() == LifeGameMessageTypes.LargeDecisionResponse) {
            LargeDecisionResponseMessage choiceMessage = (LargeDecisionResponseMessage) lifeGameMessage;

            //TODO use andrews new function to get the cash from the other player
        }
        return null;
    }

    @Override
    public void exit(GameLogic gameLogic) {
        // Must clear the sent message?
    }
}