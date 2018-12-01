package ie.ucd.engac.lifegamelogic.gamestates;

import ie.ucd.engac.lifegamelogic.GameLogic;
import ie.ucd.engac.lifegamelogic.cards.housecards.HouseCard;
import ie.ucd.engac.lifegamelogic.playerlogic.Player;
import ie.ucd.engac.messaging.LifeGameMessage;
import ie.ucd.engac.messaging.LifeGameMessageTypes;
import ie.ucd.engac.messaging.LifeGameRequestMessage;

;

public class RetirePlayerState extends GameState {

    private int numberOfHouses;
    private int currentHouseNumber;

    @Override
    public void enter(GameLogic gameLogic) {
        Player player = gameLogic.getCurrentPlayer();
        numberOfHouses = player.getNumberOfHouseCards();
        int currentCardIndex = 0;

        int playNum = gameLogic.getCurrentPlayer().getPlayerNumber();
        currentHouseNumber = currentCardIndex + 1;
        String eventMessage = "Player " + playNum + ", spin to determine sale price for house: " + currentHouseNumber + "/" + numberOfHouses;
        LifeGameRequestMessage spinRequestMessage = new LifeGameRequestMessage(LifeGameMessageTypes.SpinRequest,eventMessage, gameLogic.getShadowPlayer(gameLogic.getCurrentPlayerIndex()));
        gameLogic.setResponseMessage(spinRequestMessage);
    }

    @Override
    public GameState handleInput(GameLogic gameLogic, LifeGameMessage lifeGameMessage) {
        GameState nextState = null;
        if (lifeGameMessage.getLifeGameMessageType() == LifeGameMessageTypes.SpinResponse) {

            int currentCardIndex = 0;
            int spinNum = gameLogic.getSpinner().spinTheWheel();;
            Player retiree = gameLogic.getCurrentPlayer();
            //sell the card and move on to the next
            HouseCard soldCard = retiree.sellHouseCard(currentCardIndex, spinNum);
            if (soldCard == null){
                return nextState;
            }
            gameLogic.returnHouseCard(soldCard);
            currentHouseNumber = currentHouseNumber + 1;

            //decide next action
            if (currentHouseNumber <= numberOfHouses) { //if there are cards left to sell then keep going through sale sequence
                int playNum = gameLogic.getCurrentPlayer().getPlayerNumber();
                int currentHouseNumber = currentCardIndex + 1;
                String eventMessage = "Player " + playNum + ", spin to determine sale price for house: " + currentHouseNumber + "/" + numberOfHouses;
                LifeGameRequestMessage spinRequestMessage = new LifeGameRequestMessage(LifeGameMessageTypes.SpinRequest,eventMessage, gameLogic.getShadowPlayer(gameLogic.getCurrentPlayerIndex()));
                gameLogic.setResponseMessage(spinRequestMessage);
            }
            else { //otherwise do remainder of retirement actions
                int retirementCash = gameLogic.retireCurrentPlayer();
                String eventMessage = "Player " + retiree.getPlayerNumber() + " has retired with " + retirementCash;

                //check if the game is over or we need to keep playing on
                if (gameLogic.getNumberOfPlayers() == 0) {
                    nextState = new GameOverState();
                }
                else {
                    nextState = new EndTurnState(eventMessage);
                }
            }
        }
        return nextState;
    }

}
