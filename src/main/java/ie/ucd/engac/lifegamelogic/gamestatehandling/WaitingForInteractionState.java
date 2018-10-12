package ie.ucd.engac.lifegamelogic.gamestatehandling;

import ie.ucd.engac.messaging.LifeGameMessageTypes;

public class WaitingForInteractionState implements GameState {

	@Override
	public void enter(GameLogic gameLogic) {
		// If not the first go, should pass move to next player
		if(gameLogic.getCurrentPlayer().getCareerPath() == null) {
			/* Need to send a response stating that a decision is required 
			* on the career path to be taken for this player
			*/
			
		}
		else {
			gameLogic.setNextPlayerToCurrent();
		}
	}
	
	@Override
	public GameState handleInput(GameLogic gameLogic, UserInput userInput) {
		if(userInput.getLifeGameMessageType() == LifeGameMessageTypes.SpinRequest) {
			// This means the current SpinRequest should be interpreted as a spin to move
			// the next player.
			
			
			return new UpdatePlayersAndBoardState();
		}
		
		return null;
	}

	@Override
	public void exit(GameLogic gameLogic) {
		// TODO Auto-generated method stub
	}

	

}
