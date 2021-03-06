package ie.ucd.engac.lifegamelogic.gamestates;

import ie.ucd.engac.lifegamelogic.GameLogic;
import ie.ucd.engac.messaging.*;

import java.util.ArrayList;

public class HouseTileDecisionState extends GameState {

    @Override
    public void enter(GameLogic gameLogic) {
        // Get the two top CareerCards

        ArrayList<Chooseable> choices = new ArrayList<>();
        choices.add(new ChooseableString("Do nothing"));
        choices.add(new ChooseableString("Buy a house"));

        if(gameLogic.getCurrentPlayer().getNumberOfHouseCards() != 0) {
            choices.add(new ChooseableString("Sell a house"));
        }

        String eventMessage = "HouseTile: What action would you like to perform?";

        LifeGameMessageTypes requestType = LifeGameMessageTypes.LargeDecisionRequest;
        LifeGameMessage replyMessage = new DecisionRequestMessage(choices,gameLogic.getCurrentPlayer().getPlayerNumber(), eventMessage, requestType, gameLogic.getCurrentShadowPlayer());
        // Need to store both choices so that we can assign the chosen one to the
        // correct player,
        // and push the unchosen one to the bottom of the correct deck.
        gameLogic.setResponseMessage(replyMessage);

    }

    @Override
    public GameState handleInput(GameLogic gameLogic, LifeGameMessage lifeGameMessage) {
        GameState nextState = null;
        if (lifeGameMessage.getLifeGameMessageType() == LifeGameMessageTypes.LargeDecisionResponse) {
            DecisionResponseMessage choiceMessage = (DecisionResponseMessage) lifeGameMessage;

            int choiceIndex = choiceMessage.getChoiceIndex();

            if (choiceIndex == 0){ //do nothing, turn ends
                nextState = new EndTurnState();
            }
            else if (choiceIndex == 1){ //if they wish to buy
                nextState = new HouseChoiceState();
            }
            else { //if they wish to sell
                nextState = new HouseSaleState();
            }
        }
        return nextState;
    }

}