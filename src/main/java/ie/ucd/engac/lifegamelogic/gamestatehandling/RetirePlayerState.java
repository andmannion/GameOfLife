package ie.ucd.engac.lifegamelogic.gamestatehandling;

import ie.ucd.engac.lifegamelogic.Spinner;
import ie.ucd.engac.lifegamelogic.playerlogic.Player;
import ie.ucd.engac.messaging.LifeGameMessage;
import ie.ucd.engac.messaging.LifeGameMessageTypes;
import ie.ucd.engac.messaging.ShadowPlayer;
import ie.ucd.engac.messaging.SpinRequestMessage;

public class RetirePlayerState implements GameState {

    private int numberOfHouses;
    private int currentCardIndex;

    @Override
    public void enter(GameLogic gameLogic) {
        Player player = gameLogic.getCurrentPlayer();
        numberOfHouses = player.getNumberOfHouseCards();
        currentCardIndex = 0;

        int playNum = gameLogic.getCurrentPlayer().getPlayerNumber();
        int curentHouseNumber = currentCardIndex + 1;
        String eventMessage = "Player " + playNum + ", spin to determine sale price for house: " + curentHouseNumber + "/" + numberOfHouses;
        SpinRequestMessage spinRequestMessage = new SpinRequestMessage(gameLogic.getCurrentPlayer().getShadowPlayer(gameLogic), playNum, eventMessage);
        gameLogic.setResponseMessage(spinRequestMessage);
    }

    @Override
    public GameState handleInput(GameLogic gameLogic, LifeGameMessage lifeGameMessage) {
        GameState nextState = null;
        if (lifeGameMessage.getLifeGameMessageType() == LifeGameMessageTypes.SpinResponse) {
            int spinNum = gameLogic.getSpinner().spinTheWheel();;
            Player retiree = gameLogic.getCurrentPlayer();
            //sell the card and move on to the next
            retiree.sellHouseCard(currentCardIndex, spinNum);
            currentCardIndex = currentCardIndex + 1;
            if (currentCardIndex < numberOfHouses) { //if there are cards left to sell
                int playNum = gameLogic.getCurrentPlayer().getPlayerNumber();
                int curentHouseNumber = currentCardIndex + 1;
                String eventMessage = "Player " + playNum + ", spin to determine sale price for house: " + curentHouseNumber + "/" + numberOfHouses;
                SpinRequestMessage spinRequestMessage = new SpinRequestMessage(gameLogic.getCurrentPlayer().getShadowPlayer(gameLogic), playNum, eventMessage);
                gameLogic.setResponseMessage(spinRequestMessage);
            }
            else {
                int retirementCash = gameLogic.retireCurrentPlayer();
                String eventMessage = "Player " + retiree.getPlayerNumber() + " has retired with " + retirementCash;
                if (gameLogic.getNumberOfPlayers() == 0) {
                    nextState = new GameOverState();
                    System.out.println("Game Over"); //TODO remove
                }
                else {
                    eventMessage = "Player " + retiree.getPlayerNumber() + " has retired.";
                    nextState = new EndTurnState(eventMessage);
                }
            }
        }
        return nextState;
    }

    @Override
    public void exit(GameLogic gameLogic) {}
}
