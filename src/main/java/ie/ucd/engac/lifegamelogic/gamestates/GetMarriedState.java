package ie.ucd.engac.lifegamelogic.gamestates;

import ie.ucd.engac.GameConfig;
import ie.ucd.engac.lifegamelogic.GameLogic;
import ie.ucd.engac.lifegamelogic.playerlogic.MaritalStatus;
import ie.ucd.engac.messaging.LifeGameMessage;
import ie.ucd.engac.messaging.LifeGameMessageTypes;
import ie.ucd.engac.messaging.SpinRequestMessage;

public class GetMarriedState extends GameState {
	
	private int playersLeftToSpin;
	private int playerGettingMarriedIndex;
	private int playerToSpinIndex;
	private int playerToSpinNumber;
	private int playerGettingMarriedNumber;
	
	@Override
	public void enter(GameLogic gameLogic) {
		// Update the current player to have a spouse			
		gameLogic.getCurrentPlayer().setMaritalStatus(MaritalStatus.Married);
		
		playerGettingMarriedNumber = gameLogic.getCurrentPlayer().getPlayerNumber();
		playerGettingMarriedIndex = gameLogic.getCurrentPlayerIndex();
		playerToSpinIndex = gameLogic.getNextPlayerIndex(playerGettingMarriedIndex);
        playerToSpinNumber = gameLogic.getPlayerByIndex(playerToSpinIndex).getPlayerNumber();
		playersLeftToSpin = gameLogic.getNumberOfPlayers() - 1;
		
		String eventMsg = "Player " + playerToSpinNumber + ", spin the wheel to decide the gift to give.";
		
		LifeGameMessage responseMessage = new SpinRequestMessage(gameLogic.getShadowPlayer(gameLogic.getCurrentPlayerIndex()),
																 gameLogic.getCurrentPlayer().getPlayerNumber(),
																 eventMsg);
		gameLogic.setResponseMessage(responseMessage);
	}
	
	@Override
	public GameState handleInput(GameLogic gameLogic, LifeGameMessage lifeGameMessage) {
	    GameState nextState = null;
		if(lifeGameMessage.getLifeGameMessageType() == LifeGameMessageTypes.SpinResponse) {
			// Spin the wheel for the next player
			String paymentMsg;

    		int spinResult = gameLogic.getSpinner().spinTheWheel();

			int getMarriedPayment;
			
			//compute payment
			if(spinResult % 2 == 0) {
				// Current spinner must give the player getting married 100K
                getMarriedPayment = GameConfig.get_married_even_payment;
			}
			else {
                getMarriedPayment = GameConfig.get_married_odd_payment;
			}

			//make payment
            gameLogic.subtractFromPlayersBalance(playerToSpinIndex, getMarriedPayment);
            gameLogic.getCurrentPlayer().addToBalance(getMarriedPayment);

            playerToSpinNumber = gameLogic.getPlayerByIndex(playerToSpinIndex).getPlayerNumber();
            playerGettingMarriedNumber = gameLogic.getPlayerByIndex(playerGettingMarriedIndex).getPlayerNumber();

            paymentMsg =  "Player " + playerToSpinNumber + " paid player " + playerGettingMarriedNumber + " " + getMarriedPayment + ".\n";
			
			playersLeftToSpin--;
			playerToSpinIndex = gameLogic.getNextPlayerIndex(playerToSpinIndex);
            playerToSpinNumber = gameLogic.getPlayerByIndex(playerToSpinIndex).getPlayerNumber();
			
			if(playersLeftToSpin == 0) {
				String eventMsg = paymentMsg + 
						"You got married, player " + playerGettingMarriedNumber + ", so take an extra turn.";

                nextState = new HandlePlayerMoveState(eventMsg);
			}
			else {
				String eventMsg = "Player " + playerToSpinNumber + ", spin the wheel to decide the gift to give.";
				
				LifeGameMessage responseMessage = new SpinRequestMessage(gameLogic.getShadowPlayer(gameLogic.getCurrentPlayerIndex()),
						 gameLogic.getCurrentPlayer().getPlayerNumber(),
						 eventMsg);
				gameLogic.setResponseMessage(responseMessage);
                nextState = null;
			}
		}
		return nextState;
	}

	@Override
	public void exit(GameLogic gameLogic) {
		// TODO Auto-generated method stub
		
	}
}
