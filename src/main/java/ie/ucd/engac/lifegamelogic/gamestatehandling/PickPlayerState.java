package ie.ucd.engac.lifegamelogic.gamestatehandling;

import ie.ucd.engac.lifegamelogic.cards.actioncards.PlayersPayActionCard;
import ie.ucd.engac.messaging.*;

import java.util.ArrayList;

public class PickPlayerState implements GameState {
    private PlayersPayActionCard playersPayActionCard;

    public PickPlayerState(PlayersPayActionCard playersPayActionCard){
        this.playersPayActionCard = playersPayActionCard;
    }

    @Override
    public void enter(GameLogic gameLogic) {
        //get number of players to initialise message, send all players apart from this user as options
        ArrayList<Chooseable> choices = new ArrayList<>();
        int numPlayers = gameLogic.getNumberOfPlayers();
        for (int i = 0;i<numPlayers;i++){
            if(i != gameLogic.getCurrentPlayerIndex()){
                String string = "Player " + gameLogic.getPlayerByIndex(i).getPlayerNumber();
                ChooseableString cString = new ChooseableString(string);
                choices.add(cString);
            }
        }

        String eventMessage = "Pick a player to receive 20k from";

        LifeGameMessage replyMessage = new LargeDecisionRequestMessage(choices,gameLogic.getCurrentPlayer().getPlayerNumber(), eventMessage);

        gameLogic.setResponseMessage(replyMessage);
    }

    @Override
    @SuppressWarnings("Duplicates")
    public GameState handleInput(GameLogic gameLogic, LifeGameMessage lifeGameMessage) {
        GameState nextState = null;
        if (lifeGameMessage.getLifeGameMessageType() == LifeGameMessageTypes.LargeDecisionResponse) {
            LargeDecisionResponseMessage choiceMessage = (LargeDecisionResponseMessage) lifeGameMessage;

            int amount = playersPayActionCard.getAmountToPay();
            int choiceIndex = choiceMessage.getChoiceIndex();
            int playerIndex;

            //map choice back to player index, accounting for the current player
            if (choiceIndex < gameLogic.getCurrentPlayerIndex()) {
                playerIndex = choiceIndex;
            }
            else{
                playerIndex = choiceIndex + 1;
            }
            gameLogic.getCurrentPlayer().addToBalance(amount);
            gameLogic.subtractFromPlayersBalance(playerIndex,amount);
            nextState = new EndTurnState();
        }
        return nextState;
    }

    @Override
    public void exit(GameLogic gameLogic) {
        // Must clear the sent message?
    }
}