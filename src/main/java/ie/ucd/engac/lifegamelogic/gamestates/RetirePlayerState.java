package ie.ucd.engac.lifegamelogic.gamestates;

import ie.ucd.engac.lifegamelogic.GameLogic;
import ie.ucd.engac.lifegamelogic.cards.housecards.HouseCard;
import ie.ucd.engac.lifegamelogic.playerlogic.Player;
import ie.ucd.engac.messaging.*;

public class RetirePlayerState extends GameState {

    private int numberOfHouses;
    private int currentHouseNumber;
    private int spinNum;
    private boolean spinResultAvailable;

    @Override
    public void enter(GameLogic gameLogic) {
        Player player = gameLogic.getCurrentPlayer();
        numberOfHouses = player.getNumberOfHouseCards();
        int currentCardIndex = 0;
        spinResultAvailable = false;
        int playNum = gameLogic.getCurrentPlayer().getPlayerNumber();
        currentHouseNumber = currentCardIndex + 1;
        String eventMessage = "Player " + playNum + ", spin to determine sale price for house: " + currentHouseNumber + "/" + numberOfHouses;
        LifeGameRequestMessage spinRequestMessage = new LifeGameRequestMessage(LifeGameMessageTypes.SpinRequest,eventMessage, gameLogic.getShadowPlayer(gameLogic.getCurrentPlayerIndex()));
        gameLogic.setResponseMessage(spinRequestMessage);
    }

    @Override
    public GameState handleInput(GameLogic gameLogic, LifeGameMessage lifeGameMessage) {
        GameState nextState = null;
        if (lifeGameMessage.getLifeGameMessageType() == LifeGameMessageTypes.SpinResponse && !spinResultAvailable) {
            spinNum = gameLogic.getSpinner().spinTheWheel();
            spinResultAvailable = true;

            LifeGameMessage replyMessage = new SpinResultMessage(spinNum);
            gameLogic.setResponseMessage(replyMessage);
            nextState = null;
        }
        else if(lifeGameMessage.getLifeGameMessageType() == LifeGameMessageTypes.AckResponse && spinResultAvailable){
            int currentCardIndex = 0;
            Player retiree = gameLogic.getCurrentPlayer();
            //sell the card and move on to the next
            HouseCard soldCard = retiree.sellHouseCard(currentCardIndex, spinNum);

            if (soldCard == null){
                throw new RuntimeException("Tried to sell a card that did not exist in retirement.");
            }

            gameLogic.returnHouseCard(soldCard);
            currentHouseNumber = currentHouseNumber + 1;
            spinResultAvailable = false;

            nextState = getNextRetirementAction(gameLogic, retiree);
        }
        return nextState;
    }

    private GameState getNextRetirementAction(GameLogic gameLogic, Player retiree) {
        GameState nextState = null;
        //decide next action
        if (currentHouseNumber <= numberOfHouses) { //if there are cards left to sell then keep going through sale sequence
            int playNum = gameLogic.getCurrentPlayer().getPlayerNumber();
            String eventMessage = "Player " + playNum + ", spin to determine sale price for house: " + currentHouseNumber + "/" + numberOfHouses;
            LifeGameRequestMessage spinRequestMessage = new LifeGameRequestMessage(LifeGameMessageTypes.SpinRequest,eventMessage, gameLogic.getShadowPlayer(gameLogic.getCurrentPlayerIndex()));
            gameLogic.setResponseMessage(spinRequestMessage);
        }
        else { //otherwise do remainder of retirement actions
            nextState = GameState.retirePlayer(gameLogic, retiree);
        }
        return nextState;
    }
}
