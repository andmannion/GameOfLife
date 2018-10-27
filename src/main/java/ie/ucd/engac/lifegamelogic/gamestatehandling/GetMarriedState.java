package ie.ucd.engac.lifegamelogic.gamestatehandling;

import ie.ucd.engac.lifegamelogic.Spinner;
import ie.ucd.engac.lifegamelogic.playerlogic.MaritalStatus;
import ie.ucd.engac.messaging.LifeGameMessage;
import ie.ucd.engac.messaging.LifeGameMessageTypes;
import ie.ucd.engac.messaging.ShadowPlayer;
import ie.ucd.engac.messaging.SpinRequestMessage;

public class GetMarriedState implements GameState {
	private final int GET_MARRIED_EVEN_PAYMENT = 50000;
	private final int GET_MARRIED_ODD_PAYMENT = 100000;
	
	private int playersLeftToSpin;
	private int playerGettingMarriedIndex;
	private int playerToSpinIndex;
	private int playerGettingMarriedNumber;
	
	@Override
	public void enter(GameLogic gameLogic) {	
		System.out.println("Getting married...");
		
		// Update the current player to have a spouse			
		gameLogic.getCurrentPlayer().setMaritalStatus(MaritalStatus.Married);
		
		playerGettingMarriedNumber = gameLogic.getCurrentPlayer().getPlayerNumber();
		playerGettingMarriedIndex = gameLogic.getCurrentPlayerIndex();
		playerToSpinIndex = gameLogic.getNextPlayerIndex(playerGettingMarriedIndex);
		playersLeftToSpin = gameLogic.getNumberOfPlayers() - 1;
		
		String eventMsg = "Player " + playerGettingMarriedNumber + ", spin the wheel.";
		
		LifeGameMessage responseMessage = new SpinRequestMessage(new ShadowPlayer(gameLogic.getCurrentPlayer()),
																 gameLogic.getCurrentPlayer().getPlayerNumber(),
																 eventMsg);
		gameLogic.setResponseMessage(responseMessage);
	}
	

	@Override
	public GameState handleInput(GameLogic gameLogic, LifeGameMessage lifeGameMessage) {
		if(lifeGameMessage.getLifeGameMessageType() == LifeGameMessageTypes.SpinResponse) {
			// Spin the wheel for the next player
			String paymentMsg = "";
			
			int spinResult = Spinner.spinTheWheel();
			
			
			if(spinResult % 2 == 0) {
				// Current spinner must give the player getting married 100K
				gameLogic.getPlayerByIndex(playerToSpinIndex).subtractFromBalance(GET_MARRIED_EVEN_PAYMENT);
				gameLogic.getCurrentPlayer().addToBalance(GET_MARRIED_EVEN_PAYMENT);
				
				paymentMsg =  "Player " + playerToSpinIndex + " paid player " + playerGettingMarriedIndex + " " + GET_MARRIED_EVEN_PAYMENT + ".\n";
			}
			else {
				gameLogic.getPlayerByIndex(playerToSpinIndex).subtractFromBalance(GET_MARRIED_ODD_PAYMENT);
				gameLogic.getCurrentPlayer().addToBalance(GET_MARRIED_ODD_PAYMENT);
				
				paymentMsg =  "Player " + playerToSpinIndex + " paid player " + playerGettingMarriedIndex + " " + GET_MARRIED_ODD_PAYMENT + ".\n";
			}
			
			playersLeftToSpin--;
			playerToSpinIndex = gameLogic.getNextPlayerIndex(playerToSpinIndex);
			
			if(playersLeftToSpin == 0) {
				String eventMsg = paymentMsg + 
						"You got married, player " + playerGettingMarriedIndex + ", so take an extra turn.";
				
				// The current player gets another go, so don't advance the current player
				LifeGameMessage responseMessage = new SpinRequestMessage(new ShadowPlayer(gameLogic.getCurrentPlayer()),
																		 playerGettingMarriedIndex,
																		 eventMsg);				
				gameLogic.setResponseMessage(responseMessage);				
				return new HandlePlayerMoveState();
			}
			else {
				String eventMsg = "Player " + playerToSpinIndex + ", spin the wheel.";
				
				LifeGameMessage responseMessage = new SpinRequestMessage(new ShadowPlayer(gameLogic.getCurrentPlayer()),
						 gameLogic.getCurrentPlayer().getPlayerNumber(),
						 eventMsg);
				gameLogic.setResponseMessage(responseMessage);
			}
		}
		
		return null;
	}

	@Override
	public void exit(GameLogic gameLogic) {
		// TODO Auto-generated method stub
		
	}
}
