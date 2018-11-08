package ie.ucd.engac.lifegamelogic.gamestatehandling;

import java.util.ArrayList;
import java.util.HashMap;

import ie.ucd.engac.GameConfig;
import ie.ucd.engac.lifegamelogic.Spinner;
import ie.ucd.engac.messaging.LifeGameMessage;
import ie.ucd.engac.messaging.LifeGameMessageTypes;
import ie.ucd.engac.messaging.ShadowPlayer;
import ie.ucd.engac.messaging.SpinRequestMessage;

public class SpinToWinGetWinnerState implements GameState {
	private static final int SPIN_TO_WIN_PRIZE_NOT_WON = -1;
	
	private final HashMap<Integer, ArrayList<Integer>> playerNumberChoiceMap;
	private int currentPlayerSpinningTheWheelIndex;
	
	protected SpinToWinGetWinnerState(HashMap<Integer, ArrayList<Integer>> playerNumberChoiceMap) {
		this.playerNumberChoiceMap = playerNumberChoiceMap; 
	}
	
	@Override
	public void enter(GameLogic gameLogic) {
		// TODO Auto-generated method stub
		currentPlayerSpinningTheWheelIndex = gameLogic.getCurrentPlayerIndex();
	}

	@Override
	public GameState handleInput(GameLogic gameLogic, LifeGameMessage lifeGameMessage) {
		// TODO test for >2 players
		if(lifeGameMessage.getLifeGameMessageType() == LifeGameMessageTypes.SpinResponse) {
			// Must keep track of the player that is currently spinning

			int numberSpun = gameLogic.getSpinner().spinTheWheel();
			
			int winningPlayerIndex = parseSpinToWinAction(numberSpun);
			
			if(winningPlayerIndex >= 0) {			
				// Update the player that won with the amount of money necessary
				assignSpinToWinPrize(gameLogic, winningPlayerIndex);
				
				// Set the next state to HandlePlayerMoveState, 
				// Set the message to a spinrequest, shadow player is the one that won the game

				String eventMsg = "You won 200K, player " + winningPlayerIndex + 
						". Player " + gameLogic.getCurrentPlayer().getPlayerNumber() +
					    "'s turn is over.";
				LifeGameMessage responseMessage = new SpinRequestMessage(gameLogic.getShadowPlayer(currentPlayerSpinningTheWheelIndex),
						gameLogic.getPlayerByIndex(currentPlayerSpinningTheWheelIndex).getPlayerNumber(), eventMsg);
				gameLogic.setResponseMessage(responseMessage);
				
				return new EndTurnState(eventMsg);
			}
			else {
				// No one won this turn
				int playerNumber = gameLogic.getPlayerByIndex(currentPlayerSpinningTheWheelIndex).getPlayerNumber();
				String eventMsg = "Player " + playerNumber + " spin the wheel to try to win.";
				LifeGameMessage responseMessage = new SpinRequestMessage(gameLogic.getShadowPlayer(currentPlayerSpinningTheWheelIndex),
                        playerNumber, eventMsg);
				gameLogic.setResponseMessage(responseMessage);
			}
		}
		
		return null;
	}

	@Override
	public void exit(GameLogic gameLogic) {
		// TODO Auto-generated method stub
		
	}

	private int parseSpinToWinAction(int numberSpun) {
		// Must check the values in the hashmap
		for(HashMap.Entry<Integer,ArrayList<Integer>> kvps : playerNumberChoiceMap.entrySet()) {
			if(kvps.getValue().contains(numberSpun)) {
				return kvps.getKey();
			}
		}
		
		return SPIN_TO_WIN_PRIZE_NOT_WON;
	}
	
	private void assignSpinToWinPrize(GameLogic gameLogic, int winningPlayerIndex) {
		gameLogic.getPlayerByIndex(winningPlayerIndex).addToBalance(GameConfig.spin_to_win_prize_money);
	}
}
