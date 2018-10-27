package ie.ucd.engac.lifegamelogic.gamestatehandling;

import ie.ucd.engac.messaging.*;
import java.util.ArrayList;

public class HouseTileDecisionState implements GameState {

    @Override
    public void enter(GameLogic gameLogic) {
        // Get the two top CareerCards

        ArrayList<Chooseable> choices = new ArrayList<>();
        choices.add(new ChooseableString("Do nothing"));
        choices.add(new ChooseableString("Buy a house"));

        if(gameLogic.getCurrentPlayer().getNumberOfHouseCards() != 0) {
            choices.add(new ChooseableString("Sell a house"));
        }

        LifeGameMessage replyMessage = new LargeDecisionRequestMessage(choices,gameLogic.getCurrentPlayer().getPlayerNumber());
        // Need to store both choices so that we can assign the chosen one to the
        // correct player,
        // and push the unchosen one to the bottom of the correct deck.
        gameLogic.setResponseMessage(replyMessage);

    }

    @Override
    @SuppressWarnings("Duplicates")
    public GameState handleInput(GameLogic gameLogic, LifeGameMessage lifeGameMessage) {
        GameState nextState = null;
        if (lifeGameMessage.getLifeGameMessageType() == LifeGameMessageTypes.LargeDecisionResponse) {
            LargeDecisionResponseMessage choiceMessage = (LargeDecisionResponseMessage) lifeGameMessage;

            int choiceIndex = choiceMessage.getChoiceIndex();

            if (choiceIndex == 0){ //do nothing, turn ends
                return new EndTurnState();
            }
            else if (choiceIndex == 1){ //if they wish to buy
                return new HouseChoiceState();
            }
            else { //if they wish to sell
                return new HouseSaleState();
            }
        }
        return null;
    }

    @Override
    public void exit(GameLogic gameLogic) {
        // Must clear the sent message?
    }
}