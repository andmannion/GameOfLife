package ie.ucd.engac.lifegamelogic.gamestates;

import ie.ucd.engac.lifegamelogic.GameLogic;
import ie.ucd.engac.lifegamelogic.cards.housecards.HouseCard;
import ie.ucd.engac.lifegamelogic.playerlogic.Player;
import ie.ucd.engac.messaging.*;

import java.util.ArrayList;

public class HouseSaleState extends GameState {

    private int choiceIndex;
    private boolean choseCard = false;
    private int spinNum = 0;

    @Override
    public void enter(GameLogic gameLogic) {
        // Get the CareerCards owned by this player
        ArrayList<HouseCard> cards = gameLogic.getCurrentPlayer().getHouseCards();
        ArrayList<Chooseable> choices = new ArrayList<>(cards);
        String eventMessage = "Which house would you like to sell?";

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

            choiceIndex = choiceMessage.getChoiceIndex();

            int playNum = gameLogic.getCurrentPlayer().getPlayerNumber();
            String eventMessage = "Player " + playNum + ", spin to determine sale price.";
            LifeGameRequestMessage spinRequestMessage = new LifeGameRequestMessage(LifeGameMessageTypes.SpinRequest,eventMessage, gameLogic.getShadowPlayer(gameLogic.getCurrentPlayerIndex()));
            gameLogic.setResponseMessage(spinRequestMessage);

            choseCard = true;
            nextState = null;
        }
        else if (lifeGameMessage.getLifeGameMessageType() == LifeGameMessageTypes.SpinResponse && choseCard) {
            spinNum = gameLogic.getSpinner().spinTheWheel();
            LifeGameMessage replyMessage = new SpinResultMessage(spinNum);
            gameLogic.setResponseMessage(replyMessage);
            nextState = null;
        }
        else if (lifeGameMessage.getLifeGameMessageType() == LifeGameMessageTypes.AckResponse && choseCard) {
            Player player = gameLogic.getCurrentPlayer();
            HouseCard soldCard = player.sellHouseCard(choiceIndex,spinNum);
            if (soldCard == null){
                nextState = null;
            } else {
                gameLogic.returnHouseCard(soldCard);
                nextState = new EndTurnState();
            }
        }
        return nextState;
    }

}