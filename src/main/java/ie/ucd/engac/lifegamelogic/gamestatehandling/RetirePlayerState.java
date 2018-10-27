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
        SpinRequestMessage spinRequestMessage = new SpinRequestMessage(new ShadowPlayer(gameLogic.getCurrentPlayer(), gameLogic), playNum, eventMessage);
        gameLogic.setResponseMessage(spinRequestMessage);
    }

    @Override
    public GameState handleInput(GameLogic gameLogic, LifeGameMessage lifeGameMessage) {
        GameState nextState = null;
        if (lifeGameMessage.getLifeGameMessageType() == LifeGameMessageTypes.SpinResponse) {
            int spinNum = Spinner.spinTheWheel();
            Player player = gameLogic.getCurrentPlayer();
            //sell the card and move on to the next
            player.sellHouseCard(currentCardIndex, spinNum);
            currentCardIndex = currentCardIndex + 1;
            if (currentCardIndex < numberOfHouses) { //if there are cards left to sell
                int playNum = gameLogic.getCurrentPlayer().getPlayerNumber();
                int curentHouseNumber = currentCardIndex + 1;
                String eventMessage = "Player " + playNum + ", spin to determine sale price for house: " + curentHouseNumber + "/" + numberOfHouses;
                SpinRequestMessage spinRequestMessage = new SpinRequestMessage(new ShadowPlayer(gameLogic.getCurrentPlayer(), gameLogic), playNum, eventMessage);
                gameLogic.setResponseMessage(spinRequestMessage);
            } else {
                player.retirePlayer(gameLogic.getNumberOfRetiredPlayers(), gameLogic);
                nextState = new EndTurnState();
            }
        }
        return nextState;
    }

    @Override
    public void exit(GameLogic gameLogic) {}
}
