package ie.ucd.engac.lifegamelogic.gamestates;

import ie.ucd.engac.lifegamelogic.GameLogic;
import ie.ucd.engac.lifegamelogic.cards.housecards.HouseCard;
import ie.ucd.engac.lifegamelogic.playerlogic.Player;
import ie.ucd.engac.messaging.*;

import java.util.ArrayList;

public class HouseSaleState extends GameState { //TODO this entire class

    private int choiceIndex;
    private boolean choseCard = false;

    @Override
    public void enter(GameLogic gameLogic) {
        // Get the CareerCards owned by this player

        ArrayList<HouseCard> cards = gameLogic.getCurrentPlayer().getHouseCards();
        ArrayList<Chooseable> choices = new ArrayList<>();
        for (HouseCard houseCard:cards){
            choices.add( (Chooseable) houseCard );
        }
        String eventMessage = "Which house would you like to sell?";

        LifeGameMessageTypes requestType = LifeGameMessageTypes.LargeDecisionRequest;
        LifeGameMessage replyMessage = new DecisionRequestMessage(choices,gameLogic.getCurrentPlayer().getPlayerNumber(), eventMessage, requestType);
        // Need to store both choices so that we can assign the chosen one to the
        // correct player,
        // and push the unchosen one to the bottom of the correct deck.
        gameLogic.setResponseMessage(replyMessage);

    }

    @Override
    public GameState handleInput(GameLogic gameLogic, LifeGameMessage lifeGameMessage) {

        if (lifeGameMessage.getLifeGameMessageType() == LifeGameMessageTypes.LargeDecisionResponse) {
            DecisionResponseMessage choiceMessage = (DecisionResponseMessage) lifeGameMessage;

            choiceIndex = choiceMessage.getChoiceIndex();

            int playNum = gameLogic.getCurrentPlayer().getPlayerNumber();
            String eventMessage = "Player " + playNum + ", spin to determine sale price.";
            SpinRequestMessage spinRequestMessage = new SpinRequestMessage(gameLogic.getShadowPlayer(gameLogic.getCurrentPlayerIndex()), playNum, eventMessage);
            gameLogic.setResponseMessage(spinRequestMessage);

            choseCard = true;
            return null;
        }
        else if(lifeGameMessage.getLifeGameMessageType() == LifeGameMessageTypes.SpinResponse && choseCard) {
            int spinNum = gameLogic.getSpinner().spinTheWheel();
            Player player = gameLogic.getCurrentPlayer();
            HouseCard soldCard = player.sellHouseCard(choiceIndex,spinNum);
            if (soldCard == null){
                return null;
            }
            gameLogic.returnHouseCard(soldCard);
            return new EndTurnState();
        }
        return null;
    }

    @Override
    public void exit(GameLogic gameLogic) {
        // Must clear the sent message?
    }
}