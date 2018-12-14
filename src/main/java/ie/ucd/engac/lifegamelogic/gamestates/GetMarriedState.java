package ie.ucd.engac.lifegamelogic.gamestates;

import ie.ucd.engac.GameConfig;
import ie.ucd.engac.lifegamelogic.GameLogic;
import ie.ucd.engac.lifegamelogic.playerlogic.MaritalStatus;
import ie.ucd.engac.messaging.LifeGameMessage;
import ie.ucd.engac.messaging.LifeGameMessageTypes;
import ie.ucd.engac.messaging.LifeGameRequestMessage;
import ie.ucd.engac.messaging.SpinResultMessage;

public class GetMarriedState extends GameState {
	
	private int playersLeftToSpin;
	private int playerGettingMarriedIndex;
	private int playerToSpinIndex;
	private int playerToSpinNumber;
	private int playerGettingMarriedNumber;
	private int spinResult = 0;
	private boolean spinComplete = false;

	@Override
	public void enter(GameLogic gameLogic) {
		// Update the current player to have a spouse			
		gameLogic.getCurrentPlayer().setMaritalStatus(MaritalStatus.Married);
		
		playerGettingMarriedNumber = gameLogic.getCurrentPlayer().getPlayerNumber();
		playerGettingMarriedIndex = gameLogic.getCurrentPlayerIndex();
		playerToSpinIndex = gameLogic.getNextPlayerIndex(playerGettingMarriedIndex);
        playerToSpinNumber = gameLogic.getPlayerByIndex(playerToSpinIndex).getPlayerNumber();
		playersLeftToSpin = gameLogic.getNumberOfPlayers() - 1;
		
		String eventMsg = "Player " + playerToSpinNumber + ", spin the wheel to decide the marriage gift to give.";
		
		LifeGameMessage responseMessage = new LifeGameRequestMessage(LifeGameMessageTypes.SpinRequest,
																	 eventMsg,
																	 gameLogic.getShadowPlayer(gameLogic.getCurrentPlayerIndex()));
		gameLogic.setResponseMessage(responseMessage);
	}
	
	@Override
	public GameState handleInput(GameLogic gameLogic, LifeGameMessage lifeGameMessage) {
	    GameState nextState = null;
		if(lifeGameMessage.getLifeGameMessageType() == LifeGameMessageTypes.SpinResponse) {
			// Spin the wheel for the next player
			spinResult = gameLogic.getSpinner().spinTheWheel();

            LifeGameMessage replyMessage = new SpinResultMessage(spinResult);
            gameLogic.setResponseMessage(replyMessage);

            spinComplete = true;
            nextState = null;
		}
    	else if (lifeGameMessage.getLifeGameMessageType() == LifeGameMessageTypes.AckResponse && spinComplete){
    		// Spin result information has been successfully sent, can begin payment procedure for a given other player
			String paymentMsg;
			int getMarriedPayment;
			
			// Compute payment to the person getting married based on the number spun
			if(spinResult % 2 == 0) {
				// Current spinner must give the player getting married 100K
                getMarriedPayment = GameConfig.get_married_even_payment;
			}
			else {
                getMarriedPayment = GameConfig.get_married_odd_payment;
			}

			// Make payment to the player getting married
            gameLogic.subtractFromPlayersBalance(playerToSpinIndex, getMarriedPayment);
            gameLogic.getCurrentPlayer().addToBalance(getMarriedPayment);

            playerToSpinNumber = gameLogic.getPlayerByIndex(playerToSpinIndex).getPlayerNumber();
            playerGettingMarriedNumber = gameLogic.getPlayerByIndex(playerGettingMarriedIndex).getPlayerNumber();

            paymentMsg =  "Player " + playerToSpinNumber + " paid player " + playerGettingMarriedNumber + " " + getMarriedPayment + ".\n";
			
			playersLeftToSpin--;
			playerToSpinIndex = gameLogic.getNextPlayerIndex(playerToSpinIndex);
            playerToSpinNumber = gameLogic.getPlayerByIndex(playerToSpinIndex).getPlayerNumber();
			
			if(playersLeftToSpin == 0) {
				// All players have had their contribution deducted and applied
				String eventMsg = paymentMsg + 
						"You got married, player " + playerGettingMarriedNumber + ", so take an extra turn.";

                nextState = new HandlePlayerMoveState(eventMsg);
			}
			else {
				// Prompt the next player to spin so that their contribution can be applied
				spinComplete = false;
				String eventMsg = "Player " + playerToSpinNumber + ", spin the wheel to decide the marriage gift to give.";
				
				LifeGameMessage responseMessage = new LifeGameRequestMessage(LifeGameMessageTypes.SpinRequest,eventMsg, gameLogic.getShadowPlayer(gameLogic.getCurrentPlayerIndex())
				);
				gameLogic.setResponseMessage(responseMessage);
                nextState = null;
			}
		}
		return nextState;
	}
}
