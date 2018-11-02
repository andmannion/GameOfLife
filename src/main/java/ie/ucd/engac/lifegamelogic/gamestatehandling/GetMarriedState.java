package ie.ucd.engac.lifegamelogic.gamestatehandling;

import ie.ucd.engac.lifegamelogic.Spinner;
import ie.ucd.engac.lifegamelogic.playerlogic.MaritalStatus;
import ie.ucd.engac.messaging.LifeGameMessage;
import ie.ucd.engac.messaging.LifeGameMessageTypes;
import ie.ucd.engac.messaging.ShadowPlayer;
import ie.ucd.engac.messaging.SpinRequestMessage;

public class GetMarriedState implements GameState {
	public static final int GET_MARRIED_EVEN_PAYMENT = 50000;
	public static final int GET_MARRIED_ODD_PAYMENT = 100000;
	
	private int playersLeftToSpin;
	private int playerGettingMarriedIndex;
	private int playerToSpinIndex;
	private int playerToSpinNumber;
	private int playerGettingMarriedNumber;
	
	@Override
	public void enter(GameLogic gameLogic) {	
		System.out.println("Getting married...");
		
		// Update the current player to have a spouse			
		gameLogic.getCurrentPlayer().setMaritalStatus(MaritalStatus.Married);
		
		playerGettingMarriedNumber = gameLogic.getCurrentPlayer().getPlayerNumber();
		playerGettingMarriedIndex = gameLogic.getCurrentPlayerIndex();
		playerToSpinIndex = gameLogic.getNextPlayerIndex(playerGettingMarriedIndex);
        playerToSpinNumber = gameLogic.getPlayerByIndex(playerToSpinIndex).getPlayerNumber();
		playersLeftToSpin = gameLogic.getNumberOfPlayers() - 1;
		
		String eventMsg = "Player " + playerToSpinNumber + ", spin the wheel to decide the gift to give.";
		
		LifeGameMessage responseMessage = new SpinRequestMessage(new ShadowPlayer(gameLogic.getCurrentPlayer(), gameLogic),
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

			int spinResult = Spinner.spinTheWheel();
			int getMarriedPayment;
			
			//compute payment
			if(spinResult % 2 == 0) {
				// Current spinner must give the player getting married 100K
                getMarriedPayment = GET_MARRIED_EVEN_PAYMENT;
			}
			else {
                getMarriedPayment = GET_MARRIED_ODD_PAYMENT;
			}

			//make payment
            gameLogic.getPlayerByIndex(playerToSpinIndex).subtractFromBalance(getMarriedPayment, gameLogic);
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
				
				LifeGameMessage responseMessage = new SpinRequestMessage(new ShadowPlayer(gameLogic.getCurrentPlayer(), gameLogic),
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
